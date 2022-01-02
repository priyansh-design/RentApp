package com.example.rentapp;

import android.net.Uri;

import java.util.ArrayList;

public class PostDetails {
    String name,email,mobile_number,apartment_type,renter_type,description;
    ArrayList<Uri> uri_list;
    ArrayList<String> facilities;

    public PostDetails() {
    }

    public PostDetails(String name, String email, String mobile_number, String apartment_type, String renter_type, String description, ArrayList<Uri> uri_list, ArrayList<String> facilities) {
        this.name = name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.apartment_type = apartment_type;
        this.renter_type = renter_type;
        this.description = description;
        this.uri_list = uri_list;
        this.facilities = facilities;
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

    public String getApartment_type() {
        return apartment_type;
    }

    public void setApartment_type(String apartment_type) {
        this.apartment_type = apartment_type;
    }

    public String getRenter_type() {
        return renter_type;
    }

    public void setRenter_type(String renter_type) {
        this.renter_type = renter_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Uri> getUri_list() {
        return uri_list;
    }

    public void setUri_list(ArrayList<Uri> uri_list) {
        this.uri_list = uri_list;
    }

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
    }
}
