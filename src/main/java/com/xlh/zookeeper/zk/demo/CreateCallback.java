package com.xlh.zookeeper.zk.demo;

import org.apache.zookeeper.AsyncCallback;

/**
 * @author xiaolei hu
 * @date 2018/12/31 19:15
 **/
public class CreateCallback implements AsyncCallback.StringCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("创建节点：" + path);
        System.out.println((String)ctx);
    }
}
