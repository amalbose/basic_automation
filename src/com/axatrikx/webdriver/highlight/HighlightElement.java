package com.axatrikx.webdriver.highlight;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HighlightElement {

	WebDriver driver;
	WebElement prevElement;
	String prevBackGround;

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "res/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	// @Test
	public void doSearchWithoutHightlight() {
		driver.get("http://axatrikx.com");
		driver.findElement(By.xpath("//*[@id='search-4']/form/div/input")).sendKeys("WebDriver" + Keys.ENTER);
		// Not the prefered way to find this element, but just for the sake of
		// this tutorial.
		// div[contains(@class,'post-list')]/div[@class='post-row'][1]/article[1]/div/h2/a
		driver.findElement(By.xpath("//article[1]/div/h2/a")).click();
	}

	// @Test
	public void doSearchWithHightlight() {
		driver.get("http://axatrikx.com");
		findElement(By.xpath("//*[@id='search-4']/form/div/input")).sendKeys("WebDriver" + Keys.ENTER);
		findElement(By.xpath("//article[1]/div/h2/a")).click();
	}

	@Test
	public void doSearchWithHightlightAndUnhighlight() {
		driver.get("http://axatrikx.com");
		findElement(By.xpath("//*[@id='search-4']/form/div/input")).sendKeys("WebDriver");
		findElement(By.xpath("//article[1]/div/h2/a")).click();
	}

	/**
	 * Custom findElement method which highlights the element
	 * 
	 * @param locator
	 * @return
	 */
	private WebElement findElement(By locator) {
		WebElement elem = driver.findElement(locator);
		highlightElement(elem);
		return elem;
	}

	/**
	 * Adds a background to the given element. The element and its initial
	 * background color are saved.
	 * 
	 * @param elem
	 */
	private void highlightElement(WebElement elem) {
		removeHighlight();
		if (driver instanceof JavascriptExecutor) {
			prevBackGround = (String) ((JavascriptExecutor) driver)
					.executeScript("return arguments[0].style.backgroundColor", elem);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.backgroundColor='yellow'", elem);
			// setting previous element
			prevElement = elem;
			System.out.println(prevBackGround);
		}
	}

	/**
	 * Removes the background color of the previously highlighted element.
	 */
	private void removeHighlight() {
		if (prevElement != null && driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript("arguments[0].style.backgroundColor='" + prevBackGround + "'",
					prevElement);
		}
	}

	@AfterClass()
	public void tearDown() {
		driver.quit();
	}
}
