package controller;

import DBAccess.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import utils.Helper;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentModifyScreen implements Initializable {
    public Button saveButton;
    public Button cancelButton;
    public ComboBox modifyContactComboBox;
    public TextField modifyTitle;
    public TextField modifyDescription;
    public DatePicker modifyDate;
    public ComboBox modifyCustomerBox;
    public ComboBox modifyUserBox;
    public ComboBox<LocalTime> modifyStartTime;
    public ComboBox<LocalTime> modifyEndTime;
    public ComboBox modifyLocationBox;
    public TextField modifyAppointmentID;
    public ComboBox modifyTypeComboBox;
    private Appointment selectedAppointment;
    private ObservableList<String> types = FXCollections.observableArrayList("Product", "Engineering", "Marketing", "Sales");

    /**
     * initializes combo boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Contact > contacts = DBContact.getAllContacts();
        modifyContactComboBox.setItems(contacts);

        javafx.collections.ObservableList<User> userList = DBUser.getAllUsers();
        modifyUserBox.setItems(userList);

        ObservableList<Countries> countries = DBCountries.getAllCountries();
        modifyLocationBox.setItems(countries);

        ObservableList<Customer> customers = DBCustomer.getAllCustomers();
        modifyCustomerBox.setItems(customers);

        modifyStartTime.setItems(Helper.getLocalStartTimeList());

        modifyEndTime.setItems(Helper.getLocalEndTimeList());

        modifyTypeComboBox.setItems(types);

    }

    /**
     * sets field to screen to modify selected appointment
     * @param selectedAppointment
     */
    public void setAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
        modifyAppointmentID.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        modifyTitle.setText(selectedAppointment.getAppointmentTitle());
        modifyDescription.setText(selectedAppointment.getDescription());
        modifyLocationBox.setValue(selectedAppointment.getLocation());
        modifyTypeComboBox.setValue(selectedAppointment.getType());
        modifyDate.setValue(selectedAppointment.getStart().toLocalDate());
        modifyStartTime.setValue(LocalTime.from(selectedAppointment.getStart()));
        modifyEndTime.setValue(LocalTime.from(selectedAppointment.getEnd()));
        modifyCustomerBox.setValue(selectedAppointment.getCustomerID());
        modifyUserBox.setValue(selectedAppointment.getUserID());
        modifyContactComboBox.setValue(selectedAppointment.getContactID());

    }

    /**
     * handler to save modification changes and update to database
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveButton(javafx.event.ActionEvent actionEvent) throws IOException {

        int appointmentID = Integer.parseInt(modifyAppointmentID.getText());
        String appointmentTitle = modifyTitle.getText();
        String description = modifyDescription.getText();
        String location = modifyLocationBox.getSelectionModel().getSelectedItem().toString();
        String type = modifyTypeComboBox.getSelectionModel().getSelectedItem().toString();
        LocalDate date = modifyDate.getValue();
        LocalDateTime start = LocalDateTime.of(date, modifyStartTime.getValue());
        LocalDateTime end = LocalDateTime.of(date, modifyEndTime.getValue());
        int customerID = (int) modifyCustomerBox.getSelectionModel().getSelectedItem();
        int userID = (int) modifyUserBox.getSelectionModel().getSelectedItem();
        int contactID = (int) modifyContactComboBox.getSelectionModel().getSelectedItem();

        Appointment newAppointment = new Appointment(appointmentID,appointmentTitle,description,location,type, start, end,customerID,userID, contactID);
        Timestamp startTimestamp = Timestamp.valueOf(start);
        Timestamp endTimestamp = Timestamp.valueOf(end);
        boolean conflictExists = DBAppointment.checkAppointmentConflict(startTimestamp, endTimestamp, customerID, appointmentID);
        if(!appointmentInputValidation(appointmentTitle) || !appointmentInputValidation(description)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid input for each field before saving. No fields can be left blank");
            alert.showAndWait();
            return;
        }
        if(end.isBefore(start)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("End time cannot be before start time. Please choose a new end time that is after the start time.");
            alert.showAndWait();
            return;
        }
        if(start.isEqual(end)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Start time cannot be equal to end time. Please choose a new end time that is after the start time.");
            alert.showAndWait();
            return;
        }
        if(conflictExists == true){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Conflict exists");
            alert.setContentText("These appointments overlap. Please choose a new time.");

            alert.showAndWait();
            return;
        }
        DBAppointment.modifyAppointment(newAppointment);

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    public void onCancelButton(javafx.event.ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    //check if input fields are empty or blank
    public static Boolean appointmentInputValidation(String string){
        if (string.isEmpty() || string.isBlank()) {
            return false;
        } else {
            return true;
        }
    }

    public void onModifyTypeComboBox(ActionEvent actionEvent) {
    }
}
