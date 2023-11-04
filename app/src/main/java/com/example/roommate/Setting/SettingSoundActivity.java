package com.example.roommate.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.roommate.R;

public class SettingSoundActivity extends AppCompatActivity {

    String[] items = {"소리", "진동", "무음"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sound);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        Spinner spinner1 = findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);

        Spinner spinner2 = findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);

        Spinner spinner3 = findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter);

        Spinner spinner4 = findViewById(R.id.spinner4);
        spinner4.setAdapter(adapter);

        Spinner spinner5 = findViewById(R.id.spinner5);
        spinner5.setAdapter(adapter);

        Spinner spinner6 = findViewById(R.id.spinner6);
        spinner6.setAdapter(adapter);
    }
}