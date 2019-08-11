package VidaAutomation.VidaAutomation.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import VidaAutomation.VidaAutomation.pages.BasePage;
import VidaAutomation.VidaAutomation.pages.HomePage;
import VidaAutomation.VidaAutomation.pages.LoginPage;
import VidaAutomation.VidaAutomation.pages.NowPage;
import VidaAutomation.VidaAutomation.utilities.ApiBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class LoginConfig {

	HomePage homePage;
	LoginPage loginPage;
	NowPage nowPage;
	
	public Logger logger = LogManager.getLogger(BasePage.class.getName()); ;


	public LoginConfig(AppiumDriver<MobileElement> appiumDriver) {
		loginPage = new LoginPage(appiumDriver);
		homePage = new HomePage(appiumDriver);
		nowPage = new NowPage(appiumDriver);

	}

	public boolean wasLoginSuccessful() {
		try {
			nowPage.dismissNotificationAlert();
		} catch(Exception e) {
			logger.info("No Alert was found to dismiss");
		}
		return nowPage.isStartBtnDisplayed();
	}

	private void loginSteps(String email, String password) {
		homePage.clickStartupLoginBtn()
		.inputEmail(email)
		.inputPassword(password)
		.clickLogin();
	}

	public void login() {
		loginSteps(ApiBase.properties.getProperty("email"), ApiBase.properties.getProperty("password"));
	}
}