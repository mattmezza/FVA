package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.exceptions.InvalidActionException;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class FeatureExtractorTask extends AsyncTask<String, Void, String> {

	public FeatureExtractorTask(int pAction, List<Bitmap> pImages, String pUsername, int pEar, float pQuality) {
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
	protected String doInBackground(String... params) {
		try {
			this.result = this.fea.extractFeatures(this.action, this.images, this.username, this.ear, this.quality);
		} catch (Exception e) {
			this.ex = e;
			if (this.delegate != null)
				this.delegate.onExtractorError(e);
			return null;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(this.delegate!=null && this.ex==null)
			this.delegate.onExtractorFinished(this.result);
	}

	private ExtractorDelegate delegate;
	
	private int action;
	private List<Bitmap> images;
	private String username;
	private int ear;
	private float quality;
	private FeatureExtractorAbstraction fea = new FeatureExtractorAbstraction();
	private Map<String, List<List<IFeature>>> result = null;
	private Exception ex;
	
}
