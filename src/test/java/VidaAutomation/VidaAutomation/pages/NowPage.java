package VidaAutomation.VidaAutomation.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class NowPage extends BasePage {

	AppiumDriver<MobileElement> appiumDriver; 

	@AndroidFindBy(id = "com.vida.healthcoach:id/now_tab_title_text")
	@iOSFindBy(id = "titleLabel")
	private MobileElement nowHeaderLabel;
	
	@AndroidFindBy(id = "com.vida.healthcoach:id/action_button_container")
	@iOSFindBy(id = "startButton")
	private MobileElement startButton;
	
	@AndroidFindBy(id = "android:id/button2")
	private MobileElement laterButton;

	public NowPage(AppiumDriver<MobileElement> appiumDriver) {
		super(appiumDriver);
		this.appiumDriver = appiumDriver;
	}

	public NowPage dismissNotificationAlert() {
		try {
			laterButton.click();
		}catch(Exception e) {
			logger.info("No Alert was found to dismiss");
		}
		return new NowPage(appiumDriver);
	}
	
	public String getHeaderText() {
		return nowHeaderLabel.getText();
	}
	
	public boolean isStartBtnDisplayed() {
		return startButton.isDisplayed();
	}

}
