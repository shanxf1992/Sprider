package com.itheima.sprider_JD_MT;

import com.itheima.entity.Pet;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class savePetTask implements Runnable {

    private ArrayBlockingQueue<String> queue;
    private CloseableHttpClient httpClients;
    private DataSource dataSource;

    public savePetTask(ArrayBlockingQueue<String> queue, CloseableHttpClient httpClients, DataSource dataSource) {
        this.queue = queue;
        this.httpClients = httpClients;
        this.dataSource = dataSource;
    }

    public void run() {
        //循环从queue中取pid
        while (true) {
            try {
                String pid = queue.poll(5000, TimeUnit.MILLISECONDS);
                if (pid == null) {
                    break;
                }

                String url = "https://item.jd.com/" + pid + ".html";
                Pet pet = parsePet(url);
                saveProduct(pet, dataSource);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获取pet信息
    private Pet parsePet(String url) {
        HttpGet httpGet = new HttpGet(url);

        //设置请求头
        String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
        httpGet.setHeader("User-Agent", User_Agent);

        //发送请求, 获取请求页面
        CloseableHttpResponse response = null;
        String html = null;
        try {

            response = httpClients.execute(httpGet);
            html = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        return pet;
    }

    //保存商品到数据库
    private void saveProduct(Pet pet, DataSource dataSource) throws Exception {

        QueryRunner queryRunner = new QueryRunner(dataSource);

        String sql = "insert into pet(pid, breed, shapes, weight, sex, name, price)" +
                " values(?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {pet.getPid(), pet.getBreed(), pet.getShapes(), pet.getWeight(), pet.getSex(), pet.getName(), pet.getPrice().toString()};
        queryRunner.update(sql,  params);
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
}