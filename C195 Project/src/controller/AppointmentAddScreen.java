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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentAddScreen implements Initializable {
    public Button saveButton;
    public Button cancelButton;
    public ComboBox locationComboBox;
    public ComboBox<Contact> contactComboBox;
    public TextField titleField;
    public TextField descriptionField;
    public TextField typeField;
    public DatePicker datePicker;
    public ComboBox <Customer>customerComboBox;
    public ComboBox <User> userComboBox;
    public ComboBox<LocalTime> startComboBox;
    public ComboBox<LocalTime> endComboBox;
    public ComboBox typeComboBox;
    private ObservableList<String> types = FXCollections.observableArrayList("Product", "Engineering", "Marketing", "Sales");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contact> contacts = DBContact.getAllContacts();
        contactComboBox.setItems(contacts);

        ObservableList<User> userList = DBUser.getAllUsers();
        userComboBox.setItems(userList);

        ObservableList<Countries> countries = DBCountries.getAllCountries();
        locationComboBox.setItems(countries);

        ObservableList<Customer> customers = DBCustomer.getAllCustomers();
        customerComboBox.setItems(customers);

        startComboBox.setItems(Helper.getLocalStartTimeList());

        endComboBox.setItems(Helper.getLocalEndTimeList());

        typeComboBox.setItems(types);

    }

    public void onSaveButton(ActionEvent actionEvent) throws IOException, SQLException {

        String appointmentTitle = titleField.getText();
        String description = descriptionField.getText();
        String location = locationComboBox.getSelectionModel().getSelectedItem().toString();
        String type = typeComboBox.getSelectionModel().getSelectedItem().toString();
        LocalDate date = datePicker.getValue();
        LocalDateTime start = LocalDateTime.of(date, startComboBox.getValue());
        LocalDateTime end = LocalDateTime.of(date, endComboBox.getValue());
        int customerID = customerComboBox.getSelectionModel().getSelectedItem().getId();
        int userID = userComboBox.getSelectionModel().getSelectedItem().getUserID();
        int contactID = contactComboBox.getSelectionModel().getSelectedItem().getContactID();

        Appointment newAppointment = new Appointment(-1,appointmentTitle,description,location,type,start,end,customerID,userID, contactID);
        Timestamp startTimestamp = Timestamp.valueOf(start);
        Timestamp endTimestamp = Timestamp.valueOf(end);
        boolean conflictExists = DBAppointment.checkAppointmentConflict(startTimestamp, endTimestamp, customerID, -1);

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
        else{
            DBAppointment.addAppointment(newAppointment);
        }


        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000,600);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    public void onCancelButton(ActionEvent actionEvent) throws IOException{
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


    public void onLocationComboBox(ActionEvent actionEvent) {
    }

    public void onContactComboBox(ActionEvent actionEvent) {

    }

    public void onDatePicker(ActionEvent actionEvent) {
    }

    public void onCustomerComboBox(ActionEvent actionEvent) {
    }

    public void onUserComboBox(ActionEvent actionEvent) {

    }

    public void onStartComboBox(ActionEvent actionEvent) {
    }

    public void onEndComboBox(ActionEvent actionEvent) {
    }

    public void onTypeComboBox(ActionEvent actionEvent) {
    }
}
