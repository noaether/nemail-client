package ca.noae.TestSuite.User;

import org.junit.jupiter.api.Test;

import ca.noae.User.ConsoleUI;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleUITest {

  /**
   * Tests the {@link ConsoleUI#clearScreen()} method.
   */
  @Test
  public void testClearScreen() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    ConsoleUI.clearScreen();

    assertEquals("\033[H\033[2J", outContent.toString());
  }

  /**
   * Tests the {@link ConsoleUI#createTable(String[], String[][])} method.
   */
  @Test
  public void testCreateTable() {
    @SuppressWarnings("checkstyle:nowhitespaceafter")
    String[] columnNames = { "ID", "Name", "Age" };

    @SuppressWarnings("checkstyle:nowhitespaceafter")
    String[][] data = {
        { "1", "John", "25" },
        { "2", "Jane", "30" },
        { "3", "Bob", "40" }
    };

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    ConsoleUI.createTable(columnNames, data);

    String expectedOutput = "-------------------\n"
        + "| ID | Name | Age | \n"
        + "-------------------\n"
        + "| 1  | John | 25  | \n"
        + "| 2  | Jane | 30  | \n"
        + "| 3  | Bob  | 40  | \n"
        + "-------------------\n";

    assertEquals(expectedOutput, outContent.toString());
  }
}
