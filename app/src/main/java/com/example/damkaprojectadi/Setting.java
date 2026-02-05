package com.example.damkaprojectadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private Button btns;
    String[] arrcolor = { "בחר צבע","Red", "Blue", "Pink", "Yellow" };
    Spinner spinner;
    boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        btns = findViewById(R.id.btnSet);
        btns.setOnClickListener(this);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrcolor);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa); //Setting the ArrayAdapter data on the Spinner

    }

    @Override
    public void onClick(View view) {
        if (view == btns) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!firstTime)
        {
            Intent intent = new Intent();
            intent.putExtra("color",arrcolor[position]);
            setResult(RESULT_OK, intent);
            finish(); // return to MainActivity
        }
        firstTime = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "onNothingSelected", Toast.LENGTH_SHORT).show();

    }
}