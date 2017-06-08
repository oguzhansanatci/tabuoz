package com.example.staha.tabuoz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kerim on 11/05/2017.
 */

public class BTListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ArrayList> bt_list;

    public BTListAdapter(Context mContext, List<ArrayList> bt_list) {
        this.mContext = mContext;
        this.bt_list = bt_list;
    }

    @Override
    public int getCount() {
        return bt_list.size();
    }

    @Override
    public Object getItem(int position) {
        return bt_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.bt_item, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_name);
        tv.setText(bt_list.get(position).toString());
        return null;
    }
}