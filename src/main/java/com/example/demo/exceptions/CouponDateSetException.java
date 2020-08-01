package com.example.demo.exceptions;

public class CouponDateSetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CouponDateSetException() {
		super("invalid date");
	}

}
