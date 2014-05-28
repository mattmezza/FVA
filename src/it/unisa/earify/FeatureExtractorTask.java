package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.exceptions.InvalidActionException;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Thread che esegue in modo asincrono gli algoritmi di estrazione delle caratteristiche e notifica il risultato a un
 * delegato di tipo ExtractorDelegate.
 * @author simone
 *
 */
public class FeatureExtractorTask extends AsyncTask<String, Void, String> {

	/**
	 * Crea un nuovo thread in grado di estrarre le caratteristiche delle immagini date. Vedere 
	 * {@link FeatureExtractorAbstraction#extractFeatures(int, List, String, int, float) extractFeatures} per ulteriori informazioni.
	 * Bisogna impostare il delegato prima di eseguire il task.
	 * @param pAction Il tipo di azione scelta dall'utente. Usare le costanti di classe REGISTRATION, VERIFICAION e RECOGNITION
	 * @param pImages Lista di immagini di cui calcolare i vettori delle caratteristiche
	 * @param pUsername Nome dell'utente
	 * @param pEar Identificativo dell'orecchio. Usare le costanti di classe EAR_RIGHT e EAR_LEFT
	 * @param pQuality Punteggio di qualità delle immagini date. Valore tra 0 e 1
	 * @return Una mappa che associa ogni algoritmo a una lista di lista di features.
	 * @throws InvalidActionException Se l'azione specificata non è valida
	 */
	public FeatureExtractorTask(int pAction, List<Image> pImages, String pUsername, int pEar, float pQuality) {
		this.action = pAction;
		this.images = pImages;
		this.username = pUsername;
		this.ear = pEar;
		this.quality = pQuality;
	}
	
	/**
	 * Imposta il delegato del task, che verrà richiamato quando questo sarà completo o se si verificherà un
	 * errore
	 * @param extractorDelegate Delegato
	 */
	public void setDelegate(ExtractorDelegate extractorDelegate) {
		this.delegate = extractorDelegate;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			this.result = this.fea.extractFeatures(this.action, this.images, this.username, this.ear, this.quality);
		} catch (Exception e) {
			this.ex = e;
			if (this.delegate != null)
				this.delegate.onExtractorError(e);
			return null;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if(this.delegate!=null && this.ex==null)
			this.delegate.onExtractorFinished(this.result);
	}

	private ExtractorDelegate delegate;
	
	private int action;
	private List<Image> images;
	private String username;
	private int ear;
	private float quality;
	private FeatureExtractorAbstraction fea = new FeatureExtractorAbstraction();
	private Map<String, List<List<IFeature>>> result = null;
	private Exception ex;
	
}
