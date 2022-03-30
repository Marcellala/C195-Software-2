package main;

import DBAccess.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

    }

    public static void main (String[] args){


        DBConnection.openConnection();
        launch(args);

        /**
         * loads language bundle for login screen
         */
        Locale france = new Locale("fr","FR");

        try{
            ResourceBundle rb = ResourceBundle.getBundle("Language_Files/rb",Locale.getDefault());

            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr"))
                System.out.println(rb.getString("intro"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBConnection.closeConnection();
    }
}