package com.online.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverScript {
	protected static WebDriver driver;
	public static Properties Config = null;
	public static FileInputStream fis;
	public static File directory = new File(".");
	public static String os;
	public static String browser;

	public DriverScript() {
		try {
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() throws Exception {
		if (Config == null) {

			Config = new Properties();

			try {
				String fisFilePath = System.getProperty("user.dir")
						+ "src//test//java//com//online//resources//config.properties";
				fis = new FileInputStream(fisFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				Config.load(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (driver == null)
			
			createNewDriverInstance();
	}

	private void createNewDriverInstance() throws Exception {
		
		/*
		 *Feature was being used to parameterize the data using Constant.java file.
		 * Now we are using properties file to store the data, so deprecated variable uses.
		browser = Constants.Browser;
		os = Constants.OS;
		*/

		browser = Config.getProperty("Browser");
		os = Config.getProperty("OS");
		//System.out.println("initialize Browser: " + browser);
		//System.out.println("initialize OS: " + os);

		switch (browser) {
		case "chrome":
			String importDir = System.getProperty("user.dir");
			String downloadFilepath = importDir + File.separator + "src" + File.separator + "test" + File.separator + "Imports" + File.separator + "Downloads" ;
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", downloadFilepath);
			String chromDrvrPath;
			chromDrvrPath = directory.getCanonicalPath() + File.separator + "lib" + File.separator;
			os: switch (os) {
			case "linux32":
			case "linux64":
			case "mac":				
				System.setProperty("webdriver.chrome.driver",
						chromDrvrPath + "chromedriver_" + os + File.separator + "chromedriver");
				break os;
			case "win":	
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

	public WebDriver getDriver() {
		return driver;
	}

	public void quitDriver() {
		driver.manage().deleteAllCookies();
		driver.quit();
		driver = null;
	}
}
