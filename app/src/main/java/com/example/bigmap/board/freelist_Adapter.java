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
    private TextView titleTextView;
    private TextView userTextView;
    private TextView timeTextView;

    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<freelist_item> listViewItemList = new ArrayList<freelist_item>();

    //ListVIewAdapter의 생성자
    public freelist_Adapter(){

    }
    //adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount(){
        return listViewItemList.size();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.freelist_item, parent, false);
        }
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height=250;
        convertView.setLayoutParams(layoutParams);
//화면에 표시될 view로부터 위젯에 대한 참조 획득
        titleTextView = (TextView)convertView.findViewById(R.id.view_title);
        userTextView = (TextView)convertView.findViewById(R.id.user);
        timeTextView = (TextView)convertView.findViewById(R.id.time_text_view);

//아이템 내 위젯에 반영
        titleTextView.setText(freelist_item.get(position).getView_title());
        userTextView.setText(freelist_item.get(position).getUser());
        timeTextView.setText(freelist_item.get(position).getTime_text_view());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return freelist_item.get(position);
    }

    public void addItem(String title, String user, String time){
        freelist_item item = new freelist_item();

        item.setTitle(title);
        item.setUser(user);
        item.setTime(time);

        listViewItemList.add(item);
    }

}
