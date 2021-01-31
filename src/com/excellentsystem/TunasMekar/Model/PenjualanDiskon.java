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
public class PenjualanDiskon {

    private final StringProperty noPenjualan = new SimpleStringProperty();
    private final StringProperty kodeDiskon = new SimpleStringProperty();
    private final StringProperty namaDiskon = new SimpleStringProperty();
    private final DoubleProperty diskonPersen = new SimpleDoubleProperty();
    private final DoubleProperty diskonRp = new SimpleDoubleProperty();
    private final IntegerProperty qty = new SimpleIntegerProperty();
    private final DoubleProperty totalDiskon = new SimpleDoubleProperty();

    public double getTotalDiskon() {
        return totalDiskon.get();
    }

    public void setTotalDiskon(double value) {
        totalDiskon.set(value);
    }

    public DoubleProperty totalDiskonProperty() {
        return totalDiskon;
    }

    public int getQty() {
        return qty.get();
    }

    public void setQty(int value) {
        qty.set(value);
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public double getDiskonRp() {
        return diskonRp.get();
    }

    public void setDiskonRp(double value) {
        diskonRp.set(value);
    }

    public DoubleProperty diskonRpProperty() {
        return diskonRp;
    }

    public double getDiskonPersen() {
        return diskonPersen.get();
    }

    public void setDiskonPersen(double value) {
        diskonPersen.set(value);
    }

    public DoubleProperty diskonPersenProperty() {
        return diskonPersen;
    }

    public String getNamaDiskon() {
        return namaDiskon.get();
    }

    public void setNamaDiskon(String value) {
        namaDiskon.set(value);
    }

    public StringProperty namaDiskonProperty() {
        return namaDiskon;
    }

    public String getKodeDiskon() {
        return kodeDiskon.get();
    }

    public void setKodeDiskon(String value) {
        kodeDiskon.set(value);
    }

    public StringProperty kodeDiskonProperty() {
        return kodeDiskon;
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
