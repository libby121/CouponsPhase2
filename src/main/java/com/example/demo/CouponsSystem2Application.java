package com.example.demo;

import java.sql.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.beans.Category;
import com.example.demo.beans.Company;
import com.example.demo.beans.Coupon;
import com.example.demo.db.CouponRepository;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
 import com.example.demo.jobThread.CouponDailyJob;
import com.example.demo.login.ClientType;
import com.example.demo.login.LoginManager;
import com.example.demo.tests.Test;

/**
 * Spring context is responsible for the instantiation and configuration of beans and classes 
 * (marked throughout the meta-code in annotations, xml or other files).
 * By using the getBean() method an instantiated bean is retrieved from context container.
 *
 * @author ליבי
 *
 */
@SpringBootApplication
public class CouponsSystem2Application {

	public static void main(String[] args) {
		
	ConfigurableApplicationContext ctx=	SpringApplication.run(CouponsSystem2Application.class, args);
	Test test=ctx.getBean(Test.class);
	test.TestAll();
	
}}