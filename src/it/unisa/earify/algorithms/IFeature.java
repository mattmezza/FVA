package it.unisa.earify.algorithms;

import java.io.Serializable;

public interface IFeature extends Serializable {

	public double getDistance(IFeature pFeature);
	
}
