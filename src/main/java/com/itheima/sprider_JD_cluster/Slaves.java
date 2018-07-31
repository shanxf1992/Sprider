package com.itheima.sprider_JD_cluster;

import com.itheima.utils.JDBCUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Slaves {

    //创建线成池
    public static DataSource dataSource = JDBCUtils.getDataSource();
    public static ExecutorService pool = Executors.newFixedThreadPool(20);
    public static CloseableHttpClient httpClients = HttpClients.createDefault();

    public static void main(String[] args) {

        //开启多个线程将 queue中的pid进行解析, 并存储
            for (int i = 0; i <20 ; i++) {
                SavePetTask savePetTask = new SavePetTask(httpClients, dataSource);
                pool.execute(savePetTask);
            }
    }
}
