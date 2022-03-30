package DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomer {

    /**
     * method to add customer data to tableview on CustomerScreen
     *
     * @return
     */
    public static ObservableList<Customer> getAllCustomers() {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {

            String sql = "SELECT * from customers INNER JOIN first_level_divisions AS d ON d.division_ID = customers.Division_ID";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                int divisionID = rs.getInt("Division_ID");
                Countries thisCountry = DBCountries.getCountryByDivisionID(divisionID);
                String country = "";
                if (thisCountry != null) {
                    country = thisCountry.getCountryName();
                }
                String division = rs.getString("Division");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                Customer c = new Customer(customerID, name, address,divisionID, postalCode, phoneNumber);
                c.setDivision(division);
                c.setCountry(country);
                customerList.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customerList;
    }

    /**
     * method to add customer to database
     * @param customer
     */
    public static void addCustomer(Customer customer) {

        try {

            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code,Phone, Division_ID) VALUES (?,?,?,?,?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3,customer.getPostalCode());
            ps.setString(4,customer.getPhoneNumber());
            ps.setInt(5, customer.getDivisionID());

            int status = ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * method to modify customer in database
     * @param customer
     */
    public static void modifyCustomer(Customer customer) {

        try {

            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? Where Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3,customer.getPostalCode());
            ps.setString(4,customer.getPhoneNumber());
            ps.setInt(5, customer.getDivisionID());
            ps.setInt(6, customer.getId());

            int status = ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * method to delete customer in database and associated appointments from database
     * @param customerId
     */
    public static void deleteCustomer(int customerId) {

        try {

            String sql = "DELETE FROM customers where customer_id = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, customerId);

            String sql2 = "DELETE FROM appointment where customer_id = ?";
            PreparedStatement pStatement = DBConnection.getConnection().prepareStatement(sql2);
            pStatement.setInt(1, customerId);

            int status = ps.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
