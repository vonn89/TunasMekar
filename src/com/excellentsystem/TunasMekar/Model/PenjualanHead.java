/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Model;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Excellent
 */
public class PenjualanHead {

    private final StringProperty noPenjualan = new SimpleStringProperty();
    private final StringProperty tglPenjualan = new SimpleStringProperty();
    private final StringProperty kategoriPenjualan = new SimpleStringProperty();
    private final StringProperty pelanggan = new SimpleStringProperty();
    
    private final DoubleProperty totalPenjualan = new SimpleDoubleProperty();
    private final DoubleProperty totalDiskon = new SimpleDoubleProperty();
    private final DoubleProperty grandtotal = new SimpleDoubleProperty();
    private final DoubleProperty pembayaran = new SimpleDoubleProperty();
    private final DoubleProperty sisaPembayaran = new SimpleDoubleProperty();
    
    private final StringProperty kodeUser = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty userBatal = new SimpleStringProperty();
    private List<PenjualanDetail> listPenjualanDetail;
    private List<Pembayaran> listPembayaran;
    private List<PenjualanDiskon> listPenjualanDiskon;
    private Pelanggan customer;
    private final BooleanProperty selected = new SimpleBooleanProperty();

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public Pelanggan getCustomer() {
        return customer;
    }

    public void setCustomer(Pelanggan customer) {
        this.customer = customer;
    }
    
    public List<PenjualanDiskon> getListPenjualanDiskon() {
        return listPenjualanDiskon;
    }

    public void setListPenjualanDiskon(List<PenjualanDiskon> listPenjualanDiskon) {
        this.listPenjualanDiskon = listPenjualanDiskon;
    }
    
    
    public double getGrandtotal() {
        return grandtotal.get();
    }

    public void setGrandtotal(double value) {
        grandtotal.set(value);
    }

    public DoubleProperty grandtotalProperty() {
        return grandtotal;
    }

    public double getTotalDiskon() {
        return totalDiskon.get();
    }

    public void setTotalDiskon(double value) {
        totalDiskon.set(value);
    }

    public DoubleProperty totalDiskonProperty() {
        return totalDiskon;
    }

    public String getPelanggan() {
        return pelanggan.get();
    }

    public void setPelanggan(String value) {
        pelanggan.set(value);
    }

    public StringProperty pelangganProperty() {
        return pelanggan;
    }

    public List<Pembayaran> getListPembayaran() {
        return listPembayaran;
    }

    public void setListPembayaran(List<Pembayaran> listPembayaran) {
        this.listPembayaran = listPembayaran;
    }
    
    
    public List<PenjualanDetail> getListPenjualanDetail() {
        return listPenjualanDetail;
    }

    public void setListPenjualanDetail(List<PenjualanDetail> listPenjualanDetail) {
        this.listPenjualanDetail = listPenjualanDetail;
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

    public String getKategoriPenjualan() {
        return kategoriPenjualan.get();
    }

    public void setKategoriPenjualan(String value) {
        kategoriPenjualan.set(value);
    }

    public StringProperty kategoriPenjualanProperty() {
        return kategoriPenjualan;
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


    public String getKodeUser() {
        return kodeUser.get();
    }

    public void setKodeUser(String value) {
        kodeUser.set(value);
    }

    public StringProperty kodeUserProperty() {
        return kodeUser;
    }

    public double getSisaPembayaran() {
        return sisaPembayaran.get();
    }

    public void setSisaPembayaran(double value) {
        sisaPembayaran.set(value);
    }

    public DoubleProperty sisaPembayaranProperty() {
        return sisaPembayaran;
    }

    public double getPembayaran() {
        return pembayaran.get();
    }

    public void setPembayaran(double value) {
        pembayaran.set(value);
    }

    public DoubleProperty pembayaranProperty() {
        return pembayaran;
    }

    public double getTotalPenjualan() {
        return totalPenjualan.get();
    }

    public void setTotalPenjualan(double value) {
        totalPenjualan.set(value);
    }

    public DoubleProperty totalPenjualanProperty() {
        return totalPenjualan;
    }


    public String getTglPenjualan() {
        return tglPenjualan.get();
    }

    public void setTglPenjualan(String value) {
        tglPenjualan.set(value);
    }

    public StringProperty tglPenjualanProperty() {
        return tglPenjualan;
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
