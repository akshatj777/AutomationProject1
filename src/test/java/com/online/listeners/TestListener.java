package com.online.listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.online.baseClass.TestBase;


public class TestListener extends TestBase implements ITestListener {
	 
	String RootFile = System.getProperty("user.dir");
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailure(ITestResult result) {
		System.out.println("***** Error "+result.getName()+" test has failed *****");
    	String methodName=result.getName().toString().trim();
    	takeScreenShot(methodName);
		
	}
	
	 public void takeScreenShot(String methodName) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String date = sdf.format(new Date());
			String url = driver.getCurrentUrl().replaceAll("[\\/:*\\?\"<>\\|]", "_");
			String ext = ".png";
			String path = getScreenshotSavePath() + File.separator + date + "_" + url + ext;

			try {
				if (driver instanceof TakesScreenshot) {
					File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					org.openqa.selenium.io.FileHandler.copy(tmpFile, new File(path));
					log.error("Captured Screenshot for Failure: "+path);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	    }

	 protected String getScreenshotSavePath() {
			String packageName = this.getClass().getPackage().getName();
			File dir = new File(System.getProperty("user.dir")+File.separator+"screenshot"+File.separator + packageName + File.separator);
			dir.mkdirs();
			return dir.getAbsolutePath();
		}
	 
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

}
