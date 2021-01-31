/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.Report;

import com.excellentsystem.TunasMekar.DAO.BarangDAO;
import com.excellentsystem.TunasMekar.DAO.StokBarangDAO;
import com.excellentsystem.TunasMekar.Function;
import com.excellentsystem.TunasMekar.Koneksi;
import static com.excellentsystem.TunasMekar.Main.rp;
import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglNormal;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.StokBarang;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class LaporanKartuStokBarangController {

    @SuppressWarnings("rawtypes")
    private JasperPrint jasperPrint;
    @FXML private ImageView imageView;
    @FXML private TextField pageField;
    @FXML private Label totalPageLabel;
    @FXML private Slider zoomLevel;
    private double zoomFactor;
    private double imageHeight;
    private double imageWidth;

    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    private ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private ObservableList<Barang> filterData = FXCollections.observableArrayList();
    
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private TextField searchField;
    public void initialize(){
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()).minusMonths(1));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
        allBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        filterData.addAll(allBarang);
        barangTable.setItems(filterData);
        
        barangTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> getLogBarang());
        Function.setNumberField(pageField, rp);
        getBarang();
    }
    private void getBarang(){
        try(Connection con = Koneksi.getConnection()){
            allBarang.clear();
            allBarang.addAll(BarangDAO.getAllByKategoriAndStatus(con, "%", "true"));
            getLogBarang();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    private Boolean checkColumn(String column) {
        return column != null && column.toLowerCase().contains(searchField.getText().toLowerCase());
    }
    private void searchBarang() {
        filterData.clear();
        allBarang.forEach((temp) -> {
            if (searchField.getText() == null || searchField.getText().equals("")) {
                filterData.add(temp);
            } else {
                if (checkColumn(temp.getKodeBarang()) ||
                    checkColumn(temp.getKodeKategori()) ||
                    checkColumn(temp.getNamaBarang()) ||
                    checkColumn(temp.getSupplier())) 
                    filterData.add(temp);
            }
        });
    }
    @FXML
    private void getLogBarang(){
        if(barangTable.getSelectionModel().getSelectedItem()!=null){
            try(Connection con = Koneksi.getConnection()){
                List<StokBarang> listKartuStok = StokBarangDAO.getAllByDateAndBarang(con, 
                        tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), 
                        barangTable.getSelectionModel().getSelectedItem().getKodeBarang());
                for(StokBarang l : listKartuStok){
                    l.setBarang(barangTable.getSelectionModel().getSelectedItem());
                }
                Collections.sort(listKartuStok, (o1, o2) -> {
                    return ((StokBarang) o1).getTanggal().compareTo(((StokBarang) o2).getTanggal());
                });
                JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("LaporanKartuStokBarang.jrxml"));
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listKartuStok);
                Map hash = new HashMap();
                hash.put("tanggal", tglNormal.format(tglBarang.parse(tglAwalPicker.getValue().toString()))+" - "+
                        tglNormal.format(tglBarang.parse(tglAkhirPicker.getValue().toString())));
                hash.put("barang", barangTable.getSelectionModel().getSelectedItem().getNamaBarang());
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Application error - \n" +e);
                alert.showAndWait();
            }
        }
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
