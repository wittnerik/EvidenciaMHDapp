package com.example.evidenciamhd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrolejbusVyhladavanie extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView list;
    private ListViewAdapter adapter;
    private SearchView editsearch;
    private String[] EvcList;
    private DatabaseReference reff;
    private Vozidlo vozidlo;
    public static ArrayList<Integer> vozidloArrayList = new ArrayList<>();

    int mhd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trolejbusy_vzhladavanie);

        FirebaseApp.initializeApp(this);
        reff = FirebaseDatabase.getInstance().getReference("Vozidlo");
        vozidlo = new Vozidlo();
        list =  findViewById(R.id.listview);
        vozidloArrayList = new ArrayList<>();;

        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        mhd = getIntent().getExtras().getInt("mhd");

        System.out.println("mhd "+mhd);


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    vozidlo = ds.getValue(Vozidlo.class);
                    if(mhd==2) {
                        if (vozidlo.getEvc()>300&&vozidlo.getEvc()<400||vozidlo.getEvc()>=3428&&vozidlo.getEvc()<=3437){
                            vozidloArrayList.add(vozidlo.getEvc());
                        }


                    }
                    if (mhd==1){
                        if(vozidlo.getEvc()<=123||vozidlo.getEvc()>=701&&vozidlo.getEvc()<=736){
                            vozidloArrayList.add(vozidlo.getEvc());
                        }
                    }
                    if(mhd==3){
                        if(vozidlo.getEvc()>=9000){
                            vozidloArrayList.add(vozidlo.getEvc());
                        }
                    }
                    //vozidloArrayList.add(vozidlo.getEvc());
                    System.out.println(vozidlo.getEvc());
                    System.out.println(vozidlo.getEvc());

                    // Pass results to ListViewAdapter Class
                    adapter = new ListViewAdapter(TrolejbusVyhladavanie.this);

                    list.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Locate the EditText in listview_main.xml

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int mapka=vozidloArrayList.get(position).intValue();
                System.out.println(mapka);

                Toast.makeText(TrolejbusVyhladavanie.this, "" + vozidloArrayList.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TrolejbusVyhladavanie.this, InformacieOVozidle.class);
                intent.putExtra("mapka",mapka);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.filter(newText);
        return false;
    }
}





