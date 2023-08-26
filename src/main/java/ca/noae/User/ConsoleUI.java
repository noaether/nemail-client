package ca.noae.User;

import java.io.IOException;
import java.util.Arrays;

import ca.noae.Objects.CodeElements.Generated;

public final class ConsoleUI {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  @Generated({ "Utility class cannot be instantiated" })
  private ConsoleUI() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   *
   * Clears the console screen using platform-specific commands. On Windows, the
   * "cls" command is used,
   * while on other platforms, an escape sequence is used to clear the screen. If
   * an error occurs while
   * attempting to clear the screen, an error message is printed to the console.
   */
  public static void clearScreen() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        System.out.print("\033[H\033[2J");
        System.out.flush();
      }
    } catch (IOException | InterruptedException ex) {
      System.err.println("Failed to clear the console screen: " + ex.getMessage());
    }
  }

  /**
   *
   * Creates a formatted table based on the given column names and data.
   *
   * @param columnNames an array of Strings representing the column names of the
   *                    table
   * @param data        a two-dimensional array of Strings representing the data
   *                    to be displayed in the table
   */
  public static void createTable(final String[] columnNames, final String[][] data) {
    int[] columnWidths = new int[columnNames.length];
    for (int i = 0; i < columnNames.length; i++) {
      columnWidths[i] = columnNames[i].length();
    }
    for (String[] row : data) {
      for (int i = 0; i < columnNames.length; i++) {
        if (row[i].length() > columnWidths[i]) {
          columnWidths[i] = row[i].length();
        }
      }
    }
    int totalWidth = Arrays.stream(columnWidths).sum() + columnWidths.length * 3 + 1;
    StringBuilder separatorBuilder = new StringBuilder();
    for (int i = 0; i < totalWidth; i++) {
      separatorBuilder.append("-");
    }
    String separator = separatorBuilder.toString();
    System.out.println(separator);
    System.out.print("| ");
    for (int i = 0; i < columnNames.length; i++) {
      System.out.printf("%-" + columnWidths[i] + "s | ", columnNames[i]);
    }
    System.out.println();
    System.out.println(separator);
    for (String[] row : data) {
      System.out.print("| ");
      for (int i = 0; i < columnNames.length; i++) {
        System.out.printf("%-" + columnWidths[i] + "s | ", row[i]);
      }
      System.out.println();
    }
    System.out.println(separator);
  }
}
