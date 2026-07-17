package testCases;

import org.testng.annotations.Test;

import junit.framework.Assert;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProvider1;


public class TC003_LoginDDT extends BaseClass{
	
	@Test(dataProvider="LoginCredential",dataProviderClass=DataProvider1.class, groups="Datadriven")
	public void verify_LoginDDT(String email, String pwd, String exp) {
		
		logger.info("*****************Starting TC003_LoginDDT*****************");
		
		
		try {
		//homepage
		HomePage hp = new HomePage(driver);
				
		hp.clickMyAccount();
		hp.clickLogin();
				
		//Loginpage
		LoginPage lp = new LoginPage(driver);
				
		lp.setEmail(email);
		lp.setPassword(pwd);
		lp.clickLogin();
				
		//MyAccount page
		MyAccountPage map = new MyAccountPage(driver);
		boolean targetPage = map.isMyAccountPageExists();
		
		/*
		 * Data is valid - login success - test passes - logout
		 * 				   login failed - test fails
		 * 
		 * Data is invalid - login success - test fail - logout
		 * Data is invalid - login failed - test pass
		*/
		
		if(exp.equalsIgnoreCase("valid")) {
			
			if(targetPage==true){
				
				map.clickLogout();
		
				Assert.assertTrue(true);
			}
			else {
				Assert.assertTrue(false);
			}
		}
		if(exp.equalsIgnoreCase("invalid")) {
			if(targetPage==true) {
				map.clickLogout();
				Assert.assertTrue(false);
			}
			else {
				Assert.assertTrue(true);
			}
		}
		}catch(Exception e) {
			Assert.fail();
		}
		
		logger.info("*****************Finished TC003_LoginDDT*****************");
	}

}
