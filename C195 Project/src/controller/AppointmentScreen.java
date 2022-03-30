package controller;

import DBAccess.DBAppointment;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AppointmentScreen implements Initializable {
    public Button cancelButton;
    public Button addButton;
    public Button saveButton;
    public Button modifyButton;
    public TableView appointmentTable;
    public RadioButton weeklyRadioButton;
    public RadioButton monthlyRadioButton;
    public RadioButton allRadioButton;
    public TableColumn appointmentIdCol;
    public TableColumn appTitleCol;
    public TableColumn appDescriptionCol;
    public TableColumn appLocationCol;
    public TableColumn appContactCol;
    public TableColumn appTypeCol;
    public TableColumn appStartCol;
    public TableColumn appEndCol;
    public TableColumn appCustomerIdCol;
    public TableColumn appUserIdCol;
    public Button doneButton;
    public Button deleteButton;

    /**
     * populates the Appointments table view on the Appointment screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentTable.setItems(DBAppointment.getAllAppointments());
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

    }

    /**
     * method to add appointment and move to add appointment screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddButton(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentAddScreen.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method to select appointment to modify and load onto AppointmentModifyScreen
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/view/AppointmentModifyScreen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        AppointmentModifyScreen appointmentModifyScreen = loader.getController();
        Appointment selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null){
            Alert alert = new Alert(Alert.AlertType.WARNING.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select an appointment from the table to modify");

            alert.showAndWait();

            return;
        }
        else {
            appointmentModifyScreen.setAppointment((Appointment) appointmentTable.getSelectionModel().getSelectedItem());
        }
        stage.setTitle("Modify Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method to delete appointment and confirm with alert message
     * @param actionEvent
     * @throws IOException
     */
    public void
    onDeleteButton(ActionEvent actionEvent) throws IOException {

        Appointment selection = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setContentText("Please select an appointment to delete");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setContentText("Are you sure you want to delete this appointment? " + "\n" + "ID: " + selection.getAppointmentID() + " " + selection.getType());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK){
                DBAppointment.deleteAppointment(selection.getAppointmentID());
                appointmentTable.setItems(DBAppointment.getAllAppointments());
                appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                appTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
                appDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                appLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
                appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                appContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
                appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
                appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
                appCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                appUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
                }
            }
        }

    /**
     * lamda expression used to improve filter to view appointments by week (used C195MultiLambdaExample as reference)
     * @param actionEvent
     */
    public void onWeeklyRadioButton(ActionEvent actionEvent) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus7 = now.plusDays(7);
        ObservableList<Appointment> appointmentList =DBAppointment.getAllAppointments();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDateTime rowDate = row.getStart();

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus7);
        });
        appointmentTable.setItems(filteredData);
    }

    /**
     * lamda expression used to improve filter to view appointments by month (used C195MultiLambdaExample as reference)
     * @param actionEvent
     */
    public void onMonthlyRadioButton(ActionEvent actionEvent) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusMonth = now.plusMonths(1);
        ObservableList<Appointment> appointmentList =DBAppointment.getAllAppointments();
        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentList);
        filteredData.setPredicate(row -> {

            LocalDateTime rowDate = row.getStart();

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlusMonth);
        });
        appointmentTable.setItems(filteredData);
    }

    public void onAllRadioButton(ActionEvent actionEvent) {

        appointmentTable.setItems(DBAppointment.getAllAppointments());

    }

    /**
     * handler to take user back to main screen
     * @param actionEvent
     * @throws IOException
     */
    public void onDoneButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,800,600);
        stage.setTitle("Homepage");
        stage.setScene(scene);
        stage.show();
    }


}
