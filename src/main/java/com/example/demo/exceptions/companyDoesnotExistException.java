package com.example.demo.exceptions;

public class companyDoesnotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public companyDoesnotExistException() {
		super("oops! company does not exist, please choose another id");
	}
}
