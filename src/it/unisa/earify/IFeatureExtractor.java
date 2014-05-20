package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;

import java.util.List;
import java.util.Map;

public interface IFeatureExtractor {
	
	public void register(List<Image> pImages, String pUsername, int pEar, float pQuality);
	
	public Map<String, List<List<IFeature>>> extractFeature(List<Image> pImages, String pUsername, int pEar, float pQuality);
	
}
