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
import java.util.Map;

public class comment_Adapter extends BaseAdapter {
    private List<comment_item> listViewItemList = new ArrayList<>();
    private Context context;

    public comment_Adapter() {

    }
    public comment_Adapter(Context context, List<comment_item> itemList) {
        this.context = context;
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.commentlist_item, parent, false);
        }

        comment_item item = listViewItemList.get(position);

        TextView userTextView = convertView.findViewById(R.id.commentuser);
        TextView commentTextView = convertView.findViewById(R.id.comment);
        TextView timeTextView = convertView.findViewById(R.id.time_text_view);


        userTextView.setText(item.getUser());
        commentTextView.setText(item.getComment());
        timeTextView.setText(item.getTime());

        return convertView;
    }

    public void addItem(String user, String comment, String time) {
        comment_item item = new comment_item(user, comment, time);
        listViewItemList.add(item);
        notifyDataSetChanged();
    }
}
