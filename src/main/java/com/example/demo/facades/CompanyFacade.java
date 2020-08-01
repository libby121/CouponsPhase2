package com.example.demo.facades;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.demo.beans.Category;
import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.ShoppingCart;
import com.example.demo.exceptions.CouponDateSetException;
import com.example.demo.exceptions.CouponDoesnotExistException;
import com.example.demo.exceptions.CouponExistsException;
import com.example.demo.exceptions.CouponOfAnotherCompanyException;
import com.example.demo.exceptions.CouponOutOfStockException;
import com.example.demo.exceptions.CouponsCategoreyException;
import com.example.demo.exceptions.NoSuchCouponException;
import com.example.demo.exceptions.companyDoesnotExistException;
import com.example.demo.exceptions.loginException;
import com.example.demo.exceptions.maxPriceException;
import com.example.demo.exceptions.unchangeableCouponCompanyId;

@Service
@Scope(value = "prototype")
public class CompanyFacade extends Facade {
	/**
	 * On methods that return a List, if the List is empty than nothing is returned
	 * back(no exception is thrown and so on the console in main nothing will be
	 * printed, the program will continue to run).
	 */

	private int id;

	public boolean login(String email, String password) throws loginException {
		Company c = companyRepo.findByEmailAndPassword(email, password).orElseThrow(loginException::new);
		id = c.getId();
		return true;
	}

	public List<Coupon> getCouponsByCategory(Category cat) {
		if (couponRepo.findCompanyCouponsByCatgory(id, cat).isEmpty())
			System.out.println("no coupons by " + cat + " category");
		return couponRepo.findCompanyCouponsByCatgory(id, cat);

	}

	public void addCoupon(Coupon coup)
			throws CouponExistsException, CouponDateSetException, companyDoesnotExistException, CouponOutOfStockException {

		Calendar cal = Calendar.getInstance();
		if (coup.getStartDate().after(coup.getEndDate()) || coup.getStartDate().before(cal.getTime()))
			throw new CouponDateSetException();
		if(coup.getAmount()<=0)throw new CouponOutOfStockException();
		if (getCompanyCoupons() != null) {
			for (Coupon c : getCompanyCoupons()) {
				if (c.getTitle().equals(coup.getTitle()))
					throw new CouponExistsException();

			}
		}
		coup.setCompany(companyRepo.findById(id).orElseThrow(companyDoesnotExistException::new));
		coup.setSalePrice(false);
		couponRepo.save(coup);
		System.out.println("coupon " + coup.getTitle() + " was successfully added!");
	}

	public List<Coupon> getCompanyCoupons() {
		if (couponRepo.findByCompanyId(id).isEmpty())
			System.out.println("no coupon to company with id " + id);
		return couponRepo.findByCompanyId(id);
	}

	public void updateCoupon(Coupon coupon) throws unchangeableCouponCompanyId {
		if (coupon.getCompany().getId() == id) {
			couponRepo.save(coupon);
			System.out.println("coupon " + coupon.getTitle() + " was successfully updated!");
		} else
			throw new unchangeableCouponCompanyId();
	}

	public void deleteCoupon(int Couponid) throws NoSuchCouponException, CouponOfAnotherCompanyException, IOException,
			CouponDoesnotExistException, companyDoesnotExistException {

		Company comp = companyRepo.findById(id).orElseThrow(companyDoesnotExistException::new);
		Coupon coup = couponRepo.findById(Couponid).orElseThrow(CouponDoesnotExistException::new);
		if (!comp.getCoupons().contains(coup))
			throw new CouponOfAnotherCompanyException();

		couponRepo.deleteCouponFromCart(Couponid);

		;
		couponRepo.deleteById(Couponid);
		System.out.println("coupon was successfully deleted!");

	}

	public void fileRead() throws IOException {
		try (FileReader reader = new FileReader("CouponsArchiveFile.txt")) {
			int tav = reader.read();

			while (tav != -1) {
				System.out.print((char) tav);
				tav = reader.read();
			}
		}
	}

	public List<Coupon> getCouponUpToMaxPrice(double maxPrice) {
		if (couponRepo.findByCompanyIdAndPriceLessThanEqual(id, maxPrice) == null)
			System.out.println("no coupons up to " + maxPrice + " ₪");
		return couponRepo.findByCompanyIdAndPriceLessThanEqual(id, maxPrice);
	}

	public Company getCompanyDetails() throws companyDoesnotExistException {
		return companyRepo.findById(id).orElseThrow(companyDoesnotExistException::new);
	}

}
