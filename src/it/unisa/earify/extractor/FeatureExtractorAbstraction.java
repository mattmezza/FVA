package it.unisa.earify.extractor;

import java.util.List;
import java.util.Map;

import android.media.Image;

public class FeatureExtractorAbstraction {
	public static final int REGISTRATION = 0;
	public static final int VERIFICATION = 1;
	public static final int RECOGNITION	 = 2;
	
	public Map<String, List<List<Integer>>> extractFeatures(int pAction, List<Image> images, String username, float quality) {
		return null;
	}
}
