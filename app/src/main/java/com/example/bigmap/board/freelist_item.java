package com.example.bigmap.board;

public class freelist_item {
    private String titleStr;
    private String userStr;
    private String timeStr;

    public freelist_item(String title, String user, String time){
        this.titleStr = title;
        this.userStr = user;
        this.timeStr = time;
    }

    public void setTitle(String title){
        titleStr = title;
    }
    public void setUser(String user){
        userStr = user;
    }
    public void setTime(String time){
        timeStr = time;
    }

    public String getTitle()
    {
        return this.titleStr;
    }

    public String getUser()
    {
        return this.userStr;
    }

    public String getTime()
    {
        return this.timeStr;
    }
}
