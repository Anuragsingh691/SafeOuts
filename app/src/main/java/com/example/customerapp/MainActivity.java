package com.example.customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private String mPhotoUrl;
    private String mUsername;
    RecyclerView recyclerView;
    private DatabaseReference TutorRefs;
    private ImageView logo;
    RecyclerView.LayoutManager layoutManager;
    TextView location2,Dlocation,Dcurr,DchangeLoc;
    Dialog dialogLoc;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private GoogleSignInClient mSignInClient;


    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.home_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserDetails.class));
            }
        });

        recyclerView = findViewById(R.id.offers_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        TutorRefs = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        location2 = findViewById(R.id.location_txt);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, PhoneAuthActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() !=null){
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        dialogLoc=new Dialog(MainActivity.this);
        dialogLoc.setContentView(R.layout.loc_dialog);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            dialogLoc.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loc_dialog_bg));
        }
        Window window=dialogLoc.getWindow();
        WindowManager.LayoutParams wlp=window.getAttributes();
        wlp.gravity= Gravity.TOP;
        window.setAttributes(wlp);
        dialogLoc.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        Dlocation=dialogLoc.findViewById(R.id.location_txt_detail_dialog);
        Dcurr=dialogLoc.findViewById(R.id.curr_loc_txt);
        DchangeLoc=dialogLoc.findViewById(R.id.change_loc_txt);
        Dcurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLoc.dismiss();
            }
        });
        DchangeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ChangeLocation.class));
            }
        });
        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLoc.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(TutorRefs, Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.textproductName.setText(products.getPName());
                productViewHolder.textProductInfo.setText(products.getDistance()+" kms");
                productViewHolder.txtProductPrice.setText(products.getNOPLive()+" peoples");
                Picasso.get().load(products.getImage()).into(productViewHolder.ProductImage);
                Picasso.get().load(products.getImage()).into(productViewHolder.ProductImagelogo);
                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent detintent=new Intent(MainActivity.this,RestaurantDetails.class);
                        detintent.putExtra("pid",products.getPid());
                        startActivity(detintent);

                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_item, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(
                                                location.getLatitude(), location.getLongitude(),
                                                1
                                        );
                                        location2.setText(addresses.get(0).getSubLocality());
                                        Dlocation.setText(addresses.get(0).getSubLocality());
                                    } catch (IOException e) {
                                        Toast.makeText(MainActivity.this, "Exception occured", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    Double lat = location.getLatitude();
                                    Double longtt = location.getLatitude();

                                } else {
                                    Toast.makeText(MainActivity.this, "Device location is turned off", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());
            }
        }
    }

    public void zomato(View view) {

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.application.zomato");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.application.zomato"));
            startActivity(intent);
        }

    }

    public void mswiggy(View view) {

        Intent intent = getPackageManager().getLaunchIntentForPackage("in.swiggy.android");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "in.swiggy.android"));
            startActivity(intent);
        }

    }


    public void searchbaractivity(View view) {
        Intent mainIntent = new Intent(this, searchbar.class);
        startActivity(mainIntent);
    }
}

