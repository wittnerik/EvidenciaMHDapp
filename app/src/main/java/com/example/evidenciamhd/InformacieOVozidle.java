package com.example.evidenciamhd;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class InformacieOVozidle extends AppCompatActivity {

    private TextView ecv, stk;
    EditText typ;
    Button button;
    DatabaseReference reff;
    Vozidlo vozidlo;
    int mapka;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacie_o_vozidle);

        ecv= findViewById(R.id.ecv);
        typ = findViewById(R.id.typ);
        stk = findViewById(R.id.stk);
        button = findViewById(R.id.button);
        vozidlo = new Vozidlo();
        FirebaseApp.initializeApp(this);
        reff = FirebaseDatabase.getInstance().getReference().child("Vozidlo");


        mapka = getIntent().getExtras().getInt("mapka");

        setValues();
        datePicker();

        ecv.setText(String.valueOf(mapka));

        vozidlo.setEvc(mapka);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gtyp = typ.getText().toString();
                String gstk = stk.getText().toString();

                vozidlo.setTyp(gtyp);
                vozidlo.setStk(gstk);

                reff.setValue(vozidlo, String.valueOf(vozidlo.getEvc()));
                Toast.makeText(InformacieOVozidle.this, "Done", Toast.LENGTH_LONG).show();
            }
        });




    }

    public void setValues(){
        reff = FirebaseDatabase.getInstance().getReference().child("Vozidlo").child(String.valueOf(mapka));
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String styp = dataSnapshot.child("typ").getValue().toString();
                String sstk = dataSnapshot.child("stk").getValue().toString();

                typ.setText(styp);
                stk.setText(sstk);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void datePicker(){
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
                month = month + 1 ;
                String date = dayOfMonth + "/" + month + "/" + year;
                stk.setText(date);
            }
        };
    }



}
