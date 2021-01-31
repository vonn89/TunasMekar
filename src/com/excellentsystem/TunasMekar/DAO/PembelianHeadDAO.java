/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglKode;
import com.excellentsystem.TunasMekar.Model.PembelianHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class PembelianHeadDAO {
    
    public static List<PembelianHead> getAllByDateAndStatus(
            Connection con, String tglMulai, String tglAkhir, String status)throws Exception{
        String sql = "select * from tt_pembelian_head where left(tgl_pembelian,10) between ? and ? ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PembelianHead> listPembelian = new ArrayList<>();
        while(rs.next()){
            PembelianHead p = new PembelianHead();
            p.setNoPembelian(rs.getString(1));
            p.setTglPembelian(rs.getString(2));
            p.setSupplier(rs.getString(3));
            p.setPaymentTerm(rs.getString(4));
            p.setJatuhTempo(rs.getString(5));
            p.setTotalPembelian(rs.getDouble(6));
            p.setPembayaran(rs.getDouble(7));
            p.setSisaPembayaran(rs.getDouble(8));
            p.setKodeUser(rs.getString(9));
            p.setStatus(rs.getString(10));
            p.setTglBatal(rs.getString(11));
            p.setUserBatal(rs.getString(12));
            listPembelian.add(p);
        }
        return listPembelian;
    }
    public static PembelianHead get(Connection con, String noPembelian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_pembelian_head "
                + " where no_pembelian = ?");
        ps.setString(1, noPembelian);
        PembelianHead p = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            p = new PembelianHead();
            p.setNoPembelian(rs.getString(1));
            p.setTglPembelian(rs.getString(2));
            p.setSupplier(rs.getString(3));
            p.setPaymentTerm(rs.getString(4));
            p.setJatuhTempo(rs.getString(5));
            p.setTotalPembelian(rs.getDouble(6));
            p.setPembayaran(rs.getDouble(7));
            p.setSisaPembayaran(rs.getDouble(8));
            p.setKodeUser(rs.getString(9));
            p.setStatus(rs.getString(10));
            p.setTglBatal(rs.getString(11));
            p.setUserBatal(rs.getString(12));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(no_pembelian,3)) from tt_pembelian_head " 
                + " where left(no_pembelian,9) = ?");
        DecimalFormat df = new DecimalFormat("000");
        ps.setString(1, "PO-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PO-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "PO-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, PembelianHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_pembelian_head values(?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPembelian());
        ps.setString(2, p.getTglPembelian());
        ps.setString(3, p.getSupplier());
        ps.setString(4, p.getPaymentTerm());
        ps.setString(5, p.getJatuhTempo());
        ps.setDouble(6, p.getTotalPembelian());
        ps.setDouble(7, p.getPembayaran());
        ps.setDouble(8, p.getSisaPembayaran());
        ps.setString(9, p.getKodeUser());
        ps.setString(10, p.getStatus());
        ps.setString(11, p.getTglBatal());
        ps.setString(12, p.getUserBatal());
        ps.executeUpdate();
    }
    public static void update(Connection con, PembelianHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_pembelian_head set "
                + " tgl_pembelian = ?, supplier = ?, payment_term = ?, jatuh_tempo = ?, total_pembelian = ?, pembayaran = ?, sisa_pembayaran = ?, "
                + " kode_user = ?, status = ?, tgl_batal = ?, user_batal = ? where no_pembelian = ?");
        ps.setString(1, p.getTglPembelian());
        ps.setString(2, p.getSupplier());
        ps.setString(3, p.getPaymentTerm());
        ps.setString(4, p.getJatuhTempo());
        ps.setDouble(5, p.getTotalPembelian());
        ps.setDouble(6, p.getPembayaran());
        ps.setDouble(7, p.getSisaPembayaran());
        ps.setString(8, p.getKodeUser());
        ps.setString(9, p.getStatus());
        ps.setString(10, p.getTglBatal());
        ps.setString(11, p.getUserBatal());        
        ps.setString(12, p.getNoPembelian());
        ps.executeUpdate();
    }
}
