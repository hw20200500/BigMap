package com.example.bigmap.board;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bigmap.R;

import java.util.ArrayList;
import java.util.List;

public class noticelist_Adapter extends BaseAdapter {
    private Context context;
    private List<noticelist_item> listViewItemList;

    public noticelist_Adapter(Context context, List<noticelist_item> itemList) {
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
            convertView = inflater.inflate(R.layout.noticelist_item, parent, false);
        }

        noticelist_item item = listViewItemList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.view_title);
        TextView userTextView = convertView.findViewById(R.id.user);
        TextView timeTextView = convertView.findViewById(R.id.time_text_view);

        titleTextView.setText(item.getTitle());
        userTextView.setText(item.getUser());
        timeTextView.setText(item.getTime());

        // 클릭 이벤트 처리
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 아이템의 postId 가져오기
                String postId = item.getPostId();

                // board_free_detail 액티비티로 이동하면서 postId 값을 전달
                Intent intent = new Intent(context, board_free_detail.class);
                intent.putExtra("postId", postId);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addItem(String postId, String title, String user, String time) {
        noticelist_item item = new noticelist_item(postId, title, user, time);
        listViewItemList.add(item);
        notifyDataSetChanged();
    }


}
