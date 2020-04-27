/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package examples.generators.hash;

import util.Util;

import java.math.BigInteger;

import circuit.config.Config;
import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;
import circuit.structure.WireArray;
import examples.gadgets.hash.MerkleTreePathGadget;
import examples.gadgets.hash.SHA2;
import examples.gadgets.hash.SubsetSumHashGadgetIG;
import examples.gadgets.myMath;

public class SubsetSumHashGadge_IG_Test extends CircuitGenerator {

	private Wire[] original;
	private Wire[] output;
	SubsetSumHashGadgetIG subsetSumHashGadget;

	public SubsetSumHashGadge_IG_Test(String circuitName) {
		super(circuitName);
	}

	@Override
	protected void buildCircuit() {
		original = createProverWitnessWireArray(6);
		output = createInputWireArray(3);
		Wire[] nextInputBits = new WireArray(original).getBits(Config.LOG2_FIELD_PRIME).asArray();
		subsetSumHashGadget = new SubsetSumHashGadgetIG(nextInputBits, false);
		Wire[] currentHash = subsetSumHashGadget.getOutputWires();
		Wire result = currentHash[0].isEqualTo(output[0]);
		makeOutputArray(currentHash,"test");
		makeOutput(result,"result");
	}

	@Override
	public void generateSampleInput(CircuitEvaluator circuitEvaluator) {

		BigInteger[] image = new BigInteger[6];
		for(int i = 0; i < image.length; i++){
			image[i] = new BigInteger("255");
		}
        
        for(int i = 0; i < image.length; i++){
            circuitEvaluator.setWireValue(original[i],image[i]);
		}
		
		BigInteger[] gadget = myMath.getBitArray(image, Config.LOG2_FIELD_PRIME);
		subsetSumHashGadget = new SubsetSumHashGadgetIG(gadget,false);
		BigInteger[] result = subsetSumHashGadget.getOutput();
		
        for(int i = 0; i < result.length; i++){
            circuitEvaluator.setWireValue(output[i],result[i]);
		}
		for(int i = 0; i < result.length; i++){
            System.out.println("result["+i+"] :: "+result[i]);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		SubsetSumHashGadge_IG_Test generator = new SubsetSumHashGadge_IG_Test("test_IG");
		generator.generateCircuit();
		generator.evalCircuit();
		generator.prepFiles();
		generator.runLibsnark();		
	}

	
}
