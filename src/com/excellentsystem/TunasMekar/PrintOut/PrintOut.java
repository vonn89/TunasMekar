/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.PrintOut;

import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglLengkap;
import static com.excellentsystem.TunasMekar.Main.tglSql;
import com.excellentsystem.TunasMekar.Model.PenjualanDetail;
import com.excellentsystem.TunasMekar.Model.PenjualanDiskon;
import com.excellentsystem.TunasMekar.Model.PenjualanHead;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author excellent
 */
public class PrintOut {
//    public void printStrukPenjualan(PenjualanHead p, double bayar, double kembali)throws Exception{
//        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("Struk.jrxml"));
//        Map parameters = new HashMap<>();
//        parameters.put("nama", sistem.getNama());
//        parameters.put("alamat",sistem.getAlamat());
//        parameters.put("noTelp",sistem.getNoTelp());
//        parameters.put("footnote",sistem.getFootnote());
//        parameters.put("footnote",sistem.getFootnote());
//        parameters.put("bayar",bayar);
//        parameters.put("kembali",kembali);
//        List<PenjualanDetail> allPenjualan = new ArrayList<>();
//        for(PenjualanDetail d : p.getListPenjualanDetail()){
//            d.setPenjualanHead(p);
//            allPenjualan.add(d);
//        }
//        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(allPenjualan);
//        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//        JasperPrint jaspedfrint = JasperFillManager.fillReport(jasperReport,parameters, beanColDataSource);
//        PrinterJob job = PrinterJob.getPrinterJob();
//        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
//        int selectedService = 0;
//        for(int i = 0; i < services.length;i++){
//            if(services[i].getName().toUpperCase().contains(sistem.getPrinter()))
//                selectedService = i;
//        }
//        job.setPrintService(services[selectedService]);
//        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//        MediaSizeName mediaSizeName = MediaSize.findMedia(40000,40000,MediaPrintableArea.MM);
//        printRequestAttributeSet.add(mediaSizeName);
//        printRequestAttributeSet.add(new Copies(1));
//        JRPrintServiceExporter exporter;
//        exporter = new JRPrintServiceExporter();
//        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jaspedfrint);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
//        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
//        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
//        exporter.exportReport();
//    }
    public static DecimalFormat df = new DecimalFormat("###,##0.##");
    public void printStrukPenjualanDirect(PenjualanHead p)throws Exception{
        String data =   "<ESC>E1<ESC>a1"+sistem.getNama()+"\n"+
                        "<ESC>E0<ESC>a1"+sistem.getAlamat()+"\n"+
                        "<ESC>a1"+sistem.getNoTelp()+"\n"+
                        "<ESC>a1---------------------------------------------\n"+
                        "<ESC>a0  No      : "+p.getNoPenjualan()+"\n"+
                        "<ESC>a0  Tanggal : "+tglLengkap.format(tglSql.parse(p.getTglPenjualan()))+"\n";
        if(p.getPelanggan()!=null && !p.getPelanggan().equals(""))
            data = data + "<ESC>a0  Pelanggan : "+p.getCustomer().getNama()+"\n";
        data = data + "<ESC>a1---------------------------------------------\n";
        for(PenjualanDetail d : p.getListPenjualanDetail()){
            String qty = df.format(d.getQty());
            if(d.getQty()<10)
                qty = " "+df.format(d.getQty());
            
            String harga = df.format(d.getHarga());
            if(d.getHarga()<1000)
                harga = "    "+df.format(d.getHarga());
            else if(d.getHarga()<10000)
                harga = "  "+df.format(d.getHarga());
            else if(d.getHarga()<100000)
                harga = " "+df.format(d.getHarga());
            
            data = data + "<ESC>a0  "+d.getNamaBarang()+"  ("+d.getSatuan()+")\n"+
                          "<ESC>a0   "+qty+"    x  "+harga+"                "+ubahString(d.getTotalHarga())+"\n" ;
        }
        data = data  +  "<ESC>a1---------------------------------------------\n";
        for(PenjualanDiskon pd : p.getListPenjualanDiskon()){
            data = data + "<ESC>a0   "+pd.getNamaDiskon()+"\n"+
                                 "    x  "+pd.getQty()+"                "+ubahString(pd.getTotalDiskon())+"\n" ;
        }
        data = data  +  "<ESC>a1---------------------------------------------\n"+
                        "<ESC>a2Total Penjualan = Rp "+ubahString(p.getTotalPenjualan())+"   \n"+
                        "<ESC>a2Total Diskon    = Rp "+ubahString(p.getTotalDiskon())+"   \n"+
                        "<ESC>a2Grandtotal      = Rp "+ubahString(p.getGrandtotal())+"   \n";
        if(p.getSisaPembayaran()>0){
            data = data +   "<ESC>a1---------------------------------------------\n"+
                            "<ESC>a2Pembayaran      = Rp "+ubahString(p.getPembayaran())+"   \n"+
                            "<ESC>a2Sisa Pembayaran = Rp "+ubahString(p.getSisaPembayaran())+"   \n";
        }
        data = data +   "<ESC>a1---------------------------------------------\n"+
                        "<ESC>a1"+sistem.getFootnote()+"\n\n\n\n\n\n<GS><v><48>";
        char ch = 27;
        char gs = 29;
        char v = 86;
        char char48 = 48;
        data = data.replace("<ESC>", String.valueOf(ch));
        data = data.replace("<GS>", String.valueOf(gs));
        data = data.replace("<v>", String.valueOf(v));
        data = data.replace("<48>", String.valueOf(char48));
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        int selectedService = 0;
        for(int i = 0; i < services.length;i++){
            if(services[i].getName().toUpperCase().contains(sistem.getPrinter()))
                selectedService = i;
        }

        DocPrintJob job = services[selectedService].createPrintJob();
        try (InputStream is = new ByteArrayInputStream(data.getBytes())) {
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(is, flavor, null);
            job.print(doc, null);
        }
    }
    public void printStrukGroupPenjualanDirect(List<PenjualanHead> listPenjualan)throws Exception{
        String noPenjualan = "";
        String pelanggan = "";
        double totalPenjualan = 0;
        double totalDiskon = 0;
        double grandtotal = 0;
        double pembayaran = 0;
        double sisaPembayaran = 0;
        for(PenjualanHead p : listPenjualan){
            if(noPenjualan.equals(""))
                noPenjualan = p.getNoPenjualan();
            else
                noPenjualan = noPenjualan + " + " +p.getNoPenjualan();
            if(p.getPelanggan()!=null && !p.getPelanggan().equals("")){
                if(pelanggan.equals(""))
                    pelanggan = p.getCustomer().getNama();
                else
                    pelanggan = pelanggan + " + " +p.getCustomer().getNama();
            }
            totalPenjualan = totalPenjualan + p.getTotalPenjualan();
            totalDiskon = totalDiskon + p.getTotalDiskon();
            grandtotal = grandtotal + p.getGrandtotal();
            pembayaran = pembayaran + p.getPembayaran();
            sisaPembayaran = sisaPembayaran + p.getSisaPembayaran();
        }
        
        String data =   "<ESC>E1<ESC>a1"+sistem.getNama()+"\n"+
                        "<ESC>E0<ESC>a1"+sistem.getAlamat()+"\n"+
                        "<ESC>a1"+sistem.getNoTelp()+"\n"+
                        "<ESC>a1---------------------------------------------\n"+
                        "<ESC>a0  No      : "+noPenjualan+"\n"+
                        "<ESC>a0  Tanggal : "+tglLengkap.format(tglBarang.parse(sistem.getTglSystem()))+"\n";
        if(!pelanggan.equals(""))
            data = data + "<ESC>a0  Pelanggan : "+pelanggan+"\n";
        data = data + "<ESC>a1---------------------------------------------\n";
        for(PenjualanHead p : listPenjualan){
            for(PenjualanDetail d : p.getListPenjualanDetail()){
                String qty = df.format(d.getQty());
                if(d.getQty()<10)
                    qty = " "+df.format(d.getQty());

                String harga = df.format(d.getHarga());
                if(d.getHarga()<1000)
                    harga = "    "+df.format(d.getHarga());
                else if(d.getHarga()<10000)
                    harga = "  "+df.format(d.getHarga());
                else if(d.getHarga()<100000)
                    harga = " "+df.format(d.getHarga());

                data = data + "<ESC>a0  "+d.getNamaBarang()+"  ("+d.getSatuan()+")\n"+
                              "<ESC>a0   "+qty+"    x  "+harga+"                "+ubahString(d.getTotalHarga())+"\n" ;
            }
        }
        data = data  +  "<ESC>a1---------------------------------------------\n";
        for(PenjualanHead p : listPenjualan){
            for(PenjualanDiskon pd : p.getListPenjualanDiskon()){
                data = data + "<ESC>a0   "+pd.getNamaDiskon()+"\n"+
                                     "    x  "+pd.getQty()+"                "+ubahString(pd.getTotalDiskon())+"\n" ;
            }
        }
        data = data  +  "<ESC>a1---------------------------------------------\n"+
                        "<ESC>a2Total Penjualan = Rp "+ubahString(totalPenjualan)+"   \n";
        if(totalDiskon>0){
            data = data +   "<ESC>a2Total Diskon    = Rp "+ubahString(totalDiskon)+"   \n"+
                            "<ESC>a2Grandtotal      = Rp "+ubahString(grandtotal)+"   \n";
        }
        if(sisaPembayaran>0){
            data = data +   "<ESC>a1---------------------------------------------\n"+
                            "<ESC>a2Pembayaran      = Rp "+ubahString(pembayaran)+"   \n"+
                            "<ESC>a2Sisa Pembayaran = Rp "+ubahString(sisaPembayaran)+"   \n";
        }
        data = data +   "<ESC>a1---------------------------------------------\n"+
                        "<ESC>a1"+sistem.getFootnote()+"\n\n\n\n\n\n<GS><v><48>";
        char ch = 27;
        char gs = 29;
        char v = 86;
        char char48 = 48;
        data = data.replace("<ESC>", String.valueOf(ch));
        data = data.replace("<GS>", String.valueOf(gs));
        data = data.replace("<v>", String.valueOf(v));
        data = data.replace("<48>", String.valueOf(char48));
        System.out.println(data);
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        int selectedService = 0;
        for(int i = 0; i < services.length;i++){
            if(services[i].getName().toUpperCase().contains(sistem.getPrinter()))
                selectedService = i;
        }

        DocPrintJob job = services[selectedService].createPrintJob();
        try (InputStream is = new ByteArrayInputStream(data.getBytes())) {
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(is, flavor, null);
            job.print(doc, null);
        }
    }
    private String ubahString(double x){
        String string = df.format(x);
        if(x<10)
            string = "          "+df.format(x);
        else if(x<100)
            string = "         "+df.format(x);
        else if(x<1000)
            string = "        "+df.format(x);
        else if(x<10000)
            string = "      "+df.format(x);
        else if(x<100000)
            string = "     "+df.format(x);
        else if(x<1000000)
            string = "    "+df.format(x);
        else if(x<10000000)
            string = "  "+df.format(x);
        else if(x<100000000)
            string = " "+df.format(x);
        return string;
    }
}
