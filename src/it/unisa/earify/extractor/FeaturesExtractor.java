package it.unisa.earify.extractor;

import it.unisa.earify.algorithms.IFeature;

import java.util.List;
import java.util.Map;

import org.opencv.android.OpenCVLoader;

import android.media.Image;

public class FeaturesExtractor implements IFeatureExtractor {
	private static final String DATABASE_PATH = "data.db";

	@Override
	public void register(List<Image> pImages, String pUsername, int pEar, float pQuality) {
		Map<String, List<List<IFeature>>> features = this.extractFeature(pImages, pUsername, pEar, pQuality);
		
		
	}

	@Override
	public Map<String, List<List<IFeature>>> extractFeature(List<Image> pImages, String pUsername, int pEar, float pQuality) 	 {
		return null;
	}

}
