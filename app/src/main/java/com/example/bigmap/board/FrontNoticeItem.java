package com.example.bigmap.board;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigmap.R;

public class FrontNoticeItem extends LinearLayout {
    private TextView textViewTitle;

    public FrontNoticeItem(Context context) {
        super(context);
        init(context);
    }

    public FrontNoticeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FrontNoticeItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.front_notice_item, this);
        textViewTitle = findViewById(R.id.view_title);
    }

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }
}
