package com.example.rentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class VerificationPage extends AppCompatActivity {
    EditText t1,t2,t3,t4,t5,t6;
    TextView usernumber;
    TextView resend_otp;
    Button verify_button;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String entered_otp;
    String mobile_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);
        t1=findViewById(R.id.otp_box_1);
        t2=findViewById(R.id.otp_box_2);
        t3=findViewById(R.id.otp_box_3);
        t4=findViewById(R.id.otp_box_4);
        t5=findViewById(R.id.otp_box_5);
        t6=findViewById(R.id.otp_box_6);
        verify_button=findViewById(R.id.verifyotp_button);
        resend_otp=findViewById(R.id.resendotp);
        progressBar=findViewById(R.id.progessBar);
        firebaseAuth=FirebaseAuth.getInstance();
        mobile_number=getIntent().getStringExtra("usernumber");
        joinOtp();
        usernumber=findViewById(R.id.usernumber);
        usernumber.setText(mobile_number);



        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entered_otp=t1.getText().toString()+t2.getText().toString()+t3.getText().toString()+t4.getText().toString()+t5.getText().toString()+t6.getText().toString();

                if(entered_otp.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter the otp",Toast.LENGTH_SHORT).show();

                }
                else if(t1.getText().toString().isEmpty()||t2.getText().toString().isEmpty()||t3.getText().toString().isEmpty()||t4.getText().toString().isEmpty()||t5.getText().toString().isEmpty()||t6.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter the correct OTP",Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    String receivedotp=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(receivedotp,entered_otp);
                    signInWithCredential(credential);
                }

            }
        });


    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(VerificationPage.this,EnterDetails.class);
                    intent.putExtra("usernumber",mobile_number);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void joinOtp() {

        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        t5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    t6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}