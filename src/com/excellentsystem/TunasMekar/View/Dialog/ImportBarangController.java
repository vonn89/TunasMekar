/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.View.Dialog;

import static com.excellentsystem.TunasMekar.Function.createRow;
import static com.excellentsystem.TunasMekar.Function.getTableCell;
import com.excellentsystem.TunasMekar.Main;
import static com.excellentsystem.TunasMekar.Main.qty;
import static com.excellentsystem.TunasMekar.Main.rp;
import com.excellentsystem.TunasMekar.Model.Barang;
import com.excellentsystem.TunasMekar.Model.Satuan;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class ImportBarangController {

    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, String> kodeKategoriColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    
    @FXML private TableView<Satuan> satuanTable;
    @FXML private TableColumn<Satuan, String> kodeSatuanColumn;
    @FXML private TableColumn<Satuan, String> kodeBarcodeColumn;
    @FXML private TableColumn<Satuan, Number> qtyColumn;
    @FXML private TableColumn<Satuan, Number> hargaRetailColumn;
    @FXML private TableColumn<Satuan, Number> hargaGrosirColumn;
    
    @FXML public Button saveButton;
    
    private Stage stage;
    private Main mainApp;   
    public ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private ObservableList<Satuan> allSatuan = FXCollections.observableArrayList();
    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        
        kodeSatuanColumn.setCellValueFactory(cellData -> cellData.getValue().kodeSatuanProperty());
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory((c) -> getTableCell(qty));
        hargaRetailColumn.setCellValueFactory(cellData -> cellData.getValue().hargaRetailProperty());
        hargaRetailColumn.setCellFactory((c) -> getTableCell(rp));
        hargaGrosirColumn.setCellValueFactory(cellData -> cellData.getValue().hargaGrosirProperty());
        hargaGrosirColumn.setCellFactory((c) -> getTableCell(rp));
        
        barangTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectBarang(newValue));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNewBarang = new MenuItem("Tambah Barang Baru");
        addNewBarang.setOnAction((ActionEvent e)->{
            newBarang();
        });
        MenuItem importBarang = new MenuItem("Import Excel");
        importBarang.setOnAction((ActionEvent e)->{
            importBarang();
        });
        MenuItem download = new MenuItem("Download Format Excel");
        download.setOnAction((ActionEvent e)->{
            exportExcel();
        });
        MenuItem refreshBarang = new MenuItem("Refresh");
        refreshBarang.setOnAction((ActionEvent e) -> {
            barangTable.refresh();
        });
        rowMenu.getItems().addAll(addNewBarang, importBarang, download, refreshBarang);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNewBarang = new MenuItem("Tambah Barang Baru");
                        addNewBarang.setOnAction((ActionEvent e)->{
                            newBarang();
                        });
                        MenuItem editBarang = new MenuItem("Ubah Barang");
                        editBarang.setOnAction((ActionEvent e)->{
                            updateBarang(item);
                        });
                        MenuItem deleteBarang = new MenuItem("Hapus Barang");
                        deleteBarang.setOnAction((ActionEvent e) -> {
                            deleteBarang(item);
                        });
                        MenuItem importBarang = new MenuItem("Import Excel");
                        importBarang.setOnAction((ActionEvent e)->{
                            importBarang();
                        });
                        MenuItem download = new MenuItem("Download Format Excel");
                        download.setOnAction((ActionEvent e)->{
                            exportExcel();
                        });
                        MenuItem refreshBarang = new MenuItem("Refresh");
                        refreshBarang.setOnAction((ActionEvent e) -> {
                            barangTable.refresh();
                        });
                        rowMenu.getItems().addAll(addNewBarang, editBarang, deleteBarang, importBarang, download, refreshBarang);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    updateBarang(row.getItem());
            });
            return row;
        });
        final ContextMenu satuanRowMenu = new ContextMenu();
        MenuItem addNewSatuan = new MenuItem("Tambah Satuan Baru");
        addNewSatuan.setOnAction((ActionEvent e)->{
            addNewSatuan(barangTable.getSelectionModel().getSelectedItem());
        });
        MenuItem refreshSatuan = new MenuItem("Refresh");
        refreshSatuan.setOnAction((ActionEvent e) -> {
            satuanTable.refresh();
        });
        if(barangTable.getSelectionModel().getSelectedItem()!=null)
            satuanRowMenu.getItems().addAll(addNewSatuan);
        satuanRowMenu.getItems().addAll(refreshSatuan);
        satuanTable.setContextMenu(satuanRowMenu);
        satuanTable.setRowFactory(table -> {
            TableRow<Satuan> row = new TableRow<Satuan>() {
                @Override
                public void updateItem(Satuan item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNewSatuan = new MenuItem("Tambah Satuan Baru");
                        addNewSatuan.setOnAction((ActionEvent e)->{
                            addNewSatuan(barangTable.getSelectionModel().getSelectedItem());
                        });
                        MenuItem editSatuan = new MenuItem("Ubah Satuan");
                        editSatuan.setOnAction((ActionEvent e) -> {
                            editSatuan(barangTable.getSelectionModel().getSelectedItem(), item);
                        });
                        MenuItem deleteSatuan = new MenuItem("Hapus Satuan");
                        deleteSatuan.setOnAction((ActionEvent e) -> {
                            Barang b = barangTable.getSelectionModel().getSelectedItem();
                            b.getAllSatuan().remove(item);
                            selectBarang(b);
                        });
                        MenuItem refreshSatuan = new MenuItem("Refresh");
                        refreshSatuan.setOnAction((ActionEvent e) -> {
                            satuanTable.refresh();
                        });
                            rowMenu.getItems().addAll(addNewSatuan, editSatuan, deleteSatuan);
                        rowMenu.getItems().addAll(refreshSatuan);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2 && row.getItem()!=null)
                    editSatuan(barangTable.getSelectionModel().getSelectedItem(), row.getItem());
            });
            return row;
        });
    }    
    public void setMainApp(Main mainApp, Stage stage){
        this.mainApp = mainApp;
        this.stage = stage;
        barangTable.setItems(allBarang);
        satuanTable.setItems(allSatuan);
        stage.setOnCloseRequest((event) -> {
            mainApp.closeDialog(stage);
        });
   } 
    private void selectBarang(Barang value){
        allSatuan.clear();
        if(value!=null){
            allSatuan.addAll(value.getAllSatuan());
        }else{
            barangTable.getSelectionModel().clearSelection();
        }
    } 
    private void importBarang(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .xls or .xlsx files");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Document 2007", "*.xlsx"),new FileChooser.ExtensionFilter("Excel Document 1997-2007", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(mainApp.MainStage);
        if (selectedFile != null) {
            Task<String> task = new Task<String>() {
                @Override 
                public String call() throws Exception{
                    String status = "";
                    List<Barang> listBarang = new ArrayList<>();
                    String excelFilePath = selectedFile.getPath();
                    try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
                        Workbook workbook;
                        if (excelFilePath.endsWith("xlsx")) {
                            workbook = new XSSFWorkbook(inputStream);
                        } else if (excelFilePath.endsWith("xls")) {
                            workbook = new HSSFWorkbook(inputStream);
                        } else {
                            throw new IllegalArgumentException("The specified file is not Excel file");
                        }
                        workbook.setMissingCellPolicy(Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        Sheet firstSheet = workbook.getSheetAt(0);
                        Iterator<Row> iterator = firstSheet.iterator();
                        iterator.next();
                        while (iterator.hasNext()) {
                            Row row = iterator.next();
                            int i =0;
                            Barang barang = new Barang();
                            barang.setKodeBarang("New Barang");
                            barang.setAllSatuan(new ArrayList<>());
                            Satuan satuan = new Satuan();
                            for (int cn=0; cn<row.getLastCellNum(); cn++)  {
                                if(i==0){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        return "kode kategori masih ada yang kosong";
                                    } else {
                                        barang.setKodeKategori(cell.getStringCellValue());
                                    }
                                }else if(i==1){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        return "nama barang masih ada yang kosong";
                                    } else {
                                        barang.setNamaBarang(cell.getStringCellValue());
                                    }
                                }else if(i==2){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        return barang.getNamaBarang()+" masih memiliki kode satuan yang kosong";
                                    } else {
                                        satuan.setKodeSatuan(cell.getStringCellValue());
                                    }
                                }else if(i==3){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        satuan.setKodeBarcode("");
                                    } else {
                                        try{
                                            satuan.setKodeBarcode(new DecimalFormat("#").format(cell.getNumericCellValue()));
                                        }catch(Exception e){
                                            satuan.setKodeBarcode(cell.getStringCellValue());
                                        }
                                    }
                                }else if(i==4){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        return barang.getNamaBarang()+"-"+satuan.getKodeSatuan()+" qty masih kosong";
                                    } else {
                                        satuan.setQty(cell.getNumericCellValue());
                                    }
                                }else if(i==5){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        satuan.setHargaGrosir(0);
                                    } else {
                                        satuan.setHargaGrosir(cell.getNumericCellValue());
                                    }
                                }else if(i==6){
                                    Cell cell = row.getCell(cn);
                                    if (cell == null) {
                                        satuan.setHargaRetail(0);
                                    } else {
                                        satuan.setHargaRetail(cell.getNumericCellValue());
                                    }
                                }
                                i=i+1;
                            }
                            if(barang.getKodeKategori()==null || barang.getKodeKategori().equals("")){
                                return barang.getNamaBarang()+" kategori masih kosong";
                            }else if(barang.getNamaBarang()==null || barang.getKodeKategori().equals("")){
                                return "Nama barang masih ada yang kosong";
                            }else if(satuan.getKodeSatuan()==null || satuan.getKodeSatuan().equals("")){
                                return barang.getNamaBarang()+" satuan masih  kosong";
                            }else if(satuan.getQty()==0 ){
                                return barang.getNamaBarang()+" qty masih kosong";
                            }else{
                                boolean statusInput = false;
                                for(Barang b : listBarang){
                                    if(b.getNamaBarang().equalsIgnoreCase(barang.getNamaBarang())){
                                        if(b.getKodeKategori().equalsIgnoreCase(barang.getKodeKategori())){
                                            //cek dalam satu barang tidak boleh ada 2 satuan sama
                                            for(Satuan s : b.getAllSatuan()){
                                                if(s.getKodeSatuan().equals(satuan.getKodeSatuan()))
                                                    status = status + b.getNamaBarang()+" memiliki 2 atau lebih satuan "+s.getKodeSatuan();
                                            }

                                            b.getAllSatuan().add(satuan);
                                            statusInput = true;
                                        }else{
                                            //cek dalam apa ada 2 / lbih barang dengan nama sama tapi kategori berbeda
                                            status = status + "2 / lebih barang dengan nama "+b.getNamaBarang()+" memiliki kategori berbeda";
                                        }
                                    }
                                }
                                if(!statusInput){
                                    barang.setSupplier("");
                                    barang.setStokMinimal(0);
                                    barang.getAllSatuan().add(satuan);
                                    listBarang.add(barang);
                                }
                            }
                        }
                        workbook.close();
                    }
                    //cek nama barang sudah ada di list belum?
                    for(Barang b : listBarang){
                        for(Barang barang : allBarang){
                            if(b.getNamaBarang().equalsIgnoreCase(barang.getNamaBarang()))
                                status = status + b.getNamaBarang()+" sudah terdaftar";
                        }
                    }
                    allBarang.addAll(listBarang);
                    if(status.equals(""))
                        return "true";
                    else
                        return status;
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((ev) -> {
                mainApp.closeLoading();
                if(!task.getValue().equals("true"))
                    mainApp.showMessage(Modality.NONE, "Failed", task.getValue());
                else
                    allBarang.clear();
            });
            task.setOnFailed((e) -> {
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                mainApp.closeLoading();
            });
            new Thread(task).start();
        }
    }   
    private void exportExcel() {
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select location to export");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Document 2007", "*.xlsx"),
                    new FileChooser.ExtensionFilter("Excel Document 1997-2007", "*.xls")
            );
            File file = fileChooser.showSaveDialog(mainApp.MainStage);
            if (file != null) {
                Workbook workbook;
                if (file.getPath().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook();
                } else if (file.getPath().endsWith("xls")) {
                    workbook = new HSSFWorkbook();
                } else {
                    throw new IllegalArgumentException("The specified file is not Excel file");
                }
                Sheet sheet = workbook.createSheet("Format Excel - Data Barang");
                int rc = 0;
                int c = 7;
                
                createRow(workbook, sheet, rc, c,"Header");
                sheet.getRow(rc).getCell(0).setCellValue("Kode Kategori"); 
                sheet.getRow(rc).getCell(1).setCellValue("Nama Barang"); 
                sheet.getRow(rc).getCell(2).setCellValue("Satuan"); 
                sheet.getRow(rc).getCell(3).setCellValue("Kode Barcode"); 
                sheet.getRow(rc).getCell(4).setCellValue("Qty"); 
                sheet.getRow(rc).getCell(5).setCellValue("Harga Grosir"); 
                sheet.getRow(rc).getCell(6).setCellValue("Harga Retail"); 
                rc++;
                
                for(int i=0 ; i<c ; i++){ sheet.autoSizeColumn(i);}
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
            e.printStackTrace();
        }
    }
    private void newBarang() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setNewBarang();
        x.saveButton.setOnAction((event) -> {
            boolean status = false;
            for(Barang barang : allBarang){
                if(x.namaBarangField.getText().equalsIgnoreCase(barang.getNamaBarang()))
                    status = true;
            }
            if(x.namaBarangField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) 
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            else if(x.allSatuan.isEmpty())
                mainApp.showMessage(Modality.NONE, "Warning", "Satuan barang masih kosong");
            else if(status)
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang "+x.namaBarangField.getText()+" sudah terdaftar");
            else{
                Barang b = new Barang();
                b.setKodeBarang("New Barang");
                b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                b.setNamaBarang(x.namaBarangField.getText());
                b.setSupplier(x.supplierField.getText());
                b.setStokMinimal(0);
                b.setStatus("true");
                b.setAllSatuan(x.allSatuan);
                allBarang.add(b);
                selectBarang(null);
                mainApp.closeDialog(child);
            }
        });
    }
    private void updateBarang(Barang barang) {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.setBarang(barang);
        x.namaBarangField.setDisable(true);
        x.saveButton.setOnAction((event) -> {
            if(x.namaBarangField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            else if (x.kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("")) 
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            else if(x.allSatuan.isEmpty())
                mainApp.showMessage(Modality.NONE, "Warning", "Satuan barang masih kosong");
            else {
                for(Barang b : allBarang){
                    if(b.getNamaBarang().equalsIgnoreCase(barang.getNamaBarang())){
                        b.setKodeKategori(x.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                        b.setNamaBarang(x.namaBarangField.getText());
                        b.setSupplier(x.supplierField.getText());
                        b.setStokMinimal(0);
                        b.setStatus("true");
                        b.setAllSatuan(x.allSatuan);
                    }
                }
                selectBarang(null);
                mainApp.closeDialog(child);
            }
        });
    }
    private void deleteBarang(Barang barang) {
        allBarang.remove(barang);
        selectBarang(null);
    }
    private void addNewSatuan(Barang barang){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/SatuanBarang.fxml");
        SatuanBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.saveButton.setOnAction((event) -> {
            if("".equals(x.kodeSatuanField.getText())) 
                mainApp.showMessage(Modality.NONE, "Warning", "Kode satuan masih kosong");
            else if("".equals(x.qtyField.getText()) || Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Qty masih kosong");
            else if("".equals(x.hargaRetailField.getText()) || Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
            else if("".equals(x.hargaGrosirField.getText()) || Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
            else{
                Satuan satuan = null;
                for(Satuan temp : barang.getAllSatuan()){
                    if(temp.getKodeSatuan().equals(x.kodeSatuanField.getText()))
                        satuan = temp;
                }
                if(satuan==null){
                    satuan = new Satuan();
                    satuan.setKodeBarang(barang.getKodeBarang());
                    satuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    satuan.setKodeSatuan(x.kodeSatuanField.getText());
                    satuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    satuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    satuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    barang.getAllSatuan().add(satuan);
                }else{
                    Satuan newSatuan = new Satuan();
                    newSatuan.setKodeBarang(barang.getKodeBarang());
                    newSatuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    newSatuan.setKodeSatuan(x.kodeSatuanField.getText());
                    newSatuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    newSatuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    newSatuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    barang.getAllSatuan().remove(satuan);
                    barang.getAllSatuan().add(newSatuan);
                }
                selectBarang(barang);
                x.close();
            }
        });
    }
    private void editSatuan(Barang b, Satuan s){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/SatuanBarang.fxml");
        SatuanBarangController x = loader.getController();
        x.setMainApp(mainApp, child);
        x.kodeSatuanField.setText(s.getKodeSatuan());
        if(s.getKodeBarang()!=null && s.getKodeBarcode() !=null && !s.getKodeBarcode().startsWith(s.getKodeBarang()))
            x.kodeBarcodeField.setText(s.getKodeBarcode());
        x.qtyField.setText(qty.format(s.getQty()));
        x.hargaRetailField.setText(rp.format(s.getHargaRetail()));
        x.hargaGrosirField.setText(rp.format(s.getHargaGrosir()));
        x.kodeSatuanField.setDisable(true);
        x.saveButton.setOnAction((event) -> {
            if("".equals(x.qtyField.getText()) || Double.parseDouble(x.qtyField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Qty masih kosong");
            else if("".equals(x.hargaRetailField.getText()) || Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga retail masih kosong");
            else if("".equals(x.hargaGrosirField.getText()) || Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")) <= 0) 
                mainApp.showMessage(Modality.NONE, "Warning", "Harga grosir masih kosong");
            else{
                Satuan satuan = null;
                for(Satuan temp : b.getAllSatuan()){
                    if(temp.getKodeSatuan().equals(x.kodeSatuanField.getText()))
                        satuan = temp;
                }
                if(satuan==null){
                    satuan = new Satuan();
                    satuan.setKodeBarang(b.getKodeBarang());
                    satuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    satuan.setKodeSatuan(x.kodeSatuanField.getText());
                    satuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    satuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    satuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    b.getAllSatuan().add(satuan);
                }else{
                    Satuan newSatuan = new Satuan();
                    newSatuan.setKodeBarang(b.getKodeBarang());
                    newSatuan.setKodeBarcode(x.kodeBarcodeField.getText());
                    newSatuan.setKodeSatuan(x.kodeSatuanField.getText());
                    newSatuan.setQty(Double.parseDouble(x.qtyField.getText().replaceAll(",", "")));
                    newSatuan.setHargaRetail(Double.parseDouble(x.hargaRetailField.getText().replaceAll(",", "")));
                    newSatuan.setHargaGrosir(Double.parseDouble(x.hargaGrosirField.getText().replaceAll(",", "")));
                    b.getAllSatuan().remove(satuan);
                    b.getAllSatuan().add(newSatuan);
                }
                selectBarang(b);
                x.close();
            }
        });
    }
    @FXML
    private void close(){
        mainApp.closeDialog(stage);
    }
    
}
