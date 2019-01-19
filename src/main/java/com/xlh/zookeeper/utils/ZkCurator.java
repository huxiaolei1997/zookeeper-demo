package com.xlh.zookeeper.utils;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaolei hu
 * @date 2019/1/19 13:51
 **/
public class ZkCurator {
    private CuratorFramework client;
    private static final Logger logger = LoggerFactory.getLogger(ZkCurator.class);

    public ZkCurator(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 初始化操作
     */
    public void init() {
        // 使用命名空间
        client = client.usingNamespace("zk-curator-connector");
    }

    /**
     * 判断 zk 是否连接
     * @return
     */
    public boolean isZkAlive() {
        return client.isStarted();
    }
}
