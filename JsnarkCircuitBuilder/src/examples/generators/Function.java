/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package examples.generators;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;

public class Function extends CircuitGenerator {

	private Wire input;

	public Function(String circuitName) {
		super(circuitName);
	}

	@Override
	protected void buildCircuit() {
		// declare input.
		input = createInputWire("input");
		// r1 = in * in
		Wire r1 = input.mul(input);
		// r2 = r1 * in
		Wire r2 = r1.mul(input);
		// result = r2 + in + 5
		Wire result = input.add(5).add(r2);
		// mark the wire as output
		makeOutput(result);
	}

	@Override
	public void generateSampleInput(CircuitEvaluator circuitEvaluator) {
			circuitEvaluator.setWireValue(input, 3);
	}

	public static void main(String[] args) throws Exception {
		Function generator = new Function("Function");
		generator.generateCircuit();
		generator.evalCircuit();
		generator.prepFiles();
		generator.runLibsnark();
	}

}
