package it.unisa.earify.exceptions;

public class InvalidActionException extends Exception {
	public InvalidActionException(int pAction) {
		super("Invalid action: " + pAction);
	}
}
