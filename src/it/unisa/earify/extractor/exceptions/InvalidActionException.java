package it.unisa.earify.extractor.exceptions;

public class InvalidActionException extends Exception {
	public InvalidActionException(int pAction) {
		super("Invalid action: " + pAction);
	}
}
