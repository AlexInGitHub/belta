package com.dmall;

import com.google.common.io.ByteStreams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sinan.chen on 17/12/18.
 */
public class GreetingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("Hello "+ new String(ByteStreams.toByteArray(req.getInputStream())));
//            byte[] buff = new byte[10 * 1024];
//            while(req.getInputStream().read(buff) != -1) {
//                System.out.println(new String(buff));
//                Thread.sleep(Integer.parseInt(System.getProperty("latency")));
//            }
            Thread.sleep(Integer.parseInt(System.getProperty("latency")));
            resp.getOutputStream().print("Success!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
