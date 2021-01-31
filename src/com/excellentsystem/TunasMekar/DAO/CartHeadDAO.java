/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TunasMekar.DAO;

import com.excellentsystem.TunasMekar.Model.CartHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Excellent
 */
public class CartHeadDAO {
    
    public static List<CartHead> getAllByKategoriPenjualan(Connection con, String kategoriPenjualan)throws Exception{
        String sql = "select * from tt_cart_head ";
        if(!kategoriPenjualan.equals("%"))
            sql = sql + " where kategori_penjualan = '"+kategoriPenjualan+"' ";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<CartHead> allCart = new ArrayList<>();
        while(rs.next()){
            CartHead c = new CartHead();
            c.setNoCart(rs.getString(1));
            c.setKategoriPenjualan(rs.getString(2));
            allCart.add(c);
        }
        return allCart;
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select min(right(a.no_cart,2)+1) as nextID "
                + " from tt_cart_head a left join tt_cart_head b on right(a.no_cart,2) + 1 = right(b.no_cart,2) "
                + " where b.no_cart is null");
        DecimalFormat df = new DecimalFormat("00");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            if(rs.getInt(1)==100)
                return "false";
            else if(rs.getInt(1)==0)
                return "C-"+df.format(1);
            else
                return "C-"+df.format(rs.getInt(1));
        }else
            return "false";
    }
    public static void insert(Connection con, CartHead c)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_cart_head values(?,?)");
        ps.setString(1, c.getNoCart());
        ps.setString(2, c.getKategoriPenjualan());
        ps.executeUpdate();
    }
    public static void delete(Connection con, CartHead c)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tt_cart_head where no_cart=?");
        ps.setString(1, c.getNoCart());
        ps.executeUpdate();
    }
}
