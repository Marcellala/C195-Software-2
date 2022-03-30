package model;

import java.time.LocalDateTime;

public class Report {

    private String month;
    private String totalNumberOfAppointments;
    private String type;

    private int appointmentID;
    private String title;
    private String appointmentType;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int contactID;

    private int year;
    private String totalCustomerAppointments;

    /*
    constructor for total number of appointment types per month
     */
    public Report(String month, String type, String totalNumberOfAppointments) {
        this.month = month;
        this.type = type;
        this.totalNumberOfAppointments = totalNumberOfAppointments;
    }

    /*
     * constructor for contact schedule
     */
    public Report(int appointmentID, String title, String appointmentType, String description, LocalDateTime start, LocalDateTime end, int customerID, int contactID) {

        this.appointmentID = appointmentID;
        this.title = title;
        this.appointmentType = appointmentType;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.contactID = contactID;
    }

    /*
    constructor for total customer appointments per year
     */
    public Report(int year, int customerID, String totalCustomerAppointments) {
        this.year = year;
        this.customerID = customerID;
        this.totalCustomerAppointments = totalCustomerAppointments;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    private String monthName;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotalNumberOfAppointments() {
        return totalNumberOfAppointments;
    }

    public void setTotalNumberOfAppointments(String totalNumberOfAppointments) {
        this.totalNumberOfAppointments = totalNumberOfAppointments;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String type) {
        this.appointmentType = type;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTotalCustomerAppointments() {
        return totalCustomerAppointments;
    }

    public void setTotalCustomerAppointments(String totalCustomerAppointments) {
        this.totalCustomerAppointments = totalCustomerAppointments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}

