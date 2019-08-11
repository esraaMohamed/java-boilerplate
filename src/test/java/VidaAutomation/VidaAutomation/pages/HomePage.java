package VidaAutomation.VidaAutomation.pages;

import org.openqa.selenium.NoSuchElementException;

import VidaAutomation.VidaAutomation.utilities.ApiBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class HomePage extends BasePage {
	AppiumDriver<MobileElement> appiumDriver;

	@AndroidFindBy(id = "com.vida.healthcoach:id/startup_login_button")
	@iOSFindBy(id = "loginButtonValuePropVC")
	private MobileElement startupLoginButton;

	@AndroidFindBy(id = "com.vida.healthcoach:id/startup_get_started_button")
	@iOSFindBy(id = "Get started")
	private MobileElement getStartedButton;

	public HomePage(AppiumDriver<MobileElement> appiumDriver) {
		super(appiumDriver);
		this.appiumDriver = appiumDriver;
		ApiBase.setup();
	}

	public boolean isClickStartUpLoginButtonDisplayed() {
		return startupLoginButton.isDisplayed();
	}

	public LoginPage clickStartupLoginBtn() {
		try {
			dismissAlert();
		} catch(NoSuchElementException e) {
			logger.info("No Alert was found to dismiss");
		}
		startupLoginButton.click();
		return new LoginPage(appiumDriver);
	}

}