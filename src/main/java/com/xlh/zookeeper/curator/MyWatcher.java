package com.xlh.zookeeper.curator;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author xiaolei hu
 * @date 2019/1/13 18:49
 **/
public class MyWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("触发watcher. 节点路径为：" + watchedEvent.getPath());
    }
}
