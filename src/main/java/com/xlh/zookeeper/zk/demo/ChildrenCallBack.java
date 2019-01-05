package com.xlh.zookeeper.zk.demo;

import org.apache.zookeeper.AsyncCallback;

import java.util.List;

/**
 * @author xiaolei hu
 * @date 2019/1/5 22:04
 **/
public class ChildrenCallBack implements AsyncCallback.ChildrenCallback {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        for (String s : children) {
            System.out.println(s);
        }
        System.out.println("ChildrenCallback:" + path);
        System.out.println((String) ctx);
    }
}
