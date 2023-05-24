package com.example.bigmap.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bigmap.R;

import java.util.List;

public class FrontQnaAdapter extends ArrayAdapter<String> {
    private List<String> itemList;

    public FrontQnaAdapter(@NonNull Context context, int resource, @NonNull List<String> itemList) {
        super(context, resource, itemList);
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        // 최대 3개의 아이템만 표시
        return Math.min(super.getCount(), 3);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.front_qna_item, null);

            holder = new ViewHolder();
            holder.titleTextView = view.findViewById(R.id.view_title);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String title = itemList.get(position);

        holder.titleTextView.setText(title);

        return view;
    }

    private static class ViewHolder {
        TextView titleTextView;
    }
}
