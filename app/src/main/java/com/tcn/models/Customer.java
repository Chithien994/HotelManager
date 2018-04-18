package com.tcn.models;

import java.io.Serializable;

/**
 * Created by MyPC on 15/03/2018.
 */

public class Customer implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String cmt;
    private int idRoom;

    public Customer(int id, String name, String phone, String cmt, int idRoom) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.cmt = cmt;
        this.idRoom = idRoom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }
}
