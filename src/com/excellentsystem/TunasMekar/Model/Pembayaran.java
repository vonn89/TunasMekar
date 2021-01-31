/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class Pembayaran {

    private final StringProperty noPembayaran = new SimpleStringProperty();
    private final StringProperty tglPembayaran = new SimpleStringProperty();
    private final StringProperty noTransaksi = new SimpleStringProperty();
    private final StringProperty jenisPembayaran = new SimpleStringProperty();
    private final StringProperty keterangan = new SimpleStringProperty();
    private final DoubleProperty jumlahPembayaran = new SimpleDoubleProperty();
    private final StringProperty kodeUser = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty userBatal = new SimpleStringProperty();

    public String getNoTransaksi() {
        return noTransaksi.get();
    }

    public void setNoTransaksi(String value) {
        noTransaksi.set(value);
    }

    public StringProperty noTransaksiProperty() {
        return noTransaksi;
    }

    public String getKodeUser() {
        return kodeUser.get();
    }

    public void setKodeUser(String value) {
        kodeUser.set(value);
    }

    public StringProperty kodeUserProperty() {
        return kodeUser;
    }

    public String getUserBatal() {
        return userBatal.get();
    }

    public void setUserBatal(String value) {
        userBatal.set(value);
    }

    public StringProperty userBatalProperty() {
        return userBatal;
    }

    public String getTglBatal() {
        return tglBatal.get();
    }

    public void setTglBatal(String value) {
        tglBatal.set(value);
    }

    public StringProperty tglBatalProperty() {
        return tglBatal;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }


    public double getJumlahPembayaran() {
        return jumlahPembayaran.get();
    }

    public void setJumlahPembayaran(double value) {
        jumlahPembayaran.set(value);
    }

    public DoubleProperty jumlahPembayaranProperty() {
        return jumlahPembayaran;
    }

    public String getKeterangan() {
        return keterangan.get();
    }

    public void setKeterangan(String value) {
        keterangan.set(value);
    }

    public StringProperty keteranganProperty() {
        return keterangan;
    }

    public String getJenisPembayaran() {
        return jenisPembayaran.get();
    }

    public void setJenisPembayaran(String value) {
        jenisPembayaran.set(value);
    }

    public StringProperty jenisPembayaranProperty() {
        return jenisPembayaran;
    }


    public String getTglPembayaran() {
        return tglPembayaran.get();
    }

    public void setTglPembayaran(String value) {
        tglPembayaran.set(value);
    }

    public StringProperty tglPembayaranProperty() {
        return tglPembayaran;
    }

    public String getNoPembayaran() {
        return noPembayaran.get();
    }

    public void setNoPembayaran(String value) {
        noPembayaran.set(value);
    }

    public StringProperty noPembayaranProperty() {
        return noPembayaran;
    }
    
}
