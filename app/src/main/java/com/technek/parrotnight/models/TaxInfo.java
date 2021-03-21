package com.technek.parrotnight.models;

public class TaxInfo {

    public static final String TABLE_NAME = "customer_details";

    private int ID;
    private String itemCode;
    private String itemName;
    private double unitPrice;
    private double levies;
    private double taxable;
    private double taxRate;
    private double loyalty_redeem_value;
    private double loyaty_points_amount;

    private String SUOM;
    private String location;

    public TaxInfo() {
        clear();
    }

    public TaxInfo(int ID, String itemCode, String itemName, double unitPrice, double levies, double taxable, double taxRate, double loyalty_redeem_value, double loyaty_points_amount, String SUOM, String location) {
        this.ID = ID;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.levies = levies;
        this.taxable = taxable;
        this.taxRate = taxRate;
        this.loyalty_redeem_value = loyalty_redeem_value;
        this.loyaty_points_amount = loyaty_points_amount;
        this.SUOM = SUOM;
        this.location = location;
    }

    public final void clear() {
        this.ID = -1;
        this.itemCode = "";
        this.itemName = "";
        this.unitPrice = 0d;
        this.levies = 0d;
        this.taxable = 0d;
        this.taxRate = 0d;
        this.loyalty_redeem_value = 0d;
        this.loyaty_points_amount = 0d;
        this.SUOM = "";
        this.location = "";
    }

    public double getLoyalty_redeem_value() {
        return loyalty_redeem_value;
    }

    public void setLoyalty_redeem_value(double loyalty_redeem_value) {
        this.loyalty_redeem_value = loyalty_redeem_value;
    }

    public double getLoyaty_points_amount() {
        return loyaty_points_amount;
    }

    public void setLoyaty_points_amount(double loyaty_points_amount) {
        this.loyaty_points_amount = loyaty_points_amount;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLevies() {
        return levies;
    }

    public void setLevies(double levies) {
        this.levies = levies;
    }

    public double getTaxable() {
        return taxable;
    }

    public void setTaxable(double taxable) {
        this.taxable = taxable;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getSUOM() {
        return SUOM;
    }

    public void setSUOM(String SUOM) {
        this.SUOM = SUOM;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "TaxInfo{" +
                "ID=" + ID +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", unitPrice=" + unitPrice +
                ", levies=" + levies +
                ", taxable=" + taxable +
                ", taxRate=" + taxRate +
                ", SUOM='" + SUOM + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
