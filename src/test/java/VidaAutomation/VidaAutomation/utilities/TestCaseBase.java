package VidaAutomation.VidaAutomation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Listeners;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

@Listeners(value=LoggingHelper.class)
public abstract class TestCaseBase implements BaseTest {
	public Logger logger; 
	
	protected synchronized final static Logger configureLogging(String logName) { 
		Logger logger = null;
		try { 
			System.setProperty("logFilename", logName);
			String log4jConfigFile = "resources/log4j2.xml";
			ConfigurationSource source = new ConfigurationSource(new FileInputStream(log4jConfigFile));
			Configurator.initialize(null, source);
			logger = LogManager.getLogger(TestCaseBase.class.getName()); 
		} catch (Exception e) {
			System.out.println("Can't initialize the logger");
		}; 
		return logger; 
	}
	
	/**
	 * Get the logger instance
	 */
	public final Logger getLogger() { 
		return logger;
	}

	
	/**
	 * Helper when you absolutely have to sleep
	 * @param sleep - in MilliSeconds
	 */
	public static void YesIKnowHardCodedSleepsAreBad(long sleep) { 
		try { Thread.sleep(sleep); } catch(InterruptedException e) {}
	}
	
	/** 
	 * Generate a random string from date
	 * @return
	 */
	protected final static String getUniqueString() { 
		return new SimpleDateFormat("yyyyMMdd.HHmmss.").format(new Date()) + String.valueOf(new Random().nextInt(100));
	}

	/**
	 * Get a semi random phone number that should be valid most of the time.
	 * 	  -cycles through six different area codes
	 * 	  -other 7 digits are randomly generated and are guaranteed to not be 0's.
	 * @return
	 */
	protected final String getSemiRandomPhoneNumber() { 
		Random random = new Random();
		String [] areaCodes = {"858", "949", "714", "916", "808", "316"};

		String areaCode = areaCodes[random.nextInt(areaCodes.length)];
		String sevenDigits = "";

		for(int i = 0; i < 7; i++){
			sevenDigits += String.valueOf(random.nextInt(9) + 1); //non-zero digits
		}

		return areaCode + sevenDigits;
	}
	
	/**
	 * Take a screenshot when a test case fails
	 * @param driver
	 * @param fileName
	 * @return
	 */
	public static String screenshotOnFailure(WebDriver driver, String fileName) { 
		// capture a screenshot on failure
		Path dest = Paths.get(".", "screenshots", fileName);
		try {
			Console.printRed("Current URL: " + driver.getCurrentUrl());
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, dest.toFile());
			Console.printRed("Screenshot in ./surefire-reports/" + fileName);	    
		} catch (IOException | WebDriverException e) {
			Console.printRed("Failed to take screenshot: " + e.getMessage());
		}
		return dest.toString(); 
	}

	/**
	 * Take screenshot with unique name
	 * @param driver
	 * @param fileName 
	 * @return
	 */
	public static String takeScreenshot(WebDriver driver, String fileName) { 
		return screenshotOnFailure(driver, fileName);
	}
	
	
	/**
	 * Take a screenshot when a test case fails
	 * @param appiumDriver
	 * @param fileName
	 * @return
	 */
	public static String screenshotOnFailure(AppiumDriver<MobileElement> appiumDriver, String fileName) { 
		// capture a screenshot on failure
		Path dest = Paths.get(".", "screenshots", fileName);
		try {
			File scrFile = ((TakesScreenshot)appiumDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, dest.toFile());
			System.out.println("Screenshot in ./screenshots/ " + fileName);	    
		} catch (IOException | WebDriverException e) {
			System.out.println("Failed to take screenshot: " + e.getMessage());
		}
		return dest.toString(); 
	}
	/**
	 * Take screenshot with unique name
	 * @param driver
	 * @return
	 */
	public static String takeScreenshot(AppiumDriver<MobileElement> driver, String fileName) { 
		return screenshotOnFailure(driver, fileName);
	}
	
	public abstract RemoteWebDriver getDriver();
}
