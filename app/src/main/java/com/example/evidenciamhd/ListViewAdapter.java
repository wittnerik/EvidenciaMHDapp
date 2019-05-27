package com.example.evidenciamhd;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Integer> arraylist;

    public ListViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        arraylist = new ArrayList<>();
        arraylist.addAll(TrolejbusVyhladavanie.vozidloArrayList);
        System.out.println("zaciatok " + arraylist.size());
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        System.out.println("size: " + TrolejbusVyhladavanie.vozidloArrayList.size());
        System.out.println("arraylist size: " + arraylist.size());
        return TrolejbusVyhladavanie.vozidloArrayList.size();

    }

    @Override
    public Integer getItem(int position) {
        return TrolejbusVyhladavanie.vozidloArrayList.get(position).intValue();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            holder.name =  view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText("" + TrolejbusVyhladavanie.vozidloArrayList.get(position));
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        TrolejbusVyhladavanie.vozidloArrayList.clear();
        Log.d("vsetky", "ahoj");
        if (charText.length() == 0) {
            TrolejbusVyhladavanie.vozidloArrayList.addAll(arraylist);
            System.out.println("vsetky");
        } else {
            for (int wp : arraylist) {
                String sp = String.valueOf(wp);
                if (sp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    TrolejbusVyhladavanie.vozidloArrayList.add(wp);

                }
            }
        }
        notifyDataSetChanged();
    }

}