package it.unisa.earify.algorithms;

import java.io.Serializable;

/**
 * Generica feature, estratta da un algoritmo di estrazione delle caratteristiche
 * @author simone
 *
 */
public interface IFeature extends Serializable {

	/**
	 * Calcola la distanza della feature rispetto ad un'altra feature. Le due feature dovrebbero essere
	 * dello stesso tipo
	 * @param pFeature Feature da confrontare
	 * @return Distanza
	 */
	public double getDistance(IFeature pFeature);
	
}
