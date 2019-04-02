package com.example.evidenciamhd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrolejbusVyhladavanie extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView list;
    private ListViewAdapter adapter;
    private SearchView editsearch;
    private String[] EvcList;
    public static ArrayList<EVC> evcArrayList = new ArrayList<EVC>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trolejbusy_vzhladavanie);

        EvcList = new String[]{"701", "702", "703",
                "704", "705", "706", "707", "708",
                "709","710","711","712","713","714","715","716","717","718","719"};


        list = (ListView) findViewById(R.id.listview);

        evcArrayList = new ArrayList<>();

        for (int i = 0; i < EvcList.length; i++) {
            EVC movieNames = new EVC(EvcList[i]);
            // Binds all strings into an array
            evcArrayList.add(movieNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        //ci funguje git

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int mapka=Integer.parseInt(evcArrayList.get(position).getEvc().toString());
                System.out.println(mapka);

                Toast.makeText(TrolejbusVyhladavanie.this, evcArrayList.get(position).getEvc(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}





