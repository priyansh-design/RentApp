package com.example.rentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddPost extends AppCompatActivity {
    ArrayList<Uri> arrayList=new ArrayList<Uri>();
    ArrayList<String> fireuri=new ArrayList<String>();
    ArrayList<String> facility=new ArrayList<String>();
    ArrayList<String> uri_in_string=new ArrayList<String>();

    String name_s,house_type_s,tenant_type_s,bhk_s,furnished_type_s,rent_amount_s,buildup_area_s,address_s,mobilenumber_s,availableFrom_s;
    Button uploadImages,post_btn;
    DatePicker datePicker;
    Integer RC_PHOTO_PICKER=1;
    UploadImageAdapter post_adapter;
    RecyclerView recyclerView;
    public String current="";
    public Integer counter_of_f=0;
    Boolean flag=false;
    TextInputLayout owner_name,address,buildup_area,rent_amount,owners_number;
    TextInputEditText sss;
    RadioGroup house_type,tenant_type,bhk,furniture_status;
    CheckBox gym,swimming,playground,parking;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    StorageReference reference= FirebaseStorage.getInstance().getReference();


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
                mobilenumber_s=owners_number.getEditText().getText().toString();

                //------------radio Buttons-------------
//                int ht=house_type.getCheckedRadioButtonId();
//                int tt=tenant_type.getCheckedRadioButtonId();
//                int bk=bhk.getCheckedRadioButtonId();
//                int ft=furniture_status.getCheckedRadioButtonId();
                RadioButton ht=(RadioButton)findViewById(house_type.getCheckedRadioButtonId());
                RadioButton tt=(RadioButton)findViewById(tenant_type.getCheckedRadioButtonId());
                RadioButton bk=(RadioButton)findViewById(bhk.getCheckedRadioButtonId());
                RadioButton ft=(RadioButton)findViewById(furniture_status.getCheckedRadioButtonId());


                house_type_s=ht.getText().toString();
                tenant_type_s=tt.getText().toString();
                bhk_s=bk.getText().toString();
                furnished_type_s=ft.getText().toString();
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
//                mobilenumber_s=getIntent().getStringExtra("user_number");
                availableFrom_s= "Date: " + datePicker.getDayOfMonth()+"-"+ datePicker.getMonth()+1 +"-"+ datePicker.getYear();

                if(isValid(name_s) && isValid(address_s) && isValid(buildup_area_s) && isValid(rent_amount_s) && isValid(mobilenumber_s)){
                    String city_s=getcity(address_s);
                    if(city_s=="INVALID CITY" || city_s=="INVALID-CITY"){
                        Toast.makeText(AddPost.this,"Please Enter a Valid Address- "+ city_s,Toast.LENGTH_LONG).show();
                    }
                    else if(mobilenumber_s.trim().length()<10 || mobilenumber_s.trim().length()>10){
                        Toast.makeText(AddPost.this,"Entered number should be of 10 digits",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        DocumentReference document=firebaseFirestore.collection("Post_with_number").document("user_number");
                        document.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                if(error!=null){
                                    Toast.makeText(AddPost.this,"something_went_wrong1",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    int count=Integer.parseInt(Objects.requireNonNull(value.getString("posts_number")))+1;
                                    current=Integer.toString(count);
                                    Log.d("mytag",current);
                                    if(!flag){
                                        uploadtoStorage(arrayList, current);
                                        flag=true;

                                    }else{
                                        return;
                                    }
                                }

                            }
                        });



                    }


                }else{
                    Toast.makeText(AddPost.this,"Some fields are empty,Please check",Toast.LENGTH_SHORT).show();

                    validate(owner_name);
                    validate(address);
                    validate(buildup_area);
                    validate(rent_amount);
                    validate(owners_number);

                }




            }
        });


    }



    private  synchronized void uploadtoStorage(ArrayList<Uri> images, String count){
        Log.d("mytag","upload wale function ke andar hai apun");

        try {
            StorageReference fileref=reference.child("98989898").child(count);
            for(int i=0;i<images.size();i++){
                Log.d("mytag","for loop ke andar hai apun");
                try {
                    int index=i;
                    fileref.child(Integer.toString(i)).putFile(images.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                Log.d("mytag","get download ke andar hai apun");
                                fileref.child(Integer.toString(index)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        fireuri.add(uri.toString());

                                        Log.d("mytag",uri.toString());

                                        firebaseFirestore.collection("Post_with_number").document("user_number").collection("posts").document(count).update("uri_list",fireuri).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AddPost.this, "uri added to firestore", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddPost.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (Exception e){
                                Log.d("mytag-during",e.getMessage());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPost.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.d("mytag-upload",e.getMessage());
                }

            }

        } catch (Exception e){
            Log.d("mytag-upload",e.getMessage());
        }

        Log.d("mytag", "uploiad se aane wali list ke size " + Integer.toString(fireuri.size()));
        for (int i = 0; i < fireuri.size(); i++) {
            uri_in_string.add(fireuri.get(i).toString());
        }
        Log.d("mytag", "urilist me gaya data");
        PostDetails new_post = new PostDetails(name_s, address_s, mobilenumber_s, buildup_area_s, availableFrom_s, rent_amount_s, bhk_s, furnished_type_s, house_type_s, tenant_type_s, uri_in_string, facility,current);
//                        Toast.makeText(AddPost.this,""+arrayList.size()+ " "+"city-"+city_s+"date "+ availableFrom_s,Toast.LENGTH_SHORT).show();
        Log.d("mytag", "object created");
        try {
            String document_id = current;
            Log.d("mytag", "current ye hai " + document_id);
            Log.d("mytag", document_id);
            firebaseFirestore.collection("Post_with_number").document("user_number").collection("posts").document(document_id).set(new_post).addOnSuccessListener(Void -> {
                Toast.makeText(AddPost.this, "data send successfully", Toast.LENGTH_SHORT).show();
                Log.d("mytag", "data firestore me gaya");
                try {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("posts_number", current);
                    firebaseFirestore.collection("Post_with_number").document("user_number").update(mp).addOnSuccessListener(new OnSuccessListener<java.lang.Void>() {
                        @Override
                        public void onSuccess(java.lang.Void aVoid) {
                            Toast.makeText(AddPost.this, "posts number updated", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPost.this, "post number not updated", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPost.this, "something_went_wrong", Toast.LENGTH_SHORT).show();
                }
            });



        } catch (Exception e) {
            Log.d("mytag", e.getMessage());
        }







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