package it.unisa.earify.algorithms;

import java.util.List;

public interface ExtractionAlgorithm {

	public List<IFeature> calculate(Image image);
	
	public String getName();
	
}
