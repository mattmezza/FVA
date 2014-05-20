package it.unisa.earify.algorithms.lbp;

import it.unisa.earify.algorithms.IFeature;

import java.io.Serializable;

public class LBPFeature implements IFeature, Serializable {
	private static final long serialVersionUID = 1L;
	
	public float[] descriptors;
	
	@Override
	public double getDistance(IFeature pFeature) {
		return 0;
	}
}
