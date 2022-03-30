package model;

public class Contact {

    private int contactID;
    private String contactName;
    private String email;


    /**
     * constructor for new instance of contact
     */

    public Contact(int contactID, String contactName,String email){
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * defines the getters for the contacts class
     */
    public int getContactID(){
        return contactID;
    }

    public String getContactName(){
        return contactName;
    }

    public String getEmail(){
        return email;
    }

    /**
     * defines the setters for the contacts class
     */

    public void setContactID (int contactID){
        this.contactID = contactID;
    }

    public void setContactName(String userName){
        this.contactName = contactName;
    }

    public void setEmail (String email){
        this.email = email;
    }

    @Override
    public String toString() {
        return contactName;
    }

    }

