package com.kailaisi.eshopcache.listener;

import com.kailaisi.eshopcache.kafka.KafkaConsumer;
import com.kailaisi.eshopcache.rebuild.RebuildCacheThread;
import com.kailaisi.eshopcache.spring.SpringContext;
import com.kailaisi.eshopcache.zk.ZooKeeperSession;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 系统初始化监听器
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext sc = servletContextEvent.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);
        new Thread(new KafkaConsumer("cache-messages")).start();
        new Thread(new RebuildCacheThread()).start();
        ZooKeeperSession.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
