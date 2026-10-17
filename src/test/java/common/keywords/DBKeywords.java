package common.keywords;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBKeywords {

  private static final Logger LOGGER = LoggerFactory.getLogger(DBKeywords.class);

  private String getConnectionUrl(DBTypes dbType, String server, String port, String dbName)
  {
    LOGGER.info("Getting database connection url...");
    String url;
    switch (dbType) {
      case MYSQL -> {
        url = "jdbc:mysql://" + server + ":" + port + "/" + dbName;
        LOGGER.info("MySQL database connection URL is {}: ", url);
        return url;

      }
      case POSTGRES ->  {
        url = "jdbc:postgresql://" + server + ":" + port + "/" + dbName;
        LOGGER.info(" Postgress database connection URL is {}: ", url);
        return url;

      }
      case ORACLE -> {
        url = "jdbc:oracle:thin:@//" + server + ":" + port + "/" + dbName;
        LOGGER.info("Oracle database connection URL is {}: ", url);
        return url;

      }
      case SQLSERVER ->  {
        url = "jdbc:sqlserver://" + server + ":" + port + "/" + dbName;
        LOGGER.info("SQLServer database connection URL is {}: ", url);
        return url;

      }
      default -> {return "";}
    }
  }

  public Connection createConnection(DBTypes dbType, String server, String port, String dbName, String username, String password)
  {
    String url = getConnectionUrl(dbType, server, port, dbName);
    Connection connection = null;
    try{
      LOGGER.info("Connecting to database {} ...", url);
      connection = DriverManager.getConnection(url, username, password);
      if (connection != null)
      {
        LOGGER.info("Successfully connected to database {} ", url);
        return connection;
      }
    }
    catch(Exception e){
    LOGGER.error("Failed to create connection to database. Root cause: {}", e.getMessage());
    }

    return connection;
  }

  public void closeConnection(Connection connection)
  {
    try{
      LOGGER.info("Closing connection...");
      if (connection != null && !connection.isClosed())
      {
        connection.close();
        LOGGER.info("Successfully closed connection");
      }
      else {
        LOGGER.warn("Connection already closed");
      }
    }
    catch(Exception e){
      LOGGER.error("Failed to close connection to database. Root cause: {}", e.getMessage());
    }
  }

  public ResultSet executeQuery(Connection connection, String SQLQuery)
  {
    ResultSet resultSet = null;
    try{
      LOGGER.info("Executing query {}...", SQLQuery);
      Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      resultSet = statement.executeQuery(SQLQuery);
      LOGGER.info("Successfully executed query {} ", SQLQuery);
    }
    catch (Exception e){
      LOGGER.error("Failed to execute query '{}'. Root cause: {}", SQLQuery ,e.getMessage());
    }

    return resultSet;
  }

  public List<String> getCellValues(ResultSet resultSet, String columnName)
  {
    List<String> cellValues = new ArrayList<>();
    LOGGER.info("Getting cell values from column {} ...", columnName);
    try{
      resultSet.beforeFirst();
      while (resultSet.next())
      {
        cellValues.add(resultSet.getString(columnName));
      }
      LOGGER.info("Successfully getting cell values from column {} ", columnName);
    }
    catch (Exception e){
      LOGGER.error("Failed to get cell values from column {}. Root cause: {}", columnName, e.getMessage());
    }
    return cellValues;
  }

  public String getStringCellValue(ResultSet resultSet, int rowIndex, String columnName)
  {
    String cellValue = null;
    try{
      LOGGER.info("Retrieve string cell value at row '{}' and column '{}'...", rowIndex, columnName);
      resultSet.absolute(rowIndex);
      cellValue = resultSet.getString(columnName);
    } catch (Exception e) {
      LOGGER.info("Failed to retrieve string cell value at row '{}', and column '{}'. Root cause: {}", rowIndex, columnName, e.getMessage());
    }
    return cellValue;
  }
}
