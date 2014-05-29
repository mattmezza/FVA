package it.unisa.earify.exceptions;

/**
 * Eccezione lanciata quando si prova a eseguire azioni non permesse sul modulo
 * @author simone
 *
 */
public class InvalidActionException extends Exception {
	public InvalidActionException(int pAction) {
		super("Invalid action: " + pAction);
	}
}
