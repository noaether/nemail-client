package ca.noae.User;

import java.io.IOException;
import java.util.Arrays;

public class ConsoleUI {
  // Clears the console screen
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

  // Function to create and display a scrollable table
  public static void createTable(String[] columnNames, String[][] data) {
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
    int totalWidth = Arrays.stream(columnWidths).sum() + columnWidths.length - 1;
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
