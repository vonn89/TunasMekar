/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author yunaz
 */
public class Barang {

    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final StringProperty supplier = new SimpleStringProperty();
    private final IntegerProperty stokMinimal = new SimpleIntegerProperty();
    private final DoubleProperty hargaBeli = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();
    private List<Satuan> allSatuan;
    private Satuan satuan;
    private StokBarang stokAkhir;

    public double getHargaBeli() {
        return hargaBeli.get();
    }

    public void setHargaBeli(double value) {
        hargaBeli.set(value);
    }

    public DoubleProperty hargaBeliProperty() {
        return hargaBeli;
    }

    public StokBarang getStokAkhir() {
        return stokAkhir;
    }

    public void setStokAkhir(StokBarang stokAkhir) {
        this.stokAkhir = stokAkhir;
    }
    
    public int getStokMinimal() {
        return stokMinimal.get();
    }

    public void setStokMinimal(int value) {
        stokMinimal.set(value);
    }

    public IntegerProperty stokMinimalProperty() {
        return stokMinimal;
    }

    public String getSupplier() {
        return supplier.get();
    }

    public void setSupplier(String value) {
        supplier.set(value);
    }

    public StringProperty supplierProperty() {
        return supplier;
    }
    
    public Satuan getSatuan() {
        return satuan;
    }

    public void setSatuan(Satuan satuan) {
        this.satuan = satuan;
    }
    


    public List<Satuan> getAllSatuan() {
        return allSatuan;
    }

    public void setAllSatuan(List<Satuan> allSatuan) {
        this.allSatuan = allSatuan;
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



    public String getNamaBarang() {
        return namaBarang.get();
    }

    public void setNamaBarang(String value) {
        namaBarang.set(value);
    }

    public StringProperty namaBarangProperty() {
        return namaBarang;
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
