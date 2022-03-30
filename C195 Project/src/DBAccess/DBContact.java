package DBAccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContact {
    /**
     * method to get all contacts
     * @return
     */
    public static ObservableList<Contact> getAllContacts() {

        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try {

            String sql = "SELECT * FROM contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("email");
                Contact c = new Contact(contactID, contactName,contactEmail);
                contactList.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactList;
    }

    public static Contact getContact(int cID) {

        Contact contact=  null;

        try {

            String sql = "SELECT * FROM contacts WHERE contact_id =? ";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1,cID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("email");
                contact = new Contact(contactID, contactName,contactEmail);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contact;
    }



}
