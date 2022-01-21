package com.example.rentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterDetails extends AppCompatActivity {
    public String name,email,address,city,mobile_number;
    TextInputLayout _name,_email,_address,_city;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button submit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
        mobile_number=getIntent().getStringExtra("usernumber");
        _name=findViewById(R.id.name);
        _email=findViewById(R.id.email);
        _address=findViewById(R.id.address);
        _city=findViewById(R.id.city);
        submit=findViewById(R.id.submit);
        progressBar=findViewById(R.id.progessBar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=_name.getEditText().getText().toString().trim();
                email=_email.getEditText().getText().toString().trim();
                address=_address.getEditText().getText().toString().trim();
                city=_city.getEditText().getText().toString().trim();

                UserDetails userDetails=new UserDetails(name,email,mobile_number,address,city);
                database=FirebaseDatabase.getInstance();
                reference=database.getReference();
                Log.d("mytag","name is" + name+" "+"email is"+ email+" "+"add is" + address);
                if(name==null || email==null || city==null || address==null){
                    Toast.makeText(EnterDetails.this,"Please fill all the details",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    reference.child("Users").child(mobile_number).setValue(userDetails);
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(EnterDetails.this,HomePage.class);
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("usernumber",mobile_number);
                    startActivity(intent);
                    finish();



                }
            }
        });



    }
}