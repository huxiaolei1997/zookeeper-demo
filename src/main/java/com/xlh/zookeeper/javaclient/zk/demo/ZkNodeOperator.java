package com.xlh.zookeeper.javaclient.zk.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaolei hu
 * @date 2018/12/31 18:53
 **/
public class ZkNodeOperator implements Watcher {
    private ZooKeeper zooKeeper = null;
    private static final String zkServerPath = "127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";
    private static final Integer timeout = 5000;

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public ZkNodeOperator() {

    }

    public ZkNodeOperator(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, timeout, new ZkNodeOperator());
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

    public static void main(String[] args) throws Exception {
        ZkNodeOperator zkServer = new ZkNodeOperator(zkServerPath);

        // 创建zk节点
        //zkServer.createZkNode("/testnode", "testnode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);

        // 同步方式修改节点数据
        //Stat stat = zkServer.getZooKeeper().setData("/testnode", "modify".getBytes(), 0);
        //System.out.println(stat.getVersion());
        // 异步方式修改节点数据
        // ...

        // 同步方式删除一个节点
//        zkServer.createZkNode("/delete-testnode", "testnode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
//        zkServer.getZooKeeper().delete("/delete-testnode", 0);

        // 异步方式删除一个节点
        String ctx = "{'delete':'success'}";

        zkServer.createZkNode("/delete-testnode-aysnc", "testnode-aysnc".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
        zkServer.getZooKeeper().delete("/delete-testnode-aysnc", 0, new DeleteCallback(), ctx);
        Thread.sleep(2000);
    }

    /**
     * 创建 zookeeper 节点
     */
    private void createZkNode(String path, byte[] data, List<ACL> acls) {
        String result = "";

        /**
         * 同步或者异步创建节点，都不支持子节点的递归创建，异步有一个callback函数
         * 参数
         * path: 创建的路径
         * data: 存储的数据的byte[]
         * acl: 控制权限策略
         * Ids.OPEN_ACL_UNSAFE -> world:anyone:cdrwa
         * CREATOR_ALL_ACL -> auth:user:password:cdrwa
         * createNode: 节点类型，是一个枚举
         * PERSISTENT: 持久节点
         * PERSISTENT_SEQUENTIAL: 持久顺序节点
         * EPHEMERAL: 临时节点
         *
         */
        try {
            // 同步创建临时节点，创建成功才会返回
            //result = zooKeeper.create(path, data, acls, CreateMode.EPHEMERAL);
            //System.out.println("创建节点：\t" + result + "\t成功...");

            result = zooKeeper.create(path, data, acls, CreateMode.PERSISTENT);
            System.out.println("创建节点：\t" + result + "\t成功...");
            // 异步创建持久节点的方法
//            String ctx = "{'create':'success'}";
//            zooKeeper.create(path, data, acls, CreateMode.PERSISTENT, new CreateCallback(), ctx);
//
//            System.out.println("创建节点：\t" + result + "\t成功...");
//            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
