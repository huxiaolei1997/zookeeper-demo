package com.xlh.zookeeper.countdown;

import com.xlh.zookeeper.countdown.DangerCenter;

import java.util.concurrent.CountDownLatch;

/**
 * @author xiaolei hu
 * @date 2018/12/31 19:50
 **/
public class StationBeijing extends DangerCenter {
    public StationBeijing(CountDownLatch countDownLatch) {
        super(countDownLatch, "北京海淀调度站");
    }

    /**
     * 检查危化品车
     * 蒸罐
     * 汽油
     * 轮胎
     * GPS
     * ...
     */
    @Override
    protected void check() {
        System.out.println("正在检查[" + this.getStation() + "]...");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("检查[" + this.getStation() + "] 完毕，可以发车~");
    }
}
