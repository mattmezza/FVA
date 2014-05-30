package it.unisa.earify.algorithms.lbp;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.sift.JSIFT;
import it.unisa.earify.algorithms.sift.SIFT;

import java.util.List;

import android.graphics.Bitmap;

/**
 * Algoritmo LBP
 * @author simone
 *
 */
public class LBP {
	public static final int ROWS = 5;
	public static final int COLS = 5;

	/**
	 * Restituisce l'istanza contenente l'implementazione corrente dell'algoritmo
	 * LBP
	 * @return
	 */
	public static ExtractionAlgorithm getInstance() {
		if(LBP.instance == null) {
			LBP.instance = new NativeLBP(ROWS, COLS);
		}
		return LBP.instance;
	}
	
	private static ExtractionAlgorithm instance;

}
