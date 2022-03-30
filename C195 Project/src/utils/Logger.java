package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Logger {

    public static final String fileName = "login_activity.txt";

    public static void createLogger (String log){
        try{


            FileWriter fWriter = new FileWriter(fileName, true);
            PrintWriter lFile = new PrintWriter(fWriter);
            lFile.println(log);
            lFile.close();
            lFile.println(log + " has been recorded successfully");


        } catch (IOException e) {
            System.out.println("Logger error: " + e.getMessage());
           // e.printStackTrace();
        }
    }
}
