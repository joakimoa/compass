package com.ojojoj.compass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    public void goToCompass(View view) {
        Intent intent = new Intent(this, Compass.class);
        startActivity(intent);

    }

    public void goToSensors(View view) {
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
    }
}
