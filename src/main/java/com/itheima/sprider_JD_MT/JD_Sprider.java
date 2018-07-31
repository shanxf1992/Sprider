package com.itheima.sprider_JD_MT;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class JD_Sprider {
    private CloseableHttpClient httpClients;

    //对象创建的时候进行初始化
    public JD_Sprider(){
        httpClients = HttpClients.createDefault();

    }

    public void spriderPetsInfoDataFromJD(String url, ArrayBlockingQueue<String> queue) throws Exception {

        //循环遍历所有页码, 获取每一页的数据
        for (int i = 1; i <= 100; i++) {
            //拼接每一页的 url
            url = url + "&page=" + (2 * i - 1);
            HttpGet httpGet = new HttpGet(url);

            //设置请求头
            String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
            httpGet.setHeader("User-Agent", User_Agent);

            //发送请求, 获取请求页面
            CloseableHttpResponse response = httpClients.execute(httpGet);
            String html = EntityUtils.toString(response.getEntity());

            //解析页面, 获取document对象
            Document document = Jsoup.parse(html);
            
            //获取所有的宠物的pids, 将pids 添加到 queue 中
            parsePids(document, queue);
        }
    }

    //解析商品的pid
    public void parsePids(Document document, ArrayBlockingQueue<String> queue) throws Exception {
        Elements elements = document
                .select("#J_goodsList")
                .select("ul")
                .select("li");

        List<String> pids = new ArrayList<String>();
        for (Element element : elements) {
            queue.put(element.attr("data-sku"));
        }
    }




}
