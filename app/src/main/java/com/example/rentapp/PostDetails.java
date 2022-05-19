package com.example.rentapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PostDetails implements Parcelable {
    String mobile_number,rent_amount,postId,city;
    String name,house_type,tenant_type,bhk,furnished_type,buildup_area,address,available_from;
    ArrayList<String> uri_list;
    ArrayList<String> ammenities;

    public PostDetails() {
    }

    public PostDetails(String mobile_number,String rent_amount) {
        this.mobile_number=mobile_number;
        this.rent_amount=rent_amount;
    }
    public PostDetails(String name,String address,String mobile_number,String buildup_area,String available_from,String rent_amount,String bhk,String furnished_type, String house_type, String tenant_type, ArrayList<String> uri_list, ArrayList<String> ammenities,String postId,String city) {
        this.name = name;
        this.postId=postId;
        this.city=city;
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




    public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
        @Override
        public PostDetails createFromParcel(Parcel in) {
            return new PostDetails(in);
        }

        @Override
        public PostDetails[] newArray(int size) {
            return new PostDetails[size];
        }
    };

    public String getPostId() { return postId; }

    public void setPostId(String postId) {
        this.postId=postId;
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


    public void setCity(String city) { this.city=city;};
    public String getCity() { return city; }


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



    public ArrayList<String> getUri_list() {
        return uri_list;
    }

    public void setUri_list(ArrayList<String> uri_list) {
        this.uri_list = uri_list;
    }

    public ArrayList<String> getAmmenities() {
        return ammenities;
    }

    public void setAmmenities(ArrayList<String> ammenities) {
        this.ammenities = ammenities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected PostDetails(Parcel in) {
        mobile_number = in.readString();
        rent_amount = in.readString();
        postId = in.readString();
        city=in.readString();
        name = in.readString();
        house_type = in.readString();
        tenant_type = in.readString();
        bhk = in.readString();
        furnished_type = in.readString();
        buildup_area = in.readString();
        address = in.readString();
        available_from = in.readString();
        uri_list = in.createStringArrayList();
        ammenities = in.createStringArrayList();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mobile_number);
        parcel.writeString(rent_amount);
        parcel.writeString(city);
        parcel.writeString(postId);
        parcel.writeString(name);
        parcel.writeString(house_type);
        parcel.writeString(tenant_type);
        parcel.writeString(bhk);
        parcel.writeString(furnished_type);
        parcel.writeString(buildup_area);
        parcel.writeString(address);
        parcel.writeString(available_from);
        parcel.writeStringList(uri_list);
        parcel.writeStringList(ammenities);
    }


}
