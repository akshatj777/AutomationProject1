package globaCom.suite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.codoid.products.exception.FilloException;
import genericLibraries.GenericFunctionLibrary;
import io.appium.java_client.android.AndroidDriver;

public class TestScript {

	AndroidDriver driver = null;
	GenericFunctionLibrary genLib = new GenericFunctionLibrary(driver);
	
	
	public TestScript(AndroidDriver driver)
	{
         this.driver = driver;		
	}
	
	
	public void MainTest() throws FilloException, IOException, InterruptedException
	{
		
		GenericFunctionLibrary genLib = new GenericFunctionLibrary(driver);
		//updateDataIntoExcel("Login_Details", "1", "Password");
		//genLib.gen_updateDataIntoExcel("Login_Details", "1", "Status", "Pass");
		System.out.println("assadasdjflkagh");
		genLib.gen_Swipe(0.0, 0.9, 0.0, 0.2, 3000);
		genLib.fg_clearAppData();
	}
	
	
	 
	
	
	}
