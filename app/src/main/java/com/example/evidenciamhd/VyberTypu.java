package com.example.evidenciamhd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class VyberTypu extends AppCompatActivity {

    private ImageButton buttonTrolejbusy;
    private ImageButton buttonAutobusy;
    private ImageButton buttonIne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vyber_typu);

        buttonTrolejbusy= findViewById(R.id.trolejbusy);

        buttonTrolejbusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VyberTypu.this, TrolejbusVyhladavanie.class));
            }
    });
    }
}
