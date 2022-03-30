package controller;

import DBAccess.DBAppointment;
import DBAccess.DBUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {
    public Button signInButton;
    public Button exitButton;
    public Label usernameLabel;
    public Label passwordLabel;
    public TextField usernameField;
    public PasswordField passwordField;
    public Label zoneIdLabel;
    public Label instructionLabel;
    public Label loginLabel;
    private String alertTitle;
    private String alertContent;
    private String alertConfirmTitle;
    private String alertConfirmContent;

    /**
     * initializes the language bundle and gets users current location
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //sets zoneID label to current date and time formatted for legibility
        Locale current = Locale.getDefault();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formatDateTime = now.format(dtFormatter);
        zoneIdLabel.setText(ZoneId.systemDefault().toString());

        Locale france = new Locale("fr","FR");

        //line 56 only for testing
        //Locale.setDefault(france);

        try{
            ResourceBundle rb = ResourceBundle.getBundle("Language_Files/rb",Locale.getDefault());

            if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr"))
                loginLabel.setText(rb.getString("intro"));
                instructionLabel.setText(rb.getString("instructions"));
                usernameLabel.setText(rb.getString("username"));
                passwordLabel.setText(rb.getString("password"));
                signInButton.setText(rb.getString("signin"));
                exitButton.setText(rb.getString("exit"));
                alertTitle = rb.getString("alertTitle");
                alertContent = rb.getString("alertContent");
                alertConfirmTitle = rb.getString("alertConfirmTitle");
                alertConfirmContent = rb.getString("alertConfirmContent");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * action even that validates user credentials and checks for 15 minute appointments upon login
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void signInButton(ActionEvent actionEvent) throws IOException, SQLException {

        String username= usernameField.getText();
        String password = passwordField.getText();
        String dateTime = ZonedDateTime.now(ZoneId.of("UTC")).toString();

        boolean loginValidation = DBUser.validate(username,password);

        if(!loginValidation){
            String loggerAttempt = dateTime + " ," + username + " has failed to log in";
            utils.Logger.createLogger(loggerAttempt);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setContentText(alertContent);
            alert.showAndWait();
        }
        else{
            String loggerAttempt = dateTime + " user: '" + username + "' has logged in";
            utils.Logger.createLogger(loggerAttempt);
            Appointment appointment = DBAppointment.get15minuteAppointment(username);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            if(appointment != null) {
                alert.setContentText("You have an appointment in 15 minutes" + "\n"
                        + "Appointment ID: " + appointment.getAppointmentID() + "\n" + "Date & Time: " + appointment.getStart());
            }
            else {
                alert.setContentText("You have no upcoming appointments");

            }
            Optional<ButtonType> result = alert.showAndWait();
            Locale english = new Locale("en","EN");
            Locale.setDefault(english);
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setTitle("Homepage");
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * confirms user exiting in FR and EN
     * @param actionEvent
     */
    public void onExitButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(alertConfirmTitle);
        alert.setHeaderText(null);
        alert.setContentText(alertConfirmContent);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    public void onUsernameField(ActionEvent actionEvent) {
    }

    public void onPasswordField(ActionEvent actionEvent) {
    }
}
