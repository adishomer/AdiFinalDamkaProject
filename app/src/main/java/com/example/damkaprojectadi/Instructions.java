package com.example.damkaprojectadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Instructions extends AppCompatActivity implements View.OnClickListener {

    private Button btni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_instructions);
        btni = findViewById(R.id.btnIn);
        btni.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if (view == btni) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}