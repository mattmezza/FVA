package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.exceptions.InvalidActionException;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

public class FeatureExtractorRunnable implements Runnable {

	public FeatureExtractorRunnable(int pAction, List<Image> pImages, String pUsername, int pEar, float pQuality) {
		this.action = pAction;
		this.images = pImages;
		this.username = pUsername;
		this.ear = pEar;
		this.quality = pQuality;
	}
	
	public void setDelegate(ExtractorDelegate extractorDelegate) {
		this.delegate = extractorDelegate;
	}
	
	@Override
	public void run() {
		FeatureExtractorAbstraction fea = new FeatureExtractorAbstraction();
		Map<String, List<List<IFeature>>> result = null;
		try {
			result = fea.extractFeatures(this.action, this.images, this.username, this.ear, this.quality);
		} catch (InvalidActionException e) {
			if (this.delegate!=null)
				this.delegate.onExtractorError(e);
			return;
		} catch (Exception e) {
			if (this.delegate != null)
				this.delegate.onExtractorError(e);
		}
		if(this.delegate!=null)
			delegate.onExtractorFinished(result);
	}

	private ExtractorDelegate delegate;
	
	private int action;
	private List<Image> images;
	private String username;
	private int ear;
	private float quality;
	
}
