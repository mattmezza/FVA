package it.unisa.earify.database.exceptions;

/**
 * Eccezione lanciata quando si prova ad aggiungere al database un utente già registrato
 * @author simone
 *
 */
public class AlreadyRegisteredUserException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AlreadyRegisteredUserException(String pMessage) {
		super(pMessage);
	}
}
