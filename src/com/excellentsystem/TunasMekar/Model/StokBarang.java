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
 * @author yunaz
 */
public class StokBarang {

    private final StringProperty tanggal = new SimpleStringProperty();
    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final DoubleProperty stokAwal = new SimpleDoubleProperty();
    private final DoubleProperty stokMasuk = new SimpleDoubleProperty();
    private final DoubleProperty stokKeluar = new SimpleDoubleProperty();
    private final DoubleProperty stokAkhir = new SimpleDoubleProperty();
    private Barang barang;
    
    public double getStokAwal() {
        return stokAwal.get();
    }

    public void setStokAwal(double value) {
        stokAwal.set(value);
    }

    public DoubleProperty stokAwalProperty() {
        return stokAwal;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }
    

    public String getTanggal() {
        return tanggal.get();
    }

    public void setTanggal(String value) {
        tanggal.set(value);
    }

    public StringProperty tanggalProperty() {
        return tanggal;
    }

    public double getStokAkhir() {
        return stokAkhir.get();
    }

    public void setStokAkhir(double value) {
        stokAkhir.set(value);
    }

    public DoubleProperty stokAkhirProperty() {
        return stokAkhir;
    }


    public double getStokKeluar() {
        return stokKeluar.get();
    }

    public void setStokKeluar(double value) {
        stokKeluar.set(value);
    }

    public DoubleProperty stokKeluarProperty() {
        return stokKeluar;
    }


    public double getStokMasuk() {
        return stokMasuk.get();
    }

    public void setStokMasuk(double value) {
        stokMasuk.set(value);
    }

    public DoubleProperty stokMasukProperty() {
        return stokMasuk;
    }
    
    public String getKodeBarang() {
        return kodeBarang.get();
    }

    public void setKodeBarang(String value) {
        kodeBarang.set(value);
    }

    public StringProperty kodeBarangProperty() {
        return kodeBarang;
    }

    
}
