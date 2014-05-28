package it.unisa.earify.algorithms;

import android.graphics.Bitmap;

/**
 * Immagine trattata dagli algoritmi
 * @author simone
 *
 */
public class Image {
	private String path;
	private Bitmap bitmap;
	
	/**
	 * Restituisce il path dell'immagine
	 * @return
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Imposta il path dell'immagine
	 * @param path Path dell'immagine
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Restituisce la bitmap dell'immagine
	 * @return
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	/**
	 * Imposta la bitmap dell'immagine
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
