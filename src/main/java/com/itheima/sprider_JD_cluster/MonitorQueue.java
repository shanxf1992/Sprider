package com.itheima.sprider_JD_cluster;

import redis.clients.jedis.Jedis;

public class MonitorQueue implements Runnable{

    private Jedis jedis = new Jedis("192.168.66.81", 6379);

    @Override
    public void run() {
        //监控 队列中的个数的
        int i = 0;
        while (true) {
            try {
                //从redis 中获取队列长度
                Long size = jedis.llen("sprider_jd_master");
                System.out.println(size);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
