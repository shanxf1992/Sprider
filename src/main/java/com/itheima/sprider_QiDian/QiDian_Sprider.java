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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class QiDian_Sprider {

    //获取小说的地址列表
    public List<String> getNovelLinkFromQiDian(String url) throws Exception {

        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建get请求
        HttpGet httpGet = new HttpGet(url);

        // 设置响应头
        String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
        httpGet.setHeader("User-Agent", User_Agent);

        // 发送get请求, 获取响应数据
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != 200) {
            return null;
        }

        //获取相应页面
        String html = EntityUtils.toString(response.getEntity(), "utf-8");

        //使用Jsoup解析html
        Document document = Jsoup.parse(html);

        //获取小说地址
        Elements elements = document.select("#rank-list-row")
                .select("div[class=rank-list sort-list]")
                .select("li")
                .select("a[href^=//book.qidian.com][class!=link]");

        //拼接小说地址, 并返回
        List<String> urlList = new ArrayList<String>();
        String link = null;
        for (Element element : elements) {
            link = element.attr("href");
            urlList.add("https:" + link);
        }

        return urlList;
    }

    //获取小说详情页地址列表
    public List<String> getNovelDetailPagesLinks(List<String> novelLinks) throws Exception {

        List<String> links = new ArrayList<String>();

        //遍历获取每一部小说的详情页地址
        for (String novelLink : novelLinks) {

            //创建新的访问链接
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(novelLink);

            //添加请求头
            String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
            httpGet.setHeader("User-Agent", User_Agent);

            //获取响应内容
            CloseableHttpResponse response = httpClient.execute(httpGet);

            //解析响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                return null;
            }

            //解析响应内容
            String html = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(html);
            String link = document.select("#readBtn")
                    .attr("href");

            links.add("https:" + link);
        }

        return links;
    }

    //获取小说所有章节内容
    public void getNovelTexts(List<String> links) throws Exception {

        //分别获取每一本小说的所有章节内容
        for (String link : links) {

            //发送请求, 获取响应内容
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

            //写入小说的章节内容
            getNovelChapterContents(link, bw);

            bw.close();



        }

    }

    //获取小说指定章节的内容
    public void getNovelChapterContents(String link, BufferedWriter bw) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        while (true) {

            HttpGet httpGet = new HttpGet(link);
            String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
            httpGet.setHeader("User-Agent", User_Agent);

            CloseableHttpResponse response = httpClient.execute(httpGet);

            //解析响应内容
            String html = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(html);

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

            //写入第一章节的内容
            for (Element capter : capters) {
                String capterContent = capter.text();
                bw.write(capterContent);
                bw.flush();
                bw.newLine();
            }
            //获取下一章url
            link = document.select("#j_chapterNext")
                    .attr("href");

            if (link == null || link.trim() == null || !link.startsWith("//read.qidian.com")) {
                break;
            }

            link = "https:" + link;
        }
    }
}
