package com.dmall.listener;

import com.dmall.util.WebAppConf;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by sinan.chen on 11/1/2019.
 */
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebAppConf.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
