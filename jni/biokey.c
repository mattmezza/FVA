
// definizione delle macro
#define BL_STRLEN 512
#define BL_MODELSIZE 84
#define BL_MAXDBSIZE 4096
#define BL_MAXVECTLEN 65536
#define BL_MAX_FACE_TRACK 16
#define BL_MAX_IDENTITIES 1024
#define BL_MAX_NUMSAMPLES 1024
#define BL_CONTRAST_MASK_SIZE 10

// definizione delle macro per il trattamento del BioKeyDB
#define BL_DB_FLIP      1
#define BL_DB_LOCALIZE  2
#define BL_DB_NORMALIZE 4
#define BL_DB_EXTRACT   8
#define BL_DB_LOG       16
#define BL_DB_SAVEFACE  32


// FLAGS per le opzioni da attivare/disttivare
#define BL_STASM 1
#define BL_VERBOSE 2

// FLAGS per le condizioni di errore
#define BL_OK_STATUS      0
#define BL_ERR_INIT       1
#define BL_ERR_BIOKEY     2
#define BL_ERR_MEM        4
#define BL_ERR_NOFACE     8
#define BL_ERR_BIOKEYDB  16
#define BL_ERR_NOSAMPLES 32
#define BL_ERR_GENERIC   64

// definiamo alcune macro
#define min(a,b) ((a<b)?a:b)
#define max(a,b) ((a<b)?b:a)
//#define isinf(a) ((a>100000000)?1:0)
//#define isnan(a) (0)

// variabili di stato per la gestione dei codici di errore
extern int  _bl_err;
extern char _bl_err_str[BL_STRLEN];

// definizione del tipo di dato pixel
typedef unsigned char PIXEL;

struct blBioKey {
	int width;
	int height;
	PIXEL **Image;
	PIXEL **M;
	long **PM2;
};



struct blScore {
	int DBpos;
	double value;
	double SRR;
};

// definizione della macro per l'allocazione di array bidimensionali
#define matrix_allocate(matrix, cols, rows, TYPE) { TYPE *imptr; int _i; matrix = (TYPE **)malloc((rows)*sizeof(TYPE *)); imptr = (TYPE*)calloc((long)(cols)*(long)(rows),sizeof(TYPE)); if (imptr == NULL){printf("\nNo memory in matrix allocate.\n"); exit(-1);} for (_i = 0; _i<rows; ++_i, imptr += cols) matrix[_i] = imptr; }



int blSelfQuotientNormalization(IplImage *src, int stp)
{
	int i=0;
	float val = 0.0;
	IplImage *sqi = NULL;

	sqi = cvCloneImage(src);

	if(stp%2==0)
		++stp;

	cvSmooth(src, sqi, CV_GAUSSIAN, stp, 0, 0, 0);

	for(i=0; i<src->width*src->height*src->nChannels; i++){
		val = ((float)(src->imageData[i]))/((float)(sqi->imageData[i]));
		src->imageData[i] = (char)(128*val);
	}
		
	cvNamedWindow("risultato", 1);
	cvShowImage("risultato", src);

}



// funzione per il caricamento di una chiave biometrica
int blReadBioKey(char *nomefile, struct blBioKey *biokey)
{
	FILE *fp = NULL;
	int row = 0;
	int col = 0;
	int cnt = 0;
	int p = 0;

	_bl_err = 0;
	

	if(!biokey){
		sprintf(_bl_err_str, "blReadBioKey(Error): memoria insufficiente per allocare la chiave biometrica");
		_bl_err = BL_ERR_MEM;
    	return _bl_err;
	}

	fp = fopen(nomefile, "r");

	if(!fp){
		sprintf(_bl_err_str, "blReadBioKey(Error): impossibile caricare la chiave biometrica: %s", nomefile);
		_bl_err = BL_ERR_BIOKEY;
    	return _bl_err;
	}

	fscanf(fp, "%d", &p);
	(biokey)->width = (int)p;
	fscanf(fp, "%d", &p);
	(biokey)->height = (int)p;

	matrix_allocate(((biokey)->Image), ((biokey)->width), ((biokey)->height), PIXEL)
	matrix_allocate(((biokey)->M), ((biokey)->width), ((biokey)->height), PIXEL)
	matrix_allocate(((biokey)->PM2), ((biokey)->width), ((biokey)->height), long)

	for(row=0; row<(biokey)->height; row++)
		for(col=0; col<(biokey)->width; col++,++cnt){
			fscanf(fp, "%d", &p);
			(biokey)->Image[row][col] = p;
			fscanf(fp, "%d", &p);
			(biokey)->M[row][col] = p;
			fscanf(fp, "%d", &p);
			(biokey)->PM2[row][col] = p;
		}

	fclose(fp);

	return _bl_err;
}

