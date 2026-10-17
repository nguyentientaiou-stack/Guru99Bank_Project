package utils;

import com.jayway.jsonpath.JsonPath;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openqa.selenium.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// reads and saves the login user in src/test/resources/test_data/RegisteredUser.json
// Guru99 demo credentials are valid for 20 days only
public class RegisterUserHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserHelper.class);;
  private final static int VALID_DAYS = 20;

  public static final String USER_FILE =
      System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
          + File.separator + "resources" + File.separator + "test_data" + File.separator
          + "RegisteredUser.json";

  public static String getUserId() {
    return read("userId");
  }
  public static String getPassword() {
    return read("password");
  }

  private static String read(String key) {
    File file = new File(USER_FILE);
    if (!file.exists()) {
      throw new IllegalStateException("Registered user file " + USER_FILE
          + " not found. Run: mvn clean test -Dtestsuite=RegisterUserTestSuite.xml");
    }
    try {
      return JsonPath.read(file, "$." + key).toString();
    } catch (Exception e) {
      throw new IllegalStateException(
          "Failed to read '" + key + "' from " + USER_FILE + ". Root cause: " + e.getMessage());
    }
  }

  // the saved user is valid when the file exists and today is before its expired date
  public static boolean isValid() {
    if (!new File(USER_FILE).exists()) {
      LOGGER.info("No saved user found at {}", USER_FILE);
      return false;
    }
    try {
      LocalDate expiredDate = LocalDate.parse(read("expiredDate"));
      LOGGER.info("Saved user expires on {}", expiredDate);
      return LocalDate.now().isBefore(expiredDate);
    } catch (Exception e) {
      LOGGER.error("Failed to read expired date from {}. Root cause: {}", USER_FILE, e.getMessage());
      return false;
    }
  }

  public static void save(String userId, String password, String email) {
    LocalDate today = LocalDate.now();
    Map<String, String> user = new LinkedHashMap<>();
    user.put("userId", userId);
    user.put("password", password);
    user.put("email", email);
    user.put("createdAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    user.put("createdDate", today.toString());
    user.put("expiredDate", today.plusDays(VALID_DAYS).toString());
    writeJsonFile(USER_FILE, user);
    LOGGER.info("Saved user '{}' with email '{}', valid until '{}'", userId, email, user.get("expiredDate"));
  }

  // write data as JSON into a file, creating parent folders and overwriting the file if it exists
  public static void writeJsonFile(String filePath, Map<String, ?> data) {
    Path path = Paths.get(filePath);
    LOGGER.info("Writing JSON data to file {}", filePath);
    try {
      if (path.getParent() != null) {
        Files.createDirectories(path.getParent());
      }
      Files.writeString(path, new Json().toJson(data));
      LOGGER.info("Wrote JSON data to file {}", filePath);
    } catch (Exception e) {
      LOGGER.error("Failed to write JSON data to file {}. Root cause: {}", filePath, e.getMessage());
    }
  }
}
