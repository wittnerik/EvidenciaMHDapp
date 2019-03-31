package com.example.evidenciamhd;

import android.content.Context;
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
    private ArrayList<EVC> arraylist;

    public ListViewAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<EVC>();
        this.arraylist.addAll(TrolejbusVyhladavanie.evcArrayList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return TrolejbusVyhladavanie.evcArrayList.size();
    }

    @Override
    public EVC getItem(int position) {
        return TrolejbusVyhladavanie.evcArrayList.get(position);
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
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(TrolejbusVyhladavanie.evcArrayList.get(position).getEvc());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        TrolejbusVyhladavanie.evcArrayList.clear();
        if (charText.length() == 0) {
            TrolejbusVyhladavanie.evcArrayList.addAll(arraylist);
        } else {
            for (EVC wp : arraylist) {
                if (wp.getEvc().toLowerCase(Locale.getDefault()).contains(charText)) {
                    TrolejbusVyhladavanie.evcArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}