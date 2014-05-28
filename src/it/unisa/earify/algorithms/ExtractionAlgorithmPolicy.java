package it.unisa.earify.algorithms;

import it.unisa.earify.config.Config;

import java.util.HashSet;
import java.util.Set;

/**
 * Componente in grado di decidere la lista degli algoritmi da utilizzare per l'estrazione delle caratteristiche
 * @author simone
 *
 */
public class ExtractionAlgorithmPolicy {

	/**
	 * Algoritmo LBP
	 */
	public static final String LBP = "LBP";
	/**
	 * Algoritmo SIFT
	 */
	public static final String SIFT = "SIFT";
	
	/**
	 * Restituisce una lista di nomi di algoritmi da utilizzare. Legge il file di configurazione per prendere
	 * la decisione
	 * @return
	 */
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
