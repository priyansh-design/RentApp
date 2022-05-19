package com.example.rentapp.Add_Post.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.rentapp.Add_Post.Controller.AddPostController;
import com.example.rentapp.PostDetails;
import com.example.rentapp.R;
import com.example.rentapp.UploadImageAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddPost extends AppCompatActivity {
    private AddPost context=this;
    ArrayList<Uri> arrayList=new ArrayList<Uri>();
    ArrayList<String> facility=new ArrayList<String>();
    ProgressBar progressBar;
    String username,name_s,house_type_s,tenant_type_s,bhk_s,furnished_type_s,rent_amount_s,buildup_area_s,address_s,mobilenumber_s,availableFrom_s,city_s;
    Button uploadImages,post_btn;
    DatePicker datePicker;
    Integer RC_PHOTO_PICKER=1;
    UploadImageAdapter post_adapter;
    RecyclerView recyclerView;
    TextInputLayout owner_name,address,buildup_area,rent_amount,owners_number;
    RadioGroup house_type,tenant_type,bhk,furniture_status;
    CheckBox gym,swimming,playground,parking;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        progressBar=(ProgressBar) findViewById(R.id.progessBar);
        uploadImages=findViewById(R.id.upload_btn);
        post_btn=findViewById(R.id.submitpost_btn);
        recyclerView=findViewById(R.id.upload_recycler);
        owner_name=findViewById(R.id.owner_name);
        address=findViewById(R.id.address);
        buildup_area=findViewById(R.id.area);
        rent_amount=findViewById(R.id.rent);
        owners_number=findViewById(R.id.mobile_num);
        house_type=findViewById(R.id.house_type);
        tenant_type=findViewById(R.id.tenant_type);
        bhk=findViewById(R.id.BHK);
        furniture_status=findViewById(R.id.furniture);
        gym=findViewById(R.id.gym);
        swimming=findViewById(R.id.swimming);
        playground=findViewById(R.id.playground);
        parking=findViewById(R.id.parking);
        datePicker=findViewById(R.id.datePicker);

        // Priyansh - Add onClickListerner to recycler viw items
        //                Delete from recycler view and update arrayList (containing uri's)

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
                post_btn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                //-----------edit Text------------------
                name_s=owner_name.getEditText().getText().toString();
                address_s=address.getEditText().getText().toString();
                buildup_area_s=buildup_area.getEditText().getText().toString();
                rent_amount_s=rent_amount.getEditText().getText().toString();
                mobilenumber_s=owners_number.getEditText().getText().toString();

                RadioButton ht=(RadioButton)findViewById(house_type.getCheckedRadioButtonId());
                RadioButton tt=(RadioButton)findViewById(tenant_type.getCheckedRadioButtonId());
                RadioButton bk=(RadioButton)findViewById(bhk.getCheckedRadioButtonId());
                RadioButton ft=(RadioButton)findViewById(furniture_status.getCheckedRadioButtonId());

                house_type_s=ht.getText().toString();
                tenant_type_s=tt.getText().toString();
                bhk_s=bk.getText().toString();
                furnished_type_s=ft.getText().toString();

                //-------------check box (can be improved)-----------------
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
                //ends
                facility.add(username);
                city_s=getcity(address_s);
                availableFrom_s= datePicker.getDayOfMonth()+"-"+ datePicker.getMonth()+1 +"-"+ datePicker.getYear();
                PostDetails postDetails=new PostDetails(name_s,address_s,mobilenumber_s,buildup_area_s,availableFrom_s,rent_amount_s,bhk_s,furnished_type_s,house_type_s,tenant_type_s,new ArrayList<String>(),facility,"0",city_s);
                ArrayList<Object> dataToController=new ArrayList<>();
                dataToController.add(context);
                dataToController.add(arrayList);
                dataToController.add(postDetails);

                AddPostController addPostController =new AddPostController(dataToController);

                postDetails=addPostController.isSuccessfullyUploaded();
                if(postDetails==null) {
                    Toast.makeText(context,"Uploading...",Toast.LENGTH_SHORT).show();
                }
                else {
                    offProgressBar();
                    // Some Problem occurs
//                     Invalid mobile_number or address -> if they contain "Invalid"
                    //some are empty, then validate those contains "MayBeEmpty"
                    if(postDetails.getMobile_number()!=null&&postDetails.getMobile_number().equals("Invalid")) Toast.makeText(context,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
                    else if(postDetails.getAddress()!=null&&postDetails.getAddress().equals("Invalid")) Toast.makeText(context,"Invalid Address",Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(context,"Fill empty blocks",Toast.LENGTH_SHORT).show();
                        //some might be empty
                        validate(owner_name);
                        validate(address);
                        validate(buildup_area);
                        validate(rent_amount);
                        validate(owners_number);
                    }

                }

            }
        });

    }

    public void onSuccessfulUpload() {
        offProgressBar();
        Toast.makeText(context,"Successfully Uploaded",Toast.LENGTH_SHORT);
        finish();
    }

    public void onFailedUpload() {
        offProgressBar();
        Toast.makeText(context,"Uploading Failed",Toast.LENGTH_SHORT);
    }

    public void offProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        post_btn.setVisibility(View.VISIBLE);
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