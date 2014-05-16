package it.unisa.earify.algorithms.sift;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.util.AndroidImageConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import mpi.cbg.fly.Feature;
import android.graphics.Bitmap;

public class JSIFT implements ExtractionAlgorithm {

	public static final String NAME = "SIFT";
	
	@Override
	public List<IFeature> calculate(Bitmap image) {
		AndroidImageConverter converter = new AndroidImageConverter(image);
		// convert bitmap to pixels table
		int pixels[] = converter.getPixelTab();

		// get the features detected into a vector
		Vector<Feature> features = mpi.cbg.fly.SIFT.getFeatures(
				image.getWidth(), image.getHeight(), pixels);
		List<IFeature> myFeatures = new ArrayList<IFeature>(features.size());
		for (Feature f : features) {
			myFeatures.add(new SIFTFeature(f));
		}
		return myFeatures;
	}

	@Override
	public String getName() {
		return JSIFT.NAME;
	}

}
