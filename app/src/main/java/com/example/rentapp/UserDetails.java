package com.example.rentapp;

public class UserDetails {
    String name,email,mobile_number,address,city;

    public UserDetails(String name, String email, String mobile_number, String address, String city) {
        this.name = name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.address = address;
        this.city = city;
    }

    public UserDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
