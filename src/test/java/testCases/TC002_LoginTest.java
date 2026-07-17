package testCases;

import org.testng.annotations.Test;

import junit.framework.Assert;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass{
	
	@Test(groups={"Sanity","Master"})
	public void verify_login() {
		logger.info("*************Starting LoginTest*************");
		
		try {
		//homepage
		HomePage hp = new HomePage(driver);
		
		hp.clickMyAccount();
		hp.clickLogin();
		
		//Loginpage
		LoginPage lp = new LoginPage(driver);
		
		lp.setEmail(p.getProperty("email"));
		lp.setPassword(p.getProperty("password"));
		lp.clickLogin();
		
		//MyAccount page
		MyAccountPage map = new MyAccountPage(driver);
		boolean targetPage = map.isMyAccountPageExists();
		//Assert.assertEquals(targetPage, true,"Login failed...");
		Assert.assertTrue(targetPage);
		}catch(Exception e) {
			Assert.fail();
		}
		
		logger.info("*************Finished LoginTest*************");
		
		
		
		
		
	}

}
