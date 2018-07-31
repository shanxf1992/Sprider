package com.itheima.sprider_JD_MT;

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

public class Main {
    //定义全局的线程安全队列, 存放 pid
    public static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
    //创建线成池
    public static DataSource dataSource = JDBCUtils.getDataSource();
    public static ExecutorService pool = Executors.newFixedThreadPool(50);
    public static CloseableHttpClient httpClients = HttpClients.createDefault();

    public static void main(String[] args) {

        //确定宠物搜索页的 url
        String url = "https://search.jd.com/Search?keyword=狗狗活体&enc=utf-8";

        //爬去对应数据
        JD_Sprider jd_sprider = new JD_Sprider();
        try {

            //开启新线程监视queue
            MonitorQueue monitorQueue = new MonitorQueue(queue, pool);
            pool.execute(monitorQueue);

            //开启多个线程将 queue中的pid进行解析, 并存储
            for (int i = 0; i <50 ; i++) {
                savePetTask savePetTask = new savePetTask(queue, httpClients, dataSource);
                pool.execute(savePetTask);
                System.out.println("线程: " + i);
            }

            //将所有的pid都存放在 queue中
            jd_sprider.spriderPetsInfoDataFromJD(url, queue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


