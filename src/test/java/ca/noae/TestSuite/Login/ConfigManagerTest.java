package ca.noae.TestSuite.Login;

import org.junit.jupiter.api.*;

import ca.noae.Login.ConfigManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {

  private static final String TEST_FILE_NAME = "test.properties";
  private static final String TEST_PROP_NAME = "test.prop";
  private static final String TEST_PROP_VALUE = "test value";

  @BeforeEach
  void setUp() throws FileNotFoundException {
    // Create a test properties file with a test property
    Properties props = new Properties();
    props.setProperty(TEST_PROP_NAME, TEST_PROP_VALUE);
    OutputStream outputStream = new FileOutputStream("config.xml");
    try {
      props.storeToXML(outputStream, props.toString());
    } catch (IOException e) {
      fail("Failed to create test properties file");
    }
  }

  @AfterEach
  void tearDown() {
    // Delete the test properties file
    File testFile = new File(TEST_FILE_NAME);
    testFile.delete();
  }

  /* @Test
  void testInitConfigManager() {
    // Test that initConfigManager loads properties from the test file
    Scanner scanner = new Scanner(System.in);
    ConfigManager.initConfigManager(scanner, TEST_FILE_NAME);
    assertEquals(TEST_PROP_VALUE, ConfigManager.getPropOrQuery(TEST_PROP_NAME));
  }

  @Test
  void testGetPropOrQueryWhenPropertiesLoaded() {
    // Test that getPropOrQuery returns a loaded property
    Scanner scanner = new Scanner(System.in);
    ConfigManager.initConfigManager(scanner, TEST_FILE_NAME);
    assertEquals(TEST_PROP_VALUE, ConfigManager.getPropOrQuery(TEST_PROP_NAME));
  } */

  @Test
  void testGetPropOrQueryWhenPropertiesNotLoaded() {
    // Test that getPropOrQuery prompts the user for input when properties are not
    // loaded
    Scanner scanner = new Scanner(new ByteArrayInputStream("test input".getBytes()));
    ConfigManager.initConfigManager(scanner, "non-existent-file");
    assertEquals("test input", ConfigManager.getPropOrQuery(TEST_PROP_NAME));
  }

  /* @Test
  void testGetPropOrQueryWithMessageWhenPropertiesLoaded() {
    // Test that getPropOrQuery returns a loaded property
    Scanner scanner = new Scanner(System.in);
    ConfigManager.initConfigManager(scanner, TEST_FILE_NAME);
    assertEquals(TEST_PROP_VALUE, ConfigManager.getPropOrQuery(TEST_PROP_NAME, "Test message"));
  }

  @Test
  void testGetPropOrQueryWithMessageWhenPropertiesNotLoaded() {
    // Test that getPropOrQuery prompts the user for input when properties are not
    // loaded
    Scanner scanner = new Scanner(new ByteArrayInputStream("test input".getBytes()));
    ConfigManager.initConfigManager(scanner, "non-existent-file");
    assertEquals("test input", ConfigManager.getPropOrQuery(TEST_PROP_NAME, "Test message"));
  } */
}
