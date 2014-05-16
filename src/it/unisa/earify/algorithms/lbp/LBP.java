package it.unisa.earify.algorithms.lbp;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.sift.JSIFT;
import it.unisa.earify.algorithms.sift.SIFT;

import java.util.List;

import android.graphics.Bitmap;

public class LBP {


	public static ExtractionAlgorithm getInstance() {
		if(LBP.instance == null) {
			LBP.instance = new JSIFT();
		}
		return LBP.instance;
	}
	
	private static ExtractionAlgorithm instance;

}
