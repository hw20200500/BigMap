package com.example.bigmap.board;

public class freelist_item {
    private String view_title;
    private String user;
    private String time_text_view;

    public freelist_item(String view_title, String user, String time_text_view){
        this.view_title = view_title;
        this.user = user;
        this.time_text_view= time_text_view;
    }

    public String getView_title()
    {
        return this.view_title;
    }

    public String getUser()
    {
        return this.user;
    }

    public String getTime_text_view()
    {
        return this.time_text_view;
    }
}
