package it.unisa.earify.algorithms;

import java.util.Set;

public class AlgorithmFactory {
	private static Set<String> ALGORITHM;
	
	static {
		
	}
	
	public static void addAlgorithm(String pAlgorithm) {
		AlgorithmFactory.ALGORITHM.add(pAlgorithm);
	}
	
	public static Set<ExtractionAlgorithm> getAlgorithms() {
		if (ALGORITHM.equals(""))
			return null;
		else if (ALGORITHM.equals(""))
			return null;
		else
			return null;
	}
}
