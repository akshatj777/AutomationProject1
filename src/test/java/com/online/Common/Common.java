package com.online.Common;

import java.util.concurrent.TimeUnit;

import com.online.baseClass.BaseClass;
import com.online.resources.DriverScript;


public class Common extends DriverScript{

	BaseClass baseClass = new BaseClass(driver);
	
	  public void setup() throws Throwable {
	        driver.navigate().to(Config.getProperty("BaseUrl"));
	        driver.manage().timeouts().pageLoadTimeout(240, TimeUnit.SECONDS);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        driver.manage().window().maximize();
	        if(DriverScript.Config.getProperty("Browser").equals("chrome"))
	        {
	        	driver.manage().window().maximize();
	        }
	    }
	
}
