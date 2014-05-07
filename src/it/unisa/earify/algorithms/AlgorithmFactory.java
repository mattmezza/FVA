package it.unisa.earify.algorithms;

import java.util.HashSet;
import java.util.Set;

public class AlgorithmFactory {
	private static Set<String> ALGORITHMS;
	
	static {
		AlgorithmFactory.ALGORITHMS = new HashSet<String>();
	}
	
	public static void addAlgorithm(String pAlgorithm) {
		AlgorithmFactory.ALGORITHMS.add(pAlgorithm);
	}
	
	public static Set<ExtractionAlgorithm> getAlgorithms() {
		Set<ExtractionAlgorithm> algorithms = new HashSet<ExtractionAlgorithm>();
		
		for (String algorithmType : AlgorithmFactory.ALGORITHMS) {
			if (algorithmType.equals("SIFT"))
				algorithms.add(null);
			else if (algorithmType.equals("LBP"))
				algorithms.add(null);
		}
		
		return algorithms;
	}
}
