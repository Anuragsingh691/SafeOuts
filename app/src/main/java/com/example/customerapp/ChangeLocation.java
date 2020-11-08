package com.example.customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLEngineResult;

public class ChangeLocation extends AppCompatActivity {
    private TextView loactionAct,currLocation,hint1;
    EditText newLocationEdt;
    ImageView searchLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    RelativeLayout belowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        loactionAct=findViewById(R.id.location_txt_detail_act);
        currLocation=findViewById(R.id.curr_loc_txt_act);
        newLocationEdt = findViewById(R.id.change_loc_edt_txt_act);
        searchLocation= findViewById(R.id.location_act_search_img);
        belowLayout=findViewById(R.id.below_layout_act_location);
        hint1=findViewById(R.id.hint1);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(getApplicationContext(),"AIzaSyCYd9DNtP8fAnic_H5XwgCef7dmqj_7vB0");
        currLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newLocationEdt.setFocusable(false);
        newLocationEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(ChangeLocation.this);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            Place place=Autocomplete.getPlaceFromIntent(data);
            newLocationEdt.setText(place.getAddress());
            belowLayout.setVisibility(View.INVISIBLE);
        }
        else if ( resultCode== AutocompleteActivity.RESULT_ERROR)
        {
            Status status=Autocomplete.getStatusFromIntent(data);
            hint1.setText(status.getStatusMessage().toString());
            Toast.makeText(this, "Error "+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Geocoder geocoder = new Geocoder(ChangeLocation.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(
                                                location.getLatitude(), location.getLongitude(),
                                                1
                                        );
                                        loactionAct.setText(addresses.get(0).getSubLocality());

                                    } catch (IOException e) {
                                        Toast.makeText(ChangeLocation.this, "Exception occured", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    Double lat = location.getLatitude();
                                    Double longtt = location.getLatitude();

                                } else {
                                    Toast.makeText(ChangeLocation.this, "Device location is turned off", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }
}