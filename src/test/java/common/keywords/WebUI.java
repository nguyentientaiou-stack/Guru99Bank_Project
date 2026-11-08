package common.keywords;

import io.qameta.allure.Attachment;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebUI = a toolbox of "keywords" (ready-made actions) for testing a website with Selenium.
 *
 * ============================== HOW TO READ THIS FILE ==============================
 *
 * 1) LOCATORS - every keyword that receives a String "locator" understands these formats:
 *      xpath://button[@id='submit']   (or just  //button[@id='submit']  - xpath is the default)
 *      css:button#submit
 *      id:submit        name:email        class:btn-primary
 *      link:Forgot password?            partial link:Forgot
 *      tag:h1           testid:login-btn (looks for data-testid="login-btn")
 *      text:Sign in     (any element whose text is exactly "Sign in")
 *
 * 2) EVERY KEYWORD LOOKS THE SAME:
 *      - write a log line
 *      - try { do the work }
 *      - catch (Exception e) { report the problem }
 *
 * 3) TWO KINDS OF KEYWORDS:
 *      - ACTION keywords (click, inputText, selectOptionByText ...) return nothing (void).
 *        If they fail they call handleError(...): it writes an error log, and ALSO stops the test
 *        when "stopTestOnError" is true.
 *      - VERIFY / WAIT keywords (verifyElementVisible, waitForElementVisible ...) return true or false.
 *        They never stop the test by themselves. YOUR test decides what to do with the true/false
 *        (for example assertTrue(...)).
 *
 * 4) TIMEOUT - keywords wait up to "defaultTimeout" seconds (30 by default) for an element.
 *    Change it with setTimeout(10). Wait keywords also accept their own seconds, e.g.
 *    waitForElementVisible("id:menu", 5).
 *
 * 5) RECOMMENDED: call setStopTestOnError(true) once after creating WebUI. Then a failed click or
 *    a missing element fails the test right there, instead of the test continuing and failing later
 *    with a confusing message.
 */
