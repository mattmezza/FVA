package it.unisa.earify.extractor;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.extractor.exceptions.InvalidActionException;

import java.util.List;
import java.util.Map;

import android.media.Image;

public class FeatureExtractorAbstraction {
	public static final int REGISTRATION = 0;
	public static final int VERIFICATION = 1;
	public static final int RECOGNITION	 = 2;
	
	public static final int EAR_DX = 0;
	public static final int EAR_SX = 1;
	
	private IFeatureExtractor implementor;
	
	public FeatureExtractorAbstraction() {
		this.implementor = new FeaturesExtractor();
	}
	
	public Map<String, List<List<IFeature>>> extractFeatures(int pAction, List<Image> pImages, String pUsername, int pEar, float pQuality) throws InvalidActionException {
		if (pAction == REGISTRATION) {
			this.implementor.register(pImages, pUsername, pEar, pQuality);
			return null;
		} else if (pAction == VERIFICATION) {
			return this.implementor.extractFeature(pImages, pUsername, pEar, pQuality);
		} else if (pAction == RECOGNITION) {
			return this.implementor.extractFeature(pImages, pUsername, pEar, pQuality);
		}
		
		throw new InvalidActionException(pAction);
	}
}
