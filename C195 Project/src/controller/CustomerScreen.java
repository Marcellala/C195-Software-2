package controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerScreen implements Initializable {
    public Button addButton;
    public Button modifyButton;
    public Button doneButton;
    public TableView customerTable;
    public TableColumn customerIDCol;
    public TableColumn customerNameCol;
    public TableColumn customerAddressCol;
    public TableColumn customerCountryCol;
    public TableColumn customerStateCol;
    public TableColumn customerPostalCol;
    public TableColumn customerPhoneCol;
    public Button deleteButton;

    /**
     * populates the customer table view on the customer screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(DBCustomer.getAllCustomers());
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

    }

    public void onAddButton(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerAddScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * gets customer to modify and sends data to customerModifyScreen for editing
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomerModifyScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene (root, 800,600);
        CustomerModifyScreen customerModifyScreen = loader.getController();
        Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            Alert alert = new Alert(Alert.AlertType.WARNING.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select a customer from the table to modify");

            alert.showAndWait();

            return;
        }
        else {
            customerModifyScreen.setCustomer((Customer) customerTable.getSelectionModel().getSelectedItem());
        }
        stage.setTitle("Modify Customer");
        stage.setScene(scene);
        stage.show();

    }

    /*
    handler to go back to main screen
     */
    public void onDoneButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,800,600);
        stage.setTitle("Homepage");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method to delete selected customer
     * @param actionEvent
     */
    public void onDeleteButton(ActionEvent actionEvent) {
        Customer selection = (Customer) customerTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setContentText("Please select a customer to delete");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setContentText("All associated appointments with this customer will also be deleted. Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK){
                DBCustomer.deleteCustomer(selection.getId());
                customerTable.setItems(DBCustomer.getAllCustomers());
               /* customerIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
                customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
                customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
                customerStateCol.setCellValueFactory(new PropertyValueFactory<>("division"));
                customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
                customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));*/
            }
        }
    }
}
