package it.unisa.earify.extractor;

import java.util.List;
import java.util.Map;

import android.media.Image;

public interface IFeatureExtractor {

	public void register(List<Image> images, String username, float quality);
	
	public Map<String, List<List<Integer>>> extractFeature(List<Image> images, String username, float quality);
	
}
