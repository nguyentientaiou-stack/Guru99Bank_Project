package Guru99;

import com.jayway.jsonpath.JsonPath;
import common.keywords.WebUI;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;
import pages.AccessDetails_Page;
import pages.CustomerRegMsq_Page;
import pages.DeleteCustomer_Page;
import pages.EditCustomer_Page;
import pages.Login_Page;
import pages.Manager_Page;
import pages.NewCustomer_Page;
import pages.Register_Page;
import utils.FileHelper;

public class BaseTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

  private final static String TEST_DATA_FOLDER = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test_data";
  public final static String ALLURE_RESULTS = System.getProperty("user.dir") + File.separator + "target" + File.separator + "allure-results";

  private String data;

  public void setData(String dataFile) {
    this.data = TEST_DATA_FOLDER + File.separator + dataFile + ".json";
  }

  public String getData() {
    return data;
  }

  public String findTestData(String testDataName) {
    File file = new File(getData());
    try {
      String dataValue = JsonPath.read(file, "$." + testDataName).toString();
      return dataValue;
    } catch (Exception e) {
      LOGGER.error("Failed to find test object '{}' in '{}'. Root cause: {}", testDataName, getData(), e.getMessage());
    }
    return null;
  }

  public static String formatDate(LocalDate date, String pattern) {
    if (date == null)
      return "";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return date.format(formatter);
  }

  //
  WebUI webUI = new WebUI();
  Faker faker = new Faker();
  FileHelper fileHelper = new FileHelper();
  SoftAssert softAssert = new SoftAssert();
  Login_Page loginPage = new Login_Page(webUI);
  Register_Page registerPage = new Register_Page(webUI);
  Manager_Page managerPage = new Manager_Page(webUI);
  AccessDetails_Page accessDetailsPage = new AccessDetails_Page(webUI);
  NewCustomer_Page newCustomerPage = new NewCustomer_Page(webUI);
  CustomerRegMsq_Page customerRegMsqPage = new CustomerRegMsq_Page(webUI);
  EditCustomer_Page editCustomerPage = new EditCustomer_Page(webUI);
  DeleteCustomer_Page deleteCustomerPage = new DeleteCustomer_Page(webUI);

  //
  public final String LOGIN_PAGE_URL = "https://demo.guru99.com/V4/index.php";
  public final String CUSTOMER_REG_MSG_PAGE_URL = "https://demo.guru99.com/V4/manager/CustomerRegMsg.php";
  public final String MANAGER_PAGE_URL = "https://demo.guru99.com/V4/manager/Managerhomepage.php";

}
