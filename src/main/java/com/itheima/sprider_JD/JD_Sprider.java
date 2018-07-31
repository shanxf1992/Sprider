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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JD_Sprider {

    private CloseableHttpClient httpClients;

    public JD_Sprider(){
        httpClients = HttpClients.createDefault();
    }

    public void spriderPetsInfoDataFromJD(String url) throws Exception {

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
            
            //获取所有的宠物的pids
            List<String> pids = parsePids(document);

            //根据宠物的pids 获取宠物的信息
            saveProductsFromPids(pids);
        }

    }

    private void saveProductsFromPids(List<String> pids) throws Exception {

        List<Pet> pets = new ArrayList<Pet>();

        for (String pid : pids) {
            //拼接url
            String url = "https://item.jd.com/" + pid + ".html";
            HttpGet httpGet = new HttpGet(url);

            //设置请求头
            String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
            httpGet.setHeader("User-Agent", User_Agent);

            //发送请求, 获取请求页面
            CloseableHttpResponse response = httpClients.execute(httpGet);
            String html = EntityUtils.toString(response.getEntity());

            //解析页面, 获取document对象
            Document document = Jsoup.parse(html);

            //定义宠物实体类
            Pet pet = new Pet();

            //设置狗狗价格
            pet.setPrice((int)(Math.random()*1000) * 1.0);

            //解析宠物品种
            Map<String, String> petInfo = parsePetInfo(document);

            //设置pet的属性
            pet.setPid(petInfo.get("商品编号"));
            pet.setName(petInfo.get("商品名称"));
            pet.setBreed(petInfo.get("狗狗品种"));
            pet.setWeight(petInfo.get("商品毛重"));
            pet.setShapes(petInfo.get("狗狗体型"));
            pet.setSex(petInfo.get("狗狗性别"));

            pets.add(pet);
        }

        saveProduct(pets);
    }


    private void saveProduct(List<Pet> pets) throws Exception {

        DataSource dataSource = JDBCUtils.getDataSource();
        QueryRunner queryRunner = new QueryRunner(dataSource);

        for (Pet pet : pets) {
            String sql = "insert into pet(pid, breed, shapes, weight, sex, name, price)" +
                    " values(?, ?, ?, ?, ?, ?, ?)";

            Object[] params = {pet.getPid(), pet.getBreed(), pet.getShapes(), pet.getWeight(), pet.getSex(), pet.getName(), pet.getPrice().toString()};
            System.out.println(pet);
            queryRunner.update(sql,  params);
        }

    }

    private Map<String, String> parsePetInfo(Document document) {

        Map<String, String> map = new HashMap<String, String>();

        //获取宠物描述列表
        Elements elements = document.select("ul[class=parameter2 p-parameter-list]")
                .select("li");

        for (Element element : elements) {
            String info = element.text();
            String[] parseInfo = info.split("：");

            map.put(parseInfo[0], parseInfo[1]);
        }

        return map;
    }

    private List<String> parsePids(Document document) {

        Elements elements = document
                .select("#J_goodsList")
                .select("ul")
                .select("li");

        List<String> pids = new ArrayList<String>();
        
        for (Element element : elements) {
            pids.add(element.attr("data-sku"));
        }

        return pids;
    }
}
