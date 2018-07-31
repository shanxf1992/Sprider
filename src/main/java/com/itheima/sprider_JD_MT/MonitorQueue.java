package com.itheima.sprider_JD_MT;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

public class MonitorQueue implements Runnable{

    private ArrayBlockingQueue<String> queue;
    private ExecutorService pool;

    public MonitorQueue(ArrayBlockingQueue<String> queue, ExecutorService pool) {
        this.queue = queue;
        this.pool = pool;
    }

    @Override
    public void run() {
        //监控 队列中的个数的
        int i = 0;
        while(true){
            int size = queue.size();
            System.out.println(size);

            if(size == 0 && i++ > 5){
                pool.shutdown();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
