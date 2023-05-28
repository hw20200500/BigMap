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

    public String getTitle()
    {
        return titleStr;
    }

    public String getUser()
    {
        return userStr;
    }

    public String getTime()
    {
        return timeStr;
    }

    public void setTitle(String title){
        this.titleStr=title;
    }
    public void setUser(String user){
        this.userStr = user;
    }
    public void setTime(String time){
        this.timeStr = time;
    }

}
