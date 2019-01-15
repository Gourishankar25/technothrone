package com.example.user.technothrone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Aghamarsh on 11-01-19.
 */

public class SplashScreen extends AppCompatActivity{

    private static int splash_time_out =2500;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        androidx.appcompat.app.ActionBar appBar = getSupportActionBar();
        appBar.hide();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        },splash_time_out);

    }

}
