package VidaAutomation.VidaAutomation.pages;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import static io.appium.java_client.touch.offset.PointOption.point;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.selenium.fluent.FluentExecutionStopped;
import org.seleniumhq.selenium.fluent.FluentWebElement;
import org.seleniumhq.selenium.fluent.Period;
import org.seleniumhq.selenium.fluent.RetryAfterStaleElement;

import com.google.common.base.Function;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;

public abstract class BasePage {

	public static final int FIND_ELEMENT_TIMEOUT = 60; 	// in seconds
	public static final int LONG_ELEMENT_TIMEOUT = 120;
	public static final int SHORT_FIND_ELEMENT_TIMEOUT = 15; 			
	public static final int REALLY_SHORT_FIND_ELEMENT_TIMEOUT = 5; 		// timeout quickly when elements are correctly not visible.
	public static final int POLL = 1000;         // poll in milliseconds
	public static final Period LONG_TIMEOUT = Period.secs(LONG_ELEMENT_TIMEOUT);
	public static final Period TIMEOUT = Period.secs(FIND_ELEMENT_TIMEOUT);
	public static final Period SHORT_TIMEOUT = Period.secs(SHORT_FIND_ELEMENT_TIMEOUT);
	public final double DOWN_START_Y_AXIS = 0.70;
	public final double DOWN_END_Y_AXIS = 0.30;
	public final int DOWN_UP_START_X_AXIS = 2;
	public final double UP_START_Y_AXIS = 0.30;
	public final double UP_END_Y_AXIS = 0.70;
	public final int LEFT_RIGHT_START_Y_AXIS = 2;
	public final double LEFT_START_X_AXIS = 0.5;
	public final double LEFT_END_X_AXIS = 0.95;
	public final double RIGHT_START_X_AXIS = 0.95;
	public final double RIGHT_END_X_AXIS = 0.5;
	int startX, startY, endX, endY;
	public Logger logger = LogManager.getLogger(BasePage.class.getName()); ;


	public enum DIRECTION {
		DOWN("down"), UP("up"), LEFT("left"), RIGHT("right");
		String direction; 

		DIRECTION(String direction) { 
			this.direction = direction; 
		}

		@Override
		public String toString() {
			return direction; 
		};
	}

	public enum DATE_PICKER_DIRECTION{
		NEXT("next"), PREVIOUS("previous");
		String direction;
		private DATE_PICKER_DIRECTION(String direction) {
			this.direction = direction;
		}
		@Override
		public String toString() {
			return direction; 
		};
	}

	protected WebDriver driver;
	protected AppiumDriver<MobileElement> appiumDriver;
	protected static String baseUrl;

	private String originalWindowHandle = null;  

	public final WebDriverWait wait;
	public int timeInMillis = 50;
	public int timeInSecs = 10;

	public BasePage(WebDriver driver) { 
		this.driver = driver; 
		PageFactory.initElements(driver, this); 
		wait = new WebDriverWait(driver, timeInSecs, timeInMillis);
	}

	public BasePage(AppiumDriver<MobileElement> appiumDriver) {
		this.appiumDriver = appiumDriver;
		PageFactory.initElements(new AppiumFieldDecorator(appiumDriver), this); 
		wait = new WebDriverWait(appiumDriver, timeInSecs, timeInMillis);
	}

	public static void setBaseUrl(String url) { 
		baseUrl = url; 
	}

	protected void checkIfOnTheRightUrl(String regex) {
		String url = driver.getCurrentUrl();
		Matcher matcher = Pattern.compile(regex).matcher(url);
		int retry = 0;
		while (retry++ < 60) {
			if (matcher.find()) {
				return;
			}
		}
		throw new RuntimeException("Not on the right page");
	}

	public void wiggleMouse() {
		Actions actions = new Actions(driver);
		actions.moveByOffset(1000, 100).perform();
	}

	public void dragAndDrop(WebElement source, WebElement destination) {
		(new Actions(driver)).dragAndDrop(source, destination).perform();
	}

