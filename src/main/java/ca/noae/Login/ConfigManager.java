package ca.noae.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import ca.noae.Objects.CodeElements.Generated;

public final class ConfigManager {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  @Generated({"Utility class cannot be instantiated"})
  private ConfigManager() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /** The Scanner object to use for input. */
  private static Scanner scanner;

  /** The Properties object to use for loading properties. */
  private static Properties props;

  /**
   * Initializes the ConfigManager by setting the scanner to be used for input and
   * loading properties from the specified file.
   *
   * @param initScanner the Scanner object to use for input
   * @param fileName    the name of the file to load properties from
   * @throws IOException if an I/O error occurs while loading the properties
   */
  public static void initConfigManager(final Scanner initScanner, final String fileName) {
    ConfigManager.scanner = initScanner;
    ConfigManager.props = new Properties();
    try (FileInputStream fis = new FileInputStream(fileName)) {
      props.load(fis);
    } catch (IOException e) {
      props = null;
    }
  }

  /**
   * Retrieves a property or a query value either from the loaded properties or by
   * prompting the user to enter a value for the specified property.
   *
   * @param prop the name of the property or query value to retrieve
   * @return the value of the specified property or query
   */
  public static String getPropOrQuery(final String prop) {
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

  /**
   * Retrieves a property or a query value either from the loaded properties or by
   * prompting the user to enter a value for the specified property.
   *
   * @param prop    the name of the property or query value to retrieve
   * @param message the message to display to the user when prompting for the
   *                property value
   * @return the value of the specified property or query
   */
  public static String getPropOrQuery(final String prop, final String message) {
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
