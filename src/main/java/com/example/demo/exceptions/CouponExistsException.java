package com.example.demo.exceptions;

public class CouponExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public  CouponExistsException() {
		super("coupon already exists in dataBase, coupon title must be unique");
	}

}
