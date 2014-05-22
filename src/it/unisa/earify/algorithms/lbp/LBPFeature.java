package it.unisa.earify.algorithms.lbp;

import it.unisa.earify.algorithms.IFeature;

import java.io.Serializable;

public class LBPFeature implements IFeature, Serializable {
	private static final long serialVersionUID = 1L;
	
	public int[] descriptors;
	
	@Override
	public double getDistance(IFeature pFeature) {
		if (pFeature instanceof LBPFeature) {
			LBPFeature feature = (LBPFeature)pFeature;
			
			if (feature.descriptors.length != feature.descriptors.length)
				return -1;
			
			double distance = 0;
			for (int i = 0; i < descriptors.length; i++) {
				distance += Math.pow(descriptors[i] - feature.descriptors[i], 2);
			}
			
			return Math.sqrt(distance);
		} else
			return -1;
	}
}
