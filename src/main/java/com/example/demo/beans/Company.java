package com.example.demo.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 *  @Entity-HiberNate annotation. Indicates an entity to be mapped to a table in the dataBase.
 *  @Table-enables changing some meta-data of the created table, such as name.
 *  @Id-an annotation that represents a primary key by which the entity will be identified.
 *  @GeneratedValue(Identity)-the marked property will be automatically incremented by one in every creation 
 *  of the class object. 
 *  @Column- java property to be mapped to a column.
 *  @OneToMany-In this case, each company entity'owns' a list of coupons. The coupons property is not mapped to a 
 *  column but it rather represents a relation.
 *  mappedBy-marks that the property in this bean does not require a separate table since it's the opposite side of @ManyToOne 
 *  relation which already exist in another bean.
 *  
 *  fetch-There are different strategies to get or fetch the data of related entities. "eager" means that 
 *  the fetched data includes information that might not be otherwise critical or essential to fetch.But is needed in this case.
 *
 */
@Entity
@Table(name = "companies")
public class Company {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;

	@Column
	private String name;
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private double balance;
	@OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
	private List<Coupon> coupons; 

	/**
	 * HiberNate requires an empty CTOR for fetching the data (wrapped as an object) from dataBase.
	 */
	public Company() { 
		super();
	}
	/**
	 * An optional CTOR for updating the company using it's Id
	 * @param id
	 * @param name
	 * @param email
	 * @param password
	 * @param balance
	 */
	public Company(int id, String name, String email, String password, double balance) {
		 
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.balance = balance;
	}
	/**
	 * CTOR for adding a company. Id is automatically generated, balance equals 0.
	 * @param name
	 * @param email
	 * @param password
	 */
	public Company(String name, String email, String password) { 
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.balance=0;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getBalance() {
		return balance;
	}


	public int getId() {
		return id;
	}

	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public List<Coupon> getCoupons() {
		return coupons;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", balance="
				+ balance + "₪]";
	}

}
