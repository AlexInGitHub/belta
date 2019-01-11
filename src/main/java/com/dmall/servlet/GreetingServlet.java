package com.dmall.servlet;

import com.dmall.util.WebAppConf;
import com.google.common.io.ByteStreams;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sinan.chen on 17/12/18.
 */
public class GreetingServlet extends HttpServlet {

    Logger log = Logger.getLogger(GreetingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = new String(ByteStreams.toByteArray(req.getInputStream()));
        log.info("Body=" + body);
        String domain = req.getHeader("Host");
        String uri = req.getRequestURI();
        sleepByDomain(domain, uri);
        resp.getOutputStream().print(WebAppConf.getResp());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private void sleepByDomain(String domain, String uri) {
        log.info(String.format("domain=%s, uri=%s", domain, uri));
        try {
            if(domain == null) {
                return;
            }
            if(domain.contains(":")) {
                domain = domain.split(":")[0];
            }
            int latency = WebAppConf.getLatency(domain, uri);
            if(latency != 0) {
                log.info(String.format("Thready is gonna sleep %ss", latency));
                Thread.sleep(latency);
            }
        }
        catch (Exception e) {
            log.error("Failed to trigger sleeping", e);
        }
    }
}
