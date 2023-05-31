package com.example.bigmap.board;

public class comment_item {
    private String 작성자;
    private String 내용;
    private String 작성_시간_날짜;

    public comment_item(String user, String comment, String time){
        this.작성자 = user;
        this.내용 = comment;
        this.작성_시간_날짜 = time;
    }


    public String getComment() {
        return 내용;
    }

    public String getUser() {
        return 작성자;
    }

    public String getTime() {
        return 작성_시간_날짜;
    }



    public void setComment(String comment) {
        this.내용 = comment;
    }

    public void setUser(String user) {
        this.작성자 = user;
    }

    public void setTime(String time) {
        this.작성_시간_날짜 = time;
    }
}
