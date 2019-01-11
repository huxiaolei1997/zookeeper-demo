package com.xlh.zookeeper.javaclient.zk.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * zookeeper 恢复之前的会话连接demo演示
 * @author xiaolei hu
 * @date 2018/12/31 18:41
 **/
public class ZkConnectionSessionWatcher implements Watcher {
    private static final Logger log = LoggerFactory.getLogger(ZkConnect.class);
    private static final String zkServerPath = "127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";
    private static final Integer timeout = 5000;

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZkConnectionSessionWatcher());

        long sessionId = zk.getSessionId();
        byte[] sessionPassword = zk.getSessionPasswd();

        log.warn("客户端开始连接 zookeeper 服务器...");
        log.warn("连接状态：{}", zk.getState());
        Thread.sleep(1000);

        log.warn("连接状态：{}", zk.getState());

        Thread.sleep(200);

        // 开始会话重连
        log.warn("开始会话重连...");

        ZooKeeper zkSession = new ZooKeeper(zkServerPath, timeout,
                new ZkConnectionSessionWatcher(),
                sessionId,
                sessionPassword);

        log.warn("重新连接状态zkSession:{}", zkSession.getState());
        Thread.sleep(1000);
        log.warn("重新连接状态zkSession:{}", zkSession.getState());
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
