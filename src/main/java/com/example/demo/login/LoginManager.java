package com.example.demo.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.example.demo.exceptions.loginException;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.facades.Facade;
/**
 * LoginManager is a singleton class used for managing the login process. If the login process succeeds
 * the  appropriate facade is instantiated by spring context and then returned to client.  
 * 
 * Marked as component for spring scanning. 
 * @author ליבי
 *
 */
@Component
public class LoginManager {

	@Autowired
	private ConfigurableApplicationContext ctx;
 
	public Facade login(String email, String password, ClientType type) throws loginException {
		switch (type) {
		case Administrator:
			AdminFacade adminFacade=ctx.getBean(AdminFacade.class);
			if (adminFacade.login(email, password))

				return   adminFacade;
			break;

		case Company:
			CompanyFacade companyFacade=ctx.getBean(CompanyFacade.class);
			if (companyFacade.login(email, password))
				return companyFacade;
			break;
		case Customer:
			CustomerFacade customerFacade=ctx.getBean(CustomerFacade.class);
			if (customerFacade.login(email, password))
				return customerFacade;
		break;
		default:
			throw new loginException();

		}return null;//exception..
	}



}
