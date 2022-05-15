package com.example.rentapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class OverviewFragment extends Fragment implements OnMapReadyCallback {
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    MapView mapView;
    String address, rent, bhk, area;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    TextView taddress, trent, tbhk, tarea, tdistance;
    Double my_lat = 0.0, my_long = 0.0, place_lat = 0.0, place_long = 0.0;
    FusedLocationProviderClient fusedLocationProviderClient;
    public Context context = this.getContext();


    public OverviewFragment(Bundle bundle) {
        this.setArguments(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PostDetails postDetails = getArguments().getParcelable("PostDetails");
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        extendedFloatingActionButton=view.findViewById(R.id.map_intent);
        mapView = (MapView) view.findViewById(R.id.map);
        taddress = view.findViewById(R.id.address_card);
        trent = view.findViewById(R.id.rent_card);
        tbhk = view.findViewById(R.id.bhk_card);
        tarea = view.findViewById(R.id.area_card);
        tdistance = view.findViewById(R.id.distance_card);
        taddress.setText(postDetails.address);
        trent.setText(postDetails.rent_amount);
        tbhk.setText(postDetails.bhk);
        tarea.setText(postDetails.buildup_area);
        address = postDetails.address;


        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);


        mapView.getMapAsync(this);

        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("google.navigation:q="+address+"&mode=l");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");

                startActivity(intent);
            }
        });




        return view;
    }

    private void getplacelocation(GoogleMap map) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> ji = geocoder.getFromLocationName(address, 10);

            Log.d("onrun", String.valueOf(ji.size()));

            if (ji.get(0) == null) {

                place_lat = ji.get(1).getLatitude();
                Log.d("onrun", String.valueOf(place_lat));
                place_long = ji.get(1).getLongitude();
                Log.d("onrun", String.valueOf(place_long));

            } else {

                place_lat = ji.get(0).getLatitude();
                Log.d("onrun", String.valueOf(place_lat));
                place_long = ji.get(0).getLongitude();
                Log.d("onrun", String.valueOf(place_long));
            }
            LatLng latLng = new LatLng(place_lat, place_long);
            Log.d("onrun", "" + latLng);
            float[] results = new float[10];
            Location.distanceBetween(my_lat, my_long, place_lat, place_long, results);
            float dis = results[0];
            int d= (int)(dis/1000.00);

            tdistance.setText(Integer.toString(d));
            map.addMarker(new MarkerOptions().position(new LatLng(place_lat, place_long)).title("Marker"));
            map.getUiSettings().setMyLocationButtonEnabled(true);
            CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(new LatLng(place_lat, place_long),15);
            map.moveCamera(cameraUpdate);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void getlocation(GoogleMap map) {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude()
                                , 2);

                        Log.d("onrun", String.valueOf(addresses.get(0).getLatitude()));
                        my_lat = addresses.get(0).getLatitude();
                        my_long = addresses.get(0).getLongitude();

                        getplacelocation(map);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getlocation(map);

//            Log.d("onrun","latitude of place"+String.valueOf(my_lat));
//            Log.d("onrun","place "+plc);


        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);

        }

    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}