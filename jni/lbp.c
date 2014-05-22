#include "lbp.h"

#define getPixel(img, x, y) img->imageData[x + y*img->width]
#define size(width, height) (width * height)
#define sizeFromZero(width, height) (size(width, height) - 1)
#define center(width, height) sizeFromZero(width, height)/2

/*
 * Calcola il valore binario di una griglia 3x3 di pixel
 */
BINARY_PATH lbpGetGridValue(PIXEL* pixels) {
	BINARY_PATH value = 0;
	PIXEL centerValue = pixels[center(GRIDSIZE, GRIDSIZE)];
	int i;

	for (i = 0; i < center(GRIDSIZE, GRIDSIZE); i++) {
		if (pixels[i] >= centerValue)
			value += (BINARY_PATH)pow(2, i);
	}

	for (i = center(GRIDSIZE, GRIDSIZE)+1; i < size(GRIDSIZE, GRIDSIZE); i++) {
		if (pixels[i] >= centerValue)
			value += (BINARY_PATH)pow(2, i-1);
	}

	return value;
}

/*
 * Calcola e restituisce l'istogramma del blocco in analisi. Il risultato è un puntatore
 * a un array di 256 interi (dove l'i-esimo valore indica il numero di elementi con valore i)
 */
int* lbpGetBlockValue(IplImage *src) {
	int* buckets = (int*)malloc(sizeof(int) * MAXBYTE);
	PIXEL* currentGrid = (PIXEL*)malloc(sizeof(PIXEL) * GRIDSIZE * GRIDSIZE);

	int i, j;

	for (i = 0; i < MAXBYTE; i++)
		buckets[i] = 0;

	for (i = 0; i < src->height-3; i++) {
		for (j = 0; j < src->width-3; j++) {
			int x, y;
			int k = 0;
			//Coordinate x e y del pixel in alto a sinistra della griglia corrente
			//NOTA: Deve scorrere tutti i pixel!!!
			for (y = i; y < i+3; y++)
				for (x = j; x < j+3; x++) {
					currentGrid[k] = getPixel(src, x, y);
					k++;
				}

			buckets[lbpGetGridValue(currentGrid)]++;

		}
	}

	free(currentGrid);

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
	int* vector = (int*)malloc(sizeof(int) * MAXBYTE * rows * cols);
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
			for (k = 0; k < MAXBYTE; k++) {
				vector[vectorCursor] = blockIstogram[k];
				vectorCursor++;
			}
			free(blockIstogram); //Cancella la porzione
		}
	}

	return vector;
}

void myDEBUG_STRING(char* string) {
	__android_log_write(ANDROID_LOG_VERBOSE,"DEBUGINFO", string);
}

void myDEBUG_INT(int n) {
	char string[10];
	sprintf(string, "%d", n);
	myDEBUG_STRING(string);
}

void myDEBUG_ARRINT(int size, int* array) {
	int i;

	for (i = 0; i < size; i++)
		myDEBUG_INT(array[i]);
}
