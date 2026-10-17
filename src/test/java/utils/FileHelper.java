package utils;

import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
  public final static String ALLURE_RESULTS = System.getProperty("user.dir") + File.separator + "target" + File.separator + "allure-results";

  public void deleteFolder(String folderPath)
  {
    Path path = Paths.get(folderPath);
    if(!Files.exists(path))
    {
      return;
    }
    LOGGER.info("Deleting files in folder {}", folderPath);
    try{
      Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      LOGGER.info("Deleted files in folder {}", folderPath);
    } catch (Exception e) {
      LOGGER.error("Failed to delete files in folder {}", folderPath);
    }
  }

  public void writeDataToJsonFile(String filePath, String key, String value)
  {
    ObjectMapper mapper = new ObjectMapper();
    File jsonFile = new File(filePath);
    ObjectNode jsonNode;
    try
    {
      // 1. If the file exists and is not empty, read the existing data.
      if (jsonFile.exists() && jsonFile.length() > 0)
      {
        jsonNode = (ObjectNode) mapper.readTree(jsonFile);
      }
      else
      {
        // If the file does not exist, create a new empty JSON object.
        jsonNode = mapper.createObjectNode();
      }
      // 2. Add or update key-value
      jsonNode.put(key, value);
      // 3. Write data to a file in a pretty-printed format.
      mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonNode);
      LOGGER.info("Successfully wrote key '{}' with value '{}' to file '{}'", key, value, filePath);
    }
    catch (Exception e)
    {
      LOGGER.error("Failed to write key '{}' to JSON file '{}'. Root cause: {}", key, filePath, e.getMessage());
    }
  }
}
