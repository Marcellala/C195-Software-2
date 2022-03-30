package controller;

import DBAccess.DBContact;
import DBAccess.DBReport;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportScreen implements Initializable {
    public Button doneButton;
    public ComboBox contactBox;
    public TableView contactScheduleTable;
    public TableColumn appointmentIdCol;
    public TableColumn titleCol;
    public TableColumn typeCol;
    public TableColumn descriptionCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn customerIdCol;
    public TableView totalTable;
    public TableColumn monthCol;
    public TableColumn totalTypeCol;
    public TableColumn totalCol;
    public TableColumn customerNameCol;
    public TableColumn customerTotalCol;
    public TableView totalCustomerAppsTable;
    public TableColumn customerYearCol;


    @Override
    /**
     * method to populate tables for reports
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        totalTable.setItems(DBReport.getTotalNumberOfAppointments());
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalNumberOfAppointments"));

        totalCustomerAppsTable.setItems(DBReport.getTotalCustomerAppointments());
        customerYearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalCustomerAppointments"));

        ObservableList<Contact> contacts = DBContact.getAllContacts();
        contactBox.setItems(contacts);

    }

    public void onDoneButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Homepage");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method to populate consultant schedule upon contact selection
     * @param event
     */
    public void onContactComboBox(Event event) {


        Contact selectedContact = (Contact) contactBox.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contactScheduleTable.setItems(DBReport.getAllAppointmentsByConsultant(selectedContact.getContactID()));
            appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        }

    }

    public void totalAppsByCustomerAction(Event event) {

    }

}