int blBuildBioKey(IplImage *src_ipl, struct blBioKey *biokey, int ch)
{
	int x1 = 0;
	int y1 = 0;
	int row = 0;
	int col = 0;
	int cnt = 0;
	int gray = 3;
	int stp=8; 
	int blk_w = 8;
	int blk_h = 8;
	unsigned int m = 0;
	PIXEL p = 0;

	_bl_err = 0;

	if(ch == -1){
		ch = 0;
		gray = 0;
	}

	if(!biokey){
		sprintf(_bl_err_str, "blBuildBioKey(Error): memoria insufficiente per allocare la chiave biometrica");
		_bl_err = BL_ERR_MEM;
    	return _bl_err;
	}

	matrix_allocate(((biokey)->Image), (src_ipl->width), (src_ipl->height), PIXEL)
	matrix_allocate(((biokey)->M),      (src_ipl->width), (src_ipl->height), PIXEL)
	matrix_allocate(((biokey)->PM2), (src_ipl->width), (src_ipl->height), long)

	(biokey)->width  = (src_ipl->width);
	(biokey)->height = (src_ipl->height);

	for (y1=0;y1<src_ipl->height;y1++)
		for (x1=0;x1<src_ipl->width;x1++)
			(biokey)->Image[y1][x1] = 
			(
			(PIXEL)((src_ipl->imageData+src_ipl->widthStep*y1)[x1*gray + 0]) +
			(PIXEL)((src_ipl->imageData+src_ipl->widthStep*y1)[x1*gray + 1]) +
			(PIXEL)((src_ipl->imageData+src_ipl->widthStep*y1)[x1*gray + 2])
			)/3;

			
	for (y1=0;y1<src_ipl->height-blk_h-1;y1++)
		for (x1=0;x1<src_ipl->width-blk_w-1;x1++){

			cnt = 0;
			m   = 0;
			for (row=0;row<blk_h;row++)
				for (col=0;col<blk_w;col++){
					m += (biokey)->Image[row+y1][col+x1];
					++cnt;
			}

			m = (PIXEL)(m/cnt);  

			(biokey)->M[y1][x1]  = m;
			(biokey)->PM2[y1][x1]  = 0;
			for (row=0;row<blk_h;row++)
				for (col=0;col<blk_w;col++){
					p = (PIXEL)((biokey)->Image[row+y1][col+x1]);
					(biokey)->PM2[y1][x1]  += (p-m)*(p-m);
			}

			(biokey)->PM2[y1][x1] = (long)sqrt((double)((biokey)->PM2[y1][x1]));

		} 

	return _bl_err;

}

int blSaveBioKey(char *nomefile, struct blBioKey *biokey)
{
	FILE *fp = NULL;
	int row = 0;
	int col = 0;
	int cnt = 0;
	int p = 0;
	
	_bl_err = 0;

	fp = fopen(nomefile, "w");

	if(!fp){
		sprintf(_bl_err_str, "blSaveBioKey(Error): impossibile scrivere la chiave biometrica: %s", nomefile);
		_bl_err = BL_ERR_BIOKEY;
    	return _bl_err;
	}

	fprintf(fp, "%d ", (biokey)->width);
	fprintf(fp, "%d ", (biokey)->height);


	for(row=0; row<(biokey)->height; row++)
		for(col=0; col<(biokey)->width; col++,++cnt){
			fprintf(fp, "%d %d %ld ", (biokey)->Image[row][col], (biokey)->M[row][col], (biokey)->PM2[row][col]);
		}

	fclose(fp);

	return _bl_err;
}

