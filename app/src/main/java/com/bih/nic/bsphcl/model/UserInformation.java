package com.bih.nic.bsphcl.model;

/**
 * Created by NIC2 on 08-03-2018.
 */

public class UserInformation {
    private String MessageString;
    private String UserID;
    private String UserName;
    private boolean Authenticated;
    private String ImeiNo;
    private String password;
    private String role;
    private String subDivision;
    private String contactNo;
    private String email;
    private String division;



    public String getMessageString() {
        return MessageString;
    }

    public void setMessageString(String messageString) {
        MessageString = messageString;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }



    public boolean getAuthenticated() {
        return Authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        Authenticated = authenticated;
    }

    public String getImeiNo() {
        return ImeiNo;
    }

    public void setImeiNo(String imeiNo) {
        ImeiNo = imeiNo;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSubDivision() {
        return subDivision;
    }

    public void setSubDivision(String subDivision) {
        this.subDivision = subDivision;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
