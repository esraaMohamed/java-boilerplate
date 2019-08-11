package VidaAutomation.VidaAutomation.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class LoggingHelper extends TestListenerAdapter {

	@Override
	public void onTestFailure(ITestResult tr) {
		Logger logger = ((TestCaseBase)tr.getInstance()).getLogger();
		logger.fatal("FAILURE in " + tr.getMethod().getMethodName());

		@SuppressWarnings("unchecked")
		AppiumDriver<MobileElement> appiumDriver = (AppiumDriver<MobileElement>) ((TestCaseBase)tr.getInstance()).getDriver(); 
		if (null != appiumDriver)	{ 		
			// capture a screenshot on failure
			String fileName = tr.getTestClass().getName() + "_" + tr.getMethod().getMethodName() + "_" 
					+ new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()) + ".jpg"; 
			TestCaseBase.takeScreenshot(appiumDriver, fileName); 
			Reporter.log("<a href='" + getImgSrcPath(fileName) + "'> <img src='" + getImgSrcPath(fileName) + "' height='100' width='100'/> </a>");

			TestDescription td = getTestDescription(tr);
			if (null == td) {             
				StringBuilder bldr = new StringBuilder(); 
				bldr.append("Failing test  ")
				.append(tr.getTestClass().getName())
				.append(":")
				.append(tr.getMethod().getMethodName())
				.append(", at time: ")
				.append(getTimeStr())
				.append(", elapsed time (secs): ")
				.append(String.valueOf((tr.getEndMillis() - tr.getStartMillis())/1000))
				.append("\n");
				logger.fatal(bldr.toString());
			} else { 
				printFailFromTestDescription(td, tr, logger);
			}
		}			
		super.onTestFailure(tr);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {
		Logger logger = ((TestCaseBase)tr.getInstance()).getLogger();
		logger.fatal("FAILURE w. Success Percentage in " + tr.getMethod().getMethodName());
		super.onTestFailedButWithinSuccessPercentage(tr);
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		Logger logger = ((TestCaseBase)tr.getInstance()).getLogger();
		TestDescription td = getTestDescription(tr);
		if (null == td) {             
			StringBuilder bldr = new StringBuilder(); 
			bldr.append("Passing test  ")
			.append(tr.getTestClass().getName())
			.append(":")
			.append(tr.getMethod().getMethodName())
			.append(", at time: ")
			.append(getTimeStr())
			.append(", elapsed time (secs): ")
			.append(String.valueOf((tr.getEndMillis() - tr.getStartMillis())/1000))
			.append("\n"); 
			logger.info(bldr.toString()); 
		} else { 
			printPassFromTestDescription(td, tr, logger);
		}
		System.out.println("PASS: " + tr.getMethod().getMethodName() + " passed!\n");
		super.onTestSuccess(tr);
	} 

	@Override
	public void onTestSkipped(ITestResult tr) {
		tr.getTestContext().getSkippedTests().removeResult(tr.getMethod());
	} 

	@Override
	public void onTestStart(ITestResult tr) {   
		TestDescription td = getTestDescription(tr);
		Logger logger = ((TestCaseBase)tr.getInstance()).getLogger();
		if (null == td) {             
			StringBuilder bldr = new StringBuilder(); 
			bldr.append("Starting test ")
			.append(tr.getTestClass().getName())
			.append(":")
			.append(tr.getMethod().getMethodName())
			.append(", at time: ")
			.append(getTimeStr())
			.append("\n");
			logger.info(bldr.toString()); 
		} else { 
			printStartFromTestDescription(td, tr, logger);
		}  
		super.onTestStart(tr);
	}

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
	}

	/**
	 * Check if this is running on the Jenkins CI server
	 * @return
	 */
	private static boolean isOnJenkins() { 
		// Jenkins sets a number of env variables for each run
		//TODO : When there is configurations for jenkins using this will tell if we are running the test on jenkins
		return false;  
	}

	private String getImgSrcPath(String fileName) { 
		String srcPath = fileName;
		if (isOnJenkins()) { 
			srcPath = new StringBuilder().append("./../../ws/target/surefire-reports/")
					.append(fileName).toString();
		}

		return srcPath; 
	}

	private String getTimeStr() { 
		DateTime time = new DateTime();
		return DateTimeFormat.forPattern("HH:mm:ss").print(time); 
	}

	private void loggerHelper(TestDescription td, ITestResult tr, Logger logger, String status) {
		String shortDescription = (null == td.description()) ? null : td.description();

		logger.info("****************************************************************************************************");
		logger.info(String.format("%-98s", "* "+status+": ")+ " *");
		logger.info(String.format("%-98s", "* " + getLeftTrimmedMethodName(tr, 96)) + " *");
		if (null != shortDescription) { 
			logger.info(String.format("%-98s", "* Test Description: " + shortDescription) + " *");
		}
		logger.info(String.format("%-98s", "* At time: " + getTimeStr()) + " *");
	}

	private void loggingTimestamp(TestDescription td, ITestResult tr, Logger logger, String status) {
		if(status.contains("pass")) {
			logger.info(String.format("%-98s", "* Elapsed time: " + 
					(String.valueOf((tr.getEndMillis() - tr.getStartMillis())/1000))
					+ " seconds")
					+ " *");
		} else if(status.contains("failing")) {
			logger.error(String.format("%-98s", "* Elapsed time: " + 
					(String.valueOf((tr.getEndMillis() - tr.getStartMillis())/1000))
					+ " seconds")
					+ " *"); 
			if (null != td.steps()) { 
				String lines[] = td.steps().split("\n");
				logger.error(String.format("%-98s", "* Test Steps: ") + " *"); 
				for (String line : lines) { 
					logger.error("*     " + String.format("%-92s", line) + " *");    
				}    
			}
		}
	}

	private void printStartFromTestDescription(TestDescription td, ITestResult tr, Logger logger) {
		loggerHelper(td, tr, logger, "Start Test");
		logger.info("****************************************************************************************************\n");
	}

	private void printPassFromTestDescription(TestDescription td, ITestResult tr, Logger logger) {
		loggerHelper(td, tr, logger, "Passing Test");
		loggingTimestamp(td, tr, logger, "passing"); 
		logger.info("****************************************************************************************************\n");
	}

	private void printFailFromTestDescription(TestDescription td, ITestResult tr, Logger logger) {
		loggerHelper(td, tr, logger, "Failing Test");
		loggingTimestamp(td, tr, logger, "failing"); 
		logger.error("****************************************************************************************************\n");
	}

	private TestDescription getTestDescription(ITestResult tr) { 
		return tr.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestDescription.class);
	}

	private String getMethodName(ITestResult tr) { 
		return (new StringBuilder()).append(tr.getTestClass().getName())
				.append(":")
				.append(tr.getMethod().getMethodName())
				.toString();
	}

	private String getLeftTrimmedMethodName(ITestResult tr, int size) { 
		String methodName = getMethodName(tr); 
		if (methodName.length() <= size) { 
			return methodName; 
		}

		int startIndex = (methodName.length() - size) + 3; // 3 more for the ... at the start
		int endIndex = methodName.length(); 
		String leftTrimmed = "..." + methodName.substring(startIndex, endIndex); 
		return leftTrimmed; 
	}
}