package com.itheima.sprider_login;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.List;

public class sprider_login {

    public static void main(String[] args) throws Exception {

        String link = "http://www.svn.club/user/login";

//        uid: XXXXX
//        pwd: www.XXXXX.cn

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(link);

        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("uid", "XXXXX"));
        nameValuePairList.add(new BasicNameValuePair("pwd", "www.XXXXX.cn"));

        //设置post请求的 请求体
        HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);
        httpPost.setEntity(httpEntity );

        //执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        //如果返回的是 302 重定向状态码
        if (statusCode == 302) {

            //获取并拼接重定向的路径
            Header[] locations = response.getHeaders("Location");
            link = "http://www.svn.club" + locations[0].getValue();

            //获取登陆后的cookie信息
            Header[] headers = response.getHeaders("Set-Cookie");
            String cookie = headers[0].getValue();

            //重新发起请求
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(link);

            String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
            httpGet.setHeader("User-Agent", User_Agent);
            httpGet.setHeader("Cookie", cookie);

            response = httpClient.execute(httpGet);
            String html = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(html);
            String text = document.select(".tb")
                    .get(0)
                    .select("tr")
                    .get(1)
                    .select("a")
                    .get(0)
                    .text();

            System.out.println(text);
        }else{
            //... ...
        }
    }
}
