package com.example.laundry;

public class ModelUser {
    String idUser,alamat,email,fName,latitude,longitude,password,telepon;

    public ModelUser(){}

    public ModelUser(String idUser, String alamat, String email, String fName, String latitude, String longitude, String password, String telepon) {
        this.idUser = idUser;
        this.alamat = alamat;
        this.email = email;
        this.fName = fName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
        this.telepon = telepon;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}
