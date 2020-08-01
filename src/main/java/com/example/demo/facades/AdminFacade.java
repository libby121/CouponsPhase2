package com.example.demo.facades;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.Customer;
import com.example.demo.beans.ShoppingCart;
import com.example.demo.db.ShoppingCartRepo;
import com.example.demo.exceptions.CustomerDoesnotExistException;
import com.example.demo.exceptions.CustomerExistsException;
import com.example.demo.exceptions.companyDoesnotExistException;
import com.example.demo.exceptions.companyExistsException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.unmodifiedCompanyNameException;

@Service
@Scope(value = "prototype")
public class AdminFacade extends Facade {

	public boolean login(String email, String password) throws loginException {
		if (email.equals("com.admin@admin") && password.equals("admin"))
			return true;
		else
			return false;
	}

	public void addCompany(Company comp) throws companyExistsException {

		for (Company c : companyRepo.findAll()) {
			if (c.getEmail().equals(comp.getEmail()) || c.getName().equals(comp.getName()))
				throw new companyExistsException();
		}
		companyRepo.save(comp);
		System.out.println("company " + comp.getName() + " was successfully added!");

	}

	public void updateCompany(Company comp) throws companyDoesnotExistException, unmodifiedCompanyNameException {
		Company compa = getCompanyByID(comp.getId());
		if (!compa.getName().equals(comp.getName()))
			throw new unmodifiedCompanyNameException();

		else {
			companyRepo.updateCompany(comp);
			System.out.println("company " + comp.getName() + " was successfully updated!");
		}

	}

	/**
	 * Deletion of a company requires first the deletion of the company'
	 * purchases(represented in the many-to-many table of customers_vs_coupons) and
	 * the deletion of company' coupons, by this order. Otherwise an exception of
	 * IntegrityConstraintViolationException will occur.
	 * 
	 * @param id
	 * @throws companyDoesnotExistException
	 */
	public void deleteCompany(int id) throws companyDoesnotExistException {
		if (!companyRepo.existsById(id))
			throw new companyDoesnotExistException();
		for (Coupon c : couponRepo.findByCompanyId(id)) {
			if (c.getCustomers() != null) {
				for (Customer cu : c.getCustomers()) {
					cartRepo.deleteCustomerCart(cu.getId());
					couponRepo.deleteCouponPurchase(c.getId(), cu.getId());
				}
			}

			// how about coupon that are not in cart??
			couponRepo.deleteCouponFromCart(c.getId());

			couponRepo.deleteById(c.getId());

		}
		companyRepo.deleteById(id);
	}

	public List<Company> getAllCompanies() {
		if (companyRepo.findAll().isEmpty())
			System.out.println("no companies were found");
		return companyRepo.findAll();
	}

	public Company getCompanyByID(int id) throws companyDoesnotExistException {
		return companyRepo.findById(id).orElseThrow(companyDoesnotExistException::new);
	}

	public void addCustomer(Customer customer) throws CustomerExistsException {
		for (Customer c : customerRepo.findAll()) {
			if (c.getEmail().equals(customer.getEmail()))
				throw new CustomerExistsException();
		}
		customer.setRevenue(0);
		customer.setPrime(false); // must set both specifically because a primitive cannot be null
		customerRepo.save(customer);
		System.out.println("customer " + customer.getFirstName() + " was successfully added!");
	}

	public void updateCustomer(Customer customer) throws CustomerDoesnotExistException {
		if (customerRepo.existsById(customer.getId())) {
			customerRepo.save(customer);
			System.out.println("customer " + customer.getFirstName() + " was successfully updated!");
		} else
			throw new CustomerDoesnotExistException();

	}

	/**
	 * A deletion of a customer requires the preceding deletion of the customer's
	 * purchases.
	 * 
	 * @param id
	 */

	public void deleteCustomer(int id) throws CustomerDoesnotExistException {
		Customer c = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
		if (c.getCoupons() != null) {
			for (Coupon coup : c.getCoupons()) {
				couponRepo.deleteCouponPurchase(c.getId(), coup.getId());
			}
		}
		cartRepo.deleteCustomerCart(id);
		customerRepo.deleteById(id);

	}

	public List<Customer> getAllCustomers() {
		if (customerRepo.findAll() == null)
			System.out.println("no customers were found");
		return customerRepo.findAll();
	}

	public Customer getOneCustomer(int id) throws CustomerDoesnotExistException {
		return customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
	}
}
