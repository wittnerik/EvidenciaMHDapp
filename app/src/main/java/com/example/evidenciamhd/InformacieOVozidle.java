package com.example.evidenciamhd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class InformacieOVozidle extends AppCompatActivity {

    public TextView textMHD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacie_o_vozidle);

        textMHD= findViewById(R.id.textMHD);

        //textMHD= findViewById(R.id.testMHD);
        int mapka = getIntent().getExtras().getInt("mapka");

        textMHD.setText(String.valueOf(mapka));




    }



}
