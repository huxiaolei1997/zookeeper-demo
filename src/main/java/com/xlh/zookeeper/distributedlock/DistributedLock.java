package com.xlh.zookeeper.distributedlock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁 - zk 实现
 *
 * @author xiaolei hu
 * @date 2019/1/19 20:49
 **/
public class DistributedLock {
    private CuratorFramework client;

    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    // 用于挂起当前请求，并且等待上一个分布式锁释放
    private CountDownLatch zkLockLatch = new CountDownLatch(1);

    // 分布式锁的总节点名
    private static final String ZK_LOCK_PROJECT = "imooc-locks";

    // 分布式锁节点
    private static final String DISTRIBUTED_LOCK = "distributed_lock";

    // 构造函数
    public DistributedLock(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 初始化锁
     */
    public void init() {
        // 使用命名空间
        client = client.usingNamespace("ZKLocks-Namespace");

        /**
         * 创建 zk 锁的总节点，相当于 eclipse 工作空间下的项目
         *          ZKLocks-Namespace
         *              |__ imooc-locks
         *                  |__ distributed_lock
         */
        try {
            if (client.checkExists().forPath("/" + ZK_LOCK_PROJECT) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK_PROJECT);

                // 针对 zk 的分布式锁节点，创建相应的 watcher 事件监听
                addWatcherToLock("/" + ZK_LOCK_PROJECT);
            }
        } catch (Exception e) {
            logger.error("客户端连接 zookeeper 服务器错误... 请重试... ");
        }
    }

    /**
     * 获取分布式锁
     */
    public void getLock() {
        // 使用while循环，当且仅当上一个锁释放并且当前请求获得锁成功后才会跳出
        while (true) {
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL) // 临时节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
                logger.info("获得分布式锁成功");
                return;
            } catch (Exception e) {
                logger.error("获取分布式锁失败...");
                try {
                    // 如果没有获取到锁，需要重新设置同步资源值
                    if (zkLockLatch.getCount() <= 0) {
                        zkLockLatch = new CountDownLatch(1);
                    }
                    // 阻塞当前线程，等待另一个线程释放锁，直到成功获取到锁，才会退出这个while循环
                    zkLockLatch.await();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    /**
     * 释放分布式锁
     */
    public boolean releaseLock() {
        try {
            if (client.checkExists().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK) != null) {
                client.delete().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        logger.info("分布式锁释放完毕");
        return true;
    }

    /**
     * 创建 watcher 监听
     *
     * @param path
     * @throws Exception
     */
    private void addWatcherToLock(String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String path = pathChildrenCacheEvent.getData().getPath();
                    logger.info("上一个会话已经释放锁或该会话已断开，节点路径为：{}", path);
                    if (path.contains(DISTRIBUTED_LOCK)) {
                        logger.info("释放计数器，让当前请求来获得分布式锁...");
                        zkLockLatch.countDown();
                    }
                }
            }
        });
    }
}
