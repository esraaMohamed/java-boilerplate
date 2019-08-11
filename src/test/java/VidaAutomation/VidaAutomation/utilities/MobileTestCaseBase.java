package VidaAutomation.VidaAutomation.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class MobileTestCaseBase extends TestCaseBase {
	public static final Object MOBILE_DRIVER_START_LOCK = new Object();
	public MOBILE_DRIVER mobileDriver;
	public int testRailRunId;
	protected static AppiumDriver<MobileElement> appiumDriver;
	private DesiredCapabilities capabilities;
	private String platformVersion, deviceName, appPath, appName, appPackage, appActivity;
	private URL connectionLinkToServer;

	/**
	 * Setting capabilities for iOS driver
	 * @return iOS driver
	 */
	private final IOSDriver<MobileElement> startIosDriver() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MOBILE_DRIVER.IOS);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, this.platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, this.deviceName);
		File app = new File(this.appPath);
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		capabilities.setCapability(MobileCapabilityType.APPLICATION_NAME, this.appName);
		capabilities.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
		capabilities.setCapability(IOSMobileCapabilityType.SUPPORTS_NETWORK_CONNECTION, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
		capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, true);
		capabilities.setCapability(IOSMobileCapabilityType.CONNECT_HARDWARE_KEYBOARD, true);
    capabilities.setCapability("isHeadless", true);
		capabilities.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, true);
		capabilities.setCapability("wdaEventloopIdleDelay", 3);
		capabilities.setCapability("eventLoopIdleDelaySec", 1);
		capabilities.setCapability("isHeadless", true);

		return new IOSDriver<MobileElement>(connectionLinkToServer, capabilities);
	}

	/**
	 * Setting capabilities for Android driver
	 * @return Android driver
	 */
	private final AndroidDriver<MobileElement> startAndroidDriver(){
		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MOBILE_DRIVER.Android);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, this.platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, this.deviceName);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, this.appPackage);
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, this.appActivity);
		capabilities.setCapability(MobileCapabilityType.APP, this.appPath);
		capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
		return new AndroidDriver<MobileElement>(connectionLinkToServer, capabilities);
	}

	/**
	 * Starting the mobile driver
	 * @return
	 */
	protected final AppiumDriver<MobileElement> startDriver() {
		synchronized(MOBILE_DRIVER_START_LOCK) {
			if (MOBILE_DRIVER.IOS == mobileDriver) {
				appiumDriver = startIosDriver();
			} else if (MOBILE_DRIVER.Android == mobileDriver) {
				appiumDriver = startAndroidDriver();
			} else {
				throw new IllegalArgumentException("Unsupported driver type: " + mobileDriver.toString());
			}
			appiumDriver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
			return appiumDriver;
		}
	}

	/**
	 * Mobile test case setup
	 * @param driver
	 * @param platformVersion
	 * @param deviceName
	 * @param serverUrl
	 * @throws IOException
	 */
	@BeforeClass(alwaysRun = true)
	@Parameters({"driver", "platformVersion", "deviceName", "serverUrl", "appPackage", "appActivity", "appPath"})
	public final void mobileTestCaseSetup(
			@Optional("android") String driver,
			@Optional("7.1.1") String platformVersion,
			@Optional("Nexus5X") String deviceName,
			@Optional("com.vida.healthcoach") String appPackage,
			@Optional("http://127.0.0.1:4723/wd/hub") String serverUrl,
			@Optional("com.vida.healthcoach.StartupActivity") String appActivity,
			@Optional("app-distro-release.apk") String appPath)throws IOException {
		logger = configureLogging(this.getClass().getName() + ".log");
		logger.info("Beginning test setup");
		mobileDriver = MOBILE_DRIVER.fromString(driver); 
		logger.info("Using the driver: " + mobileDriver.toString());
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.appPackage = appPackage;
		this.appActivity = appActivity;
		this.appPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + appPath;
		logger.info("Class run app Path: " + this.appPath);
		this.connectionLinkToServer = new URL(serverUrl);
		appiumDriver = startDriver();
	}

	/**
	 * Mobile test case tear down
	 * @throws Exception
	 */
	@AfterClass (alwaysRun = true)
	public final void mobileTestCaseTearDown() throws Exception {
		if (appiumDriver != null) {
			appiumDriver.removeApp("com.vida.healthcoach"); // Works only for android
			appiumDriver.quit();
		}
		System.out.println("Closed Appium Driver");
		logger.log(Level.INFO, "Closed Appium Driver");
	}

	public AppiumDriver<MobileElement> getDriver() {
		return appiumDriver;
	}
}
