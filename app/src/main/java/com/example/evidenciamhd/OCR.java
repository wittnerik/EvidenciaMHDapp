package com.example.evidenciamhd;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.regex.Pattern;

public class OCR extends AppCompatActivity {

    SurfaceView camera;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String result = "nic";
    SparseArray<TextBlock> items;
    Dialog dialog;
    TextView txtResult;
    Button ano, nie;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(camera.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr);
        dialog = new Dialog(this);

        camera = findViewById(R.id.surfaceView);
        textView = findViewById(R.id.text_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("OCR", "Detector dependencies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            camera.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(OCR.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(camera.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {

                @Override
                public void release() {}

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < items.size(); ++i) {
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }
                        textView.setText(stringBuilder);
                        String vysledok = verification();
                        Log.d("vysledok", "vysledok: " + vysledok);
                        if(!vysledok.equals("failed")){
                            showPopUp();
                        }

                    }
                }
            });
        }

    }

    public void showPopUp(){

        txtResult = dialog.findViewById(R.id.txtResultDrawable);
        ano = dialog.findViewById(R.id.anoButton);
        nie = dialog.findViewById(R.id.nieButton);

        dialog.setContentView(R.layout.pop_up);
        txtResult.setText(result);

        ano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mapka = Integer.parseInt(result);
                Intent intent = new Intent(OCR.this, InformacieOVozidle.class);
                intent.putExtra("mapka",mapka);
                startActivity(intent);
            }
        });

        nie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public String verification() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            stringBuilder.append(item.getValue());
            Log.d("item", "item: "+ stringBuilder);
            if (Pattern.matches("[0-9]{3}", stringBuilder.toString())) {
                result = stringBuilder.toString();
                Log.d("result", "vysledok po podmienke : " + result);
                return result;
            }
            stringBuilder.setLength(0);
        }
        return "failed";
    }



}
