package com.technek.parrotnight.dbmodels;

public class maliplusdbnotes {
    public static final String TABLE_NAME = "customer_details";

    private int id;
    private String username;
    private String email;
    private String password;
    private double loyalty_points_amount;
    private double loyalty_redeem_value;



    private double phonenumber;
    private String timestamp;
    private String firstimei;
    private String customerlocation;


    private String fullname;

    public maliplusdbnotes(int id, String username, String email, String password, double loyalty_points_amount, double loyalty_redeem_value, double phonenumber, String timestamp, String firstimei, String customerlocation, String fullname, String secondimei, String companyname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.loyalty_points_amount = loyalty_points_amount;
        this.loyalty_redeem_value = loyalty_redeem_value;
        this.phonenumber = phonenumber;
        this.timestamp = timestamp;
        this.firstimei = firstimei;
        this.customerlocation = customerlocation;
        this.fullname = fullname;
        this.secondimei = secondimei;
        this.companyname = companyname;
    }

    public double getLoyalty_points_amount() {
        return loyalty_points_amount;
    }

    public void setLoyalty_points_amount(double loyalty_points_amount) {
        this.loyalty_points_amount = loyalty_points_amount;
    }

    public double getLoyalty_redeem_value() {
        return loyalty_redeem_value;
    }

    public void setLoyalty_redeem_value(double loyalty_redeem_value) {
        this.loyalty_redeem_value = loyalty_redeem_value;
    }

    private String secondimei;
    private String companyname;
    // Create table SQL query
    public static final String CREATE_TABLE_IF_NOT_EXISTS =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " username TEXT,fullname TEXT,location TEXT, email TEXT, password TEXT,loyalty_redeem NUMERIC DEFAULT 0, loyalty_points NUMERIC DEFAULT 0,"+
                    "companyname TEXT,phonenumber TEXT, firstimei TEXT,secondimei TEXT,timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,device_system_number TEXT"
                    + ")";


    /*public maliplusdbnotes( int id ,String username, String email,String password,double phonenumber,String timestamp) {
        this.id = id;
        this.username = username;
        this.email=email;
        this.password=password;
        this.phonenumber=phonenumber;
        this.timestamp = timestamp;
    }*/


    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(double phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getFirstimei() {
        return firstimei;
    }

    public void setFirstimei(String firstimei) {
        this.firstimei = firstimei;
    }

    public String getSecondimei() {
        return secondimei;
    }
    public String getCustomerlocation() {
        return customerlocation;
    }

    public void setCustomerlocation(String customerlocation) {
        this.customerlocation = customerlocation;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public void setSecondimei(String secondimei) {
        this.secondimei = secondimei;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
    @Override
    public String toString() {
        return "maliplusdbnotes{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password=" + password +
                ", companyname=" + companyname +
                ", phonenumber=" + phonenumber +
                ", firstimei=" + firstimei +
                ", secondimei=" + secondimei+
                ", timestamp=" + timestamp +
                '}';
    }
}
