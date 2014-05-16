package it.unisa.earify.extractor;

import it.unisa.earify.algorithms.ExtractionAlgorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FeatureExtraction {

	public FeatureExtraction() {
		this.policy = new ExtractionAlgorithmPolicy();
	}
	
	public Set<ExtractionAlgorithm> getFeatureExtractionAlgorithms() {
		Set<ExtractionAlgorithm> feSet = new HashSet<ExtractionAlgorithm>();
		Set<String> algs = policy.getAlgorithms();
		for (Iterator i = algs.iterator(); i.hasNext();) {
			String string = (String) i.next();
			if (string.equals(ExtractionAlgorithmPolicy.LBP)) {
				//FeatureExtraction fe = new LBPExtractor();
				//feSet.add(fe);
			} else if (string.equals(ExtractionAlgorithmPolicy.SIFT)) {
				//FeatureExtraction fe = new SIFTExtractor();
				//feSet.add(fe);
			}
		}
		return feSet;
	}
	
	private ExtractionAlgorithmPolicy policy;
	
}
