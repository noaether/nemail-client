package ca.noae;

import java.io.IOException;

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
}
