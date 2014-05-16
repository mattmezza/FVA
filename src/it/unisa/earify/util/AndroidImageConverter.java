package it.unisa.earify.util;

import android.graphics.Bitmap;

public class AndroidImageConverter {

	public AndroidImageConverter(Bitmap pPicture) {
		this.picture = pPicture;
		this.pixelTab = this.toPixelsTab();
	}
	
	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public int[] getPixelTab() {
		return pixelTab;
	}

	public void setPixelTab(int[] pixelTab) {
		this.pixelTab = pixelTab;
	}

	private int[] toPixelsTab() {
	    int width = picture.getWidth();
	    int height = picture.getHeight();
	    int[] pixels = new int[width * height];
	    // copy pixels of picture into the tab
	    picture.getPixels(pixels, 0, picture.getWidth(), 0, 0, width, height);
	 
	    // On Android, Color are coded in 4 bytes (argb),
	    // whereas SIFT needs color coded in 3 bytes (rgb)
	 
	    for (int i = 0; i < (width * height); i++)
	        pixels[i] &= 0x00ffffff;
	    return pixels;
	}
	
	private Bitmap picture;
	private int[] pixelTab;
}
