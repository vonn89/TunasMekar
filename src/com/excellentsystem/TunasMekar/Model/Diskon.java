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
public class Diskon {

    private final StringProperty kodeDiskon = new SimpleStringProperty();
    private final StringProperty namaDiskon = new SimpleStringProperty();
    private final DoubleProperty diskonPersen = new SimpleDoubleProperty();
    private final DoubleProperty diskonRp = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();


    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
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
    
}
