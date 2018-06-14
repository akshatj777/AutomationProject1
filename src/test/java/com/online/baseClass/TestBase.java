package com.online.baseClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;

public abstract class TestBase {
	
	public static WebDriver driver = null;
	protected static long Wait_Time = 1000L;
	protected static long delay_Time = 2000L;
	protected static long LongDelay_Time = 5000L;
	public static Properties Cache=new Properties();
	public static Properties properties=new Properties();
	static InputStream inPropFile = null;
	FileInputStream fisCache;
	public static Logger log = null;
	OutputStream outPropFile;
	Actions actionEvent;
//	public static Properties config=null;
	public static Properties data=null;
	
	public static Properties Config = null;
	public static FileInputStream fis;
	public static File directory = new File(".");
	public static String os;
	private static boolean isInitalized=false;
	public static String browser;
	
	public static WebDriverWait wait = null;
			
	protected TestBase() {
		if(!isInitalized){
			initLogs();
			initConfig();
			try{
			initDriver();}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private static void initLogs(){
		if (log == null){
			// Initialize Log4j logs

			DOMConfigurator.configure(System.getProperty("user.dir")+File.separator+"config"+File.separator+"log4j.xml");
			log = Logger.getLogger("MyLogger");
			log.info("Logger is initialized..");
		}
	}
	
	private static void initConfig(){
		
		if (Config == null) {
			try{
			Config = new Properties();
			String config_fileName = "config.properties";
			String config_path = System.getProperty("user.dir") + File.separator+ "config" + File.separator + config_fileName;
			FileInputStream config_ip = new FileInputStream(config_path);
			Config.load(config_ip);
		
			//initialize data properties file
			data = new Properties();
			String data_fileName = "data.properties";
			String data_path = System.getProperty("user.dir") + File.separator+ "config" + File.separator + data_fileName;
			FileInputStream data_ip = new FileInputStream(data_path);
			data.load(data_ip);
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}}
	}

	public void initDriver() throws Exception {
		if (driver == null){
			
			createNewDriverInstance();}
	}
	
	public void createNewDriverInstance() throws IOException{
		browser = Config.getProperty("Browser");
		os = Config.getProperty("OS");
		log.info("initialize Browser: "+Config.getProperty("Browser"));
		log.info("initialize OS: "+Config.getProperty("OS"));
		
		switch (browser) {
		case "chrome":
			String importDir = System.getProperty("user.dir");
			String downloadFilepath = importDir + File.separator + "src" + File.separator + "test" + File.separator + "Imports" + File.separator + "Downloads" ;
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", downloadFilepath);
			String chromDrvrPath;
			chromDrvrPath = directory.getCanonicalPath() + File.separator + "lib" + File.separator;
			System.out.println("Chromedriver path"+chromDrvrPath);
			os: switch (os) {
			case "linux32":
			case "linux64":
			case "mac":				
				System.setProperty("webdriver.chrome.driver",
						chromDrvrPath + "chromedriver_" + os + File.separator + "chromedriver");
				break os;
			case "win":	
				String value=chromDrvrPath + "chromedriver_" + os + File.separator + "chromedriver.exe";
				System.out.println("Vale"+value);
				System.setProperty("webdriver.chrome.driver",
						chromDrvrPath + "chromedriver_" + os + File.separator + "chromedriver.exe");
				break os;
			default:
				throw new IllegalStateException("Invalid OS paramter, expected values 'linux32||linux64||mac||win'");				
			}
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");            
			options.addArguments("--disable-extensions");   
			options.addArguments("--disable-browser-side-navigation"); 
			options.setExperimentalOption("prefs", chromePrefs);           
			DesiredCapabilities cap = DesiredCapabilities.chrome();            
			cap.setCapability(ChromeOptions.CAPABILITY, options);          
			driver = new ChromeDriver(cap);
			System.out.println("The Value is"+driver);
			log.info(Config.getProperty("browser")+" driver is initialized..");
			String waitTime = "30";
			driver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			driver.manage().window().setPosition(new Point(0, 0));
			driver.manage().window().maximize();

			//Explicit Wait + Expected Conditions
			wait=new WebDriverWait(driver, 120);
            break;			
		case "ie":
			String IEDrvrPath;
			IEDrvrPath = directory.getCanonicalPath() + File.separator + "lib" + File.separator;
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability("nativeEvents", false);
			caps.setCapability("nativeEvents", true);
			caps.setCapability("ignoreZoomSetting", true);
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			System.setProperty("webdriver.ie.driver",
					IEDrvrPath + "IEDriverServer_Win32" + File.separator + "IEDriverServer.exe");
			driver = new InternetExplorerDriver(caps);

			break;
		case "phantomJS":
			String phantomJSDrvrPath;
			phantomJSDrvrPath = directory.getCanonicalPath() + File.separator + "lib" + File.separator;
			DesiredCapabilities dCaps = new DesiredCapabilities();
			dCaps.setJavascriptEnabled(true);
			dCaps.setCapability("takesScreenshot", true);

			os: switch (os) {
			case "linux32":
			case "linux64":
			case "mac":				
				dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
						phantomJSDrvrPath + "phantomjsdriver_" + os + File.separator + "phantomjs");

				break os;
			case "win":				
				dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
						phantomJSDrvrPath + "phantomjsdriver_" + os + File.separator + "phantomjs.exe");

				break os;
			default:
				
			}
			driver = new PhantomJSDriver(dCaps);
			
			break;			

		default:
			String geckoDrvrPath;
			geckoDrvrPath = directory.getCanonicalPath() + File.separator + "lib" + File.separator;
			String importDir1= System.getProperty("user.dir");
			String downloadFilepath1 = importDir1 + File.separator + "src" + File.separator + "test" + File.separator + "Imports" + File.separator + "Downloads" ;
			FirefoxProfile profile = new FirefoxProfile();
		    profile.setPreference("browser.download.folderList",2); 
			profile.setPreference("browser.download.dir", downloadFilepath1);
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf"); 
			profile.setPreference("pdfjs.disabled", true);  
			FirefoxOptions options1 = new FirefoxOptions();
			options1.setProfile(profile);
			os: switch (os) {
			case "linux32":
			case "linux64":
			case "mac":    
			System.setProperty("webdriver.gecko.driver",
			geckoDrvrPath + "geckodriver_" + os + File.separator + "geckodriver");
			break os;
			case "win":    
			System.setProperty("webdriver.gecko.driver",
			geckoDrvrPath + "geckodriver_" + os + File.separator + "geckodriver.exe");
			break os;
			default:
			throw new IllegalStateException("Invalid OS paramter, expected values 'linux32||linux64||mac||win'");    
			   }
			
			driver = new FirefoxDriver(options1);
			}
     
	}
	

	@AfterSuite
	public void tearDown() {
		quitDriver();
	}
	
	protected void assertStrings(String actual, String expected){
        	Assert.assertEquals(actual, expected);
			log.info("Actual string: [ "+actual+" ] does match with Expected string: [ "+expected+" ]");		

	} 

	/**
	 * Quit Driver.
	 */
	public static void quitDriver() {

		driver.quit();
		driver = null;
		log.info("Closing Browser.");

	}
	
	

}
