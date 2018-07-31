package com.itheima.Sprider_JD_Proxy;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sprider_JD_Proxy {

    public static void main(String[] args) {

        //使用代理服务器的方法
        //需要配置代理服务器的 ip 和 端口号(常用的代理网站: 西刺, 站大爷, 火箭等)
        HttpHost proxy = new HttpHost("60.255.186.169", 8888);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();

        //起点 url
        String url = "https://www.qidian.com/";

        //创建get请求
        HttpGet httpGet = new HttpGet(url);

        // 设置响应头
        String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
        httpGet.setHeader("User-Agent", User_Agent);

        // 发送get请求, 获取响应数据
        CloseableHttpResponse response = null;
        try {
            //通过代理服务器, 发送请求
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            System.exit(0);
        }

        //获取相应页面
        String html = null;
        try {
            html = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //使用Jsoup解析html
        Document document = Jsoup.parse(html);

        //获取小说地址
        Elements elements = document.select("#rank-list-row")
                .select("div[class=rank-list sort-list]")
                .select("li")
                .select("a[href^=//book.qidian.com][class!=link]");

        //输出小说地址, 并返回
        for (Element element : elements) {
            System.out.println(element.attr("href"));
        }

    }
}
