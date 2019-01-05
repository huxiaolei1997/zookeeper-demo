package com.xlh.zookeeper.zk.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaolei hu
 * @date 2018/12/31 13:08
 **/
public class ZkConnect implements Watcher {
    private static final Logger log = LoggerFactory.getLogger(ZkConnect.class);
    private static final String zkServerPath = "127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";
    private static final Integer timeout = 5000;

    public static void main(String[] args) throws InterruptedException, IOException {
        /**
         * 客户端和zk服务端连接是一个异步的过程
         * 当连接成功后，客户端会收到一个watch通知
         *
         * 参数
         * connectString:连接服务器的ip字符串
         * 可以是一个ip，也可以是多个ip，也可以在ip后面加路径
         * sessionTimeout 超时时间，心跳收不到了，那就超时
         * watcher：通知时间，如果有对应的事件触发，则会收到一个通知：如果不需要，那就设置为null
         * canBeReadyOnly 可读，当这个节点断开后，还是可以读到数据的，只是不能写
         * 此时数据被读取到的可能是旧数据，此处建议设置为false，不推荐使用
         * sessionId 会话的id
         * sessionPasswd 会话密码，当会话丢失后，可以依据 sessionId 和 sessionPasswd 重新获取会话
         */
        ZooKeeper zk = new ZooKeeper(zkServerPath, timeout, new ZkConnect());

        log.warn("客户端开始连接zookeeper服务器...");
        log.warn("连接状态:{}", zk.getState());

        Thread.sleep(2000);

        log.warn("连接状态：{}", zk.getState());
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.warn("接收到watch通知：{}", watchedEvent);
    }
}
