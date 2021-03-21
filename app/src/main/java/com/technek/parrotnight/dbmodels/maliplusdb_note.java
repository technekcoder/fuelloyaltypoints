package com.technek.parrotnight.dbmodels;
public class maliplusdb_note {
    public static final String TABLE_NAME = "item_master";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ITEMCODE = "itemcode";
    public static final String COLUMN_SALESPRICE = "salesprice";
    public static final String COLUMN_LEVIES = "taxlevies";
    public static final String COLUMN_TAXABLE = "taxable";
    public static final String COLUMN_SUOM = "suom";
    public static final String COLUMN_TAXRATE = "taxrate";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_ITEMNAME = "itemname";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SYNCSTATUS="post_flag";
    private int itemcode;
    private double sale_price;
    private double Levies;
    private double Taxable;
    private String Suom;
    private String location;
    private String itemname;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + COLUMN_ITEMCODE + " TEXT  NULL,"
                    + COLUMN_SALESPRICE + " TEXT  NULL,"
                    + COLUMN_LEVIES + " TEXT  NULL,"
                    + COLUMN_ITEMNAME + " TEXT,"
                    + COLUMN_TAXABLE + " TEXT  NULL,"
                    + COLUMN_SUOM + " TEXT  NULL,"
                    + COLUMN_TAXRATE + " TEXT  NULL,"
                    + COLUMN_LOCATION + " TEXT  NULL,"
                    +COLUMN_SYNCSTATUS+"TEXT NULL,"
                    + COLUMN_TIMESTAMP +" DATETIME DEFAULT CURRENT_TIMESTAMP);";
    public maliplusdb_note() {
    }

    public maliplusdb_note(int itemcode, double sale_price, double levies, double taxable, String suom, String location, String itemname) {
        this.itemcode = itemcode;
        this.sale_price = sale_price;
        this.Levies = levies;
        this.Taxable = taxable;
        this.itemname = itemname;
        this.Suom = suom;
        this.location = location;
    }

    public int getItemcode() {
        return itemcode;
    }

    public void setItemcode(int itemcode) {
        this.itemcode = itemcode;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public double getLevies() {
        return Levies;
    }

    public void setLevies(double levies) {
        this.Levies = levies;
    }

    public double getTaxable() {
        return Taxable;
    }

    public void setTaxable(double taxable) {
        this.Taxable = taxable;
    }

    public String getSuom() {
        return Suom;
    }

    public void setSuom(String suom) {
        this.Suom = suom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    private int tax;
}
