package VidaAutomation.VidaAutomation.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import VidaAutomation.VidaAutomation.pages.NowPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class NowTabConfig {

	NowPage nowPage;
	public static  Logger logger = LogManager.getLogger(NowTabConfig.class.getName()); ;

	public NowTabConfig(AppiumDriver<MobileElement> appiumDriver) {
		nowPage = new NowPage(appiumDriver);
	}

	public boolean isStartButtonDisplayed() {
		return nowPage.isStartBtnDisplayed();
	}

	public boolean doesScreenHeaderHaveTextNow() {
		return nowPage.getHeaderText().equalsIgnoreCase("now");
	}
}