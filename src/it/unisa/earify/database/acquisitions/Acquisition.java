package it.unisa.earify.database.acquisitions;

import it.unisa.earify.IFeatureExtractor;
import it.unisa.earify.database.users.User;
import it.unisa.earify.algorithms.IFeature;

import java.util.List;

/**
 * Rappresenta la singola acquisizione di un orecchio.
 * @author simone
 *
 */
public class Acquisition {
	/**
	 * Identificativo dell'orecchio destro
	 */
	public static final int EAR_RIGHT = 0;
	
	/**
	 * Identificativo dell'orecchio sinistro
	 */
	public static final int EAR_LEFT = 1;
	
	private int id;
	private String algorithm;
	private List<IFeature> features;
	private User user;
	private int ear;
	
	/**
	 * Restituisce l'id dell'acquisizione
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Imposta l'id dell'acquisizione
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Restituisce il nome dell'algoritmo usato
	 * @return
	 */
	public String getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Imposta il nome dell'algoritmo usato
	 * @param algorithm
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	/**
	 * Restituisce la lista di features relative all'acquisizione
	 * @return
	 */
	public List<IFeature> getFeatures() {
		return features;
	}
	
	/**
	 * Imposta la lista di features relative all'acquisizione
	 * @param features
	 */
	public void setFeatures(List<IFeature> features) {
		this.features = features;
	}
	
	/**
	 * Restituisce l'utente acquisito
	 * @return
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Imposta l'utente acquisito
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Restituisce l'identificativo dell'orecchio acquisito
	 * @return L'identificativo dell'orecchio (vedi EAR_LEFT e EAR_RIGHT)
	 */
	public int getEar() {
		return ear;
	}

	/**
	 * Imposta l'identificativo dell'orecchio acquisito
	 * @param ear Identificativo dell'orecchio (usare EAR_LEFT o EAR_RIGHT)
	 */
	public void setEar(int ear) {
		this.ear = ear;
	}
}
