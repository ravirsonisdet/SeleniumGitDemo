package Tests;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTest {

	WebDriver driver;
	Properties props = new Properties();

	void setup(String browser) throws Exception, IOException, InterruptedException {

		// TODO Auto-generated method stub
		System.out.println("Execution started");

		if (browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"D:\\selenium webdriver\\CromeDriver\\chromedriver-win64\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
			System.setProperty("webdriver.geco.driver",
					"D:\\selenium webdriver\\FirefoxDriver\\geckodriver-v0.34.0-win-aarch64\\geckodriver.exe");
		} else if (browser.equalsIgnoreCase("Edge")) {
			driver = new EdgeDriver();
			System.setProperty("webdriver.edge.driver", "D:\\selenium webdriver\\edgedriver_win64\\msedgedriver.exe");
		} else {
			throw new Exception("Incorrect Browser");
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		driver.get("https://shop.boeing.com/aviation-supply");
		System.out.println("Title is : " + driver.getTitle());

		// wait
		// driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		// Click on login button text to open login page
		WebElement logintext = driver.findElement(By.id("loginButton"));

		WebDriverWait webdwait = new WebDriverWait(driver, Duration.ofSeconds(10));
		webdwait.until(ExpectedConditions.visibilityOf(logintext));
		logintext.click();

		// start fetching data from properties file
		FileReader reader = new FileReader(
				"C:\\Users\\Dell\\eclipse-workspace\\SeleniumSetup1\\AviallPortalLoginData.properties");
		props.load(reader);

		String emailaddress = (String) props.get("emailaddress");
		String passwd = (String) props.get("password");

		// enter login email id and password
		WebElement username = driver.findElement(By.id("j_username"));
		username.sendKeys(emailaddress);

		WebElement password = driver.findElement(By.id("loginpassword"));
		password.sendKeys(passwd);

		TakeSceeenshot.takeSnapShot(driver, "C://Users//Dell//Desktop//imagefromselenium//LoginPage.png");

		WebElement submitbtn = driver.findElement(By.id("submitbtn"));
		submitbtn.submit();

		// wait till login gets completed
		WebDriverWait webdwait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
		webdwait2.until(ExpectedConditions.visibilityOfElementLocated(By.className("headerprofile")));

		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.className("headerprofile")));
		action.build().perform();

		// Order history page open
		driver.findElement(By.linkText("Order History")).click();

		// click on PO field

		WebDriverWait webdwait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
		webdwait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("order.purchaseOrderNumber")));

		WebElement poNumberFiled = driver.findElement(By.id("order.purchaseOrderNumber"));
		poNumberFiled.sendKeys("P2400489");

		// Click on order search button
		WebElement orderSearchBtn = driver.findElement(By.id("accountOrderHistorySeach"));
		orderSearchBtn.click();

		Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(d -> driver.findElement(By.id("order_history")).isDisplayed());

		// to perform Scroll on application using Selenium
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,350)", "");

		TakeSceeenshot.takeSnapShot(driver, "C://Users//Dell//Desktop//imagefromselenium//POSearched.png");

		// click on a PO to open the PO in a new Tab
		WebElement clickOnPO = driver.findElement(By.xpath("//*[@id=\"order_history\"]/tbody/tr/td[2]/div/p"));
		clickOnPO.click();

		// wait
		System.out.println("wait for 30 sec");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		Set<String> handles = driver.getWindowHandles();
		Iterator<String> it = handles.iterator();
		String parentTab = it.next();
		String childTab = it.next();

		System.out.println("parent tab is : " + parentTab + "Child tab is : " + childTab);

		// switch to the second tab
		driver.switchTo().window(childTab);

		TakeSceeenshot.takeSnapShot(driver, "C://Users//Dell//Desktop//imagefromselenium//POOpenedInNewTAB.png");

	}

	void tearDown() {
		System.out.println("Execution endeded");
		driver.quit();
		System.out.println("Teardown method ended");
	}

	public static void main(String[] args) throws Exception {

		SeleniumTest seleniumTest = new SeleniumTest();
		seleniumTest.setup("CHROME");
		seleniumTest.tearDown();
		System.out.println("Program completed....");

	}

}