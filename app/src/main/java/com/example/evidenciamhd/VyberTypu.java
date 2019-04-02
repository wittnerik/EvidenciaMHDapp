package com.example.evidenciamhd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import static com.example.evidenciamhd.R.drawable.autobusy;

public class VyberTypu extends AppCompatActivity {

    private ImageButton buttonTrolejbusy, buttonAutobusy, buttonIne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vyber_typu);

        buttonTrolejbusy = findViewById(R.id.trolejbusy);
        buttonAutobusy = findViewById(R.id.autobusy);
        buttonIne = findViewById(R.id.technickeVozidla);

        buttonTrolejbusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VyberTypu.this, TrolejbusVyhladavanie.class));
                 }
        });

        /*buttonAutobusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VyberTypu.this, .class));
            }
        });

        buttonIne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VyberTypu.this, .class));
            }
        });*/

    }
}
