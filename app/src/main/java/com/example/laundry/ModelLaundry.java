package com.example.laundry;

import java.text.DecimalFormat;

public class ModelLaundry {
    String IdLaundry,alamat,informasi,jamBuka,jamTutup,jenis,latitudeLaundry,longitudeLaundry,nama_laundry,telepon;
    //String jarak;
    public Double jarak;
    //Float jrk;

    public ModelLaundry(){}

//    public ModelLaundry(Double jrk){
//        this.jarak = jrk;
//    }

    public ModelLaundry(Double jrk, String idLaundry, String alamat, String informasi, String jamBuka, String jamTutup, String jenis, String latitudeLaundry, String longitudeLaundry, String nama_laundry, String telepon) {
        this.IdLaundry = idLaundry;
        this.jarak = jrk;
        this.alamat = alamat;
        this.informasi = informasi;
        this.jamBuka = jamBuka;
        this.jamTutup = jamTutup;
        this.jenis = jenis;
        this.latitudeLaundry = latitudeLaundry;
        this.longitudeLaundry = longitudeLaundry;
        this.nama_laundry = nama_laundry;
        this.telepon = telepon;
    }

    public ModelLaundry(String idLaundry, String alamat, String informasi, String jamBuka, String jamTutup, String jenis, String latitudeLaundry, String longitudeLaundry, String nama_laundry, String telepon) {
        this.IdLaundry = idLaundry;
        this.alamat = alamat;
        this.informasi = informasi;
        this.jamBuka = jamBuka;
        this.jamTutup = jamTutup;
        this.jenis = jenis;
        this.latitudeLaundry = latitudeLaundry;
        this.longitudeLaundry = longitudeLaundry;
        this.nama_laundry = nama_laundry;
        this.telepon = telepon;
    }


    public String getIdLaundry() {
        return IdLaundry;
    }

    public String getTelepon() {
        return telepon;
    }



    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public void setIdLaundry(String idLaundry) {
        IdLaundry = idLaundry;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getInformasi() {
        return informasi;
    }

    public void setInformasi(String informasi) {
        this.informasi = informasi;
    }

    public String getJamBuka() {
        return jamBuka;
    }

    public void setJamBuka(String jamBuka) {
        this.jamBuka = jamBuka;
    }

    public String getJamTutup() {
        return jamTutup;
    }

    public void setJamTutup(String jamTutup) {
        this.jamTutup = jamTutup;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getLatitudeLaundry() {
        return latitudeLaundry;
    }

    public void setLatitudeLaundry(String latitudeLaundry) {
        this.latitudeLaundry = latitudeLaundry;
    }

    public String getLongitudeLaundry() {
        return longitudeLaundry;
    }

    public void setLongitudeLaundry(String longitudeLaundry) {
        this.longitudeLaundry = longitudeLaundry;
    }

    public String getNama_laundry() {
        return nama_laundry;
    }

    public void setNama_laundry(String nama_laundry) {
        this.nama_laundry = nama_laundry;
    }




//    public void Hitung(String latiUser, String longUser){
//        double lat2 = Double.parseDouble(latiUser);
//        double lon2= Double.parseDouble(longUser);
//        double lat1 = Double.parseDouble(latitudeLaundry);
//        double lon1= Double.parseDouble(longitudeLaundry);
//
//        Double pi = 3.14159265358979;
//        Double R = 6371e3;
//
//        Double latRad1 = lat1 * (pi / 180);
//        Double latRad2 = lat2 * (pi / 180);
//        Double deltaLatRad = (lat2 - lat1) * (pi / 180);
//        Double deltaLonRad = (lon2 - lon1) * (pi / 180);
//
//        /* RUMUS HAVERSINE */
//        Double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
//        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        Double s = (R * c)/1000; // hasil jarak dalam meter
//
//        DecimalFormat df = new DecimalFormat("#.##");
//
//        this.jarak_haversein = String.valueOf(df.format(s));
//
//    }

//    public void setJarak(String jarak){
//
//        this.jarak = jarak;
//    }
//
//    public String getJarak(){
//
//        return jarak;
//    }

    public void setJarak(Double jarak){
        DecimalFormat df = new DecimalFormat("#.##");
        String h = String.valueOf(df.format(jarak));
        Double x = Double.parseDouble(h);

        this.jarak = jarak;
    }

    public Double getJarak(){
        return jarak;
    }

//    String hasil;
//    public void setHasilHaversein(String hasil){
//        this.hasil = hasil;
//    }

//    public String getHasil(){
//        return hasil;
//    }
//    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//     firestore.collection("laundry")
//             .document("oPGd7d6EPFrUJxAKfr5t")
//                            .update("jarak","1,56");

//    public FirebaseFirestore getFirestore(String jarakk) {
//        firestore.collection("laundry").document("oPGd7d6EPFrUJxAKfr5t").update("jarak",jarakk);
//        return firestore;
//    }

//    public static Comparator<ModelLaundry> UrutanJarak = new Comparator<ModelLaundry>() {
//        @Override
//        public int compare(ModelLaundry modelLaundry, ModelLaundry t1) {
//            return modelLaundry.jarak.compareTo(t1.jarak);
//        }
//    };








}
