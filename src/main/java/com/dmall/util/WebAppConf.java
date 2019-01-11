package com.dmall.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sinan.chen on 11/1/2019.
 */
public class WebAppConf {

    private static Logger log = Logger.getLogger(WebAppConf.class);

    private static Properties props = new Properties();
    private static volatile boolean isInit = false;
    private static ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

    private WebAppConf() {

    }

    public static synchronized void init() {
        if(isInit) {
            return;
        }

        final String path = getConfPath();
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if(path == null) {
                        props.load(WebAppConf.class.getResourceAsStream("/app.properties"));
                    }
                    else {
                        props.load(new FileInputStream(path));
                    }
                }
                catch (Exception e) {
                    log.error("Failed to load properties", e);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);

    }

    private static String getConfPath() {
        String path = System.getProperty("app.conf");
        if(path != null) {
            File file = new File(path);
            if(file.exists() && file.isFile()) {
                log.info(String.format("Load properties from: %s", path));
                return path;
            }
        }

        path = "/web/servers/conf/app.properties";
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            log.info(String.format("Load properties from: %s", path));
            return path;
        }

        return null;
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static int getPropertyAsInt(String key) {
        String value = props.getProperty(key);
        return Integer.parseInt(value);
    }

    public static int getLatency(String domain, String uri) {
        try {
            if(props.containsKey(domain + uri)) {
                return getPropertyAsInt(domain + uri);
            }

            if(props.containsKey(domain)) {
                return getPropertyAsInt(domain);
            }
        }
        catch (Exception ignore) {

        }
        return 0;
    }

    public static String getResp() {
        if(props.contains("response")) {
            return getProperty("response");
        }

        return "OK";
    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}
