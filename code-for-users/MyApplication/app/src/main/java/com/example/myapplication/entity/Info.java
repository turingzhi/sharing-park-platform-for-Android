package com.example.myapplication.entity;

public class Info {
    private int info_id;
    private String info_title;
    private String info_desc;
    private String info_date;
    private String info_pic;

    public Info(int info_id, String info_title, String info_desc, String info_date, String info_pic) {
        this.info_id = info_id;
        this.info_title = info_title;
        this.info_desc = info_desc;
        this.info_date = info_date;
        this.info_pic = info_pic;
    }

    public String getInfo_date() {
        return info_date;
    }

    public void setInfo_date(String info_date) {
        this.info_date = info_date;
    }

    public String getInfo_pic() {
        return info_pic;
    }

    public void setInfo_pic(String info_pic) {
        this.info_pic = info_pic;
    }

//    public Info(int info_id, String info_title, String info_desc) {
//        this.info_id = info_id;
//        this.info_title = info_title;
//        this.info_desc = info_desc;
//    }

    @Override
    public String toString() {
        return "Info{" +
                "info_id=" + info_id +
                ", info_title='" + info_title + '\'' +
                ", info_desc='" + info_desc + '\'' +
                '}';
    }

    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }

    public String getInfo_title() {
        return info_title;
    }

    public void setInfo_title(String info_title) {
        this.info_title = info_title;
    }

    public String getInfo_desc() {
        return info_desc;
    }

    public void setInfo_desc(String info_desc) {
        this.info_desc = info_desc;
    }
}
