package com.example.rentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class MarkerInfo extends AppCompatActivity {
    String name,address;
    Long  bhk;
    TextView _n,_a,_b;
    ArrayList<Uri> images=new ArrayList<>();
    SliderView sliderView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        _n=findViewById(R.id.name);
        _a=findViewById(R.id.address);
        _b=findViewById(R.id.bhk);
        name=getIntent().getStringExtra("name");
        address=getIntent().getStringExtra("address");

        _n.setText(name);
        _a.setText(address);


        sliderView=findViewById(R.id.imageslider);
        SliderAdpater sliderAdpater=new SliderAdpater(images);
        sliderView.setSliderAdapter(sliderAdpater);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

//        _b.setText(String.valueOf(bhk));

    }
}