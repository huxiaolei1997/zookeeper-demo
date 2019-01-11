package com.xlh.zookeeper.javaclient.zk.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper 获取子节点数据的 demo 演示
 *
 * @author xiaolei hu
 * @date 2019/1/5 21:44
 **/
public class ZkGetChildrenList implements Watcher {
    private ZooKeeper zookeeper = null;

    public static final String zkServerPath = "127.0.0.1:2181";
    public static final Integer timeout = 5000;

    private static CountDownLatch countDown = new CountDownLatch(1);

    public ZkGetChildrenList() {
    }

    public ZkGetChildrenList(String connectString) {
        try {
            zookeeper = new ZooKeeper(connectString, timeout, new ZkGetChildrenList());
        } catch (IOException e) {
            e.printStackTrace();
            if (zookeeper != null) {
                try {
                    zookeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        ZkGetChildrenList zkServer = new ZkGetChildrenList(zkServerPath);

        /**
         * 参数：
         * path：父节点路径
         * watch：true或者false，注册一个watch事件
         */
//		List<String> strChildList = zkServer.getZookeeper().getChildren("/imooc", true);
//		for (String s : strChildList) {
//			System.out.println(s);
//		}

        // 异步调用
        String ctx = "{'callback':'ChildrenCallback'}";
		//zkServer.getZookeeper().getChildren("/imooc", true, new ChildrenCallBack(), ctx);
        zkServer.getZookeeper().getChildren("/imooc", true, new Children2CallBack(), ctx);

        countDown.await();
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("NodeChildrenChanged");
                ZkGetChildrenList zkServer = new ZkGetChildrenList(zkServerPath);
                List<String> strChildList = zkServer.getZookeeper().getChildren(event.getPath(), false);
                for (String s : strChildList) {
                    System.out.println(s);
                }
                countDown.countDown();
            } else if (event.getType() == Event.EventType.NodeCreated) {
                System.out.println("NodeCreated");
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("NodeDataChanged");
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                System.out.println("NodeDeleted");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
