/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqtk.XuLy.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import tqtk.Utils.Util;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tqtk.Entity.SessionEntity;
import static tqtk.Tqtk.sendMessage;

/**
 *
 * @author Alex
 */
public class LayThongTinSession {
    
    private static List<SessionEntity> ListSession = new ArrayList<>();
    
    public static synchronized SessionEntity getSessionEntity(String user, String pass, int id, Util u) throws Exception {
        // login va lay thong tin session
        u.setCookie(u.msCookieManager);
        String urlWeb = "http://tamquoctruyenky.vn/auth/login";
        String html = u.getPageSource(urlWeb);
        
        String token = Util.getToken(html);
        String refer = Util.getRefer(html);

        html = u.dangNhap(urlWeb, user, pass, token, refer);
        urlWeb = "http://app.slg.vn/tamquoctruyenky/slg?server=" + id;
        html = u.getThongTinFrame(urlWeb);
        
        String address = Util.getFrameString(html);
        html = u.getThongTinPort(address);
        
        String ip = Util.getInfoSocket(html, "ip");
        String ports = Util.getInfoSocket(html, "ports");
        String sessionKey = Util.getInfoSocket(html, "sessionKey");
        String userID = Util.getInfoSocket(html, "userID");
        SessionEntity ss = new SessionEntity();
        ss.setIp(ip);
        ss.setPorts(Integer.parseInt(ports));
        ss.setSessionKey(sessionKey);
        ss.setUserId(userID);
        ss.setStringName(user);
        u.setCookie(null);
        return ss;
    }
    
    public static List<SessionEntity> getListSession() {
        return ListSession;
    }
    
}
