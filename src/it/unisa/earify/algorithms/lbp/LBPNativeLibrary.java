package it.unisa.earify.algorithms.lbp;

import android.util.Log;

public class LBPNativeLibrary {
	public native int[] extractFeatures(String pPath, int pWidth, int pHeight);
}
