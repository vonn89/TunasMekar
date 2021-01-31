/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author yunaz
 */
public class Satuan {

    private final StringProperty kodeBarang = new SimpleStringProperty();
    private final StringProperty kodeSatuan = new SimpleStringProperty();
    private final StringProperty kodeBarcode = new SimpleStringProperty();
    private final DoubleProperty qty = new SimpleDoubleProperty();
    private final DoubleProperty hargaRetail = new SimpleDoubleProperty();
    private final DoubleProperty hargaGrosir = new SimpleDoubleProperty();
    private final DoubleProperty hargaGrosirBesar = new SimpleDoubleProperty();
    private Barang barang;

    public double getHargaGrosirBesar() {
        return hargaGrosirBesar.get();
    }

    public void setHargaGrosirBesar(double value) {
        hargaGrosirBesar.set(value);
    }

    public DoubleProperty hargaGrosirBesarProperty() {
        return hargaGrosirBesar;
    }


    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }
    
    
    public double getHargaGrosir() {
        return hargaGrosir.get();
    }

    public void setHargaGrosir(double value) {
        hargaGrosir.set(value);
    }

    public DoubleProperty hargaGrosirProperty() {
        return hargaGrosir;
    }

    public double getHargaRetail() {
        return hargaRetail.get();
    }

    public void setHargaRetail(double value) {
        hargaRetail.set(value);
    }

    public DoubleProperty hargaRetailProperty() {
        return hargaRetail;
    }

    public String getKodeBarcode() {
        return kodeBarcode.get();
    }

    public void setKodeBarcode(String value) {
        kodeBarcode.set(value);
    }

    public StringProperty kodeBarcodeProperty() {
        return kodeBarcode;
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

    public String getKodeSatuan() {
        return kodeSatuan.get();
    }

    public void setKodeSatuan(String value) {
        kodeSatuan.set(value);
    }

    public StringProperty kodeSatuanProperty() {
        return kodeSatuan;
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
