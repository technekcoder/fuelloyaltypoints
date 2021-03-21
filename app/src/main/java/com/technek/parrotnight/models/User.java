package com.technek.parrotnight.models;

public class User {
    private final String FULLNAME;
    private final String USERNAME;
    private final String BRANCH_CODE;

    public User(String username, String fullname, String branchCode) {
        this.USERNAME = username;
        this.FULLNAME = fullname;
        this.BRANCH_CODE = branchCode;
    }

    public String getFULLNAME() {
        return FULLNAME;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getBRANCH_CODE() {
        return BRANCH_CODE;
    }
}
