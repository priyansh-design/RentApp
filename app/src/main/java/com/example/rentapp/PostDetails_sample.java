package com.example.rentapp;

public class PostDetails_sample {
    String address,name,email,apartment_type,renter_type;
    Long bhk;


    public PostDetails_sample(String address, String name, String email, String apartment_type, String renter_type, Long bhk) {
        this.address = address;
        this.name = name;
        this.email = email;
        this.apartment_type = apartment_type;
        this.renter_type = renter_type;
        this.bhk = bhk;
    }

    public PostDetails_sample() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Long getBhk() {
        return bhk;
    }

    public void setBhk(Long bhk) {
        this.bhk = bhk;
    }
}
