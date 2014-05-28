package it.unisa.earify.algorithms;

import it.unisa.earify.algorithms.lbp.LBP;
import it.unisa.earify.algorithms.sift.SIFT;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Contesto che istanzia la lista concreta di algoritmi per il calcolo delle caratteristiche
 * @author simone
 *
 */
public class FeatureExtraction {

	/**
	 * Istanzia il contesto
	 */
	public FeatureExtraction() {
		this.policy = new ExtractionAlgorithmPolicy();
	}
	
	/**
	 * Restituisce la lista di algoritmi da utilizzare per l'estrazione delle caratteristiche
	 * @return
	 */
	public Set<ExtractionAlgorithm> getFeatureExtractionAlgorithms() {
		Set<ExtractionAlgorithm> feSet = new HashSet<ExtractionAlgorithm>();
		Set<String> algs = policy.getAlgorithms();
		for (Iterator i = algs.iterator(); i.hasNext();) {
			String string = (String) i.next();
			if (string.equals(ExtractionAlgorithmPolicy.LBP)) {
				ExtractionAlgorithm ea = LBP.getInstance();
				feSet.add(ea);
			} else if (string.equals(ExtractionAlgorithmPolicy.SIFT)) {
				ExtractionAlgorithm ea = SIFT.getInstance();
				feSet.add(ea);
			}
		}
		return feSet;
	}
	
	private ExtractionAlgorithmPolicy policy;
	
}
