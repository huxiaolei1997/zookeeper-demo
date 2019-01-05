package com.xlh.zookeeper.countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author xiaolei hu
 * @date 2018/12/31 19:56
 **/
public class CheckStartUp {
    private static List<DangerCenter> stationList;
    private static CountDownLatch countDownLatch;

    public CheckStartUp() {
    }

    public static boolean checkAllStations() throws Exception {
        // 初始化三个调度站
        countDownLatch = new CountDownLatch(3);

        // 把所有站点添加进list
        stationList = new ArrayList<>();
        stationList.add(new StationAnhui(countDownLatch));
        stationList.add(new StationBeijing(countDownLatch));
        stationList.add(new StationZhejiang(countDownLatch));

        // 使用线程池
        Executor executor = Executors.newFixedThreadPool(stationList.size());

        for (DangerCenter center : stationList) {
            executor.execute(center);
        }

        // 等待线程池执行完毕
        countDownLatch.await();

        for (DangerCenter center : stationList) {
            if (!center.isOk()) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        boolean result = CheckStartUp.checkAllStations();
        System.out.println("监控中心针对所有危化品调度站点的检查结果为：" + result);
    }
}
