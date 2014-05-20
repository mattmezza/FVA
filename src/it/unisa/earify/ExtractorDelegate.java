package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;

import java.util.List;
import java.util.Map;

public interface ExtractorDelegate {

	public void onExtractorFinished(Map<String, List<List<IFeature>>> result);
	
	public void onExtractorError(Exception e);
	
}