	public boolean isStaging(String stagingUrl) {
		return baseUrl != null && baseUrl.toLowerCase().contains(stagingUrl);
	}

	public static boolean isProd(String prodUrl) {
		return baseUrl != null && baseUrl.toLowerCase().contains(prodUrl);
	}

	protected static boolean tryAddCookie(WebDriver driver, String name, String value, String path) { 
		String host; 
		try { 
			host = (new URI(baseUrl)).getHost(); 		
			DateTime expTime = new DateTime().plusDays(30);
			Cookie cookie = new Cookie(name, value, host, path, expTime.toDate());
			driver.manage().addCookie(cookie);
		} catch (URISyntaxException|WebDriverException e) { 
			return false; 
		}
		return true; 
	}

	/**
	 * Wait for an element to be hidden, use to wait for status divs etc to be removed
	 * @param selector
	 */
	protected void waitForElementHidden(By selector) { 
		waitForElementHidden(selector, FIND_ELEMENT_TIMEOUT);
	}

	/**
	 * Wait for an element to be hidden, use to wait for status divs etc to be removed
	 * @param selector
	 * @param timeout timeout in Seconds
	 */
	protected void waitForElementHidden(By selector, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout, POLL);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(selector));
	}

	/**
	 * Wait for an element to be visible
	 * @param selector
	 */
	protected void waitForElementVisible(By selector) { 
		waitForElementVisible(selector, FIND_ELEMENT_TIMEOUT);
	}

	/**
	 * Wait for an element to be visible
	 * @param selector
	 * @param timeout, in seconds
	 */
	protected void waitForElementVisible(final By selector, int timeout) { 
		@SuppressWarnings("deprecation")
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
		.withTimeout(timeout, TimeUnit.SECONDS)
		.pollingEvery(POLL, TimeUnit.MILLISECONDS)
		.ignoring(NoSuchElementException.class)
		.ignoring(StaleElementReferenceException.class); 

		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver browser) {
				WebElement elem = browser.findElement(selector);
				if (elem != null && elem.isEnabled() && elem.isDisplayed()) { 
					return true;
				}; 
				return false;
			}
		});
	}

	protected void waitForElementVisible(WebElement element) { 
		WebDriverWait wait = new WebDriverWait(driver, FIND_ELEMENT_TIMEOUT, POLL); 
		wait.until(ExpectedConditions.visibilityOf(element)); 
	}

	protected void waitForElementVisible(WebElement element, int timeout) { 
		WebDriverWait wait = new WebDriverWait(driver, timeout, POLL); 
		wait.until(ExpectedConditions.visibilityOf(element)); 
	}

	protected void waitForElementVisible(MobileElement element) { 
		WebDriverWait wait = new WebDriverWait(appiumDriver, FIND_ELEMENT_TIMEOUT, POLL); 
		wait.until(ExpectedConditions.visibilityOf(element)); 
	}

	/**
	 * Don't fail if the element never shows
	 * @param selector
	 */
	protected boolean tryWaitForElementVisible(By selector) { 
		try { 
			waitForElementVisible(selector); 
			return true; 
		} catch (TimeoutException | StaleElementReferenceException e) { 
			return false; 
		}
	}

	/**
	 * @param selector
	 * @param timeout (in sec)
	 */
	protected boolean tryWaitForElementVisible(By selector, int timeout) { 
		try { 
			waitForElementVisible(selector, timeout); 
			return true; 
		} catch (TimeoutException | StaleElementReferenceException | NoSuchElementException e) { 
			return false; 
		}
	}

	protected boolean tryWaitForElementVisible(WebElement element) { 
		try { 
			waitForElementVisible(element); 
			return true; 
		} catch (TimeoutException | StaleElementReferenceException | NoSuchElementException e) { 
			return false; 
		}
	}

	protected boolean tryWaitForElementVisible(MobileElement element) { 
		try { 
			waitForElementVisible(element); 
			return true; 
		} catch (TimeoutException | NoSuchElementException e) { 
			return false; 
		}
	}

	/**
	 * The (short) timeout param is useful when we assertFalse for an element
	 */
	protected boolean tryWaitForElementVisible(WebElement element, int timeout) { 
		try { 
			waitForElementVisible(element, timeout); 
			return true; 
		} catch (TimeoutException | StaleElementReferenceException | NoSuchElementException e) { 
			return false; 
		}
	}

	/**
	 * Wait for an element to be clickable
	 * @param selector
	 */
	protected void waitForElementClickable(By selector) { 
		WebDriverWait wait = new WebDriverWait(driver, FIND_ELEMENT_TIMEOUT, POLL); 
		wait.until(ExpectedConditions.elementToBeClickable(selector)); 
	}

	/**
	 * Wait for an element to be clickable
	 */
	protected boolean tryWaitForElementClickable(WebElement element) {
		try {
			waitForElementClickable(element);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	protected void waitForElementClickable(WebElement element) { 
		waitForElementClickable(element, POLL);
	}

	protected void waitForElementClickable(WebElement element, int timeout) { 
		WebDriverWait wait = new WebDriverWait(driver, FIND_ELEMENT_TIMEOUT, timeout); 
		wait.until(ExpectedConditions.elementToBeClickable(element)); 
	}

	protected void waitForElementClickable(FluentWebElement element) {
		waitForElementClickable(element.getWebElement());
	}

	protected void setTextElement(final WebElement elem, final String text) {
		elem.sendKeys(text);
	}

	protected String getCurrentBrowserName() {
		return ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
	}

	protected void setTextElement(FluentWebElement elem, String text) {
		setTextElement(elem.getWebElement(), text);
	}

	protected WebElement scrollToElement(final WebElement elem) {
		new RetryAfterStaleElement() {
			public void toRetry() {
				if (isChrome()) {
					Actions actions = new Actions(driver);
					actions.moveToElement(elem).perform();
				} else {
					// Actions not supported in FF (https://github.com/SeleniumHQ/selenium/issues/4148) & not fully supported in Safari, using outside of Chrome
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elem);
				}
			}
		}.stopAfter(TIMEOUT);
		return elem;
	}

	protected FluentWebElement scrollToElement(FluentWebElement elem) { 
		scrollToElement(elem.getWebElement());
		return elem;
	}

	protected void clickByJavaScript(FluentWebElement elem) { 
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", elem.getWebElement());
	}

	protected void clickByJavaScript(WebElement elem) { 
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", elem);
	}

	protected void selectTextInSelect(final WebElement elem, final String text) { 
		new RetryAfterStaleElement() {
			public void toRetry() {
				waitForElementClickable(elem); 
				//if safari don't scroll
				if (!getCurrentBrowserName().equals("safari")) {
					scrollToElement(elem);
				}else {
					((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", elem);
				}
				Select select = new Select(elem); 
				select.selectByVisibleText(text);
			}
		}.stopAfter(SHORT_TIMEOUT);
	}

	protected void selectPartialTextInSelect(WebElement elem, String partialText) { 
		waitForElementClickable(elem); 
		scrollToElement(elem);
		List<WebElement> options = elem.findElements(By.xpath(".//option[contains(text(), '" + partialText + "')]"));

		int optionsSize = options.size();

		if (optionsSize == 0) {
			throw new NoSuchElementException("No option that contains '" + partialText + "' found.");
		} else if (optionsSize > 1 ) {
			throw new RuntimeException("More than 1 option that contains the text '" + partialText + "' found.");
		}

		// the optionSize == 1
		Select select = new Select(elem); 
		select.selectByVisibleText(options.get(0).getText());
	}

	protected void selectTextInSelect(FluentWebElement elem, String text) { 
		selectTextInSelect(elem.getWebElement(), text);
	}

	protected void retrySelectTextInSelect(FluentWebElement elem, String text) { 
		int maxRetries = 3; 
		int retries = 0; 
		while (retries++ < maxRetries) { 
			try { 
				selectTextInSelect(elem.getWebElement(), text);
				return; 
			} catch (FluentExecutionStopped e) { 
				try { Thread.sleep(1000); } catch (InterruptedException e1) { }
			}
		}
		throw new RuntimeException("Could not select text in the element after " + String.valueOf(maxRetries) + " attempts"); 
	}

	protected WebElement findElementWithText(By selector, String text) { 
		return findElementWithText(selector, text, /*partialMatch*/ false, /*maxRetries*/ 3, /*caseAgnostic*/ false); // default to full match, 3 retries and exact casing
	}

	protected WebElement findElementWithText(By selector, String text, boolean partialMatch) { 
		return findElementWithText(selector, text, partialMatch, /*maxRetries*/ 3, /*caseAgnostic*/ false); // default to 3 retries and exact casing
	}

	protected WebElement findElementWithText(By selector, String text, boolean partialMatch, int maxRetries) { 
		return findElementWithText(selector, text, partialMatch, maxRetries, /*caseAgnostic*/ false); // default to exact casing
	}

	protected WebElement findElementWithText(By selector, String text, boolean partialMatch, int maxRetries, boolean caseAgnostic) { 
		int retries = 0;
		String origText = text;

		if (isEdge()) {
			// edge treats case of visible elements different from other browsers, always ignore case in Edge
			caseAgnostic = true;
		}

		if (caseAgnostic) { 
			text = text.toLowerCase(); 
		}

		while (retries++ < maxRetries) { 
			try { 
				List<WebElement> elems = driver.findElements(selector); 
				for (WebElement elem : elems) { 
					String elemText = elem.getText(); 
					if (caseAgnostic) { 
						elemText = elemText.toLowerCase(); 
					} 
					if (partialMatch ? elemText.contains(text) : elemText.equals(text)) { 
						return elem; 
					}
				}
			} catch (RuntimeException ignored) { 
				try { Thread.sleep(5000); } catch (InterruptedException e) {}; 
			}
		}
		throw new RuntimeException("Element with text : " + origText + " was not found, after " + String.valueOf(maxRetries) + " retries"); 
	}

	protected void waitForElementTextChange(final By by) { 
		String oldText = driver.findElement(by).getText();
		int count = 0;  
		while (count++ < FIND_ELEMENT_TIMEOUT) { 
			if ((null != findVisibleElement(by))
					&& driver.findElement(by).getText().equals(oldText)) 
			{
				try { Thread.sleep(POLL); } catch (InterruptedException e) {}
			} else { 
				return; 
			}

		}
		throw new RuntimeException("Element text for " + by.toString() + " did not change from: " + oldText); 
	}

	/**
	 * Use to return the first visible element that matches a selector, when multiple 
	 * elements match the same css selector. 
	 * This is used on pages like the checkout page, when the same 'unique' css selector matches
	 * multiple elements, only one of which is visible and editable
	 * @param by
	 * @return
	 */
	protected WebElement findVisibleElement(By by) {
		List<WebElement> elems = driver.findElements(by); 
		for (WebElement elem : elems) { 
			if (elem.isDisplayed() && elem.isEnabled()) return elem; 
		}
		return null;
	}

	/** 
	 * hit the page up/down key to scroll up/down by one page
	 */
	public void pageScrollUpOrDown(String direction) {
		Actions actions = new Actions(driver);
		switch(direction) {
		case "up":
			actions.sendKeys(Keys.PAGE_UP).build().perform();
			break;
		case "down":
			actions.sendKeys(Keys.PAGE_DOWN).build().perform();
			break;
		default:
			logger.error("Invalid page scrolling direction");
		}
	}

	/**
	 * This method will scroll down until it finds the required text if it doesn't 
	 * it will throw an exception stating so.
	 */
	protected WebElement scrollToText(By selector, String text, boolean useDesignScroll, boolean partialMatch) {
		if (useDesignScroll) {
			By leftSideHeader_selector = By.cssSelector(".edit-design_aside-section-header");
			waitForElementVisible(leftSideHeader_selector, SHORT_FIND_ELEMENT_TIMEOUT);
			retryClick(driver.findElement(leftSideHeader_selector));
		}

		try {
			WebElement elem = findElementWithText(selector, text, partialMatch, 3, true);
			return scrollToElement(elem);
		} catch(RuntimeException ex) {} // page may be loading content below the fold dynamically

		// handle progressive load/infinite scroll pages. 
		for (int i = 0; i < 5; i++) {	
			new Actions(driver).sendKeys(Keys.ARROW_DOWN).perform(); // scroll down

			try {
				return findElementWithText(selector, text, false, 3, true);
			} catch (RuntimeException ignored) {}
		}
		throw new NoSuchElementException("The string " + text + " was not found in the page");
	}

	protected WebElement scrollToText(By selector, String text, boolean useDesignScroll) {
		// By default, do full match.
		return scrollToText(selector, text, useDesignScroll, /*partialMatch*/ false); 
	}

	protected WebElement scrollToText(By selector, String text) {
		// By default, search within the non-design scroll and do full match.
		return scrollToText(selector, text, /*useDesignScroll*/ false, /*partialMatch*/ false); 
	}

	/**
	 * retry a click multiple times
	 * @param elem
	 */
	protected void retryClick(FluentWebElement elem) {
		retryClick(elem.getWebElement());
	}

	/**
	 * retry a click multiple times
	 * @param elem
	 */
	protected void retryClick(WebElement elem) {
		// Edge && IE cannot click && no way to turn off nativeEvents without other issues, try clicking with javascript instead
		if (isEdge()) {
			try {
				clickByJavaScript(elem);
			} catch (WebDriverException e) {
				// some elements can't be clicked by js, last ditch try sending an enter key
				elem.sendKeys(Keys.ENTER);
			}
			return;
		}

		int maxRetries = 5; 
		int retries = 0; 
		while (retries++ < maxRetries) { 
			try { 
				elem.click(); 
				return; 
			} catch (Exception e) { 
				try { Thread.sleep(1000); } catch (InterruptedException e1) { }
			}
		}

		throw new RuntimeException("Could not click the element after " + String.valueOf(maxRetries) + " attempts"); 
	}

	protected void clickWithJavascript(WebElement elem) {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", elem);
	}

	protected void clickWithJavascript(FluentWebElement elem) {
		clickByJavaScript(elem.getWebElement());
	}

	protected static String urlBuilder(URI host, String... pathElems) { 
		StringBuilder path = new StringBuilder();

		// normalize the base url, always remove the trailing slash
		if (null != host) { 
			path.append(host.toString()); 
			if (path.charAt(path.length() - 1) == '/')  { 
				path.deleteCharAt(path.length() - 1); 
			}
		} 

		if (null == pathElems || pathElems.length == 0) { 
			return path.toString();
		}

		// normalize part II, always make sure the first elem has a leading /
		if (null != pathElems[0] && pathElems[0].charAt(0) != '/') { 
			pathElems[0] = '/' + pathElems[0]; 
		}

		for (String pathElem : pathElems) { 
			path.append(pathElem); 
			path.append('/'); 
		}

		// trim trailing / 
		path.deleteCharAt(path.lastIndexOf("/"));

		return path.toString(); 
	}

	protected static String urlBuilder(String... pathElems) {
		URI base;
		try {
			base = new URI(baseUrl);
		} catch (URISyntaxException e) {
			throw new RuntimeException("invalid base url", e);
		}
		return urlBuilder(base, pathElems);
	}

	/**
	 * Get all text on the page
	 * @return
	 */
	public String getAllText() { 
		return driver.findElement(By.tagName("body")).getText(); 
	}

	/**
	 * Check if an element's class attribute contains a specific class name.
	 */
	protected boolean elementHasClass(WebElement elem, String className) {
		return elem.getAttribute("class").contains(className);
	}

	/**
	 * Check if an element's class attribute contains a specific class name.
	 */
	protected boolean elementHasClass(FluentWebElement elem, String className) {
		return elem.getAttribute("class").toString().contains(className);
	}

	protected boolean elementHasClass(By by, String className) {
		return driver.findElement(by).getAttribute("class").toString().contains(className);
	}

	/**
	 * Get selected item from a dropdown.
	 */
	protected WebElement getSelectedItemFromDropdown(WebElement elem) {
		return new Select(elem).getFirstSelectedOption();
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	/**
	 * helper when you absolutely have to sleep 
	 * @param sleep - in MilliSeconds
	 */
	public static void YesIKnowHardCodedSleepsAreBad(long sleep) { 
		try { Thread.sleep(sleep); } catch(InterruptedException e) {}
	}

	protected void resizeBrowser(int width, int height) { 		
		driver.manage().window().setSize(new Dimension(width, height));
	}

	protected void maximizeBrowser() { 
		driver.manage().window().maximize(); 
	}

	protected void switchToTab(int tabIndex) { 
		originalWindowHandle = driver.getWindowHandle();

		List<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(allTabs.get(tabIndex)); 
	}

	protected void switchBackToMainTab() { 
		if (null != originalWindowHandle) { 
			driver.switchTo().window(originalWindowHandle);
		}
	}

	public void killAllTabsButMain() {
		List<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
		originalWindowHandle = allTabs.get(0);

		for (String handle : allTabs) {
			if (!handle.equals(originalWindowHandle)) { 
				driver.switchTo().window(handle).close();
			}
		}
		driver.switchTo().window(originalWindowHandle);
		YesIKnowHardCodedSleepsAreBad(SHORT_FIND_ELEMENT_TIMEOUT);
	}

	public WebElement getParentOfElement(WebElement elem) { 
		return elem.findElement(By.xpath(".."));
	}

	protected boolean isEdge() {
		return driver instanceof EdgeDriver ||
				(driver instanceof RemoteWebDriver && driver.toString().contains("MicrosoftEdge"));
	}

	protected boolean isChrome() {
		return driver instanceof ChromeDriver;
	}

	protected boolean isSafari() {
		return driver instanceof SafariDriver;
	}

	protected boolean isFirefox() {
		return driver instanceof FirefoxDriver || driver instanceof GeckoDriverService;
	}

	protected boolean isMobileEmulated() {
		try {
			if (isChrome()) {
				boolean isMobile = (boolean) ((ChromeDriver) driver).getCapabilities().getCapability("mobileEmulationEnabled");
				return isMobile;
			}
		} catch (Exception ignored) {}

		return false;
	}

	//Tap an element for 250 milliseconds
	public void tapByElement (MobileElement mobileElement) {
		//logger.info("Inside the tap element method");
		new TouchAction<>( appiumDriver)
		.tap(tapOptions().withElement(element(mobileElement)))
		.waitAction(waitOptions(Duration.ofMillis(250))).perform();
	}

	//Horizontal Swipe by percentages
	public void horizontalSwipeByPercentage (AppiumDriver<MobileElement> appiumDriver, double startPercentage, double endPercentage, double anchorPercentage) {
		Dimension size = appiumDriver.manage().window().getSize();
		System.out.println("size" + size);
		int anchor = (int) (size.height / anchorPercentage);
		int startPoint = (int) (size.width * startPercentage); 
		int endPoint = (int) (size.width * endPercentage); 
		new TouchAction<>(appiumDriver)
		.press(point(startPoint, anchor))
		.waitAction(WaitOptions.waitOptions(Duration.ofMillis(LONG_ELEMENT_TIMEOUT)))
		.moveTo(point(endPoint, anchor))
		.release()
		.perform();
	}

	public void swipeByElements (MobileElement startElement, MobileElement endElement) {
		int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
		int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);
		int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
		int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);
		new TouchAction<>(appiumDriver)
		.press(point(startX,startY))
		.waitAction(waitOptions(Duration.ofMillis(LONG_ELEMENT_TIMEOUT)))
		.moveTo(point(endX, endY))
		.release().perform();
	}

	public  void swipeOnAndroid(AppiumDriver<MobileElement> appiumDriver, DIRECTION direction, long duration) {
		Dimension size = appiumDriver.manage().window().getSize();
		JavascriptExecutor js = (JavascriptExecutor) appiumDriver;
		HashMap<String, String> swipeObject = new HashMap<String, String>();
		switch (direction) {
		case RIGHT:
			swipeRight(direction, duration, size, endY, js, swipeObject);
			swipeObject.put("direction", "right"); 
			swipeObject.put("startX", "90");
			swipeObject.put("startY", "400");
			swipeObject.put("endX", "90");
			swipeObject.put("endY", "350");
			swipeObject.put("duration", "2000");
			js.executeScript("mobile: swipe", swipeObject); 
			break;
		case LEFT:
			swipeLeft(direction, duration, size, js, swipeObject, endY); 
			break;
		case UP:
			swipeUp(direction, duration, size, js, swipeObject, endX); 
			break;
		case DOWN:
			swipeDown(direction, duration, size, js, swipeObject, endX); 
			break;
		default:
			System.out.println("Invalid direction");
			break;
		}
	}

	public void swipeOnIos(AppiumDriver<MobileElement> appiumDriver, DIRECTION direction, long duration) {
		Dimension size = appiumDriver.manage().window().getSize();
		JavascriptExecutor js = (JavascriptExecutor) appiumDriver;
		HashMap<String, String> swipeObject = new HashMap<String, String>();
		switch (direction) {
		case RIGHT:
			swipeRight(direction, duration, size, endY, js, swipeObject);
			break;
		case LEFT:
			swipeLeft(direction, duration, size, js, swipeObject, endY); 
			break;
		case UP:
			swipeUp(direction, duration, size, js, swipeObject, endX); 
			break;
		case DOWN:
			swipeDown(direction, duration, size, js, swipeObject, endX); 
			break;
		default:
			System.out.println("Invalid direction");
			break;
		}
	}

	private void swipeDown(DIRECTION direction, long duration, Dimension size, JavascriptExecutor js,
			HashMap<String, String> swipeObject, int endX) {
		startY = (int) (size.height * DOWN_START_Y_AXIS);
		endY = (int) (size.height * DOWN_END_Y_AXIS);
		startX = (size.width / DOWN_UP_START_X_AXIS);
		swipeObject.put("direction", direction.toString());
		swipeObject.put("startX", Integer.toString(startX));
		swipeObject.put("startY", Integer.toString(startY));
		swipeObject.put("endX", Integer.toString(endX));
		swipeObject.put("endY", Integer.toString(endY));
		swipeObject.put("duration", Long.toString(duration));
		js.executeScript("mobile: swipe", swipeObject);
	}

	private void swipeUp(DIRECTION direction, long duration, Dimension size, JavascriptExecutor js,
			HashMap<String, String> swipeObject, int endX) {
		endY = (int) (size.height * UP_END_Y_AXIS);
		startY = (int) (size.height * UP_START_Y_AXIS);
		startX = (size.width / DOWN_UP_START_X_AXIS);
		swipeObject.put("direction", direction.toString());
		swipeObject.put("startX", Integer.toString(startX));
		swipeObject.put("startY", Integer.toString(startY));
		swipeObject.put("endX", Integer.toString(endX));
		swipeObject.put("endY", Integer.toString(endY));
		swipeObject.put("duration", Long.toString(duration));
		js.executeScript("mobile: swipe", swipeObject);
	}

	private void swipeLeft(DIRECTION direction, long duration, Dimension size, JavascriptExecutor js,
			HashMap<String, String> swipeObject, int endY) {
		startY = (int) (size.height / LEFT_RIGHT_START_Y_AXIS);
		startX = (int) (size.width * LEFT_START_X_AXIS);
		endX = (int) (size.width * LEFT_END_X_AXIS);
		swipeObject.put("direction", direction.toString());
		swipeObject.put("startX", Integer.toString(startX));
		swipeObject.put("startY", Integer.toString(startY));
		swipeObject.put("endX", Integer.toString(endX));
		swipeObject.put("endY", Integer.toString(endY));
		swipeObject.put("duration", Long.toString(duration));
		js.executeScript("mobile: swipe", swipeObject);
	}

	public void swipeLeft(DIRECTION direction, long duration, Dimension size, JavascriptExecutor js,
			HashMap<String, String> swipeObject, int endY, double leftRightStartYAxis) {
		startY = (int) (size.height / leftRightStartYAxis);
		startX = (int) (size.width * LEFT_START_X_AXIS);
		endX = (int) (size.width * LEFT_END_X_AXIS);
		swipeObject.put("direction", direction.toString());
		swipeObject.put("startX", Integer.toString(startX));
		swipeObject.put("startY", Integer.toString(startY));
		swipeObject.put("endX", Integer.toString(endX));
		swipeObject.put("endY", Integer.toString(endY));
		swipeObject.put("duration", Long.toString(duration));
		js.executeScript("mobile: swipe", swipeObject);
	}

	private void swipeRight(DIRECTION direction, long duration, Dimension size, int endY, JavascriptExecutor js,
			HashMap<String, String> swipeObject) {
		startY = (int) (size.height / LEFT_RIGHT_START_Y_AXIS);
		startX = (int) (size.width * RIGHT_START_X_AXIS);
		endX = (int) (size.width * RIGHT_END_X_AXIS);
		swipeObject.put("direction", direction.toString());
		swipeObject.put("startX", Integer.toString(startX));
		swipeObject.put("startY", Integer.toString(startY));
		swipeObject.put("endX", Integer.toString(endX));
		swipeObject.put("endY", Integer.toString(endY));
		swipeObject.put("duration", Long.toString(duration));
		js.executeScript("mobile: swipe", swipeObject);
	}

	public static boolean waitForElementPresence(AppiumDriver<MobileElement> appiumDriver, int timeLimitInSeconds, String targetResourceId) {
		MobileElement mobileElement;
		boolean isElementPresent;
		try {
			mobileElement = appiumDriver.findElementByAccessibilityId(targetResourceId);
			WebDriverWait wait = new WebDriverWait(appiumDriver, timeLimitInSeconds);
			wait.until(ExpectedConditions.visibilityOf(mobileElement));
			isElementPresent = mobileElement.isDisplayed();
			return isElementPresent;	
		} catch(Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		} 
	}

	protected MobileElement findElement(By by) {
		return appiumDriver.findElement(by);
	}

	public static void singleTapElement(AppiumDriver<MobileElement> appiumDriver, MobileElement element) {
		@SuppressWarnings("rawtypes")
		TouchAction action = new TouchAction(appiumDriver);
		System.out.println("Inside tap");
		action.tap(new TapOptions().withElement(ElementOption.element(element)))
		.perform();
	}

	public static void singleTapAndHoldElement(AppiumDriver<MobileElement> appiumDriver, MobileElement element) {
		@SuppressWarnings("rawtypes")
		TouchAction action = new TouchAction(appiumDriver);
		System.out.println("Inside tap and hold");
		action.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element (element)))
		.perform ();
	}

	public void dismissAlert() {
		logger.info("Wait to dismiss any system alerts");
		WebDriverWait wait = new WebDriverWait(appiumDriver, FIND_ELEMENT_TIMEOUT);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = appiumDriver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			logger.error("No alert was visible after 60 seconds");
		}
	}

	public static void scrollInDatePicker(AppiumDriver<MobileElement> driver, MobileElement pickerElement, DATE_PICKER_DIRECTION direction, double offset) {
		Map<String, Object> params = new HashMap<>();
		params.put("order", direction);
		params.put("offset", offset);
		params.put("element", ((RemoteWebElement) pickerElement).getId());
		driver.executeScript("mobile: selectPickerWheelValue", params);
	}
	
	
	public boolean returnTrue() {
		return true;
	}
	
	protected boolean retry(AppiumDriver<MobileElement> appiumDriver, By element, int maxRetries) {
		while (--maxRetries >= 0) {
			if(appiumDriver.findElement(element).isDisplayed()) {
				return true;
			}
		}
		return false;
	}
}
