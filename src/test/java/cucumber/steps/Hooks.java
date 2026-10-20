package cucumber.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks extends BaseStepdefs {
  @Before
  public void beforeScenario(Scenario scenario ) {
    LOGGER.info("====================== Before Scenario: {} ======================", scenario.getName());
  }

  @After
  public void afterScenario(Scenario scenario) {
    LOGGER.info("====================== After Scenario: {} ======================", scenario.getName());
    webUI.closeBrowser();
  }
}
