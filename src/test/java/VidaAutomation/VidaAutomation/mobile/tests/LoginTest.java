package VidaAutomation.VidaAutomation.mobile.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import VidaAutomation.VidaAutomation.business.LoginConfig;
import VidaAutomation.VidaAutomation.utilities.MobileTestCaseBase;


public class LoginTest extends MobileTestCaseBase {
	LoginConfig loginConfig;

	@BeforeClass
	public void init() {
		loginConfig = new LoginConfig(appiumDriver);	
	}

	@Test
	public void login() {
		loginConfig.login();
		Assert.assertTrue(loginConfig.wasLoginSuccessful(), "Login was not successful");
	}
	

}