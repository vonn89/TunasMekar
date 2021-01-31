/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar;

import static com.excellentsystem.TunasMekar.Main.sistem;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.Annotation;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author yunaz
 */
public class Function {
    public static double pembulatan(double angka){
        return (double) Math.round(angka*100)/100;
    }
    public static String getSystemDate(){
        return sistem.getTglSystem()+" "+new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
    public static Comparator<String> sortDate(DateFormat df){
        return (String t, String t1) -> {
            try{
                return Long.compare(df.parse(t).getTime(),df.parse(t1).getTime());
            }catch(ParseException e){
                return -1;
            }
        };
    }
    public static Comparator<String> sortDouble(){
        return (String t, String t1) -> {
            try{
                return Double.compare(Double.parseDouble(t.replaceAll(",", "")), Double.parseDouble(t1.replaceAll(",", "")));
            }catch(Exception e){
                return -1;
            }
        };
    }
    public static String toRGBCode( Color color ){
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
    public static TableCell getTableCell(DecimalFormat df){ 
        TableCell cell = new TableCell<Annotation, Number>(){
            @Override
            public void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty)
                    setText(null);
                else 
                    setText(df.format(value.doubleValue()));
            }
        };
        return cell;
    }
    public static TableCell getWrapTableCell(TableColumn tc){ 
        TableCell cell = new TableCell<>();
        Text text = new Text();
//        text.setFont(javafx.scene.text.Font.font("Sans Serif"));
//        text.setFill(Paint.valueOf("#0E3862"));
        text.wrappingWidthProperty().bind(tc.widthProperty());
        text.textProperty().bind(cell.itemProperty());
        cell.setGraphic(text);
        cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
        return cell ;
    }
    public static TreeTableCell getTreeTableCell(DecimalFormat df){
        return new TreeTableCell<Annotation, Number>() {
            @Override 
            public void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (value==null || empty) 
                    setText(null);
                else 
                    setText(df.format(value.doubleValue()));
            }
        };
    }
    public static TreeTableCell getWrapTreeTableCell(TreeTableColumn tc){ 
        TreeTableCell cell = new TreeTableCell<>();
        Text text = new Text();
//        text.setFont(javafx.scene.text.Font.font("Sans Serif"));
//        text.setFill(Paint.valueOf("#0E3862"));
        text.wrappingWidthProperty().bind(tc.widthProperty());
        text.textProperty().bind(cell.itemProperty());
        cell.setGraphic(text);
        cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
        return cell ;
    }
    public static DateCell getDateCellDisableBefore(LocalDate date){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: seccolor5;");
                if (item.isBefore(date)) 
                    this.setDisable(true);
            }
        };
    }
    public static DateCell getDateCellDisableAfter(LocalDate date){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: seccolor5;");
                if (item.isAfter(date)) 
                    this.setDisable(true);
            }
        };
    }
    public static DateCell getDateCellMulai(DatePicker tglAkhir, LocalDate tglSystem){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: seccolor5;");
                if(item.isAfter(tglSystem))
                    this.setDisable(true);
                if(item.isAfter(tglAkhir.getValue()))
                    this.setDisable(true);
            }
        };
    }
    public static DateCell getDateCellAkhir(DatePicker tglMulai, LocalDate tglSystem){
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                DayOfWeek day = DayOfWeek.from(item);
                if (day == DayOfWeek.SUNDAY) 
                    this.setStyle("-fx-background-color: seccolor5;");
                if(item.isAfter(tglSystem))
                    this.setDisable(true);
                if(item.isBefore(tglMulai.getValue()))
                    this.setDisable(true);
            }
        };
    }
    public static StringConverter getTglConverter(){
        StringConverter<LocalDate> date = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            @Override 
            public String toString(LocalDate date) {
                if (date != null) 
                    return dateFormatter.format(date);
                else 
                    return "";
            }
            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) 
                    return LocalDate.parse(string, dateFormatter);
                else 
                    return null;
            }
        };
        return date;
    }
    public static void setNumberField(TextField field, DecimalFormat df){
        field.setOnKeyReleased((event) -> {
            try{
                String string = field.getText();
                if(string.contains("-"))
                    field.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            field.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            field.setText(df.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        field.setText(df.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                field.end();
            }catch(Exception e){
                field.undo();
            }
        });
    }
//    public static boolean verifikasi(String username, String password, String jenis){
//        Boolean verifikasi = false;
//        for(User u : allUser){
//            if(u.getKodeUser().equals(username)&&u.getPassword().equals(password)){
//                for(Verifikasi v : u.getVerifikasi()){
//                    if(v.getJenis().equals(jenis))
//                        verifikasi = v.isStatus(); 
//                }
//            }
//        }
//        return verifikasi;
//    }
//    public static String ceksales(String kodeSales){
//        boolean status = false;
//        for(Pegawai p : allPegawai){
//            if(p.getKodePegawai().toUpperCase().equals(kodeSales.toUpperCase()))
//                status = true;
//        }
//        if(status){
//            return kodeSales.toUpperCase();
//        }else{
//            return "";
//        }
//    }
    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        
        if("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        }else if("Windows".equals(operatingSystem)) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }else if(operatingSystem.matches(".*Windows.*")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }else{
            throw new RuntimeException("Unsupported operating system.");
        }
        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }
    public static void createRow(Workbook workbook, Sheet sheet,int r,int col, String style){
        Font f = workbook.createFont();
        f.setBold(true);
        Font f2 = workbook.createFont();
        f2.setBold(true);
        f2.setColor(HSSFColor.WHITE.index);
        
        CellStyle boldFont = workbook.createCellStyle();
        boldFont.setFont(f);

        CellStyle subHeader = workbook.createCellStyle();
        subHeader.setFont(f);
        subHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);

        CellStyle header = workbook.createCellStyle();
        header.setFont(f2);
        header.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        header.setFillPattern(CellStyle.SOLID_FOREGROUND);

        sheet.createRow(r);
        for(int i=0 ; i<col ; i++){ 
            sheet.getRow(r).createCell(i);
            if(style.equals("Bold"))
                sheet.getRow(r).getCell(i).setCellStyle(boldFont);
            else if(style.equals("SubHeader"))
                sheet.getRow(r).getCell(i).setCellStyle(subHeader);
            else if(style.equals("Header"))
                sheet.getRow(r).getCell(i).setCellStyle(header);
            else if(style.equals("Detail"))
                sheet.getRow(r).getCell(i).setCellStyle(null);
        }
    }
    public static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
    public static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }
    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }
    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
}
