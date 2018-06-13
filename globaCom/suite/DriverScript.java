package globaCom.suite;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.codoid.products.exception.FilloException;

import genericLibraries.GenericFunctionLibrary;
import io.appium.java_client.android.AndroidDriver;

public class DriverScript {

	AndroidDriver driver;
	GenericFunctionLibrary genLib = new GenericFunctionLibrary(driver);
	
	
	
	@BeforeMethod
	public void setUp() throws MalformedURLException, Exception 
	{
		//driver = genLib.gen_launchApp();
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//capabilities.setCapability(CapabilityType, "Android");
		  capabilities.setCapability("platformName ", "Android");
		  capabilities.setCapability("deviceName", "DeviceName");
		  capabilities.setCapability("appPackage","com.nnacres.app");
		  
	         capabilities.setCapability("appActivity","com.nnacres.app.activity.SplashActivity");
		  driver = new AndroidDriver(new URL("http://127.0.0.1:4723"+"/wd/hub"), capabilities);
		  System.out.println("app Launched");
		
		
	}	
	
	
	
	@Test
	public void TestScript() throws FilloException, IOException, InterruptedException
	{
		
		TestScript t = new TestScript(driver);
		t.MainTest();
		
		System.out.println("AAAAAAAAAAAAAAAAA");
	}
	
	@Test 
	public void TestScript2() throws FilloException, IOException, InterruptedException
	{
		
		TestScript t = new TestScript(driver);
		t.MainTest();
		
		System.out.println("AAAAAAAAAAAAAAAAA");
	}
	
	@Test
	public void TestScript3() throws FilloException, IOException, InterruptedException
	{
		
		TestScript t = new TestScript(driver);
		t.MainTest();
		System.out.println("AAAAAAAAAAAAAAAAA");
	}
	
	@Test
	public void TestScript4() throws FilloException, IOException, InterruptedException
	{
		
		TestScript t = new TestScript(driver);
		t.MainTest();
		System.out.println("AAAAAAAAAAAAAAAAA");
	}
	
	
	
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
	
	
}
