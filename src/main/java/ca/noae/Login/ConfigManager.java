package ca.noae.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class ConfigManager {

  private static Scanner scanner;
  private static Properties props;
  private static String fileName = "app.config";

  public ConfigManager(Scanner scanner, Properties props, String fileName) throws IOException {
    ConfigManager.scanner = scanner;
    ConfigManager.props = props;
    ConfigManager.fileName = fileName;
    FileInputStream fis = new FileInputStream(fileName);
    props.load(fis);
  }

  public static String getPropOrQuery(String prop) { // TODO : Implement this method
    String configOrQuery = props.getProperty(prop);
    System.out.print(
      configOrQuery == null ? "Enter your " + prop + " :" : "Loading " + prop + " from config file... \n");
    if (configOrQuery == null) {
      configOrQuery = scanner.nextLine();
    }

    return configOrQuery;
  }

  public static String getPropOrQuery(String prop, String message) { // TODO : Implement this method
    String configOrQuery = props.getProperty(prop);
    System.out.print(
      configOrQuery == null ? message : "Loading " + prop + " from config file... \n");
    if (configOrQuery == null) {
      configOrQuery = scanner.nextLine();
      if (configOrQuery.equals("-1")) {
        System.exit(0);
      }
    }

    return configOrQuery;
  }
}
