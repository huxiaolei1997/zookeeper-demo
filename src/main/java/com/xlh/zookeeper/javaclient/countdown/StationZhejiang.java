package com.xlh.zookeeper.javaclient.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * @author xiaolei hu
 * @date 2018/12/31 19:51
 **/
public class StationZhejiang extends DangerCenter {

    public StationZhejiang(CountDownLatch countDownLatch) {
        super(countDownLatch, "浙江杭州调度站");
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("检查[" + this.getStation() + "] 完毕，可以发车~");
    }
}
