/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TunasMekar.DAO;

import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglKode;
import com.excellentsystem.TunasMekar.Model.Pembayaran;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class PembayaranDAO {
    
    public static List<Pembayaran> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_pembayaran "
                + " where mid(no_pembayaran,4,6) between '"+tglKode.format(tglBarang.parse(tglMulai))+"' "
                + " and '"+tglKode.format(tglBarang.parse(tglAkhir))+"' ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Pembayaran> listPenjualan = new ArrayList<>();
        while(rs.next()){
            Pembayaran p = new Pembayaran();
            p.setNoPembayaran(rs.getString(1));
            p.setTglPembayaran(rs.getString(2));
            p.setNoTransaksi(rs.getString(3));
            p.setJenisPembayaran(rs.getString(4));
            p.setKeterangan(rs.getString(5));
            p.setJumlahPembayaran(rs.getDouble(6));
            p.setKodeUser(rs.getString(7));
            p.setStatus(rs.getString(8));
            p.setTglBatal(rs.getString(9));
            p.setUserBatal(rs.getString(10));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static List<Pembayaran> getAllByNoTransaksiAndStatus(
            Connection con, String noTransaksi, String status)throws Exception{
        String sql = "select * from tt_pembayaran where no_transaksi = '"+noTransaksi+"'";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Pembayaran> listPenjualan = new ArrayList<>();
        while(rs.next()){
            Pembayaran p = new Pembayaran();
            p.setNoPembayaran(rs.getString(1));
            p.setTglPembayaran(rs.getString(2));
            p.setNoTransaksi(rs.getString(3));
            p.setJenisPembayaran(rs.getString(4));
            p.setKeterangan(rs.getString(5));
            p.setJumlahPembayaran(rs.getDouble(6));
            p.setKodeUser(rs.getString(7));
            p.setStatus(rs.getString(8));
            p.setTglBatal(rs.getString(9));
            p.setUserBatal(rs.getString(10));
            listPenjualan.add(p);
        }
        return listPenjualan;
    }
    public static Pembayaran get(Connection con, String noPembayaran)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembayaran where no_pembayaran = ?");
        ps.setString(1, noPembayaran);
        ResultSet rs = ps.executeQuery();
        Pembayaran p = null;
        while(rs.next()){
            p = new Pembayaran();
            p.setNoPembayaran(rs.getString(1));
            p.setTglPembayaran(rs.getString(2));
            p.setNoTransaksi(rs.getString(3));
            p.setJenisPembayaran(rs.getString(4));
            p.setKeterangan(rs.getString(5));
            p.setJumlahPembayaran(rs.getDouble(6));
            p.setKodeUser(rs.getString(7));
            p.setStatus(rs.getString(8));
            p.setTglBatal(rs.getString(9));
            p.setUserBatal(rs.getString(10));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(no_pembayaran,4)) from tt_pembayaran "
                + " where mid(no_pembayaran,4,6)=?");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        DecimalFormat df = new DecimalFormat("0000");
        ResultSet rs = ps.executeQuery();
        String id;
        if(rs.next())
            id = "PB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(rs.getInt(1)+1);
        else
            id = "PB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-0001";
        return id;
    }
    public static void insert(Connection con,Pembayaran p) throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pembayaran "
                + " values(?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPembayaran());
        ps.setString(2, p.getTglPembayaran());
        ps.setString(3, p.getNoTransaksi());
        ps.setString(4, p.getJenisPembayaran());
        ps.setString(5, p.getKeterangan());
        ps.setDouble(6, p.getJumlahPembayaran());
        ps.setString(7, p.getKodeUser());
        ps.setString(8, p.getStatus());
        ps.setString(9, p.getTglBatal());
        ps.setString(10, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, Pembayaran p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_pembayaran set "
                + "tgl_pembayaran=?, no_transaksi=?, jenis_pembayaran=?, keterangan=?, "
                + "jumlah_pembayaran=?, kode_user=?, status=?, tgl_batal=?, user_batal=?"
                + "where no_pembayaran=?");
        ps.setString(1, p.getTglPembayaran());
        ps.setString(2, p.getNoTransaksi());
        ps.setString(3, p.getJenisPembayaran());
        ps.setString(4, p.getKeterangan());
        ps.setDouble(5, p.getJumlahPembayaran());
        ps.setString(6, p.getKodeUser());
        ps.setString(7, p.getStatus());
        ps.setString(8, p.getTglBatal());
        ps.setString(9, p.getUserBatal());
        ps.setString(10, p.getNoPembayaran());
        ps.executeUpdate();
    }
}
