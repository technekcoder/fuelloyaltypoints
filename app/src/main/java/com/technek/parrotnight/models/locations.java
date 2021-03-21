package com.technek.parrotnight.models;

public class locations {
    public static final String TABLE_NAME = "locations";
    private String location;

    public static final String CREATE_TABLE_IF_NOT_EXISTS =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "location TEXT" + ")";

    public locations(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "locations{" +
                ", location='" + location + '}';

    }
}