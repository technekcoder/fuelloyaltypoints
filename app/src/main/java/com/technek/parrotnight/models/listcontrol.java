package com.technek.parrotnight.models;

public class listcontrol {

    public static final String TABLE_NAME="dbcontrol";
    public static final String COLUMN_SYNCSTATUS="syncstatus";

    public static final String COLUMN_SUBSCRIPTIONTYPE="subscriptiontype";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SUBSCRIPTIONTYPE + " TEXT,"
                    + COLUMN_TIMESTAMP+ " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_SYNCSTATUS + " TEXT);";
    private int id;
    private double subscriptiontype;
    private double syncstatus;

    public listcontrol(int id, double subscriptiontype, double syncstatus) {
        this.id = id;
        this.subscriptiontype = subscriptiontype;
        this.syncstatus = syncstatus;
    }
    public int getId() {
        return id;
    }public void setId(int id) {
        this.id = id;
    }
    public double getSubscriptiontype() {
        return subscriptiontype;
    }
    public void setSubscriptiontype(double subscriptiontype) {
        this.subscriptiontype = subscriptiontype;
    }
    public double getSyncstatus() {
        return syncstatus;
    }public void setSyncstatus(double syncstatus) {
        this.syncstatus = syncstatus;
    }
}
