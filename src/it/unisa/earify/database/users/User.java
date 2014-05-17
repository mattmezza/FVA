package it.unisa.earify.database.users;

/**
 * Rappresenta un utente del sistema
 * @author simone
 */
public class User {
	private int id;
	private String username;
	
	/**
	 * Restituisce l'identificativo dell'utente
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Imposta l'identificativo dell'utente
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Restituisce l'username
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Imposta l'username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
