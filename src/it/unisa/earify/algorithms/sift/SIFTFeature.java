package it.unisa.earify.algorithms.sift;

import mpi.cbg.fly.Feature;
import it.unisa.earify.algorithms.IFeature;

public class SIFTFeature implements IFeature {

	public SIFTFeature(Feature pFeature) {
		this.feature = pFeature;
	}
	
	@Override
	public double getDistance(IFeature pFeature) {
		SIFTFeature f = (SIFTFeature) pFeature;
		return this.feature.descriptorDistance(f.getFeature());
	}
	
	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	
	private Feature feature;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
