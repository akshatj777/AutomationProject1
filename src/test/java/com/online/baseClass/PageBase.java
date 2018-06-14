package com.online.baseClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class PageBase{

	
    public Logger log = TestBase.log;
    protected static long Wait_Time = 1000L;
	protected static long delay_Time = 2000L;
	protected static long LongDelay_Time = 5000L;
	public static Properties Cache=new Properties();
	public static Properties properties=new Properties();
	static InputStream inPropFile = null;
	FileInputStream fisCache;
    OutputStream outPropFile;
	Actions actionEvent;
	protected WebDriver driver = null;
	
    public PageBase(WebDriver driver) {
    	this.driver=driver;
    	PageFactory.initElements(driver, this);
	}
    
    public WebDriverWait waitTo()
	{
		WebDriverWait wait = new WebDriverWait(driver, 200);
		return wait;
	}

	public void delay() {
		try {
			Thread.sleep(delay_Time);
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}

	public void longDelay() {
		try {
			Thread.sleep(LongDelay_Time);
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}

	public boolean isElementVisible(final WebElement ele) {
		long start = System.currentTimeMillis();
		while (true) {
			if (ele.isDisplayed()) {
				return true;
			} else {
				if (System.currentTimeMillis() - start >= Wait_Time) {
					throw new Error("Timeout reached element not visiblie");
				} else {
					try {
						synchronized (this) {
							wait(200);
						}
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}}
	

	public List<String> getTextForElementfromList(String element) {
		List<WebElement> listItems = driver.findElements(By.cssSelector(element));
		List<String> listtexts = new ArrayList<String>();
		for (WebElement item : listItems) {

			item.getText();
			listtexts.add(item.getText());
		}
		return listtexts;
	}

	public List<WebElement> getElementsList(String element) {
		List<WebElement> listItems = driver.findElements(By.cssSelector(element));
		return listItems;
	}

	public WebElement waitFindElement(WebElement parentElement, By by) {
		WebElement ele = null;
		if (parentElement == null) {
			ele = driver.findElement(by);
		} else {
			ele = parentElement.findElement(by);
		}
		isElementVisible(ele);
		return ele;
	}

	public List<WebElement> waitFindElements(WebElement parentElement, By by) {
		if (parentElement == null) {
			return driver.findElements(by);
		}
		return parentElement.findElements(by);
	}

	public WebElement findVisibleElement(WebDriver driver, By by) {
		WebElement ele = driver.findElement(by);
		isElementVisible(ele);
		return ele;
	}

	public WebElement findVisibleElement(WebElement wEle, By by) {
		WebElement ele = wEle.findElement(by);
		isElementVisible(ele);
		return ele;
	}

	public List<WebElement> waitFindElements(WebDriver driver, By by) {
		return driver.findElements(by);
	}

	public boolean isElementEnabled(WebElement ele) {
		boolean clickable = true;
		String attr = ele.getAttribute("class");
		if ((attr != null) && (attr.indexOf("disabled") > -1)) {
			clickable = false;
		}
		return clickable;
	}

	public WebElement getVisibleDropDownParentElement(String parent) {
		List<WebElement> comboListParentElements = waitFindElements(driver, By.cssSelector(parent));
		for (WebElement el : comboListParentElements) {
			if (el.isDisplayed()) {
				return el;
			}
		}
		return null;
	}

	public void selectElementByIndex(String element, int idx) {
		List<WebElement> listItems = driver.findElements(By.cssSelector(element));
		listItems.get(idx).click();
	}

	public void selectElementByDesc(String element, String desc) {
		List<WebElement> listItems = driver.findElements(By.cssSelector(element));
		for (WebElement item : listItems) {

			if (item.getText().equalsIgnoreCase(desc)) {
				item.click();
				delay();
				return;
			}
		}
	}

	public void verifyTextForElementfromList(String element, String itemtext) {
		List<WebElement> listItems = driver.findElements(By.cssSelector(element));
		for (WebElement item : listItems) {
			item.getText().equalsIgnoreCase(itemtext);
		}
	}

	public void selectDropdownVisibleElement(String selectElement, String desc) {
		Select select = new Select(driver.findElement(By.cssSelector(selectElement)));
		select.selectByVisibleText(desc);
	}

	public void iFillInText(WebElement obj, String text) {
		if (!isElementVisible(obj)) {
			return;
		}
		obj.clear();
		if ("".equals(text)) {
			return;
		}
		obj.sendKeys(text);
	}

	public void clickElement(WebElement element) {
		if (isElementVisible(element)) {
			element.click();
		}
		/*
		 * WebDriverWait wait = new WebDriverWait(driver,10);
		 * wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		 * driver.findElement(by).click();
		 */
	}

	public void iWillWaitToSeeElement(WebElement element, String text) {
		if (isElementVisible(element)) {
			Assert.assertTrue(element.isDisplayed());
		}
	}

	public void verifyTextForElement(WebElement ele, String text) {
		if (isElementVisible(ele)) {
			Assert.assertEquals(ele.getText(), text);
		}
	}

	public String getTextForElement(WebElement ele) {
		if (isElementVisible(ele)) {
			ele.getText();
		}
		return ele.getText();
    }

    public void verifyElementCount(String element, int count) {
        List<WebElement> listItems = driver.findElements(By.cssSelector(element));
        int countelement = listItems.size();
        delay();
        Assert.assertEquals(countelement, count);
    }

    public int getElementCount(String element) {
        List<WebElement> listItems = driver.findElements(By.cssSelector(element));
        int countelement = listItems.size();
        return countelement;
    }

    public void swithToFrame(WebElement element) {
        driver.switchTo().frame(element);
    }

    public void switchBacktoOldWindow() {
    	try
    	{
    		String parentWindow = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            driver.close();
            for (String windowHandle : handles) {
                if (!windowHandle.equals(parentWindow)) {
                    driver.switchTo().window(windowHandle);
                }
            }
            delay();
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	
    }

    public void verifyTextNotPresentForElementFromList(String element, String itemtext) {
        List<WebElement> listItems = driver.findElements(By.cssSelector(element));
        for (WebElement item : listItems) {
            Assert.assertFalse(item.getText().equalsIgnoreCase(itemtext));
        }
    }
        
	public void verifyTextForElementWithMultipleSpaces(WebElement ele, String text) {
		if (isElementVisible(ele)) {
			Assert.assertEquals(ele.getText().replaceAll("\\s+", " "), text);
		}
	}

	public void elementInformation(WebElement ele) {
		System.out.println("  ");
		System.out.println("************************************");
		System.out.println("tagname " + ele.getTagName());
		System.out.println("text " + ele.getText());
		System.out.println("isdisplayed " + ele.isDisplayed());
		System.out.println("isenabled " + ele.isEnabled());
		System.out.println("value " + ele.getAttribute("value"));
		System.out.println("color " + ele.getAttribute("color"));
		System.out.println("text-align" + ele.getAttribute("text-align"));
		System.out.println("class" + ele.getAttribute("class"));
		System.out.println("class" + ele.getAttribute("style"));
		System.out.println("************************************");
	}

	public void switchToNewWindow() {
		try
		{
			String parentWindow = driver.getWindowHandle();
			Set<String> handles = driver.getWindowHandles();
			for (String windowHandle : handles) {
				if (!windowHandle.equals(parentWindow)) {
					driver.switchTo().window(windowHandle);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
	}

	public void verifyTextForElementFromListByXpath(String element, String itemtext) {
		List<WebElement> listItems = driver.findElements(By.xpath(element));
		for (WebElement item : listItems) {
			item.getText().equalsIgnoreCase(itemtext);
			/*
			 * if (item.getText().equalsIgnoreCase(itemtext)) { try {
			 * Assert.assertTrue(item.getText().equalsIgnoreCase(itemtext)); }
			 * catch (Exception e) {
			 * 
			 * } }
			 */
		}
	}

	public void selectElementByTextDescByXpath(String element, String desc) {
		List<WebElement> listItems = driver.findElements(By.xpath(element));
		for (WebElement item : listItems) {
			if (item.getText().equalsIgnoreCase(desc)) {
				item.click();
				delay();
				return;
			}
		}
	}

	public void verifyAttributeForElementFromListByXpath(String element, String attribute, String itemtext) {
		List<WebElement> listItems = driver.findElements(By.xpath(element));
		for (WebElement item : listItems) {
			item.getAttribute(attribute).equalsIgnoreCase(itemtext);
		}
	}

	public void moveToTheElement(WebElement toElement) {
		actionEvent.moveToElement(toElement).perform();
	}

	public void moveToTheElementAndClick(WebElement moveToElement, WebElement clickToElement) {
		actionEvent.moveToElement(moveToElement).perform();
		clickToElement.click();
	}

	public void moveToTheElementAndRightClick(WebElement moveToElementToRightClick) {
		actionEvent.contextClick(moveToElementToRightClick).build().perform();
	}

	public void clickAllElementofAlistbyXpath(String xpathElement) {
		List<WebElement> listItems = driver.findElements(By.xpath(xpathElement));
		for (WebElement item : listItems) {
			item.click();
		}
	}

	public void verifyElementAttributeContainsValue(WebElement element, String attribute, String contains) {
		String attr = element.getAttribute(attribute);
		Assert.assertTrue(attr.contains(contains));
	}

	public boolean isElementPresentOnPage(By locatorKey) {
		boolean value = true;
		try {
			driver.findElement(locatorKey);

		} catch (org.openqa.selenium.NoSuchElementException e) {
			value = false;
		}
		return value;
	}

	public boolean isElementNotPresentOnPage(String ele) {
		boolean value = false;
		try {
			driver.findElement(By.cssSelector(ele));
		} catch (Exception e) {
			value = true;
		}
		return value;
	}
	
	public boolean isElementNotPresentOnPage(By locator) {
		try {
			driver.findElement(locator);
	        return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void verifyarraylist(List<String> requiredcombolisttext, List<String> actualcombolisttext) {
		Assert.assertEquals(requiredcombolisttext, actualcombolisttext);
	}

	public boolean existsElement(String element) {
		try {
			driver.findElement(By.cssSelector(element));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public void switchToFrameByNameOrId(String nameOrId) {
		driver.switchTo().frame(nameOrId);
	}

	public void switchToParentFrame() {
		driver.switchTo().parentFrame();
	}

//	public void iWillWaitToSee(By locator) {
//		try {
//		 new WebDriverWait(driver,200).until(ExpectedConditions.visibilityOfElementLocated(locator));
//		} catch (NoSuchElementException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void iVerifyTextFromListOfElement(By locator, String text) {
		List<WebElement> listItems = driver.findElements(locator);
		String value = null;
		for (WebElement item : listItems) {
			System.out.println(item.getText());
			  if (item.getText().trim().equals(text)) {
				  value=item.getText().trim();  
			  } 
		}
		Assert.assertEquals(text.trim(),value.trim());
	}	
	
	public void clickSingleElementFromList(By locator, String text) {
	    List <WebElement> element = driver.findElements(locator);
	    for(WebElement ele: element) {
	    	if (ele.getText().trim().equals(text)) {
	    		ele.click();
	    		break;
	    	}
	    }
	}  
	
	public boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (org.openqa.selenium.NoSuchElementException e) {
	      return false;
	    }
	}

	public void VerifyElementCssProperty(By by,String property){
		WebElement ele = driver.findElement(by);
    	String allignment=ele.getCssValue(property);
    	Assert.assertEquals("center", allignment);
	}
	
	public String createRandomName(String name){
		return name+RandomStringUtils.randomAlphabetic(8);
	}
	
	public String createRandomNumber(int num){
		return RandomStringUtils.randomNumeric(num);
	}
	
	
//	public String fetchParticipantID(String query) throws ClassNotFoundException, SQLException  {
//		HashMap<String, HashMap<String, String>> row = new HashMap<String,HashMap<String,String>>();
//	    Class.forName("com.mysql.jdbc.Driver");
//	    String connectionString = "jdbc:mysql://"+Config.getProperty("MySQLServerName")+":3306"; 
//	    Connection con=DriverManager.getConnection(Config.getProperty("MySQLDBUserName"),driver); 
//	    Statement stmt=con.createStatement();  
//	    ResultSet rs=stmt.executeQuery(query);
//	    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
//	    while(rs.next())
//	    {
//	     HashMap<String, String> column = new HashMap<String, String>();
//	        for(int i=1;i<=rsmd.getColumnCount();i++)
//	        {
//	        column.put(rsmd.getColumnName(i),rs.getString(i));
//	        }
//	        String a = Integer.toString(rs.getRow());
//	        row.put(a, column);
//	        }
//	    String pID = row.get("1").get("id");
//	    con.close();
//	    return pID;
//}
    public void validateDateFormat(String format,String dateToValdate) throws ParseException {
    	SimpleDateFormat formatter = new SimpleDateFormat(format);
	    formatter.setLenient(false);
	    formatter.parse(dateToValdate);
    }	
	public void scrollIntoViewByJS(WebElement element){
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	public String getTheCurrentUrl(){
		return driver.getCurrentUrl();
	}
	
	public static String currentdate(int days,String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDate newYork = LocalDate.now(ZoneId.of("America/New_York"));
		LocalDate b = newYork.minus(Period.ofDays(days));
		String date = dtf.format(b);
		return date;
	}
	
	public static String currentdatefrommonth(int months,String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDate newYork = LocalDate.now(ZoneId.of("America/New_York"));
		System.out.println(newYork);
		LocalDate b = newYork.minus(Period.ofMonths(months));
		String date = dtf.format(b);
		return date;
	}
	
	public void setAttributevalue(WebElement element, String attName, String attValue) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", 
                element, attName, attValue);
    }
	
	public void clickAction(WebElement element){
		Actions action=new Actions(driver);
		action.moveToElement(element).click().build().perform();;
   }
	
	 public void verifyDownloadedFile(String downloadFilepath,String fileName,String ext) {
			try {
				File dir = new File(downloadFilepath);

				File[] dir_contents = dir.listFiles();
				for (int i = 0; i < dir_contents.length; i++) {
					if (dir_contents[i].isFile()) {
						if (dir_contents[i].getName().equals(fileName)) {
							Assert.assertEquals(fileName, dir_contents[i].getName());
							System.out.println(dir_contents[i].getName());
							Assert.assertTrue(dir_contents[i].getName().contains(ext));
							} else {
							System.out.println("No Hurray");
						}
					}
					
				}
				for (int i = 0; i < dir_contents.length; i++) {
					dir_contents[i].delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	
	 public void iVerifyTextNotPresentFromListOfElement(By locator, String text) {
			List<WebElement> listItems = driver.findElements(locator);
			boolean bol = false;
			for (WebElement item : listItems) {
				  if (item.getText().trim().equals(text)) {
					  bol = true; 
				  } 
			}
			Assert.assertFalse(bol);
		}	
	 
	public static String capitalise(final String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	
	
	public String getPropertyValue(String PropertyKey,String file){
		Properties props=null;
		FileInputStream fin =null;
		String PropertyValue = null;

		try {
			File f = new File(System.getProperty("user.dir")+File.separator+"config"+File.separator+file);
			fin = new FileInputStream(f);
			props = new Properties();
			props.load(fin);
			PropertyValue = props.getProperty(PropertyKey);
		} catch(Exception e){
			System.out.println(e.getMessage());
		} 

		return PropertyValue;
	}
    
    
  }
	

