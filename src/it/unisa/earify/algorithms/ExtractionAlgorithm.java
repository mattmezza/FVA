package it.unisa.earify.algorithms;

import java.util.List;

/**
 * Generico algoritmo di estrazione delle caratteristiche. Un algoritmo concreto di estrazione
 * delle caratteristiche deve implementare questa interfaccia
 * @author simone
 *
 */
public interface ExtractionAlgorithm {

	/**
	 * Calcola il vettore delle caratteristiche relativo all'immagine data
	 * @param image Immagine
	 * @return Vettore delle caratteristiche dell'immagine
	 */
	public List<IFeature> calculate(Image image);
	
	/**
	 * Restituisce il nome dell'algortimo
	 * @return Nome dell'algoritmo
	 */
	public String getName();
	
}
