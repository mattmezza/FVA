package it.unisa.earify.algorithms.exceptions;

/**
 * L'algoritmo specificato non esiste
 * @author simone
 *
 */
public class NoAlgorithmException extends Exception {
	public NoAlgorithmException() {
		super("No algorithm specified");
	}
}
