package it.unisa.earify.algorithms.sift;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mpi.cbg.fly.Feature;
import it.unisa.earify.algorithms.IFeature;

/**
 * Feature relativa all'algoritmo SIFT.
 * @author simone
 *
 */
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
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeFloat(this.feature.scale);
		out.writeFloat(this.feature.orientation);
		out.writeInt(this.feature.descriptor.length);
		for (int i = 0; i < this.feature.descriptor.length; i++)
			out.writeFloat(this.feature.descriptor[i]);
		out.writeInt(this.feature.location.length);
		for (int i = 0; i < this.feature.location.length; i++)
			out.writeFloat(this.feature.location[i]);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.feature = new Feature();
		this.feature.scale = in.readFloat();
		this.feature.orientation = in.readFloat();
		
		int totalDescriptors = in.readInt();
		this.feature.descriptor = new float[totalDescriptors];
		for (int i = 0; i < totalDescriptors; i++)
			this.feature.descriptor[i] = in.readFloat();
		
		int totalLocations = in.readInt();
		this.feature.location = new float[totalLocations];
		for (int i = 0; i < totalLocations; i++)
			this.feature.location[i] = in.readFloat();
	}
	
	private Feature feature;

	private static final long serialVersionUID = 1L;
}
