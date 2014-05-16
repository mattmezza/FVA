package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.media.Image;

public interface IFeatureExtractor {
	
	public void register(List<Bitmap> pImages, String pUsername, int pEar, float pQuality);
	
	public Map<String, List<List<IFeature>>> extractFeature(List<Bitmap> pImages, String pUsername, int pEar, float pQuality);
	
}
