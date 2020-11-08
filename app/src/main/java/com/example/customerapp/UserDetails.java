package com.example.customerapp;

import androidx.appcompat.app.AppCompatActivity ;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.customerapp.utils.AppRater;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDetails extends AppCompatActivity  {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, PhoneAuthActivity.class));
            finish();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public boolean user_logout(View view) {
        mFirebaseAuth.signOut();
        mSignInClient.signOut();
        startActivity(new Intent(this, PhoneAuthActivity.class));
        finish();
        return true;

    }

    public boolean rating(View view) {
        AppRater app = new AppRater();
        app.rateNow(UserDetails.this);
        return true;
    }

    public boolean contacttracing(View view) {
        Intent wintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.who.int/news-room/q-a-detail/contact-tracing"));
        startActivity(wintent);
        return true;
    }

    public void privacy(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Safeouts/Customer-App/blob/surajsahani/PRIVACY_POLICY.md"));
        startActivity(intent);
    }

    public void upload(View view) {
        Intent intent = new Intent(UserDetails.this,UploadtActivity.class);
        startActivity(intent);
    }
    public boolean changephone(View view) {
        mFirebaseAuth.signOut();
        mSignInClient.signOut();

        startActivity(new Intent(this, PhoneAuthActivity.class));
        finish();
        return true;
    }
}