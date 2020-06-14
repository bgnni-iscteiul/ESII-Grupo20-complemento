import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Monitoring {
	// WebDriver instance
	public static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		location of drivers
		System.setProperty("webdriver.gecko.driver",
				System.getProperty("user.dir") + "\\src/main/resources\\Drivers\\geckodriver.exe");
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\src/main/resources\\Drivers\\chromedriver.exe");
//		creating driver instance
		try {
			driver = new FirefoxDriver();
		} catch (Exception e) {
			driver = new ChromeDriver();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// close the tab it has opened
		driver.close();
		// close the browser
		// driver.quit();
	}

	@Test
	public void test() throws InterruptedException, IOException {
		String urlBeginning = "192.168.99.100:8000";
		// You might have to change urlBeginning to localhost:8000

//		Availabilities

//		LogIn
		driver.get(urlBeginning + "wp-login.php");
		Thread.sleep(3000);
		boolean loginOK = false;
		boolean loggedinOK = false;
		WebElement userlogin = driver.findElement(By.id("user_login"));
		WebElement userpass = driver.findElement(By.id("user_pass"));
		if (userlogin != null && userpass != null) {
			loginOK = true;
		}
		userlogin.sendKeys("Administrator");
		userpass.sendKeys("Administrator");
		userpass.submit();
		loggedinOK = true;

		driver.get(urlBeginning);
		Thread.sleep(3000);

//		Repository
		boolean repositoryOK = false;
		WebElement repository = driver.findElement(By.name("Covid Scientific Discoveries Repository"));
		if (repository != null) {
			repositoryOK = true;
		}

//		WebPages
		boolean pagesOK = false;
		WebElement page1 = driver.findElement(By.name("Covid Scientific Discoveries"));
		WebElement page2 = driver.findElement(By.name("Covid Spread"));
		WebElement page3 = driver.findElement(By.name("Covid Queries"));
		WebElement page4 = driver.findElement(By.name("Covid Evolution"));
		WebElement page5 = driver.findElement(By.name("Covid Wiki"));
		if (page1 != null && page2 != null && page3 != null && page4 != null && page5 != null) {
			pagesOK = true;
		}

//		Email addresses
		boolean contactOK = false;
		String adminEmail = null;
		driver.get(urlBeginning);
		WebElement contact = driver.findElement(By.name("contact us"));
		if (contact != null) {
			contactOK = true;
			contact.click();
			Thread.sleep(3000);
			WebElement email = driver.findElement(By.linkText("iscte-iul.pt"));
			adminEmail = email.getText();
		}

		// Email
		String email = "Page title: " + driver.getTitle() + "\n" + "Log in availability: " + loginOK + "\n"
				+ "Logged In successfully: " + loggedinOK + "\n" + "Contacts availability: " + contactOK + "\n"
				+ "Pages availability: " + pagesOK + "\n" + "Repository Availability:" + repositoryOK;

		sendEmail(adminEmail, null, "jpfcf@iscte-iul.pt", "Monitoring Notification", email, "iscte-iul.pt");
	}

	public static void sendEmail(String to, String cc, String from, String subject, String text, String smtpHost) {
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtpHost);
			Session emailSession = Session.getDefaultInstance(properties);

			Message emailMessage = new MimeMessage(emailSession);
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			emailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			emailMessage.setFrom(new InternetAddress(from));
			emailMessage.setSubject(subject);
			emailMessage.setText(text);

			emailSession.setDebug(true);

			Transport.send(emailMessage);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JUnitCore.main("ExecuteMonitoring");
	}

}
