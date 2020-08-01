package com.example.demo.tests;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.example.demo.beans.Category;
import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.beans.Customer;
import com.example.demo.exceptions.CustomerExistsException;
import com.example.demo.exceptions.companyExistsException;
import com.example.demo.exceptions.loginException;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.facades.Facade;
import com.example.demo.jobThread.CouponDailyJob;
import com.example.demo.login.ClientType;
import com.example.demo.login.LoginManager;

@Component
public class Test {

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private CouponDailyJob thread;

	public void TestAll() {

		try {
			thread.start();
			Facade facade = loginManager.login("com.admin@admin", "admin", ClientType.Administrator);

			if (facade instanceof AdminFacade) {

				AdminFacade adminFacade = (AdminFacade) facade;
//				 adminFacade.addCompany(new Company("deal kiosk6", "couponik6@gmail.com",
//				 "jjk7R67"));
				// adminFacade.addCustomer(new Customer("sabine", "clochettes",
				// "sabine1982@gmail.com", "sabine182"));

				Company comp = adminFacade.getCompanyByID(4);
				comp.setPassword("newPassword");
				adminFacade.updateCompany(comp);
//				Customer customer = adminFacade.getOneCustomer(1);
//				customer.setFirstName("Sasson");
//				adminFacade.updateCustomer(customer);
				for (Company compa : adminFacade.getAllCompanies()) {
					System.out.println(compa);
				}
				 adminFacade.deleteCompany(6);
				 //adminFacade.deleteCustomer(1);
				System.out.println("one company: " + adminFacade.getCompanyByID(4));
				//System.out.println("one customer:" + adminFacade.getOneCustomer(1));

				System.out.println("\n----------------company----------------------------------------------------");

				Facade facade2 = loginManager.login("couponik6@gmail.com", "jjk7R67", ClientType.Company);
				if (facade2 instanceof CompanyFacade) {
					CompanyFacade companyFacade = (CompanyFacade) facade2;
					Calendar cal = Calendar.getInstance();
					cal.set(2021, Calendar.FEBRUARY, 28);
					Calendar cal2 = Calendar.getInstance();
					cal2.set(2022, Calendar.FEBRUARY, 24);

					// companyFacade.deleteCoupon(1);
					Coupon c = new Coupon(Category.danceTickets, "Chicago2!", "theatrical adaptation",
							new Date(cal.getTimeInMillis()), new Date(cal2.getTimeInMillis()), 700, 384.9,
							"images from the instagram page");

					 //companyFacade.addCoupon(c);

					for (Coupon coup : companyFacade.getCompanyCoupons()) {
						System.out.println(coup);
					}

					System.out.println(companyFacade.getCompanyDetails());
					for (Coupon coupon : companyFacade.getCouponsByCategory(Category.electronics)) {
						System.out.println("by category: " + coupon);
					}
					for (Coupon coupon2 : companyFacade.getCouponUpToMaxPrice(4)) {
						System.out.println(coupon2);
					}

//					Coupon coupi = companyFacade.getCompanyCoupons().get(0);
//					System.out.println(coupi);
//					coupi.setAmount(3400);
//
//					companyFacade.updateCoupon(coupi);

					System.out
							.println("\n------customer--------------------------------------------------------------");
					Facade facade3 = loginManager.login("samira@gmail.com", "$hsy8sb4", ClientType.Customer);
					CustomerFacade customerFacade = (CustomerFacade) facade3;

					// customerFacade.purchaseCoupon(customerFacade.getOneCoupon(1));

					for (Coupon co : customerFacade.getCustomerCoupons()) {
						System.out.println(co);
					}
					for (Coupon co : customerFacade.getCustomerCouponsBYCategory(Category.electronics)) {
						System.out.println(co);
					}
					for (Coupon co : customerFacade.getCustomerCouponsUpToPrice(100)) {
						System.out.println(co);
					}

					System.out.println(customerFacade.getCustomerDetails());
					// customerFacade.cancelOrder(1);

					System.out.println("\n---------------------------------------------------------------------");

					System.out.println(customerFacade.getOrCreateCart());
				//companyFacade.deleteCoupon(1);
					//adminFacade.deleteCustomer(1);

					System.out.println(customerFacade.addToCart(13));
					// System.out.println(customerFacade.removeFromCart(1));
					// System.out.println(customerFacade.deleteCartById("c38498d2-ffab-41ef-a430-c090a1fd7b13"));

					System.out.println("\n----------------------------------------------------------------------");
					companyFacade.fileRead();

				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			/**
			 * The JobThread is interrupted at the end of the program so it will all be
			 * stopped and closed. Before stopping the JobThread, the main thread-the
			 * program is being delayed for one second so that everything will be printed
			 * properly to console.
			 */
		} finally {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			thread.JobStop();

		}

	}
}
