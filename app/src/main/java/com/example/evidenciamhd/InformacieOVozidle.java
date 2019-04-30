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
    private TextView ecv, stk;
    private EditText typ;
    public ImageButton MHD;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button button, buttonDelete;
    DatabaseReference reff;
    Vozidlo vozidlo;
    int mapka;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacie_o_vozidle);
        ecv=findViewById(R.id.ecv);
        MHD = findViewById(R.id.MHD);
        mapka = getIntent().getExtras().getInt("mapka");
        ecv.setText(String.valueOf(mapka));
        MHD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        typ = findViewById(R.id.typ);
        stk = findViewById(R.id.stk);
        button = findViewById(R.id.button);
        buttonDelete = findViewById(R.id.buttonDelete);
        vozidlo = new Vozidlo();
        FirebaseApp.initializeApp(this); 
        reff = FirebaseDatabase.getInstance().getReference().child("Vozidlo");

        setValues();
        datePicker();

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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ecv.setText("");
                typ.setText("");
                stk.setText("");


                reff = FirebaseDatabase.getInstance().getReference()
                        .child("Vozidlo").child(String.valueOf(mapka));
                reff.removeValue();
                System.out.println("odstranene");

                Toast.makeText(InformacieOVozidle.this, "Odstranene", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setValues(){
        reff = FirebaseDatabase.getInstance().getReference().child("Vozidlo").child(String.valueOf(mapka));
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("evc").exists()){
                    Intent intent = new Intent(InformacieOVozidle.this, TrolejbusVyhladavanie.class);
                    startActivity(intent);

                }else {
                    String styp = dataSnapshot.child("typ").getValue().toString();
                    String sstk = dataSnapshot.child("stk").getValue().toString();

                    typ.setText(styp);
                    stk.setText(sstk);
                }
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

    private void selectImage() {
        final CharSequence[] items = { "Odfotiť", "Výber z galérie",
                "Naspäť" };

        AlertDialog.Builder builder = new AlertDialog.Builder(InformacieOVozidle.this);
        builder.setTitle("Pridaj fotku");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                String userChoosenTask;
                if (items[item].equals("Odfotiť")) {
                    userChoosenTask ="Odfotiť";
                        cameraIntent();

                } else if (items[item].equals("Výber z galérie")) {
                    userChoosenTask ="Výber z galérie";
                        galleryIntent();

                } else if (items[item].equals("Naspäť")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Vyber subor"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        MHD.setImageBitmap(Bitmap.createScaledBitmap( thumbnail, 120, 120, false));


        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".pngx");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MHD.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MHD.setImageBitmap(bm);
    }
}
