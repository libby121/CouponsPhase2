package com.example.demo.beans;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

 
 @Entity
public class ShoppingCart {
	
	 
   @Id
 	private String Id;
	 
    @ManyToMany(fetch=FetchType.EAGER)
     private Set<Coupon> coupons;
   
    @OneToOne
   private Customer customer;
  
  //@ManyToMany(mappedBy="carts")
	// private Set<Coupon>cartItems;
 
	public ShoppingCart() {
		super();
	}
	public String getCartId() {
		return Id;
	}
	public void setCartId(String cartId) {
		this.Id = cartId;
	}
//	public Customer getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
	public Set<Coupon> getCoupons() {
		return coupons;
	}
//	public void setCoupon(Set<Coupon> coupon) {
//		this.coupon = coupon;
//	}
	public ShoppingCart( Customer customer) {
		super();
		this.Id = String.valueOf(UUID.randomUUID());
		this.customer = customer;
	}
//	
	@Override
	public String toString() {
		return "ShoppingCart [Id=" + Id + ", coupons=" + coupons + ", customer=" + customer + "]";
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
//	public void setCoupons(Set<Coupon> coupons) {
//		this.coupons = coupons;
//	}
	
	
	

}
