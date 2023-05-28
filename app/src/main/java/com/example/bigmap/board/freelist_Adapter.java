package com.example.bigmap.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigmap.R;

import java.util.ArrayList;
import java.util.List;

public class freelist_Adapter extends BaseAdapter {
    private Context context;
    private List<freelist_item> listViewItemList;

    public freelist_Adapter(Context context, List<freelist_item> itemList) {
        this.context=context;
        this.listViewItemList = itemList;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.freelist_item, parent, false);
        }

        freelist_item item = listViewItemList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.view_title);
        TextView userTextView = convertView.findViewById(R.id.user);
        TextView timeTextView = convertView.findViewById(R.id.time_text_view);

        titleTextView.setText(item.getTitle());
        userTextView.setText(item.getUser());
        timeTextView.setText(item.getTime());

        return convertView;
    }

    public void addItem(String title, String user, String time) {
        freelist_item item = new freelist_item(title, user, time);
        listViewItemList.add(item);
        notifyDataSetChanged();
    }

}
