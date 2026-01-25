package com.example.damkaprojectadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private Button btnInstructions;
    private Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnPlay= findViewById(R.id.btn1);
        btnInstructions= findViewById(R.id.btn2);
        btnSetting= findViewById(R.id.btn3);

        btnPlay.setOnClickListener(this);
        btnInstructions.setOnClickListener(this);
        btnSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==btnPlay)
        {
            Intent intent= new Intent(this, GameActivity.class);
            startActivity(intent);
        }
        if (view==btnInstructions)
        {
            Intent intent= new Intent(this, Instructions.class);
            startActivity(intent);
        }
        if (view==btnSetting)
        {
            Intent intent= new Intent(this, Setting.class);
            startActivity(intent);
        }

    }
}