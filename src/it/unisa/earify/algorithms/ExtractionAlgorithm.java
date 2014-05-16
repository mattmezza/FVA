package it.unisa.earify.algorithms;

import java.util.List;

import android.graphics.Bitmap;

public interface ExtractionAlgorithm {

	public List<IFeature> calculate(Bitmap image);
	
	public String getName();
	
}
