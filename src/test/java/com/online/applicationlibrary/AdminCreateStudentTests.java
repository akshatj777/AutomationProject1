package com.online.applicationlibrary;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.online.baseClass.TestBase;
import com.online.pages.Create_Student;

public class AdminCreateStudentTests extends TestBase{

	Create_Student obj_createstudent;
	WebDriver driver;
	@Test (priority=1, description = "Open Google Search URL")	
	public void open_url() throws Throwable {

		log.info("Open Google Search URL.");
		driver.get(data.getProperty("base.url"));

		log.info("Get input string from properties file and put it into the search box.");
		obj_createstudent = new Create_Student (driver);
		obj_createstudent.search_by_first_option(data.getProperty("TestCase_1.searchString_1"));
		
		log.info("Assert actual searched string with expected string from properties file.");
		assertStrings(obj_createstudent.get_first_option(),data.getProperty("TestCase_1.assertString_1"));

		//	  Assert.assertTrue(obj_google_search.get_first_option().equals(getPropertyValue("TestCase_1.assertString_1")));

	}
	
}
