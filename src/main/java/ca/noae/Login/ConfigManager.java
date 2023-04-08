package ca.noae.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class ConfigManager {

  private static Scanner scanner;
  private static Properties props;

  public ConfigManager(Scanner scanner, String fileName) {
    ConfigManager.scanner = scanner;
    ConfigManager.props = new Properties();
    try (FileInputStream fis = new FileInputStream(fileName)) {
      props.load(fis);
    } catch (IOException e) {
      props = null;
    }
  }

  public static String getPropOrQuery(String prop) { // TODO : Implement this method
    if (props == null) {
      System.out.print("Enter your " + prop + ": ");
      return scanner.nextLine();
    } else {
      String configOrQuery = props.getProperty(prop);
      System.out.print(
          configOrQuery == null ? "Enter your " + prop + ": " : "Loading " + prop + " from config file... \n");
      if (configOrQuery == null) {
        return scanner.nextLine();
      }
      return configOrQuery;
    }
  }

  public static String getPropOrQuery(String prop, String message) { // TODO : Implement this method
    if (props == null) {
      System.out.print(message);
      return scanner.nextLine();
    } else {
      String configOrQuery = props.getProperty(prop);
      System.out.print(
          configOrQuery == null ? message : "Loading " + prop + " from config file... \n");
      if (configOrQuery == null) {
        return scanner.nextLine();
      }
      return configOrQuery;
    }
  }
}
