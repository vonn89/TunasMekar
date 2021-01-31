/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Report;

import com.excellentsystem.TunasMekar.DAO.PembelianDetailDAO;
import com.excellentsystem.TunasMekar.DAO.PembelianHeadDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglNormal;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.PembelianDetail;
import com.excellentsystem.TunasMekar.Model.PembelianHead;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class LaporanPembelianController {

    @SuppressWarnings("rawtypes")
    private JasperPrint jasperPrint;
    @FXML private ImageView imageView;
    @FXML private TextField pageField;
    @FXML private Label totalPageLabel;
    @FXML private Slider zoomLevel;
    private double zoomFactor;
    private double imageHeight;
    private double imageWidth;

    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private TextField searchField;
    public void initialize(){
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()).minusMonths(1));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
        Function.setNumberField(pageField, rp);
    }
    @FXML
    private void getBarang(){
        try(Connection con = Koneksi.getConnection()){
            List<PembelianDetail> listPembelianDetail = PembelianDetailDAO.getAllByDateAndStatus(con, 
                    tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            List<PembelianHead> listPembelianHead = PembelianHeadDAO.getAllByDateAndStatus(con, 
                    tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            for(PembelianDetail d : listPembelianDetail){
                for(PembelianHead p : listPembelianHead){
                    if(d.getNoPembelian().equals(p.getNoPembelian()))
                        d.setPembelianHead(p);
                }
            }
            List<PembelianDetail> filterBarang = filterData(listPembelianDetail);
            Collections.sort(filterBarang, (o1, o2) -> {
                return ((PembelianDetail) o1).getPembelianHead().getNoPembelian().compareTo(
                        ((PembelianDetail) o2).getPembelianHead().getNoPembelian());
            });
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("LaporanPembelian.jrxml"));
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(filterBarang);
            Map hash = new HashMap();
            hash.put("tanggal", tglNormal.format(tglBarang.parse(tglAwalPicker.getValue().toString()))+" - "+
                    tglNormal.format(tglBarang.parse(tglAkhirPicker.getValue().toString())));
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport,hash, beanColDataSource);
            zoomFactor = 1.5d;
            zoomLevel.setValue(100d);
            imageView.setX(0);
            imageView.setY(0);
            imageHeight = jasperPrint.getPageHeight();
            imageWidth = jasperPrint.getPageWidth();
            zoomLevel.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                zoomFactor = newValue.doubleValue() * 1.5 / 100;
                imageView.setFitHeight(imageHeight * zoomFactor);
                imageView.setFitWidth(imageWidth * zoomFactor);
            });
            if(jasperPrint.getPages().size() > 0){
                showImage(1);
                pageField.setText("1");
                totalPageLabel.setText(String.valueOf(jasperPrint.getPages().size()));
            }else{
                imageView.setImage(null);
                pageField.setText("0");
                totalPageLabel.setText("0");
            }
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    private Boolean checkColumn(String column){
        if(column!=null){
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        }
        return false;
    }
    private List<PembelianDetail> filterData(List<PembelianDetail> listSatuan) throws Exception{
        List<PembelianDetail> filterData = new ArrayList<>();
        for (PembelianDetail temp : listSatuan) {
            if (searchField.getText() == null || searchField.getText().equals(""))
                filterData.add(temp);
            else{
                if(checkColumn(temp.getNoPembelian())||
                    checkColumn(tglLengkap.format(tglSql.parse(temp.getPembelianHead().getTglPembelian())))||
                    checkColumn(temp.getPembelianHead().getSupplier())||
                    checkColumn(rp.format(temp.getPembelianHead().getTotalPembelian()))||
                    checkColumn(rp.format(temp.getPembelianHead().getPembayaran()))||
                    checkColumn(rp.format(temp.getPembelianHead().getSisaPembayaran()))||
                    checkColumn(temp.getPembelianHead().getKodeUser())||
                    checkColumn(temp.getKodeBarang())||
                    checkColumn(temp.getNamaBarang())||
                    checkColumn(temp.getSatuan())||
                    checkColumn(rp.format(temp.getQty()))||
                    checkColumn(rp.format(temp.getQtyMasuk()))||
                    checkColumn(rp.format(temp.getHargaBeli()))||
                    checkColumn(rp.format(temp.getHargaPpn()))||
                    checkColumn(rp.format(temp.getTotal())))
                    filterData.add(temp);
            }
        }
        return filterData;
    }
    @FXML
    private void changePage(){
        try {
            int pageNumber = Integer.parseInt(pageField.getText().replaceAll(",", ""));
            if(1<=pageNumber && pageNumber<=jasperPrint.getPages().size()){
                showImage(pageNumber);
                pageField.setText(rp.format(pageNumber));
            }else{
                pageField.setText("0");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    public void prevPage(){
        try {
            if(Integer.parseInt(pageField.getText().replaceAll(",", ""))>1){
                int pageNumber = Integer.parseInt(pageField.getText().replaceAll(",", ""))-1;
                showImage(pageNumber);
                pageField.setText(rp.format(pageNumber));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    public void nextPage(){
        try {
            if(Integer.parseInt(pageField.getText().replaceAll(",", ""))<jasperPrint.getPages().size()){
                int pageNumber = Integer.parseInt(pageField.getText().replaceAll(",", ""))+1;
                showImage(pageNumber);
                pageField.setText(rp.format(pageNumber));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    private void firstPage(){
        try {
            if(jasperPrint.getPages().size() > 0){
                showImage(1);
                pageField.setText("1");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    @FXML
    private void lastPage(){
        try {
            if(jasperPrint.getPages().size() > 0){
                showImage(jasperPrint.getPages().size());
                pageField.setText(rp.format(jasperPrint.getPages().size()));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    private void showImage(int pageNumber)throws Exception{
        imageView.setFitHeight(imageHeight * zoomFactor);
        imageView.setFitWidth(imageWidth * zoomFactor);
        BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, pageNumber-1, 2);
        WritableImage fxImage = new WritableImage(jasperPrint.getPageWidth(), jasperPrint.getPageHeight());
        imageView.setImage(SwingFXUtils.toFXImage(image, fxImage));
    }
    @FXML
    private void print() {
        try{
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(JRException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    
    
}
