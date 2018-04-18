package com.tcn.models;

import java.io.Serializable;

/**
 * Created by MyPC on 14/03/2018.
 */

public class RoomModels implements Serializable{
    public static final int PHONG_SACH = 0;
    public static final int PHONG_BAN = 1;
    public  static final int PHONG_DANG_SUA = 2;

    public static final int VIP = 0;
    public static final int LOVE = 1;
    public static final int CO_BAN = 2;
    public static final int DORM = 3;

    public static final int CHUA_DAT = 0;
    public static final int DA_DAT = 1;

    public static final int PRICE_ROOM = 200000;

    private int id;
    private String roomNumber;
    private int roomType;
    private int reserve;
    private int roomPlan;
    private String checkInDate;
    private String checkOutDate;
    private String totalPrice;
    private String roomCharge;
    private String moneyService;
    private int numberPeople;

    public RoomModels(int id, String roomNumber, int roomType, int reserve, int roomPlan, String checkInDate, String checkOutDate, String totalPrice, String roomCharge, String moneyService, int numberPeople) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.reserve = reserve;
        this.roomPlan = roomPlan;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.roomCharge = roomCharge;
        this.moneyService = moneyService;
        this.numberPeople = numberPeople;
    }

    public int getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(int numberPeople) {
        this.numberPeople = numberPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public int getRoomPlan() {
        return roomPlan;
    }

    public void setRoomPlan(int roomPlan) {
        this.roomPlan = roomPlan;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(String roomCharge) {
        this.roomCharge = roomCharge;
    }

    public String getMoneyService() {
        return moneyService;
    }

    public void setMoneyService(String moneyService) {
        this.moneyService = moneyService;
    }
}