public class WebUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebUI.class);

  // Address of the Selenium Grid. Can be changed from the command line: -Dselenium.remote.url=http://host:4444
  private static final String REMOTE_URL = System.getProperty("selenium.remote.url", "http://localhost:4444");

  // Headless browsers start with a tiny window by default, so we give them a normal desktop size.
  private static final String WINDOW_SIZE = "--window-size=1920,1080";

  private WebDriver driver;
  private int defaultTimeout = 30; // seconds
  private boolean stopTestOnError = false;

  // ===========================================================================================
  // 1. SMALL PRIVATE HELPERS (used by the keywords below)
  // ===========================================================================================

  /** Creates a "waiter" that can wait up to the given seconds for something to happen. */
  private WebDriverWait getWait(int seconds) {
    return new WebDriverWait(driver, Duration.ofSeconds(seconds));
  }

  private JavascriptExecutor getJs() {
    return (JavascriptExecutor) driver;
  }

  /** Selenium error messages are very long. We only keep the first line so logs stay readable. */
  private String shortMessage(Exception e) {
    return StringUtils.substringBefore(String.valueOf(e.getMessage()), "\n");
  }

  /** Only writes an error log. Used by verify/wait keywords (they return false instead of failing). */
  private void logError(String message, Exception e) {
    LOGGER.error(message + ". Root cause: " + shortMessage(e));
  }

  /** Writes an error log and, if stopTestOnError is true, stops the test by throwing an exception. */
  private void handleError(String message, Exception e) {
    logError(message, e);
    if (stopTestOnError) {
      throw new RuntimeException(message, e);
    }
  }

  /** Ctrl on Windows/Linux, Command on Mac. */
  private Keys getCommandKey() {
    String os = System.getProperty("os.name", "").toLowerCase();
    if (os.contains("mac")) {
      return Keys.COMMAND;
    }
    return Keys.CONTROL;
  }

  /** Waits until the element exists on the page, then returns it. Throws an exception if it never appears. */
  private WebElement getElement(String locator) {
    return getWait(defaultTimeout).until(ExpectedConditions.presenceOfElementLocated(getBy(locator)));
  }

  /** Waits until the element is visible AND enabled (ready to be clicked), then returns it. */
  private WebElement getClickableElement(String locator) {
    return getWait(defaultTimeout).until(ExpectedConditions.elementToBeClickable(getBy(locator)));
  }

  /** Scrolls the page so the element is in the middle of the screen. */
  private void scrollToCenter(WebElement element) {
    getJs().executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
  }

  /**
   * Turns our locator text (see "LOCATORS" at the top) into a Selenium "By".
   * Example: "css:#login" becomes By.cssSelector("#login").
   */
  private By getBy(String locator) {
    if (StringUtils.isBlank(locator)) {
      // BasePage.findTestObject() returns null when the key is missing in the JSON file.
      throw new IllegalArgumentException("Locator is null or empty - check the key exists in the objects repository");
    }
    String prefix = StringUtils.substringBefore(locator, ":"); // text before the first ':'
    String value = StringUtils.substringAfter(locator, ":");   // text after the first ':'
    switch (prefix.toLowerCase()) {
      case "css":
        return By.cssSelector(value);
      case "xpath":
        return By.xpath(value);
      case "id":
        return By.id(value);
      case "name":
        return By.name(value);
      case "class":
        return By.className(value);
      case "link":
        return By.linkText(value);
      case "partial link":
        return By.partialLinkText(value);
      case "tag":
        return By.tagName(value);
      case "testid":
        return By.cssSelector("[data-testid='" + value + "']");
      case "text":
        return By.xpath("//*[normalize-space(text())='" + value + "']");
      default:
        return By.xpath(locator); // no known prefix -> the whole text is a xpath
    }
  }

  /**
   * Builds a locator from a template. %s is replaced by your value.
   * Example: formatLocator("//tr[td='%s']//button", "Bob")  ->  //tr[td='Bob']//button
   */
  public static String formatLocator(String template, Object... values) {
    return String.format(template, values);
  }

  // ===========================================================================================
  // 2. SETTINGS
  // ===========================================================================================

  public WebDriver getDriver() {
    return driver;
  }

  public void setDriver(WebDriver driver) {
    this.driver = driver;
  }

  /** How many seconds keywords wait for an element before giving up. */
  public void setTimeout(int seconds) {
    this.defaultTimeout = seconds;
  }

  /** true = a failed ACTION keyword stops the test immediately. false = it only writes an error log. */
  public void setStopTestOnError(boolean stopTestOnError) {
    this.stopTestOnError = stopTestOnError;
  }

  // ===========================================================================================
  // 3. BROWSER: open and close
  // ===========================================================================================

  /**
   * Opens a browser. browserName can be:
   * CHROME, FIREFOX, EDGE, CHROME_HEADLESS, FIREFOX_HEADLESS, EDGE_HEADLESS, CHROME_REMOTE, FIREFOX_REMOTE
   * ("headless" = no visible window, "remote" = runs on the Selenium Grid)
   */
  public WebDriver openBrowser(String browserName) {
    String browser = StringUtils.defaultString(browserName).trim().toUpperCase();
    try {
      LOGGER.info("Opening browser '{}'", browser);
      switch (browser) {
        case "CHROME":
          driver = new ChromeDriver();
          break;
        case "FIREFOX":
          driver = new FirefoxDriver();
          break;
        case "EDGE":
          driver = new EdgeDriver();
          break;
        case "CHROME_HEADLESS":
          ChromeOptions headlessChrome = new ChromeOptions();
          headlessChrome.addArguments("--headless=new", WINDOW_SIZE);
          driver = new ChromeDriver(headlessChrome);
          break;
        case "FIREFOX_HEADLESS":
          FirefoxOptions headlessFirefox = new FirefoxOptions();
          headlessFirefox.addArguments("--headless", "-width", "1920", "-height", "1080");
          driver = new FirefoxDriver(headlessFirefox);
          break;
        case "EDGE_HEADLESS":
          EdgeOptions headlessEdge = new EdgeOptions();
          headlessEdge.addArguments("--headless=new", WINDOW_SIZE);
          driver = new EdgeDriver(headlessEdge);
          break;
        case "CHROME_REMOTE":
          ChromeOptions remoteChrome = new ChromeOptions();
          remoteChrome.addArguments("--remote-allow-origins=*", "--headless=new", WINDOW_SIZE);
          remoteChrome.setPlatformName("windows");
          remoteChrome.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
          /*driver = new RemoteWebDriver(new URL(REMOTE_URL), remoteChrome);*/
          driver = new RemoteWebDriver(URI.create(REMOTE_URL).toURL(), remoteChrome);
          break;
        case "FIREFOX_REMOTE":
          FirefoxOptions remoteFirefox = new FirefoxOptions();
          remoteFirefox.addArguments("--headless", "-width", "1920", "-height", "1080");
          remoteFirefox.setPlatformName("windows");
          remoteFirefox.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
          /*driver = new RemoteWebDriver(new URL(REMOTE_URL), remoteFirefox);*/
          driver = new RemoteWebDriver(URI.create(REMOTE_URL).toURL(), remoteFirefox);
          break;
        default:
          throw new IllegalArgumentException("Browser '" + browserName + "' is not supported");
      }
    } catch (Exception e) {
      handleError("Failed to open browser '" + browser + "'", e);
    }
    return driver;
  }

  public void closeBrowser() {
    try {
      LOGGER.info("Closing browser");
      if (driver != null) {
        driver.quit();
        driver = null;
      }
    } catch (Exception e) {
      logError("Failed to close browser", e);
    }
  }

  // ===========================================================================================
  // 4. WINDOWS AND TABS
  // ===========================================================================================

  public void maximizeWindow() {
    try {
      LOGGER.info("Maximizing the window");
      driver.manage().window().maximize();
    } catch (Exception e) {
      handleError("Failed to maximize the window", e);
    }
  }

  public void minimizeWindow() {
    try {
      LOGGER.info("Minimizing the window");
      driver.manage().window().minimize();
    } catch (Exception e) {
      handleError("Failed to minimize the window", e);
    }
  }

  /** Useful to test how the page looks on a small screen, e.g. setWindowSize(375, 812). */
  public void setWindowSize(int width, int height) {
    try {
      LOGGER.info("Setting window size to {}x{}", width, height);
      driver.manage().window().setSize(new Dimension(width, height));
    } catch (Exception e) {
      handleError("Failed to set window size to " + width + "x" + height, e);
    }
  }

  public int getWindowCount() {
    try {
      return driver.getWindowHandles().size();
    } catch (Exception e) {
      logError("Failed to get the number of windows", e);
    }
    return -1;
  }

  /** Opens an empty new tab and moves to it. */
  public void openNewTab() {
    try {
      LOGGER.info("Opening a new tab");
      driver.switchTo().newWindow(WindowType.TAB);
    } catch (Exception e) {
      handleError("Failed to open a new tab", e);
    }
  }

  public void openNewTab(String url) {
    openNewTab();
    navigateTo(url);
  }

  /**
   * Looks through all open windows for one whose title (or url) equals "value".
   * Returns true and STAYS on that window when found.
   * When not found, goes back to the window we started from and returns false.
   *
   * @param searchBy "title" or "url"
   */
  private boolean switchToWindowMatching(String searchBy, String value) {
    String startWindow = driver.getWindowHandle();
    for (String windowId : driver.getWindowHandles()) {
      driver.switchTo().window(windowId);
      String actual = searchBy.equals("title") ? driver.getTitle() : driver.getCurrentUrl();
      if (actual.equals(value)) {
        return true;
      }
    }
    driver.switchTo().window(startWindow);
    return false;
  }

  /** After closing a window, Selenium has no active window. This moves to another open one. */
  private void switchToAnyRemainingWindow(String preferredWindow) {
    List<String> remaining = new ArrayList<>(driver.getWindowHandles());
    if (remaining.isEmpty()) {
      return;
    }
    if (remaining.contains(preferredWindow)) {
      driver.switchTo().window(preferredWindow);
    } else {
      driver.switchTo().window(remaining.get(0));
    }
  }

  public void switchToWindowByTitle(String title) {
    try {
      LOGGER.info("Switching to window with title '{}'", title);
      if (!switchToWindowMatching("title", title)) {
        throw new IllegalStateException("No window has title '" + title + "'");
      }
    } catch (Exception e) {
      handleError("Failed to switch to window with title '" + title + "'", e);
    }
  }

  public void switchToWindowByUrl(String url) {
    try {
      LOGGER.info("Switching to window with url '{}'", url);
      if (!switchToWindowMatching("url", url)) {
        throw new IllegalStateException("No window has url '" + url + "'");
      }
    } catch (Exception e) {
      handleError("Failed to switch to window with url '" + url + "'", e);
    }
  }

  /** Index starts at 0 (0 = the first window that was opened). */
  public void switchToWindowByIndex(int index) {
    try {
      LOGGER.info("Switching to window at index '{}'", index);
      List<String> windowIds = new ArrayList<>(driver.getWindowHandles());
      driver.switchTo().window(windowIds.get(index));
    } catch (Exception e) {
      handleError("Failed to switch to window at index " + index, e);
    }
  }

  /** Moves to the window/tab that was opened last (e.g. after clicking a link that opens a new tab). */
  public void switchToNewestWindow() {
    try {
      LOGGER.info("Switching to the newest window");
      List<String> windowIds = new ArrayList<>(driver.getWindowHandles());
      driver.switchTo().window(windowIds.get(windowIds.size() - 1));
    } catch (Exception e) {
      handleError("Failed to switch to the newest window", e);
    }
  }

  public void closeWindowByTitle(String title) {
    try {
      LOGGER.info("Closing window with title '{}'", title);
      String startWindow = driver.getWindowHandle();
      if (!switchToWindowMatching("title", title)) {
        throw new IllegalStateException("No window has title '" + title + "'");
      }
      driver.close();
      switchToAnyRemainingWindow(startWindow);
    } catch (Exception e) {
      handleError("Failed to close window with title '" + title + "'", e);
    }
  }

  public void closeWindowByUrl(String url) {
    try {
      LOGGER.info("Closing window with url '{}'", url);
      String startWindow = driver.getWindowHandle();
      if (!switchToWindowMatching("url", url)) {
        throw new IllegalStateException("No window has url '" + url + "'");
      }
      driver.close();
      switchToAnyRemainingWindow(startWindow);
    } catch (Exception e) {
      handleError("Failed to close window with url '" + url + "'", e);
    }
  }

  public void closeWindowByIndex(int index) {
    try {
      LOGGER.info("Closing window at index '{}'", index);
      String startWindow = driver.getWindowHandle();
      List<String> windowIds = new ArrayList<>(driver.getWindowHandles());
      driver.switchTo().window(windowIds.get(index));
      driver.close();
      switchToAnyRemainingWindow(startWindow);
    } catch (Exception e) {
      handleError("Failed to close window at index " + index, e);
    }
  }

  /** Closes the window we are on and moves to another open one. */
  public void closeCurrentWindow() {
    try {
      LOGGER.info("Closing the current window");
      driver.close();
      switchToAnyRemainingWindow("");
    } catch (Exception e) {
      handleError("Failed to close the current window", e);
    }
  }

  // ===========================================================================================
  // 5. NAVIGATION AND PAGE INFORMATION
  // ===========================================================================================

  public void navigateTo(String url) {
    try {
      LOGGER.info("Navigating to '{}'", url);
      driver.navigate().to(url);
    } catch (Exception e) {
      handleError("Failed to navigate to '" + url + "'", e);
    }
  }

  public void refresh() {
    try {
      LOGGER.info("Refreshing the page");
      driver.navigate().refresh();
    } catch (Exception e) {
      handleError("Failed to refresh the page", e);
    }
  }

  public void forward() {
    try {
      LOGGER.info("Going forward");
      driver.navigate().forward();
    } catch (Exception e) {
      handleError("Failed to go forward", e);
    }
  }

  public void back() {
    try {
      LOGGER.info("Going back");
      driver.navigate().back();
    } catch (Exception e) {
      handleError("Failed to go back", e);
    }
  }

  public String getTitle() {
    try {
      String title = driver.getTitle();
      LOGGER.info("Page title is '{}'", title);
      return title;
    } catch (Exception e) {
      handleError("Failed to get the page title", e);
    }
    return null;
  }

  public String getCurrentUrl() {
    try {
      String url = driver.getCurrentUrl();
      LOGGER.info("Current URL is '{}'", url);
      return url;
    } catch (Exception e) {
      handleError("Failed to get the current URL", e);
    }
    return null;
  }

  public String getPageSource() {
    try {
      String source = driver.getPageSource();
      LOGGER.info("Got page source ({} characters)", source.length());
      return source;
    } catch (Exception e) {
      handleError("Failed to get the page source", e);
    }
    return null;
  }

  public boolean verifyCurrentURLIsSameAsExpectedURL(String expectedUrl) {
    try {
      String actual = driver.getCurrentUrl();
      if (actual.equals(expectedUrl)) {
        LOGGER.info("Current URL is '{}' as expected", expectedUrl);
        return true;
      }
      LOGGER.error("Current URL is '{}' but expected '{}'", actual, expectedUrl);
    } catch (Exception e) {
      logError("Failed to verify the current URL", e);
    }
    return false;
  }

  public boolean verifyCurrentUrlContains(String expectedPart) {
    try {
      String actual = driver.getCurrentUrl();
      if (actual.contains(expectedPart)) {
        LOGGER.info("Current URL '{}' contains '{}'", actual, expectedPart);
        return true;
      }
      LOGGER.error("Current URL '{}' does not contain '{}'", actual, expectedPart);
    } catch (Exception e) {
      logError("Failed to verify the current URL contains '" + expectedPart + "'", e);
    }
    return false;
  }

  public boolean verifyTitle(String expectedTitle) {
    try {
      String actual = driver.getTitle();
      if (actual.equals(expectedTitle)) {
        LOGGER.info("Page title is '{}' as expected", expectedTitle);
        return true;
      }
      LOGGER.error("Page title is '{}' but expected '{}'", actual, expectedTitle);
    } catch (Exception e) {
      logError("Failed to verify the page title", e);
    }
    return false;
  }

  public boolean verifyTitleContains(String expectedPart) {
    try {
      String actual = driver.getTitle();
      if (actual.contains(expectedPart)) {
        LOGGER.info("Page title '{}' contains '{}'", actual, expectedPart);
        return true;
      }
      LOGGER.error("Page title '{}' does not contain '{}'", actual, expectedPart);
    } catch (Exception e) {
      logError("Failed to verify the page title contains '" + expectedPart + "'", e);
    }
    return false;
  }

  /** Checks the visible text of the whole page contains the given text. */
  public boolean verifyPageContainsText(String text) {
    try {
      String pageText = driver.findElement(By.tagName("body")).getText();
      if (pageText.contains(text)) {
        LOGGER.info("Page contains text '{}'", text);
        return true;
      }
      LOGGER.error("Page does not contain text '{}'", text);
    } catch (Exception e) {
      logError("Failed to verify the page contains text '" + text + "'", e);
    }
    return false;
  }

  // ===========================================================================================
  // 6. FIND ELEMENTS
  // ===========================================================================================

  /** Finds one element (waits until it exists). Returns null if it is not found. */
  public WebElement findWebElement(String locator) {
    try {
      LOGGER.info("Finding web element '{}'", locator);
      return getElement(locator);
    } catch (Exception e) {
      logError("Failed to find web element '" + locator + "'", e);
    }
    return null;
  }

  /** Finds all elements that match (waits until at least one exists). Returns null if none is found. */
  public List<WebElement> findWebElements(String locator) {
    try {
      LOGGER.info("Finding web elements '{}'", locator);
      return getWait(defaultTimeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(getBy(locator)));
    } catch (Exception e) {
      logError("Failed to find web elements '" + locator + "'", e);
    }
    return null;
  }

  /** How many elements match right now. Does NOT wait. Returns 0 if there are none, -1 on error. */
  public int getElementCount(String locator) {
    try {
      int count = driver.findElements(getBy(locator)).size();
      LOGGER.info("Found {} element(s) for '{}'", count, locator);
      return count;
    } catch (Exception e) {
      logError("Failed to count elements '" + locator + "'", e);
    }
    return -1;
  }

  /** Quick check, does NOT wait: is the element in the page (even if hidden)? */
  public boolean isElementPresent(String locator) {
    try {
      return !driver.findElements(getBy(locator)).isEmpty();
    } catch (Exception e) {
      logError("Failed to check element '" + locator + "' is present", e);
    }
    return false;
  }

  /** Quick check, does NOT wait: is the element in the page AND visible? */
  public boolean isElementDisplayed(String locator) {
    try {
      List<WebElement> elements = driver.findElements(getBy(locator));
      return !elements.isEmpty() && elements.get(0).isDisplayed();
    } catch (Exception e) {
      logError("Failed to check element '" + locator + "' is displayed", e);
    }
    return false;
  }

  // ===========================================================================================
  // 7. TYPING AND KEYBOARD
  // ===========================================================================================

  /** Types text into a field. The text is written in the log. */
  public void inputText(String locator, String text) {
    try {
      LOGGER.info("Input text '{}' into '{}'", text, locator);
      getElement(locator).sendKeys(text);
    } catch (Exception e) {
      handleError("Failed to input text '" + text + "' into '" + locator + "'", e);
    }
  }

  /** Same as inputText but the text is NOT written in the log. Use it for passwords. */
  public void inputSensitiveText(String locator, String text) {
    try {
      LOGGER.info("Input sensitive text into '{}'", locator);
      getElement(locator).sendKeys(text);
    } catch (Exception e) {
      handleError("Failed to input sensitive text into '" + locator + "'", e);
    }
  }

  /** Deletes what is already in the field, then types the new text. */
  public void clearAndInputText(String locator, String text) {
    try {
      LOGGER.info("Clear then input text '{}' into '{}'", text, locator);
      WebElement element = getElement(locator);
      element.clear();
      // Some websites ignore clear(). If the field still has text, delete it with the keyboard.
      if (StringUtils.isNotEmpty(element.getAttribute("value"))) {
        element.sendKeys(Keys.chord(getCommandKey(), "a"), Keys.BACK_SPACE);
      }
      element.sendKeys(text);
    } catch (Exception e) {
      handleError("Failed to clear then input text '" + text + "' into '" + locator + "'", e);
    }
  }

  /** Sets the text with JavaScript. Use only when normal typing does not work (special date/masked fields). */
  public void inputTextByJS(String locator, String text) {
    try {
      LOGGER.info("Set text '{}' into '{}' using JavaScript", text, locator);
      setValueByJS(getElement(locator), text);
    } catch (Exception e) {
      handleError("Failed to set text into '" + locator + "' using JavaScript", e);
    }
  }

  /** Puts a value into an input and tells the page "the value changed" (like a real user typing). */
  private void setValueByJS(WebElement element, String value) {
    getJs().executeScript("arguments[0].value = arguments[1];", element, value);
    getJs().executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", element);
    getJs().executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
  }

  public void clearText(String locator) {
    try {
      LOGGER.info("Clear text of '{}'", locator);
      getElement(locator).clear();
    } catch (Exception e) {
      handleError("Failed to clear text of '" + locator + "'", e);
    }
  }

  /** Clears a field with the keyboard: Select all (Ctrl+A) then Backspace. */
  public void clearTextBySendKeysControlAAndBackspace(String locator) {
    try {
      LOGGER.info("Clear text of '{}' with Ctrl+A and Backspace", locator);
      getElement(locator).sendKeys(Keys.chord(getCommandKey(), "a"), Keys.BACK_SPACE);
    } catch (Exception e) {
      handleError("Failed to clear text of '" + locator + "'", e);
    }
  }

  /** Presses a keyboard key on an element. Example: pressKey("id:search", Keys. ENTER) */
  public void pressKey(String locator, Keys key) {
    try {
      LOGGER.info("Press key '{}' on '{}'", key.name(), locator);
      getElement(locator).sendKeys(key);
    } catch (Exception e) {
      handleError("Failed to press key '" + key.name() + "' on '" + locator + "'", e);
    }
  }

  /** Presses Tab on the element (moves to the next field and triggers validation messages). */
  public void pressTabToElement(String locator) {
    pressKey(locator, Keys.TAB);
  }

  public void pressEnter(String locator) {
    pressKey(locator, Keys.ENTER);
  }

  public void pressEscape(String locator) {
    pressKey(locator, Keys.ESCAPE);
  }

  /** Presses a key on whatever has focus on the page (no element needed). Example: close a popup with ESCAPE. */
  public void pressKeyOnPage(Keys key) {
    try {
      LOGGER.info("Press key '{}' on the page", key.name());
      new Actions(driver).sendKeys(key).perform();
    } catch (Exception e) {
      handleError("Failed to press key '" + key.name() + "' on the page", e);
    }
  }

  /** Chooses a file in an upload field (an <input type="file">). Give the full path of the file. */
  public void uploadFile(String locator, String filePath) {
    try {
      LOGGER.info("Upload file '{}' using '{}'", filePath, locator);
      File file = new File(filePath);
      if (!file.isFile()) {
        throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
      }
      getElement(locator).sendKeys(file.getAbsolutePath());
    } catch (Exception e) {
      handleError("Failed to upload file '" + filePath + "' using '" + locator + "'", e);
    }
  }

  /** Puts the cursor into the element (like clicking it, but without pressing it). */
  public void focus(String locator) {
    try {
      LOGGER.info("Focus on '{}'", locator);
      getJs().executeScript("arguments[0].focus();", getElement(locator));
    } catch (Exception e) {
      handleError("Failed to focus on '" + locator + "'", e);
    }
  }

  /** Submits the form that contains this element. */
  public void submit(String locator) {
    try {
      LOGGER.info("Submit '{}'", locator);
      getElement(locator).submit();
    } catch (Exception e) {
      handleError("Failed to submit '" + locator + "'", e);
    }
  }

  // ===========================================================================================
  // 8. DELAYS - fixed waiting. Prefer the waitFor... keywords (section 15), they are faster and safer.
  // ===========================================================================================

  public void delayInSeconds(int seconds) {
    delayInMilliSeconds(seconds * 1000);
  }

  public void delayInMilliSeconds(int milliseconds) {
    try {
      LOGGER.info("Delay {} millisecond(s)", milliseconds);
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logError("Delay was interrupted", e);
    }
  }

  // ===========================================================================================
  // 9. MOUSE ACTIONS
  // ===========================================================================================

  /**
   * Clicks an element. It first waits until the element is visible and enabled.
   * If the click is blocked (something covers the element) or the page re-draws the element,
   * it scrolls to the element and tries ONE more time.
   */
  public void click(String locator) {
    try {
      LOGGER.info("Click on '{}'", locator);
      try {
        getClickableElement(locator).click();
      } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
        LOGGER.warn("First click on '{}' failed, scrolling to it and trying again", locator);
        WebElement element = getClickableElement(locator);
        scrollToCenter(element);
        element.click();
      }
    } catch (Exception e) {
      handleError("Failed to click on '" + locator + "'", e);
    }
  }

  public void click(WebElement element) {
    try {
      LOGGER.info("Click on web element");
      element.click();
    } catch (Exception e) {
      handleError("Failed to click on web element", e);
    }
  }

  /**
   * Clicks only if the element appears. Never fails. Good for things that sometimes show up,
   * like a cookie banner or a popup. Waits only 3 seconds. Returns true if it clicked.
   */
  public boolean clickIfPresent(String locator) {
    try {
      LOGGER.info("Click on '{}' if it is present", locator);
      getWait(3).until(ExpectedConditions.elementToBeClickable(getBy(locator))).click();
      return true;
    } catch (Exception e) {
      LOGGER.info("'{}' is not there, skip clicking", locator);
    }
    return false;
  }

  /** Clicks the element with JavaScript. Use only when a normal click cannot work. */
  public void clickByJS(String locator) {
    clickByJS(findWebElement(locator));
  }

  public void clickByJS(WebElement element) {
    try {
      LOGGER.info("Click with JavaScript");
      getJs().executeScript("arguments[0].click();", element);
    } catch (Exception e) {
      handleError("Failed to click with JavaScript", e);
    }
  }

  public void doubleClick(String locator) {
    doubleClick(findWebElement(locator));
  }

  public void doubleClick(WebElement element) {
    try {
      LOGGER.info("Double click");
      new Actions(driver).doubleClick(element).perform();
    } catch (Exception e) {
      handleError("Failed to double click", e);
    }
  }

  public void rightClick(String locator) {
    rightClick(findWebElement(locator));
  }

  public void rightClick(WebElement element) {
    try {
      LOGGER.info("Right click");
      new Actions(driver).contextClick(element).perform();
    } catch (Exception e) {
      handleError("Failed to right click", e);
    }
  }

  /** Moves the mouse over the element (shows hover menus / tooltips). */
  public void mouseOver(String locator) {
    mouseOver(findWebElement(locator));
  }

  public void mouseOver(WebElement element) {
    try {
      LOGGER.info("Mouse over");
      new Actions(driver).moveToElement(element).perform();
    } catch (Exception e) {
      handleError("Failed to mouse over", e);
    }
  }

  /** Hover over a menu, then click an item that appears. */
  public void hoverThenClick(String hoverLocator, String clickLocator) {
    mouseOver(hoverLocator);
    click(clickLocator);
  }

  /** Clicks at a point inside the element, measured from the element's center (in pixels). */
  public void clickOffset(String locator, int xOffset, int yOffset) {
    clickOffset(findWebElement(locator), xOffset, yOffset);
  }

  public void clickOffset(WebElement element, int xOffset, int yOffset) {
    try {
      LOGGER.info("Click at offset ({}, {})", xOffset, yOffset);
      new Actions(driver).moveToElement(element, xOffset, yOffset).click().perform();
    } catch (Exception e) {
      handleError("Failed to click at offset (" + xOffset + ", " + yOffset + ")", e);
    }
  }

  /** Drags one element and drops it on another. */
  public void dragAndDrop(String sourceLocator, String targetLocator) {
    dragAndDrop(findWebElement(sourceLocator), findWebElement(targetLocator));
  }

  public void dragAndDrop(WebElement source, WebElement target) {
    try {
      LOGGER.info("Drag and drop");
      new Actions(driver).dragAndDrop(source, target).perform();
    } catch (Exception e) {
      handleError("Failed to drag and drop", e);
    }
  }

  /** Drags an element by some pixels (sliders, resizable panels). */
  public void dragAndDropByOffset(String locator, int xOffset, int yOffset) {
    try {
      LOGGER.info("Drag '{}' by ({}, {})", locator, xOffset, yOffset);
      new Actions(driver).dragAndDropBy(getElement(locator), xOffset, yOffset).perform();
    } catch (Exception e) {
      handleError("Failed to drag '" + locator + "'", e);
    }
  }

  // ===========================================================================================
  // 10. CHECKBOX
  // ===========================================================================================

  /** Makes the checkbox checked (true) or unchecked (false). Clicks only if it is not already in that state. */
  public void setCheckbox(String locator, boolean checked) {
    try {
      LOGGER.info("Set checkbox '{}' to {}", locator, checked);
      WebElement checkbox = getClickableElement(locator);
      if (checkbox.isSelected() != checked) {
        checkbox.click();
      }
    } catch (Exception e) {
      handleError("Failed to set checkbox '" + locator + "'", e);
    }
  }

  public void check(String locator) {
    setCheckbox(locator, true);
  }

  public void uncheck(String locator) {
    setCheckbox(locator, false);
  }

  // ===========================================================================================
  // 11. JAVASCRIPT AND SCROLLING
  // ===========================================================================================

  public Object executeJavascript(String script, Object... arguments) {
    try {
      LOGGER.info("Execute JavaScript '{}'", script);
      return getJs().executeScript(script, arguments);
    } catch (Exception e) {
      handleError("Failed to execute JavaScript '" + script + "'", e);
    }
    return null;
  }

  /** Scrolls until the element is in the middle of the screen. */
  public void scrollToElement(String locator) {
    scrollToElement(findWebElement(locator));
  }

  public void scrollToElement(WebElement element) {
    try {
      LOGGER.info("Scroll to element");
      scrollToCenter(element);
    } catch (Exception e) {
      handleError("Failed to scroll to element", e);
    }
  }

  public void scrollToElementAtPageCenter(String locator) {
    scrollToElementAtPageCenter(findWebElement(locator));
  }

  public void scrollToElementAtPageCenter(WebElement element) {
    try {
      LOGGER.info("Scroll element to the center of the page");
      getJs().executeScript(
          "var rect = arguments[0].getBoundingClientRect();"
              + "var top = window.pageYOffset + rect.top - (window.innerHeight / 2) + (rect.height / 2);"
              + "var left = window.pageXOffset + rect.left - (window.innerWidth / 2) + (rect.width / 2);"
              + "window.scrollTo(left, top);", element);
    } catch (Exception e) {
      handleError("Failed to scroll element to the center of the page", e);
    }
  }

  public void scrollToTop() {
    executeJavascript("window.scrollTo(0, 0);");
  }

  public void scrollToBottom() {
    executeJavascript("window.scrollTo(0, document.body.scrollHeight);");
  }

  /** Scrolls by pixels. scrollBy(0, 500) scrolls down 500 pixels. */
  public void scrollBy(int x, int y) {
    executeJavascript("window.scrollBy(arguments[0], arguments[1]);", x, y);
  }

  public void setAttributeByJS(String locator, String attributeName, String value) {
    try {
      LOGGER.info("Set attribute '{}' = '{}' on '{}'", attributeName, value, locator);
      getJs().executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", getElement(locator), attributeName, value);
    } catch (Exception e) {
      handleError("Failed to set attribute '" + attributeName + "' on '" + locator + "'", e);
    }
  }

  public void removeAttributeByJS(String locator, String attributeName) {
    try {
      LOGGER.info("Remove attribute '{}' from '{}'", attributeName, locator);
      getJs().executeScript("arguments[0].removeAttribute(arguments[1]);", getElement(locator), attributeName);
    } catch (Exception e) {
      handleError("Failed to remove attribute '" + attributeName + "' from '" + locator + "'", e);
    }
  }

  /** Deletes elements from the page. Useful for ads/popups that cover the buttons you need to click. */
  public void removeElementsByJS(String locator) {
    try {
      LOGGER.info("Remove elements '{}' from the page", locator);
      List<WebElement> elements = driver.findElements(getBy(locator));
      for (WebElement element : elements) {
        getJs().executeScript("arguments[0].remove();", element);
      }
      LOGGER.info("Removed {} element(s)", elements.size());
    } catch (Exception e) {
      handleError("Failed to remove elements '" + locator + "'", e);
    }
  }

  // ===========================================================================================
  // 12. COPY AND PASTE
  // ===========================================================================================

  /** Selects all text in the element and copies it (Ctrl+A, Ctrl+C). */
  public void copyText(String locator) {
    try {
      LOGGER.info("Copy text of '{}'", locator);
      WebElement element = getElement(locator);
      element.sendKeys(Keys.chord(getCommandKey(), "a"));
      element.sendKeys(Keys.chord(getCommandKey(), "c"));
    } catch (Exception e) {
      handleError("Failed to copy text of '" + locator + "'", e);
    }
  }

  /** Pastes (Ctrl+V) into the element. */
  public void pasteText(String locator) {
    try {
      LOGGER.info("Paste text into '{}'", locator);
      getElement(locator).sendKeys(Keys.chord(getCommandKey(), "v"));
    } catch (Exception e) {
      handleError("Failed to paste text into '" + locator + "'", e);
    }
  }

  // ===========================================================================================
  // 13. VERIFY KEYWORDS - return true if the check passes, false if not (they never stop the test)
  // ===========================================================================================

  /** Waits (up to the default timeout) for the element to become visible. */
  public boolean verifyElementVisible(String locator) {
    return waitForElementVisible(locator);
  }

  public boolean verifyElementVisible(String locator, int seconds) {
    return waitForElementVisible(locator, seconds);
  }

  /** True when the element is hidden OR not in the page at all. Waits up to the default timeout. */
  public boolean verifyElementInvisible(String locator) {
    return waitForElementInvisible(locator);
  }

  public boolean verifyElementInvisible(String locator, int seconds) {
    return waitForElementInvisible(locator, seconds);
  }

  /** Waits for the element to exist in the page (it may be hidden). */
  public boolean verifyElementPresent(String locator) {
    return waitForElementPresent(locator);
  }

  /** Quick check (no waiting): the element is not in the page. */
  public boolean verifyElementIsNotPresent(String locator) {
    try {
      if (driver.findElements(getBy(locator)).isEmpty()) {
        LOGGER.info("'{}' is not present, as expected", locator);
        return true;
      }
      LOGGER.error("'{}' is still present", locator);
    } catch (Exception e) {
      logError("Failed to verify '" + locator + "' is not present", e);
    }
    return false;
  }

  /** Visible and enabled. Waits up to the default timeout. */
  public boolean verifyElementClickable(String locator) {
    return waitForElementClickable(locator);
  }

  /** Quick check: the element is disabled. */
  public boolean verifyElementNotClickable(String locator) {
    try {
      if (!getElement(locator).isEnabled()) {
        LOGGER.info("'{}' is not clickable, as expected", locator);
        return true;
      }
      LOGGER.error("'{}' is still clickable", locator);
    } catch (Exception e) {
      logError("Failed to verify '" + locator + "' is not clickable", e);
    }
    return false;
  }

  /** For checkboxes / radio buttons. */
  public boolean verifyElementSelected(String locator) {
    try {
      if (getElement(locator).isSelected()) {
        LOGGER.info("'{}' is selected, as expected", locator);
        return true;
      }
      LOGGER.error("'{}' is not selected", locator);
    } catch (Exception e) {
      logError("Failed to verify '" + locator + "' is selected", e);
    }
    return false;
  }

  public boolean verifyElementNotSelected(String locator) {
    try {
      if (!getElement(locator).isSelected()) {
        LOGGER.info("'{}' is not selected, as expected", locator);
        return true;
      }
      LOGGER.error("'{}' is selected", locator);
    } catch (Exception e) {
      logError("Failed to verify '" + locator + "' is not selected", e);
    }
    return false;
  }

  /** The visible text must be exactly equal to expectedText. */
  public boolean verifyElementText(String locator, String expectedText) {
    try {
      String actualText = getElement(locator).getText();
      if (actualText.equals(expectedText)) {
        LOGGER.info("Text of '{}' is '{}', as expected", locator, expectedText);
        return true;
      }
      LOGGER.error("Text of '{}' is '{}' but expected '{}'", locator, actualText, expectedText);
    } catch (Exception e) {
      logError("Failed to verify text of '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyElementText(WebElement element, String expectedText) {
    try {
      String actualText = element.getText();
      if (actualText.equals(expectedText)) {
        LOGGER.info("Text of the element is '{}', as expected", expectedText);
        return true;
      }
      LOGGER.error("Text of the element is '{}' but expected '{}'", actualText, expectedText);
    } catch (Exception e) {
      logError("Failed to verify text of the element", e);
    }
    return false;
  }

  /** The visible text must contain expectedText. */
  public boolean verifyElementContainsText(String locator, String expectedText) {
    try {
      String actualText = getElement(locator).getText();
      if (actualText.contains(expectedText)) {
        LOGGER.info("Text of '{}' contains '{}'", locator, expectedText);
        return true;
      }
      LOGGER.error("Text of '{}' is '{}', it does not contain '{}'", locator, actualText, expectedText);
    } catch (Exception e) {
      logError("Failed to verify text of '" + locator + "'", e);
    }
    return false;
  }

  /** Same as verifyElementText but "Hello" and "hello" are treated as equal. */
  public boolean verifyElementTextIgnoreCase(String locator, String expectedText) {
    try {
      String actualText = getElement(locator).getText();
      if (actualText.equalsIgnoreCase(expectedText)) {
        LOGGER.info("Text of '{}' is '{}' (ignoring upper/lower case)", locator, expectedText);
        return true;
      }
      LOGGER.error("Text of '{}' is '{}' but expected '{}' (ignoring case)", locator, actualText, expectedText);
    } catch (Exception e) {
      logError("Failed to verify text of '" + locator + "'", e);
    }
    return false;
  }

  /** Checks an attribute value. Example: verifyElementAttribute("id:email", "placeholder", "Enter email") */
  public boolean verifyElementAttribute(String locator, String attributeName, String expectedValue) {
    try {
      String actualValue = getElement(locator).getAttribute(attributeName);
      if (StringUtils.equals(actualValue, expectedValue)) {
        LOGGER.info("Attribute '{}' of '{}' is '{}', as expected", attributeName, locator, expectedValue);
        return true;
      }
      LOGGER.error("Attribute '{}' of '{}' is '{}' but expected '{}'", attributeName, locator, actualValue, expectedValue);
    } catch (Exception e) {
      logError("Failed to verify attribute '" + attributeName + "' of '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyElementAttributeContains(String locator, String attributeName, String expectedPart) {
    try {
      String actualValue = getElement(locator).getAttribute(attributeName);
      if (actualValue != null && actualValue.contains(expectedPart)) {
        LOGGER.info("Attribute '{}' of '{}' contains '{}'", attributeName, locator, expectedPart);
        return true;
      }
      LOGGER.error("Attribute '{}' of '{}' is '{}', it does not contain '{}'", attributeName, locator, actualValue, expectedPart);
    } catch (Exception e) {
      logError("Failed to verify attribute '" + attributeName + "' of '" + locator + "'", e);
    }
    return false;
  }

  /** Checks a CSS style. Example: verifyElementCssValue("id:error", "color", "rgba(255, 0, 0, 1)") */
  public boolean verifyElementCssValue(String locator, String cssName, String expectedValue) {
    try {
      String actualValue = getElement(locator).getCssValue(cssName);
      if (actualValue.equals(expectedValue)) {
        LOGGER.info("CSS '{}' of '{}' is '{}', as expected", cssName, locator, expectedValue);
        return true;
      }
      LOGGER.error("CSS '{}' of '{}' is '{}' but expected '{}'", cssName, locator, actualValue, expectedValue);
    } catch (Exception e) {
      logError("Failed to verify CSS '" + cssName + "' of '" + locator + "'", e);
    }
    return false;
  }

  /** Checks the element has the CSS class (the "class" attribute can hold many classes). */
  public boolean verifyElementHasClass(String locator, String className) {
    try {
      String classes = StringUtils.defaultString(getElement(locator).getAttribute("class"));
      String[] classList = classes.trim().split("\\s+"); // split on spaces
      for (String oneClass : classList) {
        if (oneClass.equals(className)) {
          LOGGER.info("'{}' has class '{}'", locator, className);
          return true;
        }
      }
      LOGGER.error("'{}' has classes '{}', but not '{}'", locator, classes, className);
    } catch (Exception e) {
      logError("Failed to verify class '" + className + "' of '" + locator + "'", e);
    }
    return false;
  }

  /** Checks what is typed inside an input box. (verifyElementText cannot do this: inputs have no visible text.) */
  public boolean verifyInputValue(String locator, String expectedValue) {
    return verifyElementAttribute(locator, "value", expectedValue);
  }

  /** Checks how many elements match the locator. */
  public boolean verifyElementCount(String locator, int expectedCount) {
    int actualCount = getElementCount(locator);
    if (actualCount == expectedCount) {
      LOGGER.info("Found {} element(s) for '{}', as expected", actualCount, locator);
      return true;
    }
    LOGGER.error("Found {} element(s) for '{}' but expected {}", actualCount, locator, expectedCount);
    return false;
  }

  // ===========================================================================================
  // 14. GET KEYWORDS - read information from the page
  // ===========================================================================================

  public String getText(String locator) {
    try {
      String text = getElement(locator).getText();
      LOGGER.info("Text of '{}' is '{}'", locator, text);
      return text;
    } catch (Exception e) {
      handleError("Failed to get text of '" + locator + "'", e);
    }
    return null;
  }

  /** Visible text of every matching element, in page order. */
  public List<String> getTextOfElements(String locator) {
    try {
      List<String> texts = new ArrayList<>();
      for (WebElement element : findWebElements(locator)) {
        texts.add(element.getText());
      }
      LOGGER.info("Texts of '{}' are {}", locator, texts);
      return texts;
    } catch (Exception e) {
      handleError("Failed to get texts of '" + locator + "'", e);
    }
    return null;
  }

  /** What is typed inside an input box. */
  public String getInputValue(String locator) {
    return getAttributeValue(locator, "value");
  }

  public String getTagName(String locator) {
    try {
      String tagName = getElement(locator).getTagName();
      LOGGER.info("Tag name of '{}' is '{}'", locator, tagName);
      return tagName;
    } catch (Exception e) {
      handleError("Failed to get tag name of '" + locator + "'", e);
    }
    return null;
  }

  public String getAttributeValue(String locator, String attributeName) {
    try {
      String value = getElement(locator).getAttribute(attributeName);
      LOGGER.info("Attribute '{}' of '{}' is '{}'", attributeName, locator, value);
      return value;
    } catch (Exception e) {
      handleError("Failed to get attribute '" + attributeName + "' of '" + locator + "'", e);
    }
    return null;
  }

  public String getCssValue(String locator, String cssName) {
    try {
      String value = getElement(locator).getCssValue(cssName);
      LOGGER.info("CSS '{}' of '{}' is '{}'", cssName, locator, value);
      return value;
    } catch (Exception e) {
      handleError("Failed to get CSS '" + cssName + "' of '" + locator + "'", e);
    }
    return null;
  }

  public int getWidth(String locator) {
    try {
      int width = getElement(locator).getSize().getWidth();
      LOGGER.info("Width of '{}' is {}", locator, width);
      return width;
    } catch (Exception e) {
      handleError("Failed to get width of '" + locator + "'", e);
    }
    return -1;
  }

  public int getHeight(String locator) {
    try {
      int height = getElement(locator).getSize().getHeight();
      LOGGER.info("Height of '{}' is {}", locator, height);
      return height;
    } catch (Exception e) {
      handleError("Failed to get height of '" + locator + "'", e);
    }
    return -1;
  }

  public int getElementLeftPosition(String locator) {
    try {
      int x = getElement(locator).getLocation().getX();
      LOGGER.info("Left position of '{}' is {}", locator, x);
      return x;
    } catch (Exception e) {
      handleError("Failed to get left position of '" + locator + "'", e);
    }
    return -1;
  }

  public int getElementTopPosition(String locator) {
    try {
      int y = getElement(locator).getLocation().getY();
      LOGGER.info("Top position of '{}' is {}", locator, y);
      return y;
    } catch (Exception e) {
      handleError("Failed to get top position of '" + locator + "'", e);
    }
    return -1;
  }

  // ===========================================================================================
  // 15. WAIT KEYWORDS - wait until something happens. true = it happened in time, false = it did not.
  //     Each wait has two versions: one uses the default timeout, the other lets you give the seconds.
  // ===========================================================================================

  public boolean waitForElementPresent(String locator) {
    return waitForElementPresent(locator, defaultTimeout);
  }

  /** Waits until the element exists in the page (it may still be hidden). */
  public boolean waitForElementPresent(String locator, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to be present", seconds, locator);
      getWait(seconds).until(ExpectedConditions.presenceOfElementLocated(getBy(locator)));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' did not become present within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForElementIsNotPresent(String locator) {
    return waitForElementIsNotPresent(locator, defaultTimeout);
  }

  /** Waits until the element is removed from the page. */
  public boolean waitForElementIsNotPresent(String locator, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to be removed from the page", seconds, locator);
      getWait(seconds).until(ExpectedConditions.numberOfElementsToBe(getBy(locator), 0));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' was still present after " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForElementVisible(String locator) {
    return waitForElementVisible(locator, defaultTimeout);
  }

  /** Waits until the element is in the page AND can be seen. */
  public boolean waitForElementVisible(String locator, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to be visible", seconds, locator);
      getWait(seconds).until(ExpectedConditions.visibilityOfElementLocated(getBy(locator)));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' did not become visible within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForElementInvisible(String locator) {
    return waitForElementInvisible(locator, defaultTimeout);
  }

  /** Waits until the element is hidden or removed from the page. */
  public boolean waitForElementInvisible(String locator, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to be invisible", seconds, locator);
      getWait(seconds).until(ExpectedConditions.invisibilityOfElementLocated(getBy(locator)));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' was still visible after " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForElementClickable(String locator) {
    return waitForElementClickable(locator, defaultTimeout);
  }

  /** Waits until the element is visible and enabled. */
  public boolean waitForElementClickable(String locator, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to be clickable", seconds, locator);
      getWait(seconds).until(ExpectedConditions.elementToBeClickable(getBy(locator)));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' did not become clickable within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForTextPresent(String locator, String text) {
    return waitForTextPresent(locator, text, defaultTimeout);
  }

  /** Waits until the element's text CONTAINS the given text. */
  public boolean waitForTextPresent(String locator, String text, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to contain text '{}'", seconds, locator, text);
      getWait(seconds).until(ExpectedConditions.textToBePresentInElementLocated(getBy(locator), text));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' did not contain text '" + text + "' within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForElementText(String locator, String text) {
    return waitForElementText(locator, text, defaultTimeout);
  }

  /** Waits until the element's text is EXACTLY the given text. */
  public boolean waitForElementText(String locator, String text, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for '{}' to have text '{}'", seconds, locator, text);
      getWait(seconds).until(ExpectedConditions.textToBe(getBy(locator), text));
      return true;
    } catch (Exception e) {
      logError("'" + locator + "' did not have text '" + text + "' within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForAttributeValue(String locator, String attributeName, String value) {
    return waitForAttributeValue(locator, attributeName, value, defaultTimeout);
  }

  public boolean waitForAttributeValue(String locator, String attributeName, String value, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for attribute '{}' of '{}' to be '{}'", seconds, attributeName, locator, value);
      getWait(seconds).until(ExpectedConditions.attributeToBe(getBy(locator), attributeName, value));
      return true;
    } catch (Exception e) {
      logError("Attribute '" + attributeName + "' of '" + locator + "' did not become '" + value + "' within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForCssValue(String locator, String cssName, String expectedValue) {
    return waitForCssValue(locator, cssName, expectedValue, defaultTimeout);
  }

  /** Waits until a CSS style has the expected value (for example after an animation). */
  public boolean waitForCssValue(String locator, String cssName, String expectedValue, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for CSS '{}' of '{}' to be '{}'", seconds, cssName, locator, expectedValue);
      long endTime = System.currentTimeMillis() + seconds * 1000L;
      while (System.currentTimeMillis() < endTime) {
        try {
          // We find the element again on every loop, so it still works if the page re-draws the element.
          String actualValue = driver.findElement(getBy(locator)).getCssValue(cssName);
          if (actualValue.equals(expectedValue)) {
            return true;
          }
        } catch (NoSuchElementException | StaleElementReferenceException e) {
          // The element is not ready yet - just try again
        }
        delayInMilliSeconds(500);
      }
      LOGGER.error("CSS '{}' of '{}' did not become '{}' within {}s", cssName, locator, expectedValue, seconds);
    } catch (Exception e) {
      logError("Failed while waiting for CSS '" + cssName + "' of '" + locator + "'", e);
    }
    return false;
  }

  public boolean waitForElementCount(String locator, int expectedCount) {
    return waitForElementCount(locator, expectedCount, defaultTimeout);
  }

  /** Waits until exactly expectedCount elements match the locator. */
  public boolean waitForElementCount(String locator, int expectedCount, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for {} element(s) '{}'", seconds, expectedCount, locator);
      getWait(seconds).until(ExpectedConditions.numberOfElementsToBe(getBy(locator), expectedCount));
      return true;
    } catch (Exception e) {
      logError("Did not find " + expectedCount + " element(s) '" + locator + "' within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForUrlContains(String urlPart) {
    return waitForUrlContains(urlPart, defaultTimeout);
  }

  /** Waits until the browser address contains the text. Great after clicking Submit / Login. */
  public boolean waitForUrlContains(String urlPart, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for the URL to contain '{}'", seconds, urlPart);
      getWait(seconds).until(ExpectedConditions.urlContains(urlPart));
      return true;
    } catch (Exception e) {
      logError("URL did not contain '" + urlPart + "' within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForTitleContains(String titlePart) {
    return waitForTitleContains(titlePart, defaultTimeout);
  }

  public boolean waitForTitleContains(String titlePart, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for the title to contain '{}'", seconds, titlePart);
      getWait(seconds).until(ExpectedConditions.titleContains(titlePart));
      return true;
    } catch (Exception e) {
      logError("Title did not contain '" + titlePart + "' within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForNumberOfWindows(int expectedCount) {
    return waitForNumberOfWindows(expectedCount, defaultTimeout);
  }

  public boolean waitForNumberOfWindows(int expectedCount, int seconds) {
    try {
      LOGGER.info("Wait up to {}s for {} window(s) to be open", seconds, expectedCount);
      getWait(seconds).until(ExpectedConditions.numberOfWindowsToBe(expectedCount));
      return true;
    } catch (Exception e) {
      logError("Did not get " + expectedCount + " window(s) within " + seconds + "s", e);
    }
    return false;
  }

  public boolean waitForPageLoad() {
    return waitForPageLoad(defaultTimeout);
  }

  /** Waits until the browser says the page has finished loading. */
  public boolean waitForPageLoad(int seconds) {
    try {
      LOGGER.info("Wait up to {}s for the page to finish loading", seconds);
      // The script returns true when loaded, or null while still loading (null = "keep waiting")
      getWait(seconds).until(ExpectedConditions.jsReturnsValue(
          "return document.readyState == 'complete' ? true : null;"));
      return true;
    } catch (Exception e) {
      logError("Page did not finish loading within " + seconds + "s", e);
    }
    return false;
  }

  // ===========================================================================================
  // 16. DROPDOWN (<select> element)
  // ===========================================================================================

  private Select getSelect(String locator) {
    return new Select(getElement(locator));
  }

  /** Same as getSelect, but throws an error if the dropdown does not allow choosing several options. */
  private Select getMultiSelect(String locator) {
    Select select = getSelect(locator);
    if (!select.isMultiple()) {
      throw new UnsupportedOperationException("'" + locator + "' does not allow multiple selections");
    }
    return select;
  }

  private WebElement findOptionByText(Select select, String text) {
    for (WebElement option : select.getOptions()) {
      if (option.getText().equals(text)) {
        return option;
      }
    }
    return null;
  }

  private WebElement findOptionByValue(Select select, String value) {
    for (WebElement option : select.getOptions()) {
      if (value.equals(option.getAttribute("value"))) {
        return option;
      }
    }
    return null;
  }

  private WebElement findOptionByIndex(Select select, int index) {
    List<WebElement> options = select.getOptions();
    if (index >= 0 && index < options.size()) {
      return options.get(index);
    }
    return null;
  }

  public void selectOptionByText(String locator, String text) {
    try {
      LOGGER.info("Select option with text '{}' in '{}'", text, locator);
      getSelect(locator).selectByVisibleText(text);
    } catch (Exception e) {
      handleError("Failed to select option with text '" + text + "' in '" + locator + "'", e);
    }
  }

  public void selectOptionByValue(String locator, String value) {
    try {
      LOGGER.info("Select option with value '{}' in '{}'", value, locator);
      getSelect(locator).selectByValue(value);
    } catch (Exception e) {
      handleError("Failed to select option with value '" + value + "' in '" + locator + "'", e);
    }
  }

  /** Index starts at 0 (0 = the first option). */
  public void selectOptionByIndex(String locator, int index) {
    try {
      LOGGER.info("Select option with index '{}' in '{}'", index, locator);
      getSelect(locator).selectByIndex(index);
    } catch (Exception e) {
      handleError("Failed to select option with index " + index + " in '" + locator + "'", e);
    }
  }

  /** Only for dropdowns that allow multiple selections. */
  public void selectAllOptions(String locator) {
    try {
      LOGGER.info("Select all options in '{}'", locator);
      Select select = getMultiSelect(locator);
      int total = select.getOptions().size();
      for (int i = 0; i < total; i++) {
        select.selectByIndex(i);
      }
    } catch (Exception e) {
      handleError("Failed to select all options in '" + locator + "'", e);
    }
  }

  public void deselectOptionByText(String locator, String text) {
    try {
      LOGGER.info("Deselect option with text '{}' in '{}'", text, locator);
      getMultiSelect(locator).deselectByVisibleText(text);
    } catch (Exception e) {
      handleError("Failed to deselect option with text '" + text + "' in '" + locator + "'", e);
    }
  }

  public void deselectOptionByValue(String locator, String value) {
    try {
      LOGGER.info("Deselect option with value '{}' in '{}'", value, locator);
      getMultiSelect(locator).deselectByValue(value);
    } catch (Exception e) {
      handleError("Failed to deselect option with value '" + value + "' in '" + locator + "'", e);
    }
  }

  public void deselectOptionByIndex(String locator, int index) {
    try {
      LOGGER.info("Deselect option with index '{}' in '{}'", index, locator);
      getMultiSelect(locator).deselectByIndex(index);
    } catch (Exception e) {
      handleError("Failed to deselect option with index " + index + " in '" + locator + "'", e);
    }
  }

  public void deselectAllOptions(String locator) {
    try {
      LOGGER.info("Deselect all options in '{}'", locator);
      getMultiSelect(locator).deselectAll();
    } catch (Exception e) {
      handleError("Failed to deselect all options in '" + locator + "'", e);
    }
  }

  /** Shared by the verifyOption... keywords: checks one option is selected (or not selected). */
  private boolean checkOptionState(String locator, WebElement option, String description, boolean shouldBeSelected) {
    if (option == null) {
      LOGGER.error("Option with {} was not found in '{}'", description, locator);
      return false;
    }
    if (option.isSelected() == shouldBeSelected) {
      LOGGER.info("Option with {} in '{}' is {}, as expected", description, locator, shouldBeSelected ? "selected" : "not selected");
      return true;
    }
    LOGGER.error("Option with {} in '{}' should be {}", description, locator, shouldBeSelected ? "selected" : "not selected");
    return false;
  }

  public boolean verifyOptionSelectedByText(String locator, String text) {
    try {
      return checkOptionState(locator, findOptionByText(getSelect(locator), text), "text '" + text + "'", true);
    } catch (Exception e) {
      logError("Failed to verify option with text '" + text + "' in '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyOptionSelectedByValue(String locator, String value) {
    try {
      return checkOptionState(locator, findOptionByValue(getSelect(locator), value), "value '" + value + "'", true);
    } catch (Exception e) {
      logError("Failed to verify option with value '" + value + "' in '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyOptionSelectedByIndex(String locator, int index) {
    try {
      return checkOptionState(locator, findOptionByIndex(getSelect(locator), index), "index " + index, true);
    } catch (Exception e) {
      logError("Failed to verify option with index " + index + " in '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyOptionNotSelectedByText(String locator, String text) {
    try {
      return checkOptionState(locator, findOptionByText(getSelect(locator), text), "text '" + text + "'", false);
    } catch (Exception e) {
      logError("Failed to verify option with text '" + text + "' in '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyOptionNotSelectedByValue(String locator, String value) {
    try {
      return checkOptionState(locator, findOptionByValue(getSelect(locator), value), "value '" + value + "'", false);
    } catch (Exception e) {
      logError("Failed to verify option with value '" + value + "' in '" + locator + "'", e);
    }
    return false;
  }

  public boolean verifyOptionNotSelectedByIndex(String locator, int index) {
    try {
      return checkOptionState(locator, findOptionByIndex(getSelect(locator), index), "index " + index, false);
    } catch (Exception e) {
      logError("Failed to verify option with index " + index + " in '" + locator + "'", e);
    }
    return false;
  }

  /** For multi-select dropdowns: every option must be selected. */
  public boolean verifyAllOptionsSelected(String locator) {
    try {
      for (WebElement option : getMultiSelect(locator).getOptions()) {
        if (!option.isSelected()) {
          LOGGER.error("Option '{}' in '{}' is not selected", option.getText(), locator);
          return false;
        }
      }
      LOGGER.info("All options in '{}' are selected", locator);
      return true;
    } catch (Exception e) {
      logError("Failed to verify all options in '" + locator + "' are selected", e);
    }
    return false;
  }

  /** For multi-select dropdowns: no option may be selected. */
  public boolean verifyAllOptionsNotSelected(String locator) {
    try {
      for (WebElement option : getMultiSelect(locator).getOptions()) {
        if (option.isSelected()) {
          LOGGER.error("Option '{}' in '{}' is selected", option.getText(), locator);
          return false;
        }
      }
      LOGGER.info("No option in '{}' is selected", locator);
      return true;
    } catch (Exception e) {
      logError("Failed to verify no option in '" + locator + "' is selected", e);
    }
    return false;
  }

  /** The text of the option that is currently chosen. */
  public String getSelectedOptionText(String locator) {
    try {
      String text = getSelect(locator).getFirstSelectedOption().getText();
      LOGGER.info("Selected option in '{}' is '{}'", locator, text);
      return text;
    } catch (Exception e) {
      handleError("Failed to get selected option in '" + locator + "'", e);
    }
    return null;
  }

  /** The text of every option in the dropdown. */
  public List<String> getAllOptionTexts(String locator) {
    try {
      List<String> texts = new ArrayList<>();
      for (WebElement option : getSelect(locator).getOptions()) {
        texts.add(option.getText());
      }
      LOGGER.info("Options in '{}' are {}", locator, texts);
      return texts;
    } catch (Exception e) {
      handleError("Failed to get options of '" + locator + "'", e);
    }
    return null;
  }

  public boolean verifyDropdownContainsOption(String locator, String optionText) {
    List<String> texts = getAllOptionTexts(locator);
    if (texts != null && texts.contains(optionText)) {
      LOGGER.info("Dropdown '{}' contains option '{}'", locator, optionText);
      return true;
    }
    LOGGER.error("Dropdown '{}' does not contain option '{}'", locator, optionText);
    return false;
  }

  // ===========================================================================================
  // 17. ALERTS (the browser's popup boxes: OK / Cancel dialogs)
  // ===========================================================================================

  /** Waits until an alert appears, then returns it. */
  private Alert getAlert() {
    return getWait(defaultTimeout).until(ExpectedConditions.alertIsPresent());
  }

  public void acceptAlert() {
    try {
      LOGGER.info("Accept the alert");
      getAlert().accept();
    } catch (Exception e) {
      handleError("Failed to accept the alert", e);
    }
  }

  public void dismissAlert() {
    try {
      LOGGER.info("Dismiss the alert");
      getAlert().dismiss();
    } catch (Exception e) {
      handleError("Failed to dismiss the alert", e);
    }
  }

  public String getAlertText() {
    try {
      String text = getAlert().getText();
      LOGGER.info("Alert text is '{}'", text);
      return text;
    } catch (Exception e) {
      handleError("Failed to get the alert text", e);
    }
    return null;
  }

  /** Reads the alert text and then clicks OK. */
  public String getAlertTextThenAcceptAlert() {
    try {
      Alert alert = getAlert();
      String text = alert.getText();
      alert.accept();
      LOGGER.info("Alert text was '{}', alert accepted", text);
      return text;
    } catch (Exception e) {
      handleError("Failed to get the alert text and accept it", e);
    }
    return null;
  }

  /** Types text into a "prompt" alert. */
  public void sendTextToAlert(String text) {
    try {
      LOGGER.info("Send text '{}' to the alert", text);
      getAlert().sendKeys(text);
    } catch (Exception e) {
      handleError("Failed to send text to the alert", e);
    }
  }

  public boolean verifyAlertText(String expectedText) {
    String actualText = getAlertText();
    if (StringUtils.equals(actualText, expectedText)) {
      LOGGER.info("Alert text is '{}', as expected", expectedText);
      return true;
    }
    LOGGER.error("Alert text is '{}' but expected '{}'", actualText, expectedText);
    return false;
  }

  public boolean verifyAlertPresent() {
    return waitForAlertPresent();
  }

  /** Waits until an alert appears. */
  public boolean waitForAlertPresent() {
    try {
      LOGGER.info("Wait for an alert");
      getAlert();
      return true;
    } catch (Exception e) {
      logError("No alert appeared", e);
    }
    return false;
  }

  /** Quick check (no waiting): there is no alert on screen. */
  public boolean verifyAlertNotPresent() {
    try {
      driver.switchTo().alert();
      LOGGER.error("An alert is present");
      return false;
    } catch (NoAlertPresentException e) {
      LOGGER.info("No alert is present, as expected");
      return true;
    }
  }

  // ===========================================================================================
  // 18. FRAMES (a page inside a page - <iframe>). Switch INTO the frame before using its elements,
  //     and switch back out with switchToDefaultContent() when you are done.
  // ===========================================================================================

  public void switchToFrame(String locator) {
    try {
      LOGGER.info("Switch to frame '{}'", locator);
      driver.switchTo().frame(getElement(locator));
    } catch (Exception e) {
      handleError("Failed to switch to frame '" + locator + "'", e);
    }
  }

  public void switchToFrameByIndex(int index) {
    try {
      LOGGER.info("Switch to frame with index {}", index);
      driver.switchTo().frame(index);
    } catch (Exception e) {
      handleError("Failed to switch to frame with index " + index, e);
    }
  }

  public void switchToFrameByNameOrId(String nameOrId) {
    try {
      LOGGER.info("Switch to frame '{}'", nameOrId);
      driver.switchTo().frame(nameOrId);
    } catch (Exception e) {
      handleError("Failed to switch to frame '" + nameOrId + "'", e);
    }
  }

  /** Goes back to the main page (out of every frame). */
  public void switchToDefaultContent() {
    try {
      LOGGER.info("Switch to the main page");
      driver.switchTo().defaultContent();
    } catch (Exception e) {
      handleError("Failed to switch to the main page", e);
    }
  }

  /** Goes up one level (out of the current frame only). */
  public void switchToParentFrame() {
    try {
      LOGGER.info("Switch to the parent frame");
      driver.switchTo().parentFrame();
    } catch (Exception e) {
      handleError("Failed to switch to the parent frame", e);
    }
  }

  // ===========================================================================================
  // 19. HTML TABLES - row and column numbers start at 1. Row 1 = the first row of the table (the header).
  // ===========================================================================================

  public int getTableRowCount(String tableLocator) {
    try {
      int count = getElement(tableLocator).findElements(By.xpath(".//tr")).size();
      LOGGER.info("Table '{}' has {} row(s)", tableLocator, count);
      return count;
    } catch (Exception e) {
      handleError("Failed to get the row count of table '" + tableLocator + "'", e);
    }
    return -1;
  }

  public String getTableCellText(String tableLocator, int row, int column) {
    try {
      String cellXpath = "(.//tr)[" + row + "]/*[self::td or self::th][" + column + "]";
      String text = getElement(tableLocator).findElement(By.xpath(cellXpath)).getText();
      LOGGER.info("Cell ({}, {}) of table '{}' is '{}'", row, column, tableLocator, text);
      return text;
    } catch (Exception e) {
      handleError("Failed to get cell (" + row + ", " + column + ") of table '" + tableLocator + "'", e);
    }
    return null;
  }

  public boolean verifyTableCellText(String tableLocator, int row, int column, String expectedText) {
    String actualText = getTableCellText(tableLocator, row, column);
    if (StringUtils.equals(actualText, expectedText)) {
      return true;
    }
    LOGGER.error("Cell ({}, {}) of table '{}' is '{}' but expected '{}'", row, column, tableLocator, actualText, expectedText);
    return false;
  }

  // ===========================================================================================
  // 20. COOKIES
  // ===========================================================================================

  public void addCookie(String name, String value) {
    try {
      LOGGER.info("Add cookie '{}'", name);
      driver.manage().addCookie(new Cookie(name, value));
    } catch (Exception e) {
      handleError("Failed to add cookie '" + name + "'", e);
    }
  }

  /** Returns the cookie value, or null if the cookie does not exist. */
  public String getCookieValue(String name) {
    try {
      Cookie cookie = driver.manage().getCookieNamed(name);
      if (cookie == null) {
        LOGGER.info("Cookie '{}' does not exist", name);
        return null;
      }
      return cookie.getValue();
    } catch (Exception e) {
      handleError("Failed to get cookie '" + name + "'", e);
    }
    return null;
  }

  public boolean verifyCookieExists(String name) {
    try {
      return driver.manage().getCookieNamed(name) != null;
    } catch (Exception e) {
      logError("Failed to check cookie '" + name + "'", e);
    }
    return false;
  }

  public void deleteCookie(String name) {
    try {
      LOGGER.info("Delete cookie '{}'", name);
      driver.manage().deleteCookieNamed(name);
    } catch (Exception e) {
      handleError("Failed to delete cookie '" + name + "'", e);
    }
  }

  public void deleteAllCookies() {
    try {
      LOGGER.info("Delete all cookies");
      driver.manage().deleteAllCookies();
    } catch (Exception e) {
      handleError("Failed to delete all cookies", e);
    }
  }

  // ===========================================================================================
  // 21. SCREENSHOTS AND DOWNLOADS
  //     @Attachment = the picture is added to the Allure report automatically.
  // ===========================================================================================

  /** Takes a picture of the browser as bytes (no report attachment). */
  private byte[] getScreenshotBytes() {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }

  @Attachment(value = "Page screenshot", type = "image/png")
  public byte[] takeScreenshot() {
    return getScreenshotBytes();
  }

  @Attachment(value = "Element screenshot", type = "image/png")
  public byte[] takeScreenshotOfElement(String locator) {
    try {
      LOGGER.info("Take screenshot of '{}'", locator);
      return getElement(locator).getScreenshotAs(OutputType.BYTES);
    } catch (Exception e) {
      logError("Failed to take screenshot of '" + locator + "'", e);
    }
    return null;
  }

  /** Saves a screenshot as a file. Example path: target/screenshots/login.png */
  public boolean saveScreenshotToFile(String filePath) {
    try {
      LOGGER.info("Save screenshot to '{}'", filePath);
      File file = new File(filePath);
      if (file.getParentFile() != null) {
        file.getParentFile().mkdirs(); // create the folder if it does not exist
      }
      Files.write(file.toPath(), getScreenshotBytes());
      return true;
    } catch (Exception e) {
      logError("Failed to save screenshot to '" + filePath + "'", e);
    }
    return false;
  }

  @Attachment(value = "Page screenshot with highlighted element", type = "image/png")
  public byte[] takeScreenshotWithHighlight(String locator) {
    return takeHighlightedScreenshot(findWebElement(locator));
  }

  @Attachment(value = "Page screenshot with highlighted element", type = "image/png")
  public byte[] takeScreenshotWithHighlight(WebElement element) {
    return takeHighlightedScreenshot(element);
  }

  /** Draws a red border around the element, takes the picture, then removes the border again. */
  private byte[] takeHighlightedScreenshot(WebElement element) {
    if (element == null) {
      LOGGER.error("Element not found, taking a normal screenshot without highlight");
      return getScreenshotBytes();
    }
    String originalStyle = null;
    try {
      LOGGER.info("Take screenshot with highlighted element");
      originalStyle = element.getAttribute("style");
      getJs().executeScript("arguments[0].style.border='4px solid red';", element);
      return getScreenshotBytes();
    } catch (Exception e) {
      logError("Failed to take screenshot with highlighted element", e);
    } finally {
      // "finally" always runs: put the element's style back how it was
      try {
        if (originalStyle == null) {
          getJs().executeScript("arguments[0].removeAttribute('style');", element);
        } else {
          getJs().executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
        }
      } catch (Exception ignored) {
        // The page may have changed already - nothing to restore
      }
    }
    return null;
  }

  /**
   * Waits until a file appears in the download folder and is fully downloaded.
   * (Set the browser's download folder yourself. With a remote browser the file is saved on the grid machine.)
   */
  public boolean waitForFileDownloaded(String folder, String fileName) {
    try {
      LOGGER.info("Wait for file '{}' in folder '{}'", fileName, folder);
      File file = new File(folder, fileName);
      long endTime = System.currentTimeMillis() + defaultTimeout * 1000L;
      while (System.currentTimeMillis() < endTime) {
        // While Chrome/Firefox download, they use temporary files with these endings
        boolean stillDownloading = false;
        File[] filesInFolder = new File(folder).listFiles();
        if (filesInFolder != null) {
          for (File f : filesInFolder) {
            String name = f.getName();
            if (name.endsWith(".crdownload") || name.endsWith(".part") || name.endsWith(".tmp")) {
              stillDownloading = true;
            }
          }
        }
        if (file.isFile() && file.length() > 0 && !stillDownloading) {
          LOGGER.info("File '{}' is downloaded", file.getAbsolutePath());
          return true;
        }
        delayInMilliSeconds(500);
      }
      LOGGER.error("File '{}' was not downloaded within {}s", fileName, defaultTimeout);
    } catch (Exception e) {
      logError("Failed while waiting for file '" + fileName + "'", e);
    }
    return false;
  }

  // ===========================================================================================
  // 22. DATE FIELD
  // ===========================================================================================

  /**
   * Converts a date to the format "yyyy-MM-dd" (the only format an HTML date input accepts).
   * Understands: 01-Dec-2026, 01-12-2026, 01/12/2026, 2026-12-01
   */
  private String toIsoDate(String date) {
    String[] patterns = {"dd-MMM-yyyy", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd"};
    for (String pattern : patterns) {
      try {
        LocalDate parsed = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
        return parsed.format(DateTimeFormatter.ISO_LOCAL_DATE);
      } catch (DateTimeParseException ignored) {
        // This pattern does not fit - try the next one
      }
    }
    throw new IllegalArgumentException("Date '" + date + "' is not in a supported format");
  }

  /** Sets the value of an <input type="date"> field using JavaScript. */
  public void setDateByJS(String locator, String date) {
    try {
      LOGGER.info("Set date '{}' into '{}'", date, locator);
      setValueByJS(getElement(locator), toIsoDate(date));
    } catch (Exception e) {
      handleError("Failed to set date '" + date + "' into '" + locator + "'", e);
    }
  }

  /** Old name, kept so existing tests still work. Does the same as setDateByJS. */
  public void setDateForDTPWithInputTagByJS(String locator, String dateOfBirth) {
    setDateByJS(locator, dateOfBirth);
  }
}