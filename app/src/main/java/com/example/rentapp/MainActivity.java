package com.example.rentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText usernumber;
    Button get_otp;
    CountryCodePicker ccp;
    String countryCode;
    String entered_number;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    ProgressBar progressBar;
    String code_sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernumber=findViewById(R.id.mobile_number);
        get_otp=findViewById(R.id.generate_otp);
        ccp=findViewById(R.id.countryCode);
        firebaseAuth=FirebaseAuth.getInstance();
        countryCode=ccp.getSelectedCountryCodeWithPlus();
        progressBar=findViewById(R.id.progessBar);


///////Here we are changing the country code using country code picker///////
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode=ccp.getSelectedCountryCodeWithPlus();
            }
        });

///////Here we send the otp and request for callbacks///////
        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number=usernumber.getText().toString();
                if(number.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your number",Toast.LENGTH_SHORT).show();

                }
                else if(number.length()<10){
                    Toast.makeText(getApplicationContext(),"Please enter correct number",Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    get_otp.setVisibility(View.GONE);
                    entered_number=countryCode+number;
                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(entered_number)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(callbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });




        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is sent",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                code_sent=s;
                entered_number=countryCode+usernumber.getText().toString();
                Intent intent=new Intent(MainActivity.this,VerificationPage.class);
                intent.putExtra("otp",code_sent);
                intent.putExtra("usernumber",entered_number);
                startActivity(intent);
            }
        };

    }

/////////Here we have checked that whether the user is already login or not ,if YES then we send the user to our main page///////
    @Override
    protected void onStart(){
        super.onStart();
        if(firebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}