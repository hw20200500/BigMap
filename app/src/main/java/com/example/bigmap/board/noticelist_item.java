package com.example.bigmap.board;

public class noticelist_item {
    private String postId;
    private String 제목;
    private String 작성자;
    private String 작성_시간_날짜;

    public noticelist_item(String postId, String title, String user, String time){
        this.postId = postId;
        this.제목 = title;
        this.작성자 = user;
        this.작성_시간_날짜 = time;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return 제목;
    }

    public String getUser() {
        return 작성자;
    }

    public String getTime() {
        return 작성_시간_날짜;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setTitle(String title) {
        this.제목 = title;
    }

    public void setUser(String user) {
        this.작성자 = user;
    }

    public void setTime(String time) {
        this.작성_시간_날짜 = time;
    }
}
