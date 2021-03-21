package com.technek.parrotnight.models;

public class ClientDetails {

    private String loyaltycardno;
    private String ledgerName;
    private String baseLocation;
    private String ledger_card;
    private String ledger_points;


    public ClientDetails() {
        clear();
    }

    public ClientDetails(String loyaltycardno, String ledgerName, String baseLocation, String ledger_card, String ledger_points) {
        this.loyaltycardno = loyaltycardno;
        this.ledgerName = ledgerName;
        this.baseLocation = baseLocation;
        this.ledger_card = ledger_card;
        this.ledger_points = ledger_points;
    }

    public final void clear()
    {
        this.loyaltycardno = "";
        this.ledgerName = "";
        this.baseLocation = "";
    }

    public String getLedger_card() {
        return ledger_card;
    }

    public void setLedger_card(String ledger_card) {
        this.ledger_card = ledger_card;
    }

    public String getLedger_points() {
        return ledger_points;
    }

    public void setLedger_points(String ledger_points) {
        this.ledger_points = ledger_points;
    }

    public String getLoyaltycardno() {
        return loyaltycardno;
    }

    public void setLoyaltycardno(String loyaltycardno) {
        this.loyaltycardno = loyaltycardno;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    @Override
    public String toString() {
        return loyaltycardno;
    }
}
