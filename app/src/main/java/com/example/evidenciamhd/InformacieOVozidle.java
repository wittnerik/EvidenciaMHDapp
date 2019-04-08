package com.example.evidenciamhd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InformacieOVozidle extends AppCompatActivity {
    public TextView nazovMHD;
    public TextView textMHD;
    public ImageButton MHD;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacie_o_vozidle);
        Context c =getApplicationContext();
        nazovMHD=  (TextView)findViewById(R.id.nazovMHD);
        MHD = findViewById(R.id.MHD);
        int mapka = getIntent().getExtras().getInt("mapka");
        nazovMHD.setText(String.valueOf(mapka));
        MHD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }


    public void onRequestPermissionsResult() {


                    if(userChoosenTask.equals("Odfotiť"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Výber z galérie"))
                        galleryIntent();
                }




    private void selectImage() {
        final CharSequence[] items = { "Odfotiť", "Výber z galérie",
                "Naspäť" };

        AlertDialog.Builder builder = new AlertDialog.Builder(InformacieOVozidle.this);
        builder.setTitle("Pridaj fotku");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


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
