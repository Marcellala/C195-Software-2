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
import java.util.ResourceBundle;

public class CustomerModifyScreen implements Initializable {
    public Button saveButton;
    public Button cancelButton;
    public ComboBox modifyCountryComboBox;
    public ComboBox modifyStateComboBox;
    public TextField modifyNameField;
    public TextField modifyAddressField;
    public TextField modifyPostalCodeField;
    public TextField modifyPhoneField;
    public TextField modifyIDField;
    private Customer selectedCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> countries = DBCountries.getAllCountries();
        modifyCountryComboBox.setItems(countries);


    }

    /*
    saves customer modifications and updates information in database
     */
    public void onSaveButton(ActionEvent actionEvent) throws IOException {
        int customerID = Integer.parseInt(modifyIDField.getText());
        String name = modifyNameField.getText();
        String address = modifyAddressField.getText();
        String country = modifyCountryComboBox.getSelectionModel().getSelectedItem().toString();
        String division = modifyStateComboBox.getSelectionModel().getSelectedItem().toString();
        int divisionID = DBCountries.getDivisionId(division);
        String postalCode = modifyPostalCodeField.getText();
        String phoneNumber = modifyPhoneField.getText();

        if(!customerInputValidation(name) || !customerInputValidation(address) || !customerInputValidation(country) || !customerInputValidation(division)|| !customerInputValidation(postalCode)|| !customerInputValidation(phoneNumber)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid input for each field before saving. No fields can be left blank");
            alert.showAndWait();
            return;
        }

        Customer updateCustomer = new Customer(customerID, name, address, divisionID, postalCode, phoneNumber);
        DBCustomer.modifyCustomer(updateCustomer);

        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,800,600);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * event handler with lambda expression
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setContentText("No changes entered will be saved. Are you sure you want to cancel?");

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
     * sets fields to screen to modify selected customer
     * @param selectedCustomer
     */
    public void setCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        modifyIDField.setText(String.valueOf(selectedCustomer.getId()));
        modifyNameField.setText(selectedCustomer.getName());
        modifyAddressField.setText(selectedCustomer.getAddress());
        modifyCountryComboBox.setValue(selectedCustomer.getCountry());
        modifyStateComboBox.setValue(selectedCustomer.getDivision());
        modifyPostalCodeField.setText(selectedCustomer.getPostalCode());
        modifyPhoneField.setText(selectedCustomer.getPhoneNumber());

    }

    /**
     * method to get divisions for selected country
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyCountryComboBox(ActionEvent actionEvent) throws IOException {
        Countries selectedCountry = (Countries) modifyCountryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null)
        {
            ObservableList Division = DBCountries.getFirstLevelDivision(selectedCountry.getCountryID());
            modifyStateComboBox.setItems(Division);
        }
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
