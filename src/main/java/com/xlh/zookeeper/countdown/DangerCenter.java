package com.xlh.zookeeper.countdown;

import java.util.concurrent.CountDownLatch;

/**
 * 抽象类，用于演示危险品化工车监控中心 统一检查
 *
 * @author xiaolei hu
 * @date 2018/12/31 19:43
 **/
public abstract class DangerCenter implements Runnable {
    // 计数器
    private CountDownLatch countDownLatch;
    // 调度站
    private String station;
    // 调度站针对当前自己的站点进行检查，是否检查 ok 的标志
    private boolean ok;

    public DangerCenter(CountDownLatch countDownLatch, String station) {
        this.countDownLatch = countDownLatch;
        this.station = station;
        this.ok = false;
    }

    @Override
    public void run() {
        try {
        check();
        ok = true;
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        } finally {
            if (null != countDownLatch) {
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 检查危化品车
     * 蒸罐
     * 汽油
     * 轮胎
     * GPS
     * ...
     */
    protected abstract void check();

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
