package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.database.acquisitions.Acquisition;
import it.unisa.earify.exceptions.InvalidActionException;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * Classe attraverso cui è possibile estrarre le caratteristiche di una lista di immagini. Supporta funzioni di:
 * · Registrazione
 * · Verifica
 * · Riconoscimento
 * @author simone
 *
 */
public class FeatureExtractorAbstraction {
	/**
	 * Azione di registrazione
	 */
	public static final int REGISTRATION = 0;
	/**
	 * Azione di verifica
	 */
	public static final int VERIFICATION = 1;
	/**
	 * Azione di riconoscimento
	 */
	public static final int RECOGNITION	 = 2;
	
	/**
	 * Orecchio destro
	 */
	public static final int EAR_RIGHT = Acquisition.EAR_RIGHT;
	/**
	 * Orecchio sinistro
	 */
	public static final int EAR_LEFT = Acquisition.EAR_LEFT;
	
	/**
	 * Crea un nuovo estrattore delle caratteristiche
	 */
	public FeatureExtractorAbstraction() {
		this.implementor = new FeaturesExtractor();
	}
	
	/**
	 * Estrae le caratteristiche delle immagini passate in input, utilizzando gli algoritmi specificati.
	 * Restituisce un oggetto di tipo Map<String, List<List<IFeature>>> (ovvero una mappa del tipo "Nome algoritmo -> Lista").
	 * In particolare, la lista contiene n elementi, dove n è il numero di immagini passate in input e, per ogni immagine i, 
	 * è presente un'ulteriore lista di feature relative all'immagine i-esima.
	 * Ad esempio, se in input è data una lista di due immagini e sono attivi gli algoritmi SIFT e LBP, il valore di ritorno 
	 * sarà formato così:
	 * Map(
	 * "SIFT",  List(List<IFeature>, List<IFeature>)
	 * "LBP",   List(List<IFeature>, List<IFeature>)
	 * )
	 * 
	 * Se sono passate 3 immagini con il solo algoritmo SIFT attivo:
	 * Map(
	 * "SIFT",  List(List<IFeature>, List<IFeature>, List<IFeature>)
	 * )
	 *  
	 * @param pAction Il tipo di azione scelta dall'utente. Usare le costanti di classe REGISTRATION, VERIFICAION e RECOGNITION
	 * @param pImages Lista di immagini di cui calcolare i vettori delle caratteristiche
	 * @param pUsername Nome dell'utente
	 * @param pEar Identificativo dell'orecchio. Usare le costanti di classe EAR_RIGHT e EAR_LEFT
	 * @param pQuality Punteggio di qualità delle immagini date. Valore tra 0 e 1
	 * @return Una mappa che associa ogni algoritmo a una lista di lista di features.
	 * @throws InvalidActionException Se l'azione specificata non è valida
	 */
	public Map<String, List<List<IFeature>>> extractFeatures(int pAction, List<Image> pImages, String pUsername, int pEar, float pQuality) throws InvalidActionException {
		if (pAction == REGISTRATION) {
			this.implementor.register(pImages, pUsername, pEar, pQuality);
			return null;
		} else if (pAction == VERIFICATION) {
			return this.implementor.extractFeature(pImages, pUsername, pEar, pQuality);
		} else if (pAction == RECOGNITION) {
			return this.implementor.extractFeature(pImages, pUsername, pEar, pQuality);
		}
		
		throw new InvalidActionException(pAction);
	}
	
	private IFeatureExtractor implementor;
}
