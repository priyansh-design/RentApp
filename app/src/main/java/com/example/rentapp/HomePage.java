package com.example.rentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    GoogleMap googleMap;
    SupportMapFragment mapView;
    FusedLocationProviderClient client;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Double my_lat=0.0,my_long=0.0,p_lat=0.0,p_long=0.0;
    String name,email,mobile_number;
    TextView name_header,email_header,mobilenumber_header;
    ProgressBar progressBar;
    Button recentre_btn,filter;
    BottomSheetDialog bottomSheetDialog;
    Map<LatLng,PostDetails_sample> markertoinfo=new HashMap<>();
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        drawerLayout = findViewById(R.id.drawer_home);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        LayoutInflater layoutInflater=getLayoutInflater();
        View header_drawer=layoutInflater.inflate(R.layout.header_drawer,null);
        name_header=header_drawer.findViewById(R.id.usernameindrawer);
        email_header=header_drawer.findViewById(R.id.useremailindrawer);
        mobilenumber_header=header_drawer.findViewById(R.id.usernumberindrawer);
        progressBar=findViewById(R.id.progessBar);
        recentre_btn=findViewById(R.id.recentre_btn);
        filter=findViewById(R.id.filter_btn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showbottomsheet();
            }
        });

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        mobile_number=getIntent().getStringExtra("usernumber");

        name_header.setText(name);
        email_header.setText(email);
        mobilenumber_header.setText(mobile_number);

        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("Posts");
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(HomePage.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        recentre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
























    }

    private void showbottomsheet() {
        bottomSheetDialog=new BottomSheetDialog(HomePage.this,R.style.BottomSheetTheme);
        View sheetview=LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_filter_sheet,null);
        bottomSheetDialog.setContentView(sheetview);
        bottomSheetDialog.getWindow();
        bottomSheetDialog.show();

    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            progressBar.setVisibility(View.VISIBLE);
                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            Geocoder c_geocoder = new Geocoder(HomePage.this, Locale.getDefault());

                            try {
                                List<Address> myaddress = c_geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),2);
                                my_lat=myaddress.get(0).getLatitude();
                                my_long=myaddress.get(0).getLongitude();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int height = 70;
                            int width = 70;
                            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.current_icon);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            MarkerOptions curroptions=new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                            CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(latLng,15);
                            googleMap.moveCamera(cameraUpdate);
                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            googleMap.addMarker(curroptions);
                            CircleOptions circleOptions=new CircleOptions().center(latLng).radius(4000.0).strokeWidth(3f).strokeColor(Color.BLUE).fillColor(Color.argb(30,30,20,80));
                            googleMap.addCircle(circleOptions);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot no:snapshot.getChildren()){
                                        String pla_add= (String) no.child("address").getValue();
                                        String n=(String)no.child("name").getValue();
                                        Long bhk=(Long) no.child("bhk").getValue();
                                        String pla_email= (String) no.child("email").getValue();
                                        String pla_apart_type= (String) no.child("apartment-type").getValue();
                                        String pla_renter=(String)no.child("renter-type").getValue();
                                        PostDetails_sample post_sample=new PostDetails_sample(pla_add,n,pla_email,pla_apart_type,pla_renter,bhk);

                                        Log.d("onrun",""+pla_add);
                                        Geocoder pla_geocoder=new Geocoder(HomePage.this,Locale.getDefault());
                                        try {
                                            List<Address> pla_address=pla_geocoder.getFromLocationName(pla_add,3);
                                            Log.d("onrun",""+pla_address.get(0));
                                            if(pla_address.size()>0){
                                                p_lat=pla_address.get(0).getLatitude();
                                                p_long=pla_address.get(0).getLongitude();
                                                LatLng pla_latlng=new LatLng(p_lat,p_long);
                                                markertoinfo.put(pla_latlng,post_sample);
                                                float[] results=new float[10];
                                                Location.distanceBetween(my_lat,my_long,p_lat,p_long,results);
                                                float distance=results[0];
                                                if(distance/1000.00<=4){
                                                    int pla_height = 80;
                                                    int pla_width = 80;
                                                    BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.pla_icon);
                                                    Bitmap b = bitmapdraw.getBitmap();
                                                    Bitmap pla_smallMarker = Bitmap.createScaledBitmap(b, pla_width, pla_height, false);
                                                    String pla_desc="Owner Name: "+ n +","+"BHK: "+bhk;
                                                    Log.d("onrun",pla_desc);
                                                    MarkerOptions pla_options=new MarkerOptions().position(pla_latlng).icon(BitmapDescriptorFactory.fromBitmap(pla_smallMarker)).title(pla_add).snippet(pla_desc);

                                                    Marker marker=googleMap.addMarker(pla_options);
                                                    Log.d("onrun",""+marker.getPosition());
                                                }
                                                else{
                                                    continue;
                                                }


                                            }
                                            else{
                                                continue;
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                            circleOptions.getCenter(), getZoomLevel(googleMap.addCircle(circleOptions))));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    if(marker!=null){
                                        LatLng marker_latlng=marker.getPosition();
                                        String marker_name=markertoinfo.get(marker_latlng).getName();
                                        String marker_address=markertoinfo.get(marker_latlng).getAddress();
                                        Long marker_bhk=markertoinfo.get(marker_latlng).getBhk();
                                        String marker_apartment=markertoinfo.get(marker_latlng).getApartment_type();
                                        String marker_renter=markertoinfo.get(marker_latlng).getRenter_type();
                                        String marker_email=markertoinfo.get(marker_latlng).getEmail();
                                        Intent intent=new Intent(HomePage.this,MarkerInfo.class);
                                        intent.putExtra("name",marker_name);
                                        intent.putExtra("address",marker_address);
                                        intent.putExtra("bhk",marker_bhk);
                                        intent.putExtra("email",marker_email);
                                        intent.putExtra("apartment-type",marker_apartment);
                                        intent.putExtra("renter-type",marker_renter);


                                        startActivity(intent);
                                    }
                                }});


                        }


                    }



                    );

                }
            }
        });
    }

    private float getZoomLevel(Circle addCircle) {
        float zoomLevel = 11;
        if (addCircle != null) {
            double radius = addCircle.getRadius() + addCircle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}