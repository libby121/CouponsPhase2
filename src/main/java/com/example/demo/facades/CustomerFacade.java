package com.example.demo.facades;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.demo.beans.Category;
import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.Customer;
import com.example.demo.beans.ShoppingCart;
import com.example.demo.db.ShoppingCartRepo;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponOutOfStockException;
import com.example.demo.exceptions.CouponsCategoreyException;
import com.example.demo.exceptions.CustomerDoesnotExistException;
import com.example.demo.exceptions.NoSuchCouponException;
import com.example.demo.exceptions.PurchaseDuplicationException;
import com.example.demo.exceptions.couponExpiredException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.maxPriceException;
import com.example.demo.exceptions.noSuchCartException;

@Service
@Scope(value = "prototype")
public class CustomerFacade extends Facade {
	/**
	 * On methods that return a List, if the List is empty than nothing is returned
	 * back (no exception is thrown, the program will continue to run).
	 */

	private int id;
	private String cartId;

	public boolean login(String email, String password) throws loginException {
		if (customerRepo.existsByEmailAndPassword(email, password)) {
			Customer c = customerRepo.findByEmailAndPassword(email, password);
			id = c.getId();
			return true;
		}
		throw new loginException();

	}

	/**
	 * Before executing the purchase, it is checked whether the buyer is a prime
	 * customer. if he is then the coupon's price is 5% lower. A customer becomes
	 * prime if his revenue to the companies get's to 2000 ₪, after every purchase
	 * it is checked whether the customer should become prime.Company's balance and
	 * coupon's amount are also modified.
	 * 
	 * @param coupon
	 * @throws CustomerDoesnotExistException
	 * @throws CouponOutOfStockException
	 * @throws PurchaseDuplicationException
	 * @throws couponExpiredException
	 * @throws CouponDoesnotExistException
	 */
	public void purchaseCoupon(Coupon coupon) throws CustomerDoesnotExistException, CouponOutOfStockException,
			PurchaseDuplicationException, couponExpiredException, CouponDoesnotExistException {
		Customer customer = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
		if (!couponRepo.existsById(coupon.getId()))
			throw new CouponDoesnotExistException();
		if (coupon.getAmount() == 0)
			throw new CouponOutOfStockException();
		if (customer.getCoupons().contains(coupon)) {
			throw new PurchaseDuplicationException();
		}
		Calendar cal = Calendar.getInstance();
		if (coupon.getEndDate().before(new Date(cal.getTimeInMillis())))
			throw new couponExpiredException();

		else
			coupon.getCustomers().add(customer);

		coupon.setAmount(coupon.getAmount() - 1);
		couponRepo.save(coupon);
		double price = coupon.getPrice(customer);
		Company company = coupon.getCompany();
		company.setBalance(company.getBalance() + price);
		companyRepo.save(company);
		customer.setRevenue(customer.getRevenue() + price);
		if (customer.getRevenue() >= 2000)
			customer.setPrime(true);
		customerRepo.save(customer);
		System.out.println("coupon" + coupon.getId() + " was successfully purchased!");

	}

	/**
	 * Order cancellation requires: retrieving the canceled coupon and modifying the
	 * coupon's company, the coupon's amount, company's balance, and the customer's
	 * revenue.
	 * 
	 * @param coupId
	 * @throws CustomerDoesnotExistException
	 * @throws CouponDoesnotExistException
	 */
	public void cancelOrder(int coupId) throws CustomerDoesnotExistException, CouponDoesnotExistException {
		Customer cust = customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
		Coupon c = couponRepo.findById(coupId).orElseThrow(CouponDoesnotExistException::new);
		c.getCustomers().remove(cust);

		customerRepo.save(cust);
		c.setAmount(c.getAmount() + 1);
		couponRepo.save(c);
		Company comp = c.getCompany();
		double price = c.getPrice(cust);
		comp.setBalance(comp.getBalance() - price);
		companyRepo.save(comp);
		c.setPrice(price);
		System.out.println("order was successfully canceled!");

	}

	public List<Coupon> getCustomerCoupons() {
		if (couponRepo.findByCustomerId(id).isEmpty())
			System.out.println("no coupons to customer");
		return couponRepo.findByCustomerId(id);
	}

	public List<Coupon> getCustomerCouponsBYCategory(Category cat) {
		if (couponRepo.findByCustomerIdAndCategory(id, cat).isEmpty())
			System.out.println("no coupons by " + cat + " category to customer");
		return couponRepo.findByCustomerIdAndCategory(id, cat);
	}

	public List<Coupon> getCustomerCouponsUpToPrice(double maxprice) {
		if ((couponRepo.findByCustomerIdAndPriceLessThanEqual(id, maxprice).isEmpty()))
			System.out.println("no coupons up to " + maxprice + " ₪ to customer");
		return (couponRepo.findByCustomerIdAndPriceLessThanEqual(id, maxprice));

	}

	public Customer getCustomerDetails() throws CustomerDoesnotExistException {
		return customerRepo.findById(id).orElseThrow(CustomerDoesnotExistException::new);
	}

	public Coupon getOneCoupon(int id) throws CouponDoesnotExistException {// for purchasing one
		return couponRepo.findById(id).orElseThrow(CouponDoesnotExistException::new);
	}

	public ShoppingCart getOrCreateCart() throws CustomerDoesnotExistException {
		Customer c = customerRepo.findById(this.id).orElseThrow(CustomerDoesnotExistException::new);

		if (cartRepo.getByCustomerId(id)!=null) {
			cartId = cartRepo.getByCustomerId(id).getCartId();
			return cartRepo.getByCustomerId(id);
		}

		else {
			ShoppingCart cart = new ShoppingCart(c);
			cartRepo.save(cart);

			cartId = cart.getCartId();
			System.out.println(cartId);

			return cart;
		}

	}

	public String addToCart(int couponId)
			throws NoSuchCouponException, noSuchCartException, PurchaseDuplicationException, CouponOutOfStockException {
		System.out.println(cartId);
 		ShoppingCart cart = cartRepo.findById(cartId).orElseThrow(noSuchCartException::new);
		Coupon c = couponRepo.findById(couponId).orElseThrow(NoSuchCouponException::new);

		if (getCustomerCoupons().contains(c) || cart.getCoupons().contains(c))
			throw new PurchaseDuplicationException();
		if (c.getAmount() >= 1) {
			cart.getCoupons().add(c);
			cartRepo.save(cart);
			return "coupon was successfully added to cart";
		}
		throw new CouponOutOfStockException();
	}

	public String removeFromCart(int couponId) throws NoSuchCouponException, noSuchCartException {
		ShoppingCart cart = cartRepo.findById(cartId).orElseThrow(noSuchCartException :: new);
		Coupon c = couponRepo.findById(couponId).orElseThrow(NoSuchCouponException::new);
		cart.getCoupons().remove(c);
		cartRepo.save(cart);
		return "coupon was successfully removed from cart";
	}

	public String deleteCart() throws noSuchCartException {
		if(cartId==null)throw new noSuchCartException();

		else{cartRepo.deleteById("b31d110f-15f4-4099-907f-7de3ec055fa0");
		return "ShoppingCart was succesfully deleted ";}
	}
	
	public String deleteCartById(String id) {
		cartRepo.deleteById(id);
		return "cart  deleted by id";
	}

}
