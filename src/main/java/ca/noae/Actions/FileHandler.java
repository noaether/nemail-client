package ca.noae.Actions;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class FileHandler {
    public static void saveEmail(String to, String from, String date, String subject, String body) {
        try {
            FileWriter emailFile = new FileWriter(subject + ".txt");

            emailFile.write("To: " + to + "\n");
            emailFile.write("From: " + from + "\n");
            emailFile.write("Date: " + date + "\n");
            emailFile.write("Subject: " + subject + "\n");
            emailFile.write("Body: " + body + "\n");
            emailFile.write("\n Written by nemail-client on " + new java.util.Date() + "\n");
            emailFile.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
