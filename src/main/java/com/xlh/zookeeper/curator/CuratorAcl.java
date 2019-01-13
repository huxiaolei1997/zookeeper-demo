package com.xlh.zookeeper.curator;

import com.xlh.zookeeper.utils.AclUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

public class CuratorAcl {

    public CuratorFramework client = null;
    public static final String zkServerPath = "127.0.0.1:2181";

    public CuratorAcl() {
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder().authorization("digest", "imooc1:123456".getBytes())
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        // 实例化
        CuratorAcl cto = new CuratorAcl();
        boolean isZkCuratorStarted = cto.client.isStarted();
        System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "连接中" : "已关闭"));

        String nodePath = "/acl/father/child/sub";

        List<ACL> acls = new ArrayList<>();
        Id imooc1 = new Id("digest", AclUtils.getDigestUserPwd("imooc1:123456"));
        Id imooc2 = new Id("digest", AclUtils.getDigestUserPwd("imooc2:123456"));
        acls.add(new ACL(Perms.ALL, imooc1));
        acls.add(new ACL(Perms.READ, imooc2));
        acls.add(new ACL(Perms.DELETE | Perms.CREATE, imooc2));

        // 创建节点
		byte[] data = "spiderman".getBytes();
		cto.client.create().creatingParentsIfNeeded()
				.withMode(CreateMode.PERSISTENT)
				.withACL(acls, true)
				.forPath(nodePath, data);

// 给已存在的节点设置权限（如果当前节点是匿名权限，那么可以直接设置权限，如果当前节点本身就有权限，那么在设置权限之前需要做一个认证）
//        cto.client.setACL().withACL(acls).forPath("/curatorNode");

        // 更新节点数据
//		byte[] newData = "batman".getBytes();
//		cto.client.setData().withVersion(0).forPath(nodePath, newData);

        // 删除节点
//		cto.client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(0).forPath(nodePath);

        // 读取节点数据
//		Stat stat = new Stat();
//		byte[] data = cto.client.getData().storingStatIn(stat).forPath(nodePath);
//		System.out.println("节点" + nodePath + "的数据为: " + new String(data));
//		System.out.println("该节点的版本号为: " + stat.getVersion());


        cto.closeZKClient();
        boolean isZkCuratorStarted2 = cto.client.isStarted();
        System.out.println("当前客户的状态：" + (isZkCuratorStarted2 ? "连接中" : "已关闭"));
    }

}