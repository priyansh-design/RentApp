package com.example.rentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class MarkerInfo extends AppCompatActivity {
    String name,address,bhk;
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
        bhk=getIntent().getStringExtra("bhk");


        _n.setText(name);
        _a.setText(address);
        _b.setText(bhk);

        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.sample)
                + '/' + getResources().getResourceTypeName(R.drawable.sample) + '/' + getResources().getResourceEntryName(R.drawable.sample) );
        for(int i=0;i<3;i++){
            images.add(imageUri);
        }

//        sliderView=findViewById(R.id.imageslider);
//        SliderAdpater sliderAdpater=new SliderAdpater(images_f);
//        sliderView.setSliderAdapter(sliderAdpater);
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//        sliderView.startAutoCycle();

//

    }
}