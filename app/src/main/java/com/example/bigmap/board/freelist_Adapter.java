package com.example.bigmap.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigmap.R;

import java.util.ArrayList;

public class freelist_Adapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<freelist_item> sample;

    public freelist_Adapter(Context context, ArrayList<freelist_item> data){
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount(){
        return sample.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public freelist_item getItem(int position){
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = mLayoutInflater.inflate(R.layout.freelist_item, null);
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height=250;
        convertView.setLayoutParams(layoutParams);

        TextView view_title = (TextView)convertView.findViewById(R.id.view_title);
        TextView user = (TextView)convertView.findViewById(R.id.user);
        TextView time_text_view = (TextView)convertView.findViewById(R.id.time_text_view);

        view_title.setText(sample.get(position).getView_title());
        user.setText(sample.get(position).getUser());
        time_text_view.setText(sample.get(position).getTime_text_view());

        return convertView;
    }

}
