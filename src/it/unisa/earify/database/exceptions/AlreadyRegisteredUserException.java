package it.unisa.earify.database.exceptions;

public class AlreadyRegisteredUserException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AlreadyRegisteredUserException(String pMessage) {
		super(pMessage);
	}
}
