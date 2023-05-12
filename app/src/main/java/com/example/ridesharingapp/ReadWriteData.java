package com.example.ridesharingapp;

public class ReadWriteData {
    private String username;
    private String phone_no;
    private String gender;
    private String email;
    public ReadWriteData(String phone_no,String username,String gender,String email) {
        this.phone_no = phone_no;
        this.gender=gender;
        this.username=username;
        this.email=email;
    }

    public String getUsername() {
        return username;
    }

    public ReadWriteData() {
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
