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
public class PenjualanDetail {

    private final StringProperty noPenjualan = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final StringProperty satuan = new SimpleStringProperty();
    private final DoubleProperty qty = new SimpleDoubleProperty();
    private final DoubleProperty qtyKeluar = new SimpleDoubleProperty();
    private final DoubleProperty nilai = new SimpleDoubleProperty();
    private final DoubleProperty totalNilai = new SimpleDoubleProperty();
    private final DoubleProperty harga = new SimpleDoubleProperty();
    private final DoubleProperty totalHarga = new SimpleDoubleProperty();
    private PenjualanHead penjualanHead;
    private Barang barang;

    public double getQtyKeluar() {
        return qtyKeluar.get();
    }

    public void setQtyKeluar(double value) {
        qtyKeluar.set(value);
    }

    public DoubleProperty qtyKeluarProperty() {
        return qtyKeluar;
    }

    public double getTotalHarga() {
        return totalHarga.get();
    }

    public void setTotalHarga(double value) {
        totalHarga.set(value);
    }

    public DoubleProperty totalHargaProperty() {
        return totalHarga;
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
    
    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }
    
    
    public PenjualanHead getPenjualanHead() {
        return penjualanHead;
    }

    public void setPenjualanHead(PenjualanHead penjualanHead) {
        this.penjualanHead = penjualanHead;
    }
    

    public double getHarga() {
        return harga.get();
    }

    public void setHarga(double value) {
        harga.set(value);
    }

    public DoubleProperty hargaProperty() {
        return harga;
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

    public String getNoPenjualan() {
        return noPenjualan.get();
    }

    public void setNoPenjualan(String value) {
        noPenjualan.set(value);
    }

    public StringProperty noPenjualanProperty() {
        return noPenjualan;
    }
    
}
