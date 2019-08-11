package VidaAutomation.VidaAutomation.pages;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class LoginPage extends BasePage {
	AppiumDriver<MobileElement> appiumDriver;

	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Log in to Vida']")
	@iOSFindBy(id = "Already have an account? Log in to Vida")
	private MobileElement loginScreenHeaderText;

	@AndroidFindBy(id = "com.vida.healthcoach:id/login_email")
	@iOSFindBy(id = "emailTextField")
	private MobileElement emailTextField;

	@AndroidFindBy(id = "com.vida.healthcoach:id/login_password")
	@iOSFindBy(id = "passwordTextField")
	private MobileElement passwordTextField;

	@AndroidFindBy(id = "com.vida.healthcoach:id/login_button")
	@iOSFindBy(id = "loginButtonLoginVC")
	private MobileElement loginButton;

	public LoginPage(AppiumDriver<MobileElement> appiumDriver) {
		super(appiumDriver);
		this.appiumDriver = appiumDriver;
	}

	public boolean isLoginScreenHeaderDisplayed() {
		return loginScreenHeaderText.isDisplayed();
	}

	public LoginPage inputEmail(String email) {
		setTextElement(emailTextField, email);
		return new LoginPage(appiumDriver);
	}

	public LoginPage inputPassword(String password) {
		setTextElement(passwordTextField, password);
		return new LoginPage(appiumDriver);
	}

	public NowPage clickLogin() {
		loginButton.click();
		return new NowPage(appiumDriver);
	}
}
