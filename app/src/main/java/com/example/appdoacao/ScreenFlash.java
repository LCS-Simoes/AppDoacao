package com.example.appdoacao;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;

public class ScreenFlash extends AppCompatActivity {

    private static final long timeFlash = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_flash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ScreenFlash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, timeFlash);
    }
}