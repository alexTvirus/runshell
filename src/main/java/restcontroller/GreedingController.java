/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tqtk.Entity.SessionEntity;
import tqtk.Tqtk;
import tqtk.XuLy.login.LayThongTinSession;

@RestController
public class GreedingController {

    public static int count = 0;

    @RequestMapping(value = "/addcount", method = RequestMethod.GET)
    public int addcount() {
        count++;
        return count;
    }

    @RequestMapping(value = "/getcount", method = RequestMethod.GET)
    public int getcount() {
        return count;
    }
	
	@RequestMapping(value = "/getport", method = RequestMethod.GET)
	@ResponseBody
    public String getport() {
        return System.getenv("PORT");
    }

    @RequestMapping(value = "/starttqtk", method = RequestMethod.GET)
    @ResponseBody
    public String starttqtk() {
        if (LayThongTinSession.getListSession().size() < 1) {
            Thread t = new Thread() {
                public void run() {
                    tqtk.Tqtk.main();
                }
            };
            t.start();
        }

        return "ok";
    }

    @RequestMapping(value = "/getinfotqtk", method = RequestMethod.GET)
    @ResponseBody
    public String getinfotqtk(@RequestParam(value = "key", required = true) String key) {
        for (SessionEntity sessionEntity : LayThongTinSession.getListSession()) {
            if (key.equals(sessionEntity.getUserId())) {
                return sessionEntity.getMessage().toString();
            }
        }

        return "ko thay";
    }

}
