/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package examples.generators.hash;

import util.Util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

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
	// private Wire answer;
	private int w = 80;
	private int h = 45;
	SubsetSumHashGadgetIG subsetSumHashGadget;

	public SubsetSumHashGadge_IG_Test(String circuitName) {
		super(circuitName);
	}

	@Override
	protected void buildCircuit() {
		original = createProverWitnessWireArray(w*h);
		output = createInputWireArray(3*w*h);
		// original = createProverWitnessWireArray(4);
		// output = createInputWireArray(3*4);
		// answer=createProverWitnessWire("answer");
		for(int i=0; i<w*h; i++){
			Wire[] nextInputBits = original[i].getBitWires(Config.LOG2_FIELD_PRIME).asArray();
			subsetSumHashGadget = new SubsetSumHashGadgetIG(nextInputBits, false);
			Wire[] currentHash = subsetSumHashGadget.getOutputWires();
			// makeOutputArray(currentHash,"test");
			Wire result1 = currentHash[0].isEqualTo(output[i*3]);
			Wire result2 = currentHash[1].isEqualTo(output[i*3+1]);
			Wire result3 = currentHash[2].isEqualTo(output[i*3+2]);
			Wire result = result3.and(result2.and(result1));
			// makeOutput(result,"result");
			// answer = answer.mul(result);
		}
		// makeOutput(answer,"answer!");
	}

	@Override
	public void generateSampleInput(CircuitEvaluator circuitEvaluator) {
		// try{
			// File source = new File("/home/itsp/jsnark/JsnarkCircuitBuilder/src/45p.jpg");
			// BufferedImage image = ImageIO.read(source);
			// int w = 2;
			// int h = 2;
			int [] rgbs = new int[w*h];
			// image.getRGB(0, 0, w, h, rgbs, 0, w);
			// image.flush();
			
			for(int i=0; i<w*h; i++){
				rgbs[i] = i%255;
			}

			// for(int i=0;i<w*h;i++){
			// 	int rgb = (short) (rgbs[i] & 0xFF);
			// 	System.out.println("rgb["+i+"]= "+rgb);result
			// }

			BigInteger[] BigInteger_Image = new BigInteger[w*h];
			for(int i = 0; i < BigInteger_Image.length; i++){
				BigInteger_Image[i] = BigInteger.valueOf(rgbs[i]);
			}
			
			for(int i = 0; i < BigInteger_Image.length; i++){
				circuitEvaluator.setWireValue(original[i],BigInteger_Image[i]);
			}
			long start = System.currentTimeMillis();

			for(int i = 0; i < BigInteger_Image.length; i++){
				BigInteger[] gadget = myMath.getBitArray(BigInteger_Image[i], Config.LOG2_FIELD_PRIME);
				subsetSumHashGadget = new SubsetSumHashGadgetIG(gadget,false);
				BigInteger[] result = subsetSumHashGadget.getOutput();
				
				for(int j = 0; j < result.length; j++){
					circuitEvaluator.setWireValue(output[i*3+j],result[j]);
				}
				// for(int k = 0; k < result.length; k++){
				// 	System.out.println("result["+k+"] :: "+result[k]);
				// }

			}
			long end = System.currentTimeMillis();

			System.out.println( "실행 시간 : " + ( end - start )/1000.0 );

			// circuitEvaluator.setWireValue(answer,1);

			// BigInteger[] gadget = myMath.getBitArray(BigInteger_Image, Config.LOG2_FIELD_PRIME);
			// subsetSumHashGadget = new SubsetSumHashGadgetIG(gadget,false);
			// BigInteger[] result = subsetSumHashGadget.getOutput();
			
			// for(int i = 0; i < result.length; i++){
			// 	circuitEvaluator.setWireValue(output[i],result[i]);
			// }
			// for(int i = 0; i < result.length; i++){
			// 	System.out.println("result["+i+"] :: "+result[i]);
			// }

		// }catch (IOException e) {
		// 	System.err.println(e.getMessage());
		// }
	}
	
	
	public static void main(String[] args) throws Exception {
		
		SubsetSumHashGadge_IG_Test generator = new SubsetSumHashGadge_IG_Test("test_IG");
		generator.generateCircuit();
		generator.evalCircuit();
		generator.prepFiles();
		// generator.runLibsnark();		
	
		Runtime.getRuntime().gc();
// 비교 직전에 gc 를 사용해서 garbage collection을 실행하도록 하면 보다 더 정확하게 메모리 소비량을 얻을 수 있다. 

long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

System.out.println("used memory is " + used + " bytes");
	}

	
}
