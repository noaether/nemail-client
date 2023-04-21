package ca.noae.Login;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class ConfigManagerTest {

  /** The name of the test properties file. */
  private static final String TEST_FILE_NAME = "test.properties";

  /** The name of the test property. */
  private static final String TEST_PROP_NAME = "test.prop";

  /** The value of the test property. */
  private static final String TEST_PROP_VALUE = "test value";

  /**
   * Sets up the test environment.
   *
   * @throws FileNotFoundException if the test file cannot be created
   */
  @BeforeEach
  void setUp() throws FileNotFoundException {
    OutputStream output = new FileOutputStream(TEST_FILE_NAME);
    Properties prop = new Properties();
    prop.setProperty(TEST_PROP_NAME, TEST_PROP_VALUE);
    try {
      prop.store(output, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tears down the test environment.
   */
  @AfterEach
  void tearDown() {
    // Delete the test properties file
    File testFile = new File(TEST_FILE_NAME);
    testFile.delete();
  }

  /**
   * Tests the {@link ConfigManager#initConfigManager(Scanner, String)} method.
   *
   * Tests that the method loads properties from the test file.
   */
  @Test
  void testInitConfigManager() {
    // Test that initConfigManager loads properties from the test file
    Scanner scanner = new Scanner(System.in);
    ConfigManager.initConfigManager(scanner, TEST_FILE_NAME);
    assertEquals(TEST_PROP_VALUE, ConfigManager.getPropOrQuery(TEST_PROP_NAME));
  }

  /**
   * Tests the {@link ConfigManager#getPropOrQuery(String)} method.
   *
   * Tests that the method returns a loaded property.
   */
  @Test
  void testGetPropOrQueryWhenPropertiesLoaded() {
    // Test that getPropOrQuery returns a loaded property
    Scanner scanner = new Scanner(System.in);
    ConfigManager.initConfigManager(scanner, TEST_FILE_NAME);
    assertEquals(TEST_PROP_VALUE, ConfigManager.getPropOrQuery(TEST_PROP_NAME));
  }

  /**
   * Tests the {@link ConfigManager#getPropOrQuery(String)} method.
   *
   * Tests that the method prompts the user for input when properties are not loaded.
   */
  @Test
  void testGetPropOrQueryWhenPropertiesNotLoaded() {
    // Test that getPropOrQuery prompts the user for input when properties are not
    // loaded
    Scanner scanner = new Scanner(new ByteArrayInputStream("test input".getBytes()));
    ConfigManager.initConfigManager(scanner, "non-existent-file");
    assertEquals("test input", ConfigManager.getPropOrQuery(TEST_PROP_NAME));
  }

  /**
   * Tests the {@link ConfigManager#getPropOrQuery(String, String)} method.
   *
   * Tests that the method returns a loaded property.
   */
  @Test
  void testGetPropOrQueryWithMessageWhenPropertiesLoaded() {
    // Test that getPropOrQuery returns a loaded property
    Scanner scanner = new Scanner(System.in);
    ConfigManager.initConfigManager(scanner, TEST_FILE_NAME);
    assertEquals(TEST_PROP_VALUE, ConfigManager.getPropOrQuery(TEST_PROP_NAME, "Test message"));
  }

  /**
   * Tests the {@link ConfigManager#getPropOrQuery(String, String)} method.
   *
   * Tests that the method prompts the user for input when properties are not loaded.
   */
  @Test
  void testGetPropOrQueryWithMessageWhenPropertiesNotLoaded() {
    Scanner scanner = new Scanner(new ByteArrayInputStream("test input".getBytes()));
    ConfigManager.initConfigManager(scanner, "non-existent-file");
    assertEquals("test input", ConfigManager.getPropOrQuery(TEST_PROP_NAME, "Test message"));
  }
}
