package com.itheima.sprider_JD_cluster;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class MasterTest {

    @Test
    public void redisTest() {

        Jedis jedis = new Jedis("192.168.66.81", 6379);


        jedis.del("sprider_jd_master");
        Boolean sprider_jd_master = jedis.exists("sprider_jd_master");
        System.out.println(sprider_jd_master);

    }

}