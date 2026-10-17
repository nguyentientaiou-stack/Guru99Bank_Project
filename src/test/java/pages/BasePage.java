package pages;

import com.jayway.jsonpath.JsonPath;
import common.keywords.WebUI;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasePage {

  private final static String OBJECTS_REPO_FOLDER = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "objects_repository";
  private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
  private String repo;

  public WebUI webUI;

  public BasePage(WebUI webUI) {
    this.webUI = webUI;
  }

  public void setRepo(String repoFile)
  {
    this.repo = OBJECTS_REPO_FOLDER + File.separator + repoFile + ".json";
  }

  private String getRepo()
  {
    return repo;
  }

  public String findTestObject(String testObjectName)
  {
    File file = new File(getRepo());
    try {
      String locatorValue = JsonPath.read(file, "$." + testObjectName).toString();
      LOGGER.info(locatorValue);
      return locatorValue;
    } catch (Exception e)
    {
      LOGGER.error("Failed to find test object '{}' in '{}'. Root cause: {}", testObjectName, getRepo(), e.getMessage());
    }
    return null;
  }

  public String reformatDate(String inputDate)
  {
    try
    {
      DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
      LocalDate date = LocalDate.parse(inputDate, inputFormatter);

      DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
      String formattedDate = date.format(outputFormatter);

      LOGGER.info("Formatted date from '{}' to '{}'", inputDate, formattedDate);
      return formattedDate;
    }
    catch (Exception e)
    {
      LOGGER.error("Failed to format date '{}'. Root cause: {}", inputDate, e.getMessage());
    }
    return null;
  }
}
