package com.example.evidenciamhd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class InformacieOVozidle extends AppCompatActivity {
    private TextView ecv, stk, turnus, datum;
    private EditText typ, stav;
    public ImageButton MHD;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button button, buttonDelete;
    private DatabaseReference reff;
    Vozidlo vozidlo;
    int mapka;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacie_o_vozidle);
        ecv = findViewById(R.id.ecv);
        MHD = findViewById(R.id.MHD);
        mapka = getIntent().getExtras().getInt("mapka");
        ecv.setText(String.valueOf(mapka));

        typ = findViewById(R.id.typ);
        stk = findViewById(R.id.stk);
        turnus = findViewById(R.id.turnus);
        stav = findViewById(R.id.stav);
        datum = findViewById(R.id.datum);
        button = findViewById(R.id.button);
        buttonDelete = findViewById(R.id.buttonDelete);
        vozidlo = new Vozidlo();
        FirebaseApp.initializeApp(this);

        setValues();
        datePicker();

        vozidlo.setEvc(mapka);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gtyp = typ.getText().toString();
                String gstk = stk.getText().toString();
                String gstav = stav.getText().toString();
                String gturnus = turnus.getText().toString();
                String gdatum = datum.getText().toString();

                vozidlo.setTyp(gtyp);
                vozidlo.setStk(gstk);
                vozidlo.setStav(gstav);
                vozidlo.setTurnus(gturnus);
                vozidlo.setDatum(gdatum);

                reff.setValue(vozidlo, String.valueOf(vozidlo.getEvc()));
                Toast.makeText(InformacieOVozidle.this, "Done", Toast.LENGTH_LONG).show();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff = FirebaseDatabase.getInstance().getReference()
                        .child("Vozidlo").child(String.valueOf(mapka));
                reff.removeValue();
                System.out.println("odstranene");

                Toast.makeText(InformacieOVozidle.this, "Odstranene", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setValues() {
        reff = FirebaseDatabase.getInstance().getReference().child("Vozidlo").child(String.valueOf(mapka));
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("evc").exists()) {
                    Intent intent = new Intent(InformacieOVozidle.this, TrolejbusVyhladavanie.class);
                    startActivity(intent);

                } else {
                    String styp = dataSnapshot.child("typ").getValue().toString();
                    String sstk = dataSnapshot.child("stk").getValue().toString();
                    String sturnus = dataSnapshot.child("turnus").getValue().toString();
                    String sdatum = dataSnapshot.child("datum").getValue().toString();
                    String sstav = dataSnapshot.child("stav").getValue().toString();

                    typ.setText(styp);
                    stk.setText(sstk);
                    turnus.setText(sturnus);
                    datum.setText(sdatum);
                    stav.setText(sstav);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void datePicker() {
        stk = findViewById(R.id.stk);
        stk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InformacieOVozidle.this, android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                stk.setText(date);
            }
        };
    }

}