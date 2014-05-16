package it.unisa.earify.extractor;

import it.unisa.earify.algorithms.IFeature;

import java.util.List;
import java.util.Map;

import android.media.Image;

public interface IFeatureExtractor {
	
	public void register(List<Image> pImages, String pUsername, int pEar, float pQuality);
	
	public Map<String, List<List<IFeature>>> extractFeature(List<Image> pImages, String pUsername, int pEar, float pQuality);
	
}
