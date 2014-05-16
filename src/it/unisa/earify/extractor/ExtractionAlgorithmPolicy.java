package it.unisa.earify.extractor;

import it.unisa.earify.config.Config;

import java.util.HashSet;
import java.util.Set;

public class ExtractionAlgorithmPolicy {

	public static final String LBP = "LBP";
	public static final String SIFT = "SIFT";
	
	public Set<String> getAlgorithms() {
		Config config = Config.getInstance();
		Set<String> algs = new HashSet<String>();
		if(config.lbp()) {
			algs.add(LBP);
		}
		if(config.sift()) {
			algs.add(SIFT);
		}
		return algs;
	}
	
}
