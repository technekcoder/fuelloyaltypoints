package com.technek.parrotnight.models;

public class TransactionsLedger {

    public static final String TABLE_NAME = "Transactions";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "itemcode TEXT,location TEXT, salesprice TEXT, totalAmount TEXT, quantity TEXT,loyalty_awarded TEXT," +
                    "taxable TEXT, taxlevies TEXT, taxRate TEXT,suom TEXT,timestamp TEXT,fiscal_month TEXT,fiscal_year TEXT,processed_by TEXT,post_flag TEXT,reference_number TEXT,print_period TEXT"
                    + ")";
    private int id;
    private String itemcode;
    private String location;
    private double sale_price;
    private double totalamount;
    private double quantity;
    private String loyalty_awarded;
    private double taxable;
    private double levies;
    private double taxRate;
    private String suom;
    private String timestamp;
    private String fiscal_month;
    private String processed_by;
    private String fiscal_year;
    private String trnsync;
    private String reference_number;
    private String print_period;

    public TransactionsLedger() {
        this.id = this.id;
        this.itemcode = itemcode;
        this.location = this.location;
        this.sale_price = this.sale_price;
        this.totalamount = this.totalamount;
        this.quantity = this.quantity;
        this.loyalty_awarded = this.loyalty_awarded;
        this.taxable = this.taxable;
        this.levies = this.levies;
        this.taxRate = taxRate;
        this.suom = suom;
        this.timestamp = this.timestamp;
        this.fiscal_month = this.fiscal_month;
        this.processed_by = processed_by;
        this.fiscal_year = this.fiscal_year;
        this.trnsync = this.trnsync;
        this.reference_number = this.reference_number;
        this.print_period = this.print_period;
    }





    public TransactionsLedger(int id, String itemCode, String location, double sale_price, double totalamount, double quantity, String loyalty_awarded, double taxable, double levies, String taxrate, String suom, String timestamp, String fiscal_month, String fiscal_year, String processedby, String trnsync, String reference_number, String print_period) {
        this.id = id;
        this.itemcode = itemcode;
        this.location = location;
        this.sale_price = sale_price;
        this.totalamount = totalamount;
        this.quantity = quantity;
        this.loyalty_awarded = loyalty_awarded;
        this.taxable = taxable;
        this.levies = levies;
        this.taxRate = taxRate;
        this.suom = suom;
        this.timestamp = timestamp;
        this.fiscal_month = fiscal_month;
        this.processed_by = processed_by;
        this.fiscal_year = fiscal_year;
        this.trnsync = trnsync;
        this.reference_number = reference_number;
        this.print_period = print_period;

    }

    public String getLoyalty_awarded() {
        return loyalty_awarded;
    }

    public void setLoyalty_awarded(String loyalty_awarded) {
        this.loyalty_awarded = loyalty_awarded;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getFiscal_year() {
        return fiscal_year;
    }

    public void setFiscal_year(String fiscal_year) {
        this.fiscal_year = fiscal_year;
    }

    public String getTrnsync() {
        return trnsync;
    }

    public void setTrnsync(String trnsync) {
        this.trnsync = trnsync;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFiscal_month() {
        return fiscal_month;
    }

    public void setFiscal_month(String fiscal_month) {
        this.fiscal_month = fiscal_month;
    }

    public String getProcessed_by() {
        return processed_by;
    }

    public void setProcessed_by(String processed_by) {
        this.processed_by = processed_by;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTaxable() {
        return taxable;
    }

    public void setTaxable(double taxable) {
        this.taxable = taxable;
    }

    public double getLevies() {
        return levies;
    }

    public void setLevies(double levies) {
        this.levies = levies;
    }



    public String getSuom() {
        return suom;
    }

    public void setSuom(String suom) {
        this.suom = suom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getPrint_period() {
        return print_period;
    }

    public void setPrint_period(String print_period) {
        this.print_period = print_period;
    }

    @Override
    public String toString() {
        return "TransactionsLedger{" +
                "id='" + id + '\'' +
                ", itemcode='" + itemcode + '\'' +
                ", location='" + location + '\'' +
                ", sale_price=" + sale_price +
                ", totalamount=" + totalamount +
                ", loyalty_awarded=" + loyalty_awarded +
                ", quantity=" + quantity +
                ", taxable=" + taxable +
                ", levies=" + levies +
                ", taxRate=" + taxRate +
                ", suom='" + suom + '\'' +
                '}';
    }
}


