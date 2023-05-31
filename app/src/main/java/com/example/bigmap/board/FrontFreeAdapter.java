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

public class FrontFreeAdapter extends BaseAdapter {
    private Context context;
    private List<FrontFreeItem> listViewItemList;

    public FrontFreeAdapter(Context context, List<FrontFreeItem> itemList) {
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
            convertView = inflater.inflate(R.layout.front_free_item, parent, false);
        }

        FrontFreeItem item = listViewItemList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.view_title);

        titleTextView.setText(item.getTitle());

        return convertView;
    }

    public void addItem(String title) {
        FrontFreeItem item = new FrontFreeItem(title);
        listViewItemList.add(item);
        notifyDataSetChanged();
    }

}
