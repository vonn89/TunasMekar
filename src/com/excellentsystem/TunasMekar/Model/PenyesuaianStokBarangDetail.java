/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class PenyesuaianStokBarangDetail {

    private final StringProperty noPenyesuaian = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final DoubleProperty qty = new SimpleDoubleProperty();
    private final DoubleProperty qtyStok = new SimpleDoubleProperty();
    private final StringProperty satuan = new SimpleStringProperty();
    private final DoubleProperty nilai = new SimpleDoubleProperty();
    private final DoubleProperty totalNilai = new SimpleDoubleProperty();
    private PenyesuaianStokBarangHead penyesuaianStokBarangHead;

    public PenyesuaianStokBarangHead getPenyesuaianStokBarangHead() {
        return penyesuaianStokBarangHead;
    }

    public void setPenyesuaianStokBarangHead(PenyesuaianStokBarangHead penyesuaianStokBarangHead) {
        this.penyesuaianStokBarangHead = penyesuaianStokBarangHead;
    }
    
    public double getQtyStok() {
        return qtyStok.get();
    }

    public void setQtyStok(double value) {
        qtyStok.set(value);
    }

    public DoubleProperty qtyStokProperty() {
        return qtyStok;
    }

    public double getTotalNilai() {
        return totalNilai.get();
    }

    public void setTotalNilai(double value) {
        totalNilai.set(value);
    }

    public DoubleProperty totalNilaiProperty() {
        return totalNilai;
    }

    private Barang barang;

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
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

    public double getNilai() {
        return nilai.get();
    }

    public void setNilai(double value) {
        nilai.set(value);
    }

    public DoubleProperty nilaiProperty() {
        return nilai;
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

    public double getQty() {
        return qty.get();
    }

    public void setQty(double value) {
        qty.set(value);
    }

    public DoubleProperty qtyProperty() {
        return qty;
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


    public String getNoPenyesuaian() {
        return noPenyesuaian.get();
    }

    public void setNoPenyesuaian(String value) {
        noPenyesuaian.set(value);
    }

    public StringProperty noPenyesuaianProperty() {
        return noPenyesuaian;
    }
    
}
