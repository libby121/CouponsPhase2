package com.example.demo.beans;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="customers")
public class Customer {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;
	@Column 
	private String email;

	@Column
	private String password;
	/**
	 * A prime customer gets a 5% off discount.
	 */
	@Column(name="is_Prime")
	private boolean isPrime;
	/**
	 * Customer' total revenue to the companies. 
	 */
	@Column (length=6)
	private double revenue;
	/**
	 * Set (instead of a List) makes sure that both coupn_id and customer_id are the primary key together, 
	 * a Set in Java only accepts a unique value. 
	 */
	@ManyToMany(mappedBy="customers",fetch=FetchType.EAGER)
	private Set<Coupon>coupons;



	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public Set<Coupon> getCoupons() {
		return coupons;
	}
	public Customer() {
		super();
	}
	public Customer(String firstName, String lastName, String email, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public boolean equals(Object obj) {
		if(this==obj)return true;
		if(obj==null)return false;
		if(obj instanceof Customer) {
			Customer c=(Customer)obj;

			if((this.id==c.getId())&&this.email.equals(c.email))return true;

		}return false;
	}
	public int hashCode() {
		return id*31+email.hashCode();
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", isPrime=" + isPrime + ", revenue=" + revenue + "₪]";
	}
	
	public boolean isPrime() {
		return isPrime;
	}
	public void setPrime(boolean isPrime) {
		this.isPrime = isPrime;
	}


}

