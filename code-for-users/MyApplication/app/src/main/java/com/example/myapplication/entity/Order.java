package com.example.myapplication.entity;

public class Order {
    private int orderId;
    private int parkId;
    private int userId;
    private String startTime;
    private String endTime;
    private double totalTime;
    private double parkPrice;
    private double totalPrice;

    private String parkDesc;

    private String ownerNickname;

    private String park_loca;
    private int status;
    private String park_pic;

    public String getPark_pic() {
        return park_pic;
    }

    public void setPark_pic(String park_pic) {
        this.park_pic = park_pic;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", parkId=" + parkId +
                ", userId=" + userId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", totalTime=" + totalTime +
                ", parkPrice=" + parkPrice +
                ", totalPrice=" + totalPrice +
                ", parkDesc='" + parkDesc + '\'' +
                ", ownerNickname='" + ownerNickname + '\'' +
                ", park_loca='" + park_loca + '\'' +
                ", status=" + status +
                ", park_pic='" + park_pic + '\'' +
                '}';
    }

    public Order(int orderId, int parkId, int userId, String startTime, String endTime, double totalTime, double parkPrice, double totalPrice, String parkDesc, String ownerNickname, String park_loca, int status, String park_pic) {
        this.orderId = orderId;
        this.parkId = parkId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.parkPrice = parkPrice;
        this.totalPrice = totalPrice;
        this.parkDesc = parkDesc;
        this.ownerNickname = ownerNickname;
        this.park_loca = park_loca;
        this.status = status;
        this.park_pic = park_pic;
    }


    public Order() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getParkPrice() {
        return parkPrice;
    }

    public void setParkPrice(double parkPrice) {
        this.parkPrice = parkPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getParkDesc() {
        return parkDesc;
    }

    public void setParkDesc(String parkDesc) {
        this.parkDesc = parkDesc;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public String getPark_loca() {
        return park_loca;
    }

    public void setPark_loca(String park_loca) {
        this.park_loca = park_loca;
    }


}
