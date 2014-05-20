package it.unisa.earify.algorithms.lbp;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class NativeLBP implements ExtractionAlgorithm {

	public static final String NAME = "LBP";
	
	@Override
	public List<IFeature> calculate(Image image) {
		new LBPNativeLibrary().extractFeatures(image.getPath(), "");
		
		List<IFeature> features = new ArrayList<IFeature>();
		/*
		LBPFeature mainFeature = new LBPFeature();
		mainFeature.descriptors = calculatedFeatures;
		features.add(mainFeature);
		*/
		return features;
	}

	@Override
	public String getName() {
		return NativeLBP.NAME;
	}
}
