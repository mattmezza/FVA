package it.unisa.earify;

import it.unisa.earify.algorithms.IFeature;

import java.util.List;
import java.util.Map;

/**
 * Interfaccia che specifica il comportamento di un oggetto in grado di ricevere il risultato di un azione di estrazione
 * delle caratteristiche in modo asincrono. Un ExtractorDelegate viene notificato quando l'estrazione &egrave; completa o se si
 * &egrave; verificato un errore
 * @author simone
 *
 */
public interface ExtractorDelegate {
	/**
	 * Se l'estrazione &egrave; finita correttamente &egrave; chiamato questo metodo con il risultato dell'estrazione
	 * @param result Risultato dell'estrazione
	 */
	public void onExtractorFinished(Map<String, List<List<IFeature>>> result);
	
	/**
	 * Se si &egrave; verificato un errore durante il processo di estrazione, verr&agrave; richiamato questo metodo.
	 * @param e Errore
	 */
	public void onExtractorError(Exception e);
	
}
