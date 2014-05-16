package it.unisa.earify.algorithms.lbp;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;

import java.util.List;

import android.graphics.Bitmap;

public class NativeLBP implements ExtractionAlgorithm {

	public static final String NAME = "LBP";
	
	@Override
	public List<IFeature> calculate(Bitmap image) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return NativeLBP.NAME;
	}

}
