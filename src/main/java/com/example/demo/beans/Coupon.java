package com.example.demo.beans;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "coupons")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column

	private Category category;
	@Column
	private String title;
	@Column
	private String description;
 	@Column(name = "start_date")
	private Date startDate;
	 
	@Column(name = "end_date")
	private Date endDate;
	@Column
	private int amount;
	@Column
	private double price;
	@Column
	private String image;
	@ManyToOne
	private Company company;
	@Column(name = "discount_status", nullable = true)  
	private boolean isSalePrice;
 	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "customers_vs_coupons", joinColumns = @JoinColumn(name = "coupon_id"), 
	inverseJoinColumns = @JoinColumn(name = "customer_id"))
	Set<Customer>customers ;
// 	@ManyToMany(mappedBy="coupons",fetch=FetchType.EAGER)
// 	private Set<ShoppingCart>carts;

	public Coupon() {
		super();
	}

	

//	public Set<ShoppingCart> getCarts() {
//		return carts;
//	}



//	public void setCarts(Set<ShoppingCart> carts) {
//		this.carts = carts;
//	}



	public Coupon(int id, Category category, String title, String description, Date startDate, Date endDate, int amount,
			double price, String image, boolean isSalePrice) {
		super();
		this.id = id;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
		this.isSalePrice = isSalePrice;
	}

	/**
	 * A ctor for adding a coupon by a company- no need the company  or coupon id/
	 */
	
	public Coupon(Category category, String title, String description, Date startDate, Date endDate, int amount,
			double price, String image) {
		super();
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
		this.isSalePrice = false;
	}
 

	 
	 

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	
	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isSalePrice() {
		return isSalePrice;
	}

	public void setSalePrice(boolean isSalePrice) {
		this.isSalePrice = isSalePrice;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", category=" + category + ", title=" + title + ", description=" + description
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", amount=" + amount + ", price=" + price
				+"₪"+ ", image=" + image + ", company=" + company.getName() + ", isSalePrice=" + isSalePrice + "]";
	}

	/**
	 * An overriding of equals (and hashCode) methods is required since the class represents coupon's data that should be
	 * identified by specific rules.
	 * Will mostly be used in List remove() method. 
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof Coupon) {

			// this-> the object that calls the method
			Coupon c = (Coupon) obj;

			if (c.getId() == this.getId() && c.getTitle().equals(this.getTitle())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * hashCode- numeric representation of the object in memory.
	 */
	@Override
	public int hashCode() { 
		return id * 31 + title.hashCode();

	}

	public Set<Customer> getCustomers() {
		return customers;
	}
	/**
	 * An overloading of getPrice() method.
	 * If a customer is a prime customer the coupon's price is always lower.
	 * Otherwise the regular price is returned.
	 * 
	 * @param c
	 * @return
	 */
	
	public double getPrice(Customer c) {
		if(c.isPrime())
		return price*0.95;
		return price;
	}

	public double getPrice()
	{
		return price;
	}
	

	
	 
}
