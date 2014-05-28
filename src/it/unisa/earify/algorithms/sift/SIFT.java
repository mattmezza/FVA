package it.unisa.earify.algorithms.sift;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.util.AndroidImageConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import mpi.cbg.fly.Feature;
import android.graphics.Bitmap;

/**
 * Classe che permette di istanziare l'implenentazione corrente dell'algoritmo SIFT
 * @author simone
 *
 */
public class SIFT {

	/**
	 * Restituisce l'implementazioen corrente dell'algoritmo SIFT
	 * @return
	 */
	public static ExtractionAlgorithm getInstance() {
		if(SIFT.instance == null) {
			SIFT.instance = new JSIFT();
		}
		return SIFT.instance;
	}
	
	private static ExtractionAlgorithm instance;

}
