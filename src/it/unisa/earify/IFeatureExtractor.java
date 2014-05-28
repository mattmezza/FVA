package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.exceptions.InvalidActionException;

import java.util.List;
import java.util.Map;

public interface IFeatureExtractor {
	/**
	 * Registra l'utente specificato nel database, utilizzando la lista di immagini passate relative all'orecchio
	 * specificato.
	 * 
	 * @param pImages Lista delle immagini
	 * @param pUsername Nome dell'utente da registrare
	 * @param pEar Identificativo delle orecchie da acquisire. Usare {@link FeatureExtractorAbstraction#EAR_LEFT EAR_LEFT} e 
	 * {@link FeatureExtractorAbstraction#EAR_RIGHT EAR_RIGHT}
	 */
	public void register(List<Image> pImages, String pUsername, int pEar, float pQuality);
	
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
	 * @param pImages Lista di immagini di cui calcolare i vettori delle caratteristiche
	 * @param pUsername Nome dell'utente
	 * @param pEar Identificativo dell'orecchio. Usare le costanti di classe EAR_RIGHT e EAR_LEFT
	 * @param pQuality Punteggio di qualità delle immagini date. Valore tra 0 e 1
	 * @return Una mappa che associa ogni algoritmo a una lista di lista di features.
	 * @throws InvalidActionException Se l'azione specificata non è valida
	 */
	public Map<String, List<List<IFeature>>> extractFeature(List<Image> pImages, String pUsername, int pEar, float pQuality);
	
}
