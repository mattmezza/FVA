package it.unisa.earify.algorithms.lbp;

import android.util.Log;

/**
 * Libreria che carica l'algoritmo LBP nativo
 * @author simone
 *
 */
public class LBPNativeLibrary {
	public native int[] extractFeatures(String pPath, int pWidth, int pHeight);
}
