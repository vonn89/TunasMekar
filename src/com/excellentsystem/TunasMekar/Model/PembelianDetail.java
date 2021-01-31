/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ASUS
 */
public class PembelianDetail {

    private final StringProperty noPembelian = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final StringProperty satuan = new SimpleStringProperty();
    private final DoubleProperty qty = new SimpleDoubleProperty();
    private final DoubleProperty qtyMasuk = new SimpleDoubleProperty();
    private final DoubleProperty hargaBeli = new SimpleDoubleProperty();
    private final DoubleProperty hargaPpn = new SimpleDoubleProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();
    private PembelianHead pembelianHead;
    private Barang barang;
    private final BooleanProperty ppn = new SimpleBooleanProperty();

    public boolean isPpn() {
        return ppn.get();
    }

    public void setPpn(boolean value) {
        ppn.set(value);
    }

    public BooleanProperty ppnProperty() {
        return ppn;
    }

    public double getHargaPpn() {
        return hargaPpn.get();
    }

    public void setHargaPpn(double value) {
        hargaPpn.set(value);
    }

    public DoubleProperty hargaPpnProperty() {
        return hargaPpn;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }
    
    public PembelianHead getPembelianHead() {
        return pembelianHead;
    }

    public void setPembelianHead(PembelianHead pembelianHead) {
        this.pembelianHead = pembelianHead;
    }
    
    public double getTotal() {
        return total.get();
    }

    public void setTotal(double value) {
        total.set(value);
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public double getHargaBeli() {
        return hargaBeli.get();
    }

    public void setHargaBeli(double value) {
        hargaBeli.set(value);
    }

    public DoubleProperty hargaBeliProperty() {
        return hargaBeli;
    }

    public double getQtyMasuk() {
        return qtyMasuk.get();
    }

    public void setQtyMasuk(double value) {
        qtyMasuk.set(value);
    }

    public DoubleProperty qtyMasukProperty() {
        return qtyMasuk;
    }

    public double getQty() {
        return qty.get();
    }

    public void setQty(double value) {
        qty.set(value);
    }

    public DoubleProperty qtyProperty() {
        return qty;
    }

    public String getSatuan() {
        return satuan.get();
    }

    public void setSatuan(String value) {
        satuan.set(value);
    }

    public StringProperty satuanProperty() {
        return satuan;
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

    public String getKodeBarang() {
        return kodeBarang.get();
    }

    public void setKodeBarang(String value) {
        kodeBarang.set(value);
    }

    public StringProperty kodeBarangProperty() {
        return kodeBarang;
    }

    public int getNoUrut() {
        return noUrut.get();
    }

    public void setNoUrut(int value) {
        noUrut.set(value);
    }

    public IntegerProperty noUrutProperty() {
        return noUrut;
    }

    public String getNoPembelian() {
        return noPembelian.get();
    }

    public void setNoPembelian(String value) {
        noPembelian.set(value);
    }

    public StringProperty noPembelianProperty() {
        return noPembelian;
    }
    
}
