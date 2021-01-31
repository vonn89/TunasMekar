/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author excellent
 */
public class PenyesuaianStokBarangHead {

    private final StringProperty noPenyesuaian = new SimpleStringProperty();
    private final StringProperty tglPenyesuaian = new SimpleStringProperty();
    private final StringProperty jenisPenyesuaian = new SimpleStringProperty();
    private final StringProperty catatan = new SimpleStringProperty();
    private final StringProperty kodeUser = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty userBatal = new SimpleStringProperty();

    private List<PenyesuaianStokBarangDetail> listPenyesuaianStokBarangDetail;

    public List<PenyesuaianStokBarangDetail> getListPenyesuaianStokBarangDetail() {
        return listPenyesuaianStokBarangDetail;
    }

    public void setListPenyesuaianStokBarangDetail(List<PenyesuaianStokBarangDetail> listPenyesuaianStokBarangDetail) {
        this.listPenyesuaianStokBarangDetail = listPenyesuaianStokBarangDetail;
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

    public String getKodeUser() {
        return kodeUser.get();
    }

    public void setKodeUser(String value) {
        kodeUser.set(value);
    }

    public StringProperty kodeUserProperty() {
        return kodeUser;
    }

    public String getCatatan() {
        return catatan.get();
    }

    public void setCatatan(String value) {
        catatan.set(value);
    }

    public StringProperty catatanProperty() {
        return catatan;
    }

    public String getJenisPenyesuaian() {
        return jenisPenyesuaian.get();
    }

    public void setJenisPenyesuaian(String value) {
        jenisPenyesuaian.set(value);
    }

    public StringProperty jenisPenyesuaianProperty() {
        return jenisPenyesuaian;
    }

    public String getTglPenyesuaian() {
        return tglPenyesuaian.get();
    }

    public void setTglPenyesuaian(String value) {
        tglPenyesuaian.set(value);
    }

    public StringProperty tglPenyesuaianProperty() {
        return tglPenyesuaian;
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
