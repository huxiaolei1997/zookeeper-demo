package com.xlh.zookeeper.curator;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author xiaolei hu
 * @date 2019/1/13 18:49
 **/
public class MyCuratorWatcher implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("触发watcher. 节点路径为：" + watchedEvent.getPath());
    }
}
