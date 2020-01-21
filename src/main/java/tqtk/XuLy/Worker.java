/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqtk.XuLy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tqtk.Entity.SessionEntity;
import tqtk.XuLy.login.LayThongTinSession;
import static tqtk.XuLy.XuLyPacket.GuiPacket;

import static tqtk.XuLy.XuLyPacket.GuiPacketKhongKQ;

/**
 *
 * @author Alex
 */
public class Worker extends Thread {

    SessionEntity ss;

    public Worker(SessionEntity ss) {
        this.ss = ss;
    }

    public void GuiPacketDeLogin() throws IOException, UnknownHostException, InterruptedException {
        // packet duy tri dang nhap , neu muon truy cap vao 1 acc tu` nhieu noi thi , phai co'
        // 1 noi dang nhap truoc roi , luc nay se ko can chay packet  10100
        GuiPacketKhongKQ(ss, "10100", null);
        // packet cap nhat thong tin lien tuc tu server
//        GuiPacketKhongKQ(ss, "11102", null);
    }

    @Override
    public void run() {
        try {
            GuiPacketDeLogin();
            List<String> list1 = new ArrayList<>();
            list1.add("3");
            list1.add("101");
            List<String> list2 = new ArrayList<>();
            list2.add("3");
            list2.add("101");
            list2.add("0");
            list2.add("0");
            while (true) {
                // lay phan thuong khoan
                try {
                    StringBuilder rs = GuiPacket(ss, "62007", list1);
                    if (rs != null) {
                        System.out.println(rs);
                        if (!rs.toString().contains("Đang khai thác")) {
                            // bat dau khoan
                            System.out.println(GuiPacket(ss, "62006", list2));
                        }
                    } else {

                    }

                } catch (IOException ex) {
                    Logger.getLogger(LayThongTinSession.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(LayThongTinSession.class.getName()).log(Level.SEVERE, null, ex);
                }

                Thread.sleep(60 * 1000);
            }
        } catch (IOException ex) {
            Logger.getLogger(LayThongTinSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LayThongTinSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
