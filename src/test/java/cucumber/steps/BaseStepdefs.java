package cucumber.steps;

import common.keywords.WebUI;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.CustomerRegMsq_Page;
import pages.Login_Page;
import pages.Manager_Page;
import pages.NewCustomer_Page;

public class BaseStepdefs {

  private final static String OBJECTS_REPO_FOLDER =
      System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "objects_repository";
  public static final Logger LOGGER = LoggerFactory.getLogger(BaseStepdefs.class);

  private String repo;

  public static WebUI webUI = new WebUI();
  public static Login_Page loginPage = new Login_Page(webUI);
  public static Manager_Page mangerPage = new Manager_Page(webUI);
  public static NewCustomer_Page newCustomerPage = new NewCustomer_Page(webUI);
  public static CustomerRegMsq_Page customerRegMsqPage = new CustomerRegMsq_Page(webUI);
  public static String userName = "mngr668177 ";
  public static String password = "huhUvEr";


  public void setRepo(String repoFile) {
    this.repo = OBJECTS_REPO_FOLDER + File.separator + repoFile + ".json";
  }

  private String getRepo() {
    return repo;
  }
}
