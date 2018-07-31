package com.itheima.sprider_JD_cluster;

import com.itheima.utils.JDBCUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.sql.DataSource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 爬取京东搜索页手机的详细信息
 */

public class Master {

    public static void main(String[] args) {

        //确定宠物搜索页的 url
        String url = "https://search.jd.com/Search?keyword=狗狗活体&enc=utf-8";

        //爬去对应数据
        JD_Sprider jd_sprider = new JD_Sprider();

        try {
            //开启新线程监视queue
            MonitorQueue monitorQueue = new MonitorQueue();
            Thread monitor = new Thread(monitorQueue);
            monitor.start();

            //将所有的pid都存放在 redis 中
            jd_sprider.spriderPetsInfoDataFromJD(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


