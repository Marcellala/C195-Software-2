package controller;

import DBAccess.DBCountries;
import DBAccess.DBCustomer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Countries;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class CustomerAddScreen implements Initializable {
    public Button cancelButton;
    public Button saveButton;
    public ComboBox countryComboBox;
    public ComboBox stateComboBox;
    public TextField customerIDFIeld;
    public TextField customerNameField;
    public TextField customerAddressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;

    /**
     * sets countries to combo box
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> countries = DBCountries.getAllCountries();
        countryComboBox.setItems(countries);

    }

    /**
     * handler for the state combo box
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onCountryComboBox(ActionEvent actionEvent) throws SQLException {
        Countries selectedCountry = (Countries) countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            ObservableList Division = DBCountries.getFirstLevelDivision(selectedCountry.getCountryID());
            stateComboBox.setItems(Division);
        }
    }

    public void onStateComboBox(ActionEvent actionEvent) {

    }

    /**
     * event handler with lambda expression
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setContentText("No information entered will be saved. Are you sure you want to cancel?");

        //lambda expression to handle alert message and navigation back to customer screen
        alert.showAndWait().ifPresent((response -> {
        if (response == ButtonType.OK) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);
                stage.setTitle("Customers");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        }));
    }

    /**
     * gets customer information and saves new customer to database
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveButton(ActionEvent actionEvent) throws IOException {

        String name = customerNameField.getText();
        String address = customerAddressField.getText();
        String country = countryComboBox.getSelectionModel().getSelectedItem().toString();
        String division = stateComboBox.getSelectionModel().getSelectedItem().toString();
        int divisionID = DBCountries.getDivisionId(division);
        String postalCode = postalCodeField.getText();
        String phoneNumber = phoneNumberField.getText();

        if(!customerInputValidation(name) || !customerInputValidation(address) || !customerInputValidation(postalCode)|| !customerInputValidation(phoneNumber)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid input for each field before saving. No fields can be left blank");
            alert.showAndWait();
            return;
        }

        Customer newCustomer = new Customer(-1, name, address, divisionID, postalCode, phoneNumber);
        DBCustomer.addCustomer(newCustomer);

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();

    }

    //check if input fields are empty or blank
    public static Boolean customerInputValidation(String string){
        if (string.isEmpty() || string.isBlank()) {
            return false;
        } else {
            return true;
        }
    }
}

