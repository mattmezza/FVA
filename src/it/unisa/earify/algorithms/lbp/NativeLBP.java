package it.unisa.earify.algorithms.lbp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;

/**
 * Algoritmo che implementa l'algoritmo LBP nativo in C, utilizzando la libreria esterna
 * caricata.
 * @author simone
 *
 */
public class NativeLBP implements ExtractionAlgorithm {

	public static final String NAME = "LBP";
	private int rows;
	private int cols;
	
	public NativeLBP(int pRows, int pCols) {
		this.rows = pRows;
		this.cols = pCols;
	}
	
	@Override
	public List<IFeature> calculate(Image image) {
		int[] result = new LBPNativeLibrary().extractFeatures(image.getPath(), this.rows, this.cols);
		List<IFeature> features = new ArrayList<IFeature>();
		LBPFeature mainFeature = new LBPFeature();
		mainFeature.descriptors = result;
		features.add(mainFeature);
		
		return features;
	}

	@Override
	public String getName() {
		return NativeLBP.NAME;
	}
}
