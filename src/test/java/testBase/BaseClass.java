package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;


public class BaseClass {
	
	public Logger logger; //log4j
	public static WebDriver driver;
	public Properties p;
	
	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os", "browser"})
	public void setup(String os, String br) throws IOException {
		
		//Loading config file
		//Properties pr = new Properties();
		FileReader file = new FileReader("./src//test//resources//config.properties");
		p=new Properties();
		p.load(file);
	
		
		logger = org.apache.logging.log4j.LogManager.getLogger(this.getClass());
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote")) {
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			//capabilities.setPlatform(Platform.WIN11);
			//capabilities.setBrowserName("chrome");
			//os
			if(os.equalsIgnoreCase("windows")) {
				capabilities.setPlatform(Platform.WIN11);
			}else if(os.equalsIgnoreCase("mac")){
				capabilities.setPlatform(Platform.MAC);
			}else if(os.equalsIgnoreCase("linux")){
				capabilities.setPlatform(Platform.LINUX);
			}else {
				System.out.println("No macthing os");
				return;
			}
			//browser
			
			switch(br.toLowerCase()) {
			
			case "chrome":capabilities.setBrowserName("chrome");break;
			case "edge" : capabilities.setBrowserName("MicrosoftEdge");break;
			case "firefox" : capabilities.setBrowserName("firefox");break;
			default : System.out.println("No matching found"); return;
			}
			
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
		}
		
		if(p.getProperty("execution_env").equalsIgnoreCase("local")) {
			
			switch(br.toLowerCase()) {
			
			case "chrome" : driver = new ChromeDriver();break;
			
			case "edge" : driver = new EdgeDriver();break;
			
			default : System.out.println("Invalid browser name...."); return;
			}
		}
		
		
		//driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(p.getProperty("appURL")); //read url from properties file
		driver.manage().window().maximize();
		
	}
	
	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void teardown() {
		driver.quit();
	}

	public String randomString() {
		
		String generatedstring = RandomStringUtils.randomAlphabetic(5);
		return generatedstring;
	}
	
	public String randomNumber() {
		
		String generatednumber = RandomStringUtils.randomNumeric(10);
		return generatednumber;
	}
	
	public String randomAlphaNumeric() {
		
		String generatedstring = RandomStringUtils.randomAlphabetic(4);
		String generatednumber = RandomStringUtils.randomNumeric(4);
		return (generatedstring+"@"+generatednumber);
	}
	
	// Capture Screenshot Method
    public String captureScreen(String tname) throws IOException {
    	
    	String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        TakesScreenshot ts = (TakesScreenshot) driver;

        File sourceFile = ts.getScreenshotAs(OutputType.FILE);

        String targetFilePath = System.getProperty("user.dir") + "/Screenshots/" + tname + "_" + timeStamp + ".png";

        File targetFile = new File(targetFilePath);

        sourceFile.renameTo(targetFile);

        //System.out.println("Screenshot saved at: " + destination);

        return targetFilePath;
    }

}
