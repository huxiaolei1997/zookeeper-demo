package com.xlh.zookeeper.javaclient.zk.demo;

import org.apache.zookeeper.AsyncCallback;

/**
 * @author xiaolei hu
 * @date 2018/12/31 19:31
 **/
public class DeleteCallback implements AsyncCallback.VoidCallback {

    @Override
    public void processResult(int rc, String path, Object ctx) {
        System.out.println("异步删除一个节点：" + path);
        System.out.println((String)ctx);
    }
}
