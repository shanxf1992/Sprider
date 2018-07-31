package com.itheima.sprider_QiDian;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class QiDian_SpriderTest {

    @Test
    public void test001() throws Exception {

        String link = "https://read.qidian.com/chapter/9r9u8W1evJUCpOPIBxLXdQ2/eSlFKP1Chzg1";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(link);
        String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
        httpGet.setHeader("User-Agent", User_Agent);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        //解析响应内容
        String html = EntityUtils.toString(response.getEntity());
        Document document = Jsoup.parse(html);

        //获取小说名
        String novelName = document.select("#j_textWrap")
                .select(".read-container")
                .select(".book-cover-wrap")
                .select("h1")
                .text();

        //小说的输出路径
        String path = "C:/novels/" + novelName + ".txt";
        File file = new File(path);
        file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

        //写入小说名称
        bw.write(novelName);

        while (true) {

            httpGet = new HttpGet(link);
            User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
            httpGet.setHeader("User-Agent", User_Agent);

            response = httpClient.execute(httpGet);

            //解析响应内容
            html = EntityUtils.toString(response.getEntity());
            document = Jsoup.parse(html);

            //获取第一章节标题
            String capterName = document.select(".main-text-wrap")
                    .select(".j_chapterName")
                    .text();
            bw.write(capterName);
            bw.flush();
            bw.newLine();

            //获取第一章节的内容
            Elements capters = document.select(".main-text-wrap")
                    .select("div[class=read-content j_readContent]")
                    .select("p");

            for (Element capter : capters) {
                String capterContent = capter.text();
                bw.write(capterContent);
                bw.flush();
                bw.newLine();
            }

            //获取下一章url
            link = document.select("#j_chapterNext")
                    .attr("href");

            System.out.println(link);
            System.out.println(link.startsWith("//read.qidian.com"));
            if (link == null || link.trim() == null || !link.startsWith("//read.qidian.com")) {
                break;
            }

            link = "https:" + link;
        }


    }


}