package com.example.rentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddPost extends AppCompatActivity {
    ArrayList<Uri> arrayList=new ArrayList<Uri>();
    ArrayList<String> facility=new ArrayList<String>();

    String name_s,house_type_s,tenant_type_s,bhk_s,furnished_type_s,rent_amount_s,buildup_area_s,address_s,mobilenumber_s,availableFrom_s;
    Button uploadImages,post_btn;
    DatePicker datePicker;
    Integer RC_PHOTO_PICKER=1;
    UploadImageAdapter post_adapter;
    RecyclerView recyclerView;
    TextInputLayout owner_name,address,buildup_area,rent_amount;
    RadioGroup house_type,tenant_type,bhk,furniture_status;
    CheckBox gym,swimming,playground,parking;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        uploadImages=findViewById(R.id.upload_btn);
        post_btn=findViewById(R.id.submitpost_btn);
        recyclerView=findViewById(R.id.upload_recycler);
        owner_name=findViewById(R.id.owner_name);
        address=findViewById(R.id.address);
        buildup_area=findViewById(R.id.area);
        rent_amount=findViewById(R.id.rent);
        house_type=findViewById(R.id.house_type);
        tenant_type=findViewById(R.id.tenant_type);
        bhk=findViewById(R.id.BHK);
        furniture_status=findViewById(R.id.furniture);
        gym=findViewById(R.id.gym);
        swimming=findViewById(R.id.swimming);
        playground=findViewById(R.id.playground);
        parking=findViewById(R.id.parking);
        datePicker=findViewById(R.id.datePicker);


        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                // old
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----------edit Text------------------
                name_s=owner_name.getEditText().getText().toString();
                address_s=address.getEditText().getText().toString();
                buildup_area_s=buildup_area.getEditText().getText().toString();
                rent_amount_s=rent_amount.getEditText().getText().toString();
                //------------radio Buttons-------------
                int ht=house_type.getCheckedRadioButtonId();
                int tt=tenant_type.getCheckedRadioButtonId();
                int bk=bhk.getCheckedRadioButtonId();
                int ft=furniture_status.getCheckedRadioButtonId();

                house_type_s=findViewById(ht).toString();
                tenant_type_s=findViewById(tt).toString();
                bhk_s=findViewById(bk).toString();
                furnished_type_s=findViewById(ft).toString();
                //-------------check box-----------------
                gym=findViewById(R.id.gym);
                swimming=findViewById(R.id.swimming);
                parking=findViewById(R.id.parking);
                playground=findViewById(R.id.playground);
                if(gym.isChecked()){
                    facility.add("Gym");
                }
                if(swimming.isChecked()){
                    facility.add("Swimming");
                }
                if(parking.isChecked()){
                    facility.add("Parking");
                }
                if(playground.isChecked()){
                    facility.add("Playground");
                }
                mobilenumber_s=getIntent().getStringExtra("user_number");
                availableFrom_s= "Date: " + datePicker.getDayOfMonth()+"-"+ datePicker.getMonth()+1 +"-"+ datePicker.getYear();

                if(isValid(name_s) && isValid(address_s) && isValid(buildup_area_s) && isValid(rent_amount_s)){
                    String city_s=getcity(address_s);
                    if(city_s=="INVALID CITY" || city_s=="INVALID-CITY"){
                        Toast.makeText(AddPost.this,"Please Enter a Valid Address- "+ city_s,Toast.LENGTH_LONG).show();
                    }
                    else{
                        PostDetails new_post=new PostDetails(name_s,address_s,mobilenumber_s,buildup_area_s,availableFrom_s,rent_amount_s,bhk_s,furnished_type_s,house_type_s,tenant_type_s,arrayList,facility);
                        Toast.makeText(AddPost.this,""+arrayList.size()+ " "+"mobilenumber "+mobilenumber_s+"date "+ availableFrom_s,Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(AddPost.this,"Some fields are empty,Please check",Toast.LENGTH_SHORT).show();

                    validate(owner_name);
                    validate(address);
                    validate(buildup_area);
                    validate(rent_amount);

                }




            }
        });


    }

    private String getcity(String address){
        Geocoder geocoder=new Geocoder(AddPost.this, Locale.getDefault());
        try {
            List<Address> addresses=geocoder.getFromLocationName(address,2);
            if(addresses.size()>0){
                String city_name=addresses.get(0).getLocality();
                return city_name;
            }
            else{
                return "INVALID CITY";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "INVALID-CITY";
        }


    }
    void validate(TextInputLayout textInputLayout){
        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty())textInputLayout.setError("Please fill this field");
                else{
                    textInputLayout.setError(null);
                }
            }
        });
    }

    private boolean isValid(String s){
        if(s==null || s.isEmpty()){
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                        arrayList.add(imageUri);
                        currentItem = currentItem + 1;

                    }
                    post_adapter=new UploadImageAdapter(arrayList);
                    recyclerView.setAdapter(post_adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


                }
                else if(data.getData()!=null){
                    Uri selectedImgUri = data.getData();
                    arrayList.add(selectedImgUri);
                    post_adapter=new UploadImageAdapter(arrayList);
                    recyclerView.setAdapter(post_adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));


                }
}}
        catch(Exception e) {
            Toast.makeText(this, "Dikkt h bhai", Toast.LENGTH_SHORT).show();
            Log.e(TAG,""+e);
            Log.e(TAG,"Dikkt h bhai");
            Log.e(TAG,"request code is : "+requestCode+" and result code is :"+resultCode);

        }}}