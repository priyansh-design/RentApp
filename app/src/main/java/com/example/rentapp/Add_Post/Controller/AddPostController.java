package com.example.rentapp.Add_Post.Controller;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Pair;

import com.example.rentapp.Add_Post.Model.AddPostModel;
import com.example.rentapp.Add_Post.View.AddPost;
import com.example.rentapp.PostDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddPostController  {
    private String name_s,rent_amount_s,buildup_area_s,address_s,mobilenumber_s,city_s;
    private ArrayList<Object> receivedData;
    private PostDetails postDetails;
    private AddPost context;
    public AddPostController(ArrayList<Object> dataToController) {
        receivedData=dataToController;
        postDetails=(PostDetails) receivedData.get(2);
        context=(AddPost) receivedData.get(0);
        name_s=postDetails.getName();
        address_s=postDetails.getAddress();
        buildup_area_s=postDetails.getBuildup_area();
        rent_amount_s=postDetails.getRent_amount();
        mobilenumber_s=postDetails.getMobile_number();
        city_s=postDetails.getCity();


    }

    public PostDetails isSuccessfullyUploaded() {
        PostDetails dataToView=new PostDetails();
         if(mobilenumber_s.trim().length()<10 || mobilenumber_s.trim().length()>10){
            dataToView.setMobile_number("Invalid");
//                Toast.makeText(AddPost.this,"Entered number should be of 10 digits",Toast.LENGTH_SHORT).show();
        }
        else if(isValid(name_s) && isValid(address_s) && isValid(buildup_area_s) && isValid(rent_amount_s) && isValid(mobilenumber_s)){
        if(city_s==null||city_s.equals("INVALID CITY") || city_s.equals("INVALID-CITY")){
            dataToView.setAddress("Invalid");
//                Toast.makeText(AddPost.this,"Please Enter a Valid Address- "+ city_s,Toast.LENGTH_LONG).show();

        }
        else {
            // we can upload post

            AddPostModel dataToModel=new AddPostModel(receivedData);
            dataToModel.uploadOnFirebase();
            return null;
        }
        }else{
            dataToView.setName("MayBeEmpty");
            dataToView.setBuildup_area("MayBeEmpty");
            dataToView.setRent_amount("MayBeEmpty");
            dataToView.setMobile_number("MayBeEmpty");
            dataToView.setAddress("MayBeEmpty");
        }
        return dataToView;
    }


    private boolean isValid(String s){
        return s != null && !s.isEmpty();
    }


}
