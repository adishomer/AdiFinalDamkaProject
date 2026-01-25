package com.example.damkaprojectadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity implements View.OnClickListener {

    private Button btns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        btns = findViewById(R.id.btnSet);
        btns.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btns) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}