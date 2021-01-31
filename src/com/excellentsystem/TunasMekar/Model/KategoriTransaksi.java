/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class KategoriTransaksi {

    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty kategoriKeuangan = new SimpleStringProperty();
    private final StringProperty jenisTransaksi = new SimpleStringProperty();

    public String getJenisTransaksi() {
        return jenisTransaksi.get();
    }

    public void setJenisTransaksi(String value) {
        jenisTransaksi.set(value);
    }

    public StringProperty jenisTransaksiProperty() {
        return jenisTransaksi;
    }
    

    public String getKategoriKeuangan() {
        return kategoriKeuangan.get();
    }

    public void setKategoriKeuangan(String value) {
        kategoriKeuangan.set(value);
    }

    public StringProperty kategoriKeuanganProperty() {
        return kategoriKeuangan;
    }

    public String getKodeKategori() {
        return kodeKategori.get();
    }

    public void setKodeKategori(String value) {
        kodeKategori.set(value);
    }

    public StringProperty kodeKategoriProperty() {
        return kodeKategori;
    }
    
}
