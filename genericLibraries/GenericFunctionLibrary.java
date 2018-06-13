package genericLibraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class GenericFunctionLibrary {
	
	AndroidDriver driver;
	String currentDir = System.getProperty("user.dir")+"\\";
	
	
	public GenericFunctionLibrary(AndroidDriver driver) {
			this.driver = driver;
			
		}
	
	//############################## ANSI Color Codes ############################################//
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BOLD = "\u001B[1m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	
	//#############################################################################################//
	//############################# Function: Launch Android Application ##########################//        
	//############################# Objective: Launch Desired Application on Android ##############// 
	//############################# Created By: Vandana Maurya ####################################//
	//############################# Created On: 17th-Nov-2015 #####################################//
	
	public AndroidDriver gen_launchApp() throws IOException, InterruptedException
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, gen_getDataFromConfig("BrowserName"));
		capabilities.setCapability("deviceName", gen_getDataFromConfig("deviceName"));
		capabilities.setCapability("platformName", gen_getDataFromConfig("platformName"));
		capabilities.setCapability("app-package", gen_getDataFromConfig("app-package"));
		capabilities.setCapability("app-activity", gen_getDataFromConfig("app-activity"));
		System.out.println(currentDir+gen_getDataFromConfig("apkFolder")+gen_getDataFromConfig("apkName"));
		capabilities.setCapability("app", currentDir+gen_getDataFromConfig("apkFolder")+gen_getDataFromConfig("apkName"));
		//driver= new RemoteWebDriver( new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver= new AndroidDriver( new URL("http://127.0.0.1:"+gen_getDataFromConfig("AppiumPort")+"/wd/hub"), capabilities) ;
		System.out.println("App Launched ");
		Thread.sleep(20000);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return driver;
	}
	
	
	public String gen_getDataFromConfig(String inputdata) throws IOException
	{
		String data;
		Properties prop = new Properties();
		InputStream input = new FileInputStream(currentDir +"config.properties");
		prop.load(input);
		data = prop.getProperty(inputdata);
		return data;
	}
	
	
	public int gen_getCount(String sheetName) throws FilloException, IOException
	{
		GenericFunctionLibrary genLib = new GenericFunctionLibrary(driver);
		String SheetPath = System.getProperty("user.dir")+"\\data\\GlobaComData\\Sheets\\"+genLib.gen_getDataFromConfig(genLib.gen_getDataFromConfig("ProjName")+"_DataSheet");
		System.out.println("Get Count From: " + SheetPath);
		Fillo fillo = new Fillo();
		Connection connection = fillo.getConnection(SheetPath);
		String strQuery = "Select * from " + sheetName + "";
		Recordset recordset = connection.executeQuery(strQuery);
		int count = recordset.getCount();
		recordset.close();
		connection.close();
		return count;
    }
	
	
	public String gen_getDataFromExcel(String SheetName, String condParam, String colName) throws FilloException, IOException
	{
		Fillo fillo = new Fillo();
		String data = null;
		GenericFunctionLibrary genLib = new GenericFunctionLibrary(driver);
		String SheetPath = System.getProperty("user.dir")+"\\data\\GlobaComData\\Sheets\\"+genLib.gen_getDataFromConfig(genLib.gen_getDataFromConfig("ProjName")+"_DataSheet");
		System.out.println(SheetPath);
		String strQuery = "Select * from " + SheetName + " where SNO="+ condParam + "";
		System.out.println("strQuery----" + strQuery);
		
		try
		{
			Connection connection = fillo.getConnection(SheetPath);
			Recordset recordset = connection.executeQuery(strQuery);
			while (recordset.next()) 
			{
				data = recordset.getField(colName);
				System.out.println("sheet data output::::::::" + data);
			}
			recordset.close();
			connection.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
			System.out.println("Failed while fetching data from Excel Sheet... So try again");
			Connection connection = fillo.getConnection(SheetPath);
			Recordset recordset = connection.executeQuery(strQuery);
			while (recordset.next())
			{
				data = recordset.getField(colName);
				System.out.println("sheet data output::::::::" + data);
			}
			recordset.close();
			connection.close();
		}
		catch(Throwable e)
		{
			System.out.println(e.getLocalizedMessage());
			System.out.println("Failed to fetch data from Excel Sheet");
		}http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=732
		return data;
	}
	
	
	public void gen_updateDataIntoExcel(String SheetName, String condParam, String colName, String updateParameter) throws FilloException, IOException, InterruptedException
	{
		Fillo fillo = new Fillo();
		GenericFunctionLibrary genLib = new GenericFunctionLibrary(driver);
		String SheetPath = System.getProperty("user.dir")+"\\data\\GlobaComData\\Sheets\\"+genLib.gen_getDataFromConfig(genLib.gen_getDataFromConfig("ProjName")+"_DataSheet");
		genLib.gen_printInfoLog(SheetPath);
		String strQuery = "Update " + SheetName + " Set " + colName + "=" + "'" + updateParameter + "' where SNO=" + condParam + "";
		genLib.gen_printInfoLog("strQuery----" + strQuery);
		try
		{
			Connection connection = fillo.getConnection(SheetPath);
			connection.executeUpdate(strQuery);
			connection.close();
			genLib.gen_printPassLog("Updated Data in Excel Sheet");
		}
		catch(Exception e)
		{
		genLib.gen_printFailureLog(e.getLocalizedMessage());
		genLib.gen_printFailureLog("Failed while fetching data from Excel Sheet... So try again");
		if (isFileClosed(SheetPath, SheetName) == true)
		{
			Connection connection = fillo.getConnection(SheetPath);
			connection.executeUpdate(strQuery);
			connection.close();
		}
		else
		{
			genLib.gen_printFailureLog("File is not Closed, So Waiting...");
		}
		}
		catch(Throwable e)
		{
			System.out.println(e.getLocalizedMessage());
			System.out.println("Failed to fetch data from Excel Sheet");
		}
	}
	
	private boolean isFileClosed(String File, String FilePath) throws InterruptedException {  
        boolean closed;
        Object channel = null;
		try {
            channel = new RandomAccessFile(File, FilePath).getChannel();
            closed = true;
        } catch(Exception ex) {
            closed = false;
        } finally {
            if(channel!=null) {
               channel.wait(3000);
                }
            }
		return closed;    
	}
        
	
	
	public void gen_Swipe(Double startx, Double starty, Double endx, Double endy, int duration)
	{
		try
		{
		Dimension dimensions = driver.manage().window().getSize();
		Double WidthStart = dimensions.getWidth() * startx;
		Double HeightStart = dimensions.getHeight() * starty;
		Double WidthEnd = dimensions.getWidth() * endx;
		Double HeightEnd = dimensions.getHeight() * endy;
		
		int StartX = WidthStart.intValue();
		int StartY = HeightStart.intValue();
		int EndX = WidthEnd.intValue();
		int EndY = HeightEnd.intValue();
		
		driver.swipe(StartX, StartY, EndX, EndY, duration);
		
		System.out.println("Swipe: Done");
		}
		catch(Throwable e)
		{
			System.out.println("Swipe: Failed");
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public void gen_printPassLog(String Text)
	{
		System.out.println(ANSI_GREEN+ANSI_BOLD+Text+ANSI_RESET);
	}
	
	public void gen_printFailureLog(String Text)
	{
		System.out.println(ANSI_RED+ANSI_BOLD+Text+ANSI_RESET);
	}
	public void gen_printInfoLog(String Text)
	{
		System.out.println(ANSI_BLACK+Text+ANSI_RESET);
	}
	
	
	public String gen_getScreenshot(String ScreenshotName) throws IOException, InterruptedException
	{
	 
	 	System.out.println("Value of Driver while taking Screenshot" +driver);
		System.out.println("Capturing the snapshot of the page ");
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		timeStamp = timeStamp.replace(".", "_");
		//String timeStamp = "";
		String fileName = ScreenshotName+"_"+ timeStamp + ".png";
		File srcFiler=((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String Path = System.getProperty("user.dir") +"\\Snapshots\\" +fileName;
		System.out.println(Path);
		FileUtils.copyFile(srcFiler, new File(Path));
		return fileName;
	}

	
	public void fg_restartApp() throws IOException, InterruptedException
	{
		System.out.println("Going to Restart App");
		try
		{
		System.out.println("Restarting App for new Script");
		String command = "adb shell am start -n com.nnacres.app/com.nnacres.app.activity.SplashActivity";
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.start();
		Thread.sleep(3000);
		System.out.println("App should be restarted");
		}
		catch(Throwable e)
		{
			System.out.println("Failed to Restart App Successfully");
		}
		
	}

	//*****Module: Activity Log*****        
	//*****Objective: Clear app data and Restart App***** 
	//*****Created By: Vandana Maurya*****
	//*****Created on: 17th-Nov-2015*****

	public void fg_clearAppData() throws IOException, InterruptedException
	{
		
		String appPackage = gen_getDataFromConfig("app-package");
		String pullCommand = "adb shell pm clear "+appPackage;
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", pullCommand);
		builder.start();
		//Vandana Thread.sleep(10000);
		fg_restartApp();
		//Vandana Thread.sleep(5000);
		try
		{
			TouchAction action = new TouchAction(driver);
			action.longPress(621, 133).release().perform();
		}
		catch (Throwable e)
		{
			System.out.println("No Coachmarks Available");
		}
	}

}
