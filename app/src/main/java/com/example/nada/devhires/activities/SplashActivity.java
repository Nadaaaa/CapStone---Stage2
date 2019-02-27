package com.example.nada.devhires.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nada.devhires.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       /* final String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final int REQUEST_CODE = 12345;

        if (ActivityCompat.checkSelfPermission(SplashActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(SplashActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, REQUEST_CODE);
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}
