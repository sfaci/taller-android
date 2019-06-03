package com.sfaci.contacts.domain;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Representa uno de los contactos de la agenda
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class Friend {

    public Friend() {

    }

    private String name;
    private String email;
    private String telephone;
    private String mobile;
    private Bitmap picture;
    private Date birthDate;
    private float debts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public float getDebts() {
        return debts;
    }

    public void setDebts(float debts) {
        this.debts = debts;
    }

    @Override
    public String toString() {
        return name;
    }
}
