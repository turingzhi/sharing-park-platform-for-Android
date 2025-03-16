package com.example.myapplication.entity;

public class Park {
    int park_id;
    int owner_id;
    String park_name;
    String park_loca;
    Double park_price;
    int park_status;
    String park_descp;
    String owner_nickname;
    String park_pic;

    public Park(int park_id, int owner_id, String park_name, String park_loca, Double park_price, int park_status, String park_descp, String owner_nickname, String park_pic) {
        this.park_id = park_id;
        this.owner_id = owner_id;
        this.park_name = park_name;
        this.park_loca = park_loca;
        this.park_price = park_price;
        this.park_status = park_status;
        this.park_descp = park_descp;
        this.owner_nickname = owner_nickname;
        this.park_pic = park_pic;
    }

    @Override
    public String toString() {
        return "Park{" +
                "park_id=" + park_id +
                ", owner_id=" + owner_id +
                ", park_name='" + park_name + '\'' +
                ", park_loca='" + park_loca + '\'' +
                ", park_price=" + park_price +
                ", park_status=" + park_status +
                ", park_descp='" + park_descp + '\'' +
                ", owner_nickname='" + owner_nickname + '\'' +
                ", park_pic='" + park_pic + '\'' +
                '}';
    }

    public String getPark_pic() {
        return park_pic;
    }

    public void setPark_pic(String park_pic) {
        this.park_pic = park_pic;
    }


    public String getOwner_nickname() {
        return owner_nickname;
    }

    public void setOwner_nickname(String owner_nickname) {
        this.owner_nickname = owner_nickname;
    }

    public Park() {

    }


    public int getPark_status() {
        return park_status;
    }



    public void setPark_status(int park_status) {
        this.park_status = park_status;
    }



    public int getPark_id() {
        return park_id;
    }

    public void setPark_id(int park_id) {
        this.park_id = park_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getPark_name() {
        return park_name;
    }

    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }

    public String getPark_loca() {
        return park_loca;
    }

    public void setPark_loca(String park_loca) {
        this.park_loca = park_loca;
    }

    public Double getPark_price() {
        return park_price;
    }

    public void setPark_price(Double park_price) {
        this.park_price = park_price;
    }


    public String getPark_descp() {
        return park_descp;
    }

    public void setPark_descp(String park_descp) {
        this.park_descp = park_descp;
    }




}
