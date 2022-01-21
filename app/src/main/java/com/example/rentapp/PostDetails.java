package com.example.rentapp;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class PostDetails {
    String name,mobile_number,house_type,tenant_type,bhk,furnished_type,rent_amount,buildup_area,address,available_from;
    ArrayList<Uri> uri_list;
    ArrayList<String> ammenities;

    public PostDetails() {
    }

    public PostDetails(String name,String address,String mobile_number,String buildup_area,String available_from,String rent_amount,String bhk,String furnished_type, String house_type, String tenant_type, ArrayList<Uri> uri_list, ArrayList<String> ammenities) {
        this.name = name;
        this.address=address;
        this.available_from=available_from;
        this.mobile_number = mobile_number;
        this.house_type = house_type;
        this.tenant_type = tenant_type;
        this.bhk=bhk;
        this.rent_amount=rent_amount;
        this.furnished_type=furnished_type;
        this.buildup_area=buildup_area;
        this.uri_list = uri_list;
        this.ammenities = ammenities;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getTenant_type() {
        return tenant_type;
    }

    public void setTenant_type(String tenant_type) {
        this.tenant_type = tenant_type;
    }

    public String getBhk() {
        return bhk;
    }

    public void setBhk(String bhk) {
        this.bhk = bhk;
    }

    public String getFurnished_type() {
        return furnished_type;
    }

    public void setFurnished_type(String furnished_type) {
        this.furnished_type = furnished_type;
    }

    public String getRent_amount() {
        return rent_amount;
    }

    public void setRent_amount(String rent_amount) {
        this.rent_amount = rent_amount;
    }

    public String getBuildup_area() {
        return buildup_area;
    }

    public void setBuildup_area(String buildup_area) {
        this.buildup_area = buildup_area;
    }



    public ArrayList<Uri> getUri_list() {
        return uri_list;
    }

    public void setUri_list(ArrayList<Uri> uri_list) {
        this.uri_list = uri_list;
    }

    public ArrayList<String> getAmmenities() {
        return ammenities;
    }

    public void setAmmenities(ArrayList<String> ammenities) {
        this.ammenities = ammenities;
    }
}
