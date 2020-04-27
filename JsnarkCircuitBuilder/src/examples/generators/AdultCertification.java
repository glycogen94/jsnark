package examples.generators;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;

public class AdultCertification extends CircuitGenerator {
	private Wire age;
	
	public AdultCertification(String circuitName) {
		super(circuitName);
	}
	
	@Override
		protected void buildCircuit() {
			age = createProverWitnessWire("age");
			Wire check = age.isGreaterThanOrEqual(19, 32);
			makeOutput(check);
		}
	
	@Override
		public void generateSampleInput(CircuitEvaluator circuitEvaluator) {

			circuitEvaluator.setWireValue(age, 20);
		}
	
	public static void main(String[] args) throws Exception {
		AdultCertification generator = new AdultCertification("AdultCertification");
		generator.generateCircuit();
		generator.evalCircuit();
		generator.prepFiles();
		generator.runLibsnark();
	}
}
