package com.online.hooks;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.online.resources.DriverScript;



public class InitialSetup {
	 private WebDriver driver;

	    @BeforeSuite
	    public  void beforeScenario() {
	        driver = new DriverScript().getDriver();
	    }

	    @AfterSuite
	    public void afterScenario() throws IOException {
            driver.quit();  
	     }

}
