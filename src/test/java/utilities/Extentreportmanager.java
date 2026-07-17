package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class Extentreportmanager implements ITestListener{
	
	public ExtentSparkReporter sparkReporter; //UI of report
	public ExtentReports extent; //populate common info on the report
	public ExtentTest test; //creating test cases entries in the report and update status of the test methods
	
	String repName;
	
	public void onStart(ITestContext context) {
		
		/*SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt = new Date();
		String currentdatetimestamp=df.format(dt);*/
	    
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName = "Test-Report-"+ timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\" +repName);
		
		sparkReporter.config().setDocumentTitle("Opencart Automation Report");
		sparkReporter.config().setReportName("Opencart Functional Testing");
		sparkReporter.config().setTheme(Theme.DARK);
		
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");
		
		
		String os = context.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = context.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includedGroups = context.getCurrentXmlTest().getIncludedGroups();
		if(!includedGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
		
	  }
	
	public void onTestSuccess(ITestResult result) {
	    test = extent.createTest(result.getTestClass().getName()); //create a new entry in the report
	    test.assignCategory(result.getMethod().getGroups());
	    test.log(Status.PASS, result.getName()+ " got successfully executed"); //update status p/f/s
	  }
	
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); //create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());
		
	    test.log(Status.FAIL, result.getName() + " got failed "); //update status p/f/s
	    test.log(Status.INFO, result.getThrowable().getMessage());
	    
	    try {
	    	String imgpath = new BaseClass().captureScreen(result.getName());
	    	test.addScreenCaptureFromPath(imgpath);
	    }catch(Exception e1) {
	    	e1.printStackTrace();
	    }
	     
	  }
	
	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); //create a new entry in the report
		test.assignCategory(result.getMethod().getGroups());
	    test.log(Status.SKIP, result.getName()+ " got skipped "); //update status p/f/s
	    test.log(Status.INFO	, result.getThrowable().getMessage());
	  }
	
	public void onFinish(ITestContext context) {
	    extent.flush();
	    
	    String pathOfExtentReport = System.getProperty("user.dir")+"\\reports"+repName;
	    File extentReport = new File(pathOfExtentReport);
	    
	    try {
	    	Desktop.getDesktop().browse(extentReport.toURI());
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	  }
	
}
