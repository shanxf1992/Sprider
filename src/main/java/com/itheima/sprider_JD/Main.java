package com.itheima.sprider_JD;

import com.itheima.entity.Pet;
import com.itheima.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 爬取京东搜索页手机的详细信息
 */

public class Main {

    public static ArrayBlockingQueue<String> queue;
    public static ExecutorService pool = null;
    public static CloseableHttpClient httpClients;

    //对象创建的时候进行初始化
    public Main(){
        httpClients = HttpClients.createDefault();
        //定义全局的线程安全队列, 存放 pid
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
        //创建线程池
        pool = Executors.newFixedThreadPool(20);
    }

    public static void main(String[] args) {

        //确定宠物搜索页的 url
        String url = "https://search.jd.com/Search?keyword=手机&enc=utf-8";

        JD_Sprider jd_sprider = new JD_Sprider();
        try {
            jd_sprider.spriderPetsInfoDataFromJD(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

