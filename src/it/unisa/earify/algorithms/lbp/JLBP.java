package it.unisa.earify.algorithms.lbp;

import java.util.ArrayList;
import java.util.List;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Implementazione Java dell'algoritmo LBP
 * @author simone
 *
 */
public class JLBP implements ExtractionAlgorithm {
	
	private static final int MAXBYTE = 256;
	private static final int GRIDSIZE = 3;
	
	/*
	 * Calcola il valore binario di una griglia 3x3 di pixel
	 */
	private char lbpGetGridValue(char[] pixels) {
		char value = 0;
		char centerValue = pixels[center(GRIDSIZE, GRIDSIZE)];

		for (int i = 0; i < center(GRIDSIZE, GRIDSIZE); i++) {
			if (pixels[i] >= centerValue)
				value += (char)Math.pow(2, i);
		}

		for (int i = center(GRIDSIZE, GRIDSIZE)+1; i < size(GRIDSIZE, GRIDSIZE); i++) {
			if (pixels[i] >= centerValue)
				value += (char)Math.pow(2, i-1);
		}
		
		return value;
	}

	/*
	 * Calcola e restituisce l'istogramma del blocco in analisi. Il risultato è un puntatore
	 * a un array di 256 interi (dove l'i-esimo valore indica il numero di elementi con valore i)
	 */
	private int[] lbpGetBlockValue(Bitmap img) {
		int[] buckets = new int[MAXBYTE];
		char[] currentGrid = new char[GRIDSIZE * GRIDSIZE];

		for (int i = 0; i < MAXBYTE; i++)
			buckets[i] = 0;

		for (int i = 0; i < img.getHeight()-3; i++) {
			for (int j = 0; j < img.getWidth()-3; j++) {
				int k = 0;
				//Coordinate x e y del pixel in alto a sinistra della griglia corrente
				//NOTA: Deve scorrere tutti i pixel!!!
				for (int y = i; y < i+3; y++)
					for (int x = j; x < j+3; x++) {
						currentGrid[k] = (char) getPixel(img, x, y);
						k++;
					}
				
				buckets[lbpGetGridValue(currentGrid)]++;
			}
		}
		return buckets;
	}
	
	/*
	 * Calcola il vettore della caratteristiche di LBP e lo restituisce
	 */
	
	public int[] localBinaryPattern(Bitmap img, int rows, int cols) {
		//conversione a scala di grigi
		img = toGrayScale(img);
		
		//grandezza dei blocchi
		int blockWidth = img.getWidth()/cols;
		int blockHeight = img.getHeight()/rows;
		
		//Definisce il vettore che conterra'� il risultato
		int[] vector = new int[MAXBYTE * rows * cols];
		int vectorCursor = 0;
		
		//scorre tutti i blocchi dell'immagine
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				//Estrae il rettangolo relativo all'area di interesse e lo mette in "crop"
				Bitmap crop = Bitmap.createBitmap(img, blockWidth*j, blockHeight*i, blockWidth, blockHeight);
				
				//Crea l'istogramma del blocco e lo aggiunge al vettore del risultato
				int[] blockIstogram = lbpGetBlockValue(crop);
				for (int k = 0; k < MAXBYTE; k++) {
					vector[vectorCursor] = blockIstogram[k];
					vectorCursor++;
				}
			}
		}		
		return vector;
	}
	
	public Bitmap toGrayScale(Bitmap bmpOriginal)
	{        
	    int width, height;
	    height = bmpOriginal.getHeight();
	    width = bmpOriginal.getWidth();    

	    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    Canvas c = new Canvas(bmpGrayscale);
	    Paint paint = new Paint();
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	    paint.setColorFilter(f);
	    c.drawBitmap(bmpOriginal, 0, 0, paint);
	    return bmpGrayscale;
	}
	
	private int getPixel(Bitmap img, int x, int y){
		return img.getPixel(x,y);
	}
	
	private int size(int width, int height) {
		return width*height;
	}
	
	private int center(int width, int height) {
		return sizeFromZero(width, height)/2;
	}
	
	private int sizeFromZero(int width, int height){
		return(size(width, height) - 1);
	}

	/**
	 * Restituisce una lista composta da una singola caratteristica
	 */
	@Override
	public List<IFeature> calculate(Image image) {
		List<IFeature> result = new ArrayList<IFeature>();
		LBPFeature feature = new LBPFeature();
		feature.descriptors = this.localBinaryPattern(image.getBitmap(), 5, 5);
		result.add(feature);
		return result;
	}

	@Override
	public String getName() {
		return "LBP";
	}

}