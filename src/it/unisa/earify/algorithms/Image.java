package it.unisa.earify.algorithms;

import android.graphics.Bitmap;

public class Image {
	private String path;
	private Bitmap bitmap;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
