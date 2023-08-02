

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;

public class RepositoryParser {

	private FileInputStream stream;
	private String RepositoryFile;
	private Properties propertyFile = new Properties();

	public RepositoryParser(String fileName) throws IOException
	{
		this.RepositoryFile = fileName;
		stream = new FileInputStream(RepositoryFile);
		propertyFile.load(stream);
	}

	public By getbjectLocator(String locatorName)
	{
		String locatorType=locatorName.substring(locatorName.lastIndexOf("_")+1);
		String locatorValue=propertyFile.getProperty(locatorName);
		

		By locator = null;
		switch(locatorType)
		{
		case "Id":
		case "id":
			locator = By.id(locatorValue);
			break;
		case "Name":
		case "name":
			locator = By.name(locatorValue);
			break;
		case "CssSelector":
		case "csscelector":
			locator = By.cssSelector(locatorValue);
			break;
		case "LinkText":
		case "linktext":
			locator = By.linkText(locatorValue);
			break;
		case "PartialLinkText":
		case "partiallinktext":
			locator = By.partialLinkText(locatorValue);
			break;
		case "tagname":
		case "TagName":
			
			locator = By.tagName(locatorValue);
			break;
		case "Xpath":
		case "xpath":
			locator = By.xpath(locatorValue);
			break;
		}
		//System.out.println(locator);
		return locator;
	}
	public String getbjectProp(String locatorName)
	{
		String locatorValue=propertyFile.getProperty(locatorName);
		return locatorValue;
		
	}
	  public static void main(String a[]) throws IOException  
	{ 
	 RepositoryParser parser; 
	 parser = new RepositoryParser("ObjectRepository.properties"); 
	 System.out.println(parser.getbjectLocator("Home_profile_logout_xpath")); 
	}
	 }