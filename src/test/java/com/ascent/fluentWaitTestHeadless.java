package com.ascent;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.apache.logging.log4j.*;
import com.google.common.base.Function;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import util.RepositoryParser;

public class fluentWaitTestHeadless {
	public static Logger log=LogManager.getLogger(fluentWaitTestHeadless.class.getName());
	@SuppressWarnings("deprecation")
	public static void main(String a[]) throws IOException, InterruptedException
	{
		final RepositoryParser parser;
		WebDriver driver = null;
		
		
		
		int flag=0;
		boolean present=false;
		parser = new RepositoryParser("ObjectRepository.properties"); 
		FileReader reader=new FileReader("./src/test/java/util/TestData.properties");
        Properties props=new Properties();
        props.load(reader);
        switch(props.getProperty("browser"))
        {
        case "firefox":
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options=new FirefoxOptions();
        options.addArguments("--headless");
		driver= new FirefoxDriver(options);
		break;
        
        case "chrome":
            //WebDriverManager.chromedriver().setup();
            ChromeOptions options1 = new ChromeOptions();
            options1.addArguments("--headless");
    		driver= new ChromeDriver(options1);
    		break;
    	           	   		
        }
		driver.manage().window().maximize();
        driver.get("https://apt12.activeaero.com/login/LoginForm.cfm");
        Thread.sleep(2000);
        driver.findElement(parser.getbjectLocator("Login_usr__id")).sendKeys(props.getProperty("user_name"));
        driver.findElement(parser.getbjectLocator("Login_pwd_id")).sendKeys(props.getProperty("password"));
        driver.findElement(parser.getbjectLocator("Login_loginbtn_xpath")).click();
        log.info("Logging in....");
        
        Thread.sleep(2000);
        try {
        	driver.findElement(parser.getbjectLocator("Home_profile_threedots_xpath"));
           present = true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
           present = false;
        }
        
        if(!present)
        {
        	log.info("Already Logged In ... ");
        	driver.findElement(parser.getbjectLocator("Login_loginbtn_xpath")).click();
        }
        System.out.println("Sucessfully Logged in");
        System.out.println("Searching for the pop up");
        
		/*
		 * Thread.sleep(2000); try { present
		 * =driver.findElement(parser.getbjectLocator("Login_loginbtn_xpath")).isEnabled
		 * ();
		 * 
		 * } catch (NoSuchElementException e) { log.info("Logged in ..."+e);
		 * 
		 * }
		 * 
		 * if(present) { log.info("Already Logged In ... ");
		 * System.out.println("Already Logged In ... ");
		 * driver.findElement(parser.getbjectLocator("Login_loginbtn_xpath")).click(); }
		 */
        
        
        log.info("Searching for the pop up");
        //changes1
        Thread.sleep(5000);
		driver.switchTo().frame(parser.getbjectProp("Home_popup_frame_id"));
        
        List<WebElement> rows=driver.findElements(parser.getbjectLocator("Home_popup_table_tr_xpath"));
        System.out.println(rows.size());
        if(rows.size()>0)
        {
        	log.info("pop up message/messages present");
        	System.out.println("pop up message/messages present");
        }
        else
        {
        	log.info("No Popup Messages Available");
        	System.out.println("No Popup Messages Available ");
        }
        driver.switchTo().defaultContent();
        driver.switchTo().frame(parser.getbjectProp("Home_popup_frame_id"));
        for(int i=1;i<=rows.size();i++)
        {
        	
        	driver.findElement(By.xpath("//tr[1]/td/button")).click();
        	log.info("Record "+i+" acknowledged..");
        	Thread.sleep(5000);
        	flag=1;
        }
        
        if(flag==1)
        	log.info("All the messages acknowledged");
        	System.out.println("All the messages Acknowledged");
        driver.switchTo().defaultContent();
        Thread.sleep(10000);
		  //code for home page table decline
		  
        
			
		  try { 
				  //driver.switchTo().frame(parser.getbjectProp("Home_popup_frame_id"));
			  Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			  .withTimeout(Duration.ofHours(Integer.parseInt(props.getProperty("timeout_in_hr")))) 
			  .pollingEvery(Duration.ofMinutes(Integer.parseInt(props.getProperty("pollingtimeout_in_min"))))
			  .ignoring(NoSuchElementException.class,NoSuchWindowException.class)
			  .ignoring(IndexOutOfBoundsException.class);
			  
			  
			  WebElement foo = wait.until(new Function<WebDriver, WebElement>() { 
				  public WebElement apply(WebDriver driver) {
			  System.out.println("Waiting for Shipment Record");
			  log.info("Polling every 5 minutes to check if the shipment record present"); 
			  driver.switchTo().defaultContent();
			  List<WebElement> shipment_rows=driver.findElements(parser.getbjectLocator("Home_popup_table_tr_xpath"));
			  //List<WebElement> rows=driver.findElements(parser.getbjectLocator("Home_popup_table_tr_xpath"))
			  return shipment_rows.get(0); } }); 
			  } 
		  
		  catch(Exception e) { System.out.println(e);
		  log.info("While waiting for the shipment records to appear, the exception occurred" +e); 
		  }
		 
		  
		  List<WebElement> rows_in_homepage=driver.findElements(parser.getbjectLocator("Home_popup_table_tr_xpath")); 
		  System.out.println(rows_in_homepage.size());
		  if(rows_in_homepage.size()>0)
		  {
			  log.info("Shipment record available. Declining all the requests");
			  System.out.println("Shipment record available. Declining all the requests");
		  }
		  else
		  {
			  log.info("No Shipment records available");
			  System.out.println("No Shipment records available");
		  }
			  
		  for(int i=1;i<=rows_in_homepage.size();i++) {
		  driver.findElement(By.xpath("//tr["+i+"]/td[5]/button")).click();
		  driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS); }
		 
		  Thread.sleep(3000);
		  
		  System.out.println("Logging out......");
		  log.info("Logging out......");
		  driver.findElement(parser.getbjectLocator("Home_profile_threedots_xpath")).click();
		  driver.findElement(parser.getbjectLocator("Home_profile_logout_xpath")).click();
        
        
		  System.out.println("Closing Browser...");
        //Closing the browser
        driver.quit();
        
		  
		
		
	}
	

}
