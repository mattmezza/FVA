package com.example.earify.algorithms;

import java.util.List;

import android.media.Image;

public abstract class ExtractionAlgorithm {

	public abstract List<Integer> calculate(Image image);
	
	public static String getAlgorithm() {
		return ALGORITHM;
	}
	
	protected static String ALGORITHM;
	
}