int blFreeBioKey(struct blBioKey *biokey)
{
	free((biokey)->M[0]);
	free((biokey)->PM2[0]);
	free((biokey)->Image[0]);
	//free((biokey));

	return 0;
}



PIXEL _SRC[512][512];
PIXEL _DST[512][512];
PIXEL _AM[512][512];
PIXEL _BM[512][512];
long _APM2[512][512];
long _BPM2[512][512];

#define _A(m,n) (_SRC[m+y1][n+x1] - m1)
#define _B(m,n) (_DST[m+y2][n+x2] - m2)


// Da ripristinare...vedi eventuali copie di backup
// funzione per il confronto di due chiavi biometriche
double blCompareBioKey(struct blBioKey *biokeySRC, struct blBioKey *biokeyDST)
{

	int x1 = 0;
	int y1 = 0;
	int x2 = 0;
	int y2 = 0;
	int ch = 1;
	int row = 0;
	int col = 0;
	double corr = 0.0;
	int m1 = 0;
	int m2 = 0;
	int AB = 0;
	int A2 = 0;
	int B2 = 0;
	double corrTOT = 0.0;
	double MAXcorrTMP = 0.0;
	double corrTMP = 0.0;
	int num_increments = 0;
	int ok_flag = 0;

	int width = 8;
	int height = 8;
	int stp = 5;

	int a00, a01, a02, a03, a04, a05, a06, a07;
	int a10, a11, a12, a13, a14, a15, a16, a17;
	int a20, a21, a22, a23, a24, a25, a26, a27;
	int a30, a31, a32, a33, a34, a35, a36, a37;
	int a40, a41, a42, a43, a44, a45, a46, a47;
	int a50, a51, a52, a53, a54, a55, a56, a57;
	int a60, a61, a62, a63, a64, a65, a66, a67;
	int a70, a71, a72, a73, a74, a75, a76, a77;


	for (y1=0; y1<biokeySRC->height; y1+=1)
		for (x1=0; x1<biokeySRC->width; x1+=1){
			_SRC[y1][x1] = biokeySRC->Image[y1][x1];
			_DST[y1][x1] = biokeyDST->Image[y1][x1];
			_AM[y1][x1] = biokeySRC->M[y1][x1];
			_BM[y1][x1] = biokeyDST->M[y1][x1];
			_APM2[y1][x1] = biokeySRC->PM2[y1][x1];
			_BPM2[y1][x1] = biokeyDST->PM2[y1][x1];
		}

// non funziona con array statici
/*
	memcpy(biokeySRC->Image, _SRC, (biokeySRC->width * biokeySRC->height));
	memcpy(biokeyDST->Image, _DST, (biokeySRC->width * biokeySRC->height));
	memcpy(biokeySRC->M, _AM, (biokeySRC->width * biokeySRC->height));
	memcpy(biokeyDST->M, _BM, (biokeySRC->width * biokeySRC->height));
	memcpy(biokeySRC->PM2, _APM2, (biokeySRC->width * biokeySRC->height));
	memcpy(biokeyDST->PM2, _BPM2, (biokeySRC->width * biokeySRC->height));
*/

	for (y1=0; y1<biokeySRC->height-1; y1+=2){
		for (x1=0; x1<0.55*(biokeySRC->width-1); x1+=1){

			if(x1-stp<0 || y1-stp<0 || x1+stp>biokeySRC->width || y1+stp>biokeySRC->height)
				continue;

				A2 = _APM2[y1][x1];
				m1 = _AM[y1][x1];
				MAXcorrTMP = 0;
				ok_flag = 0;


				a00 = _A(0,0); a01 = _A(0,1); a02 = _A(0,2); a03 = _A(0,3); a04 = _A(0,4); a05 = _A(0,5); a06 = _A(0,6); a07 = _A(0,7);
				a10 = _A(1,0); a11 = _A(1,1); a12 = _A(1,2); a13 = _A(1,3); a14 = _A(1,4); a15 = _A(1,5); a16 = _A(1,6); a17 = _A(1,7);
				a20 = _A(2,0); a21 = _A(2,1); a22 = _A(2,2); a23 = _A(2,3); a24 = _A(2,4); a25 = _A(2,5); a26 = _A(2,6); a27 = _A(2,7);
				a30 = _A(3,0); a31 = _A(3,1); a32 = _A(3,2); a33 = _A(3,3); a34 = _A(3,4); a35 = _A(3,5); a36 = _A(3,6); a37 = _A(3,7);
				a40 = _A(4,0); a41 = _A(4,1); a42 = _A(4,2); a43 = _A(4,3); a44 = _A(4,4); a45 = _A(4,5); a46 = _A(4,6); a47 = _A(4,7);
				a50 = _A(5,0); a51 = _A(5,1); a52 = _A(5,2); a53 = _A(5,3); a54 = _A(5,4); a55 = _A(5,5); a56 = _A(5,6); a57 = _A(5,7);
				a60 = _A(6,0); a61 = _A(6,1); a62 = _A(6,2); a63 = _A(6,3); a64 = _A(6,4); a65 = _A(6,5); a66 = _A(6,6); a67 = _A(6,7);
				a70 = _A(7,0); a71 = _A(7,1); a72 = _A(7,2); a73 = _A(7,3); a74 = _A(7,4); a75 = _A(7,5); a76 = _A(7,6); a77 = _A(7,7);
			    
				for (y2=y1-stp;y2<y1+stp;y2+=1)
					for (x2=x1-stp;x2<x1+stp;x2+=1){

						B2 = _BPM2[y2][x2];	


						if (A2 * B2){
							m2 = _BM[y2][x2];

							AB = a00*_B(0,0) + a01*_B(0,1) + a02*_B(0,2) + a03*_B(0,3) + a04*_B(0,4) + a05*_B(0,5) + a06*_B(0,6) + a07*_B(0,7) + 
								 a10*_B(1,0) + a11*_B(1,1) + a12*_B(1,2) + a13*_B(1,3) + a14*_B(1,4) + a15*_B(1,5) + a16*_B(1,6) + a17*_B(1,7) +
								 a20*_B(2,0) + a21*_B(2,1) + a22*_B(2,2) + a23*_B(2,3) + a24*_B(2,4) + a25*_B(2,5) + a26*_B(2,6) + a27*_B(2,7) +
								 a30*_B(3,0) + a31*_B(3,1) + a32*_B(3,2) + a33*_B(3,3) + a34*_B(3,4) + a35*_B(3,5) + a36*_B(3,6) + a37*_B(3,7) +
								 a40*_B(4,0) + a41*_B(4,1) + a42*_B(4,2) + a43*_B(4,3) + a44*_B(4,4) + a45*_B(4,5) + a46*_B(4,6) + a47*_B(4,7) +
								 a50*_B(5,0) + a51*_B(5,1) + a52*_B(5,2) + a53*_B(5,3) + a54*_B(5,4) + a55*_B(5,5) + a56*_B(5,6) + a57*_B(5,7) +
								 a60*_B(6,0) + a61*_B(6,1) + a62*_B(6,2) + a63*_B(6,3) + a64*_B(6,4) + a65*_B(6,5) + a66*_B(6,6) + a67*_B(6,7) +
								 a70*_B(7,0) + a71*_B(7,1) + a72*_B(7,2) + a73*_B(7,3) + a74*_B(7,4) + a75*_B(7,5) + a76*_B(7,6) + a77*_B(7,7);

								corrTMP = ((double)AB/(A2 * B2));


							if(corrTMP > MAXcorrTMP)
								MAXcorrTMP = corrTMP;

							ok_flag = 1;
						}
					}

				if(ok_flag && MAXcorrTMP){
					corrTOT += MAXcorrTMP;
					++num_increments;
				}
				
		}
	}

	MAXcorrTMP = (corrTOT/(double)num_increments);


	return ((MAXcorrTMP < 1) ? 1-MAXcorrTMP : 0);

}