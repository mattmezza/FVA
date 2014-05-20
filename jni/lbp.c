#include <opencv/cv.h>
#include <opencv/highgui.h>

#define MAXBYTE 256
#define GRIDSIZE 3

#define getPixel(img, x, y) img->imageData[x + y*img->width]
#define sizeFromZero(width, height) width * height - 1
#define center(width, height) sizeFromZero(width, height)/2

typedef unsigned char PIXEL;
typedef unsigned char BINARY_PATH;

/*
 * Calcola il valore binario di una griglia 3x3 di pixel
 */
BINARY_PATH lbpGetGridValue(PIXEL* pixels) {
	PIXEL value = 0;
	int center = pixels[center(GRIDSIZE, GRIDSIZE)];
	int i;

	for (i = 0; i < sizeFromZero(GRIDSIZE, GRIDSIZE); i++)
		if (i < center(GRIDSIZE, GRIDSIZE)) {
			if (pixels[i] >= center)
				value += pow(2, i);
		} else {
			if (pixels[i] >= center)
				value += pow(2, i-1);
		}

	return value;
}

/*
 * Calcola e restituisce l'istogramma del blocco in analisi. Il risultato è un puntatore
 * a un array di 256 interi (dove l'i-esimo valore indica il numero di elementi con valore i)
 */
int* lbpGetBlockValue(IplImage *src) {
	int* buckets = (int*)malloc(sizeof(int) * sizeof(BINARY_PATH));
	PIXEL* currentGrid = (PIXEL*)malloc(sizeof(PIXEL) * GRIDSIZE * GRIDSIZE);

	int i, j;

	for (i = 0; i < sizeof(BINARY_PATH); i++)
		buckets[i] = 0;

	for (i = 0; i < src->height / GRIDSIZE; i++) {
		for (j = 0; j < src->width / GRIDSIZE; j++) {
			int x, y;
			int k = 0;
			//Coordinate x e y del pixel in alto a sinistra della griglia corrente
			for (y = i * GRIDSIZE; y < (i+1) * GRIDSIZE; y++)
				for (x = j * GRIDSIZE; x < (j+1) * GRIDSIZE; x++)
					currentGrid[k] = getPixel(src, x, y);

			buckets[lbpGetGridValue(currentGrid)]++;
		}
	}

	return buckets;
}

/*
 * Calcola il vettore della caratteristiche di LBP e lo restituisce
 */
int* lbp(IplImage *src, int rows, int cols) {
	//Grandezza dei blocchi
	int blockWidth = src->width / cols;
	int blockHeight = src->height / rows;

	//Definisce il vettore che conterrà il risultato
	int* vector = (int*)malloc(sizeof(int)*256*rows*cols);
	int vectorCursor = 0;

	int i;
	int j;
	int k;
	//Scorre tutti i blocchi dell'immagine
	for (i = 0; i < rows; i++) {
		for (j = 0; j < cols; j++) {
			//Estrae il rettangolo relativo all'area di interesse e lo mette in "crop"
			cvSetImageROI(src, cvRect(blockWidth*j, blockHeight*i, blockWidth, blockHeight));
			IplImage *crop = cvCreateImage(cvGetSize(src),
									   src->depth,
									   src->nChannels);
			cvCopy(src, crop, NULL);
			cvResetImageROI(src);

			//Crea l'istogramma del blocco e lo aggiunge al vettore del risultato
			int* blockIstogram = lbpGetBlockValue(crop);
			for (k = 0; k < sizeof(BINARY_PATH); k++) {
				vector[vectorCursor] = blockIstogram[k];
				vectorCursor++;
			}
			free(blockIstogram); //Cancella la porzione
		}
	}

	return vector;
}
