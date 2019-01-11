package com.xlh.zookeeper.javaclient.zk.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaolei hu
 * @date 2018/12/31 20:07
 **/
public class ZKGetNodeData implements Watcher {
    private ZooKeeper zooKeeper = null;

    private static final String zkServerPath = "127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";
    private static final Integer timeout = 5000;
    private static Stat stat = new Stat();

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public ZKGetNodeData() {
    }

    public ZKGetNodeData(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, timeout, new ZKGetNodeData());
        } catch (IOException e) {
            e.printStackTrace();
            if (null != zooKeeper) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZKGetNodeData zkServer = new ZKGetNodeData(zkServerPath);

        /**
         * path: 节点路径
         * watch: true 或 false，注册一个watch事件
         * stat 状态
         */
        byte[] resByte = zkServer.getZooKeeper().getData("/data", true, stat);
        String result = new String(resByte);
        System.out.println("当前值：" + result);
        countDownLatch.await();
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if (event.getType() == Event.EventType.NodeDataChanged) {
                ZKGetNodeData zkServer = new ZKGetNodeData(zkServerPath);
                byte[] resByte = zkServer.getZooKeeper().getData("/data", false, stat);
                String result = new String(resByte);
                System.out.println("更改后的值：" + result);
                System.out.println("版本号变化dversion:" + stat.getVersion());
                countDownLatch.countDown();
            } else if (event.getType() == Event.EventType.NodeChildrenChanged) {

            } else if (event.getType() == Event.EventType.NodeDeleted) {

            } else if (event.getType() == Event.EventType.NodeCreated) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
