package com.example.bigmap.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigmap.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class comment_Adapter extends BaseAdapter {
    private List<Map<String, String>> listViewItemList = new ArrayList<>();
    private Context context;

    public comment_Adapter(Context context, List<Map<String, String>> itemList) {
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

        TextView userTextView = convertView.findViewById(R.id.user);
        TextView commentTextView = convertView.findViewById(R.id.comment);
        TextView timeTextView = convertView.findViewById(R.id.time_text_view);

        Map<String, String> comment = listViewItemList.get(position);
        String user = comment.get("user");
        String commentText = comment.get("comment");
        String time = comment.get("timestamp");

        userTextView.setText(user);
        commentTextView.setText(commentText);
        timeTextView.setText(time);

        return convertView;
    }

    public void addItem(String user, String comment, String time) {
        Map<String, String> commentMap = new HashMap<>();
        commentMap.put("user", user);
        commentMap.put("comment", comment);
        commentMap.put("timestamp", time);
        listViewItemList.add(commentMap);
    }
}
