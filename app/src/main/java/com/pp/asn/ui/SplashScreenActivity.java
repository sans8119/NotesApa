package com.pp.asn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.pp.asn.phone2.R;

public class SplashScreenActivity extends AppCompatActivity {
    private final int TIME_TO_SHOW_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, NoteListActivity.class));
                finish();
            }
        }, TIME_TO_SHOW_SPLASH);
    }


}
