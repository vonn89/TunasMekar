/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import static com.excellentsystem.TunasMekar.Main.sistem;
import static com.excellentsystem.TunasMekar.Main.tglBarang;
import static com.excellentsystem.TunasMekar.Main.tglKode;
import com.excellentsystem.TunasMekar.Model.PenyesuaianStokBarangHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author excellent
 */
public class PenyesuaianStokBarangHeadDAO {
    public static List<PenyesuaianStokBarangHead> getAllByDateAndJenisPenyesuaianAndStatus(
            Connection con, String tglMulai, String tglAkhir, String jenisPenyesuaian, String status)throws Exception{
        String sql = "select * from tt_penyesuaian_stok_barang_head where left(tgl_penyesuaian,10) between ? and ? ";
        if(!jenisPenyesuaian.equals("%"))
            sql = sql + " and jenis_penyesuaian = '"+jenisPenyesuaian+"' ";
        if(!status.equals("%"))
            sql = sql + " and status = '"+status+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<PenyesuaianStokBarangHead> listPenyesuaianStok = new ArrayList<>();
        while(rs.next()){
            PenyesuaianStokBarangHead p = new PenyesuaianStokBarangHead();
            p.setNoPenyesuaian(rs.getString(1));
            p.setTglPenyesuaian(rs.getString(2));
            p.setJenisPenyesuaian(rs.getString(3));
            p.setCatatan(rs.getString(4));
            p.setKodeUser(rs.getString(5));
            p.setStatus(rs.getString(6));
            p.setTglBatal(rs.getString(7));
            p.setUserBatal(rs.getString(8));
            listPenyesuaianStok.add(p);
        }
        return listPenyesuaianStok;
    }
    public static PenyesuaianStokBarangHead get(Connection con, String noPenyesuaian)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_penyesuaian_stok_barang_head "
                + " where no_penyesuaian = ?");
        ps.setString(1, noPenyesuaian);
        PenyesuaianStokBarangHead p = null;
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            p = new PenyesuaianStokBarangHead();
            p.setNoPenyesuaian(rs.getString(1));
            p.setTglPenyesuaian(rs.getString(2));
            p.setJenisPenyesuaian(rs.getString(3));
            p.setCatatan(rs.getString(4));
            p.setKodeUser(rs.getString(5));
            p.setStatus(rs.getString(6));
            p.setTglBatal(rs.getString(7));
            p.setUserBatal(rs.getString(8));
        }
        return p;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select max(right(no_penyesuaian,3)) from tt_penyesuaian_stok_barang_head " 
                + " where left(no_penyesuaian,9) = ?");
        DecimalFormat df = new DecimalFormat("000");
        ps.setString(1, "PS-"+tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "PS-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(rs.getInt(1)+1);
        else
            return "PS-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+df.format(1);
    }
    public static void insert(Connection con, PenyesuaianStokBarangHead p)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_penyesuaian_stok_barang_head values(?,?,?,?,?,?,?,?)");
        ps.setString(1, p.getNoPenyesuaian());
        ps.setString(2, p.getTglPenyesuaian());
        ps.setString(3, p.getJenisPenyesuaian());
        ps.setString(4, p.getCatatan());
        ps.setString(5, p.getKodeUser());
        ps.setString(6, p.getStatus());
        ps.setString(7, p.getTglBatal());
        ps.setString(8, p.getUserBatal());
        ps.executeUpdate();
    }
}
