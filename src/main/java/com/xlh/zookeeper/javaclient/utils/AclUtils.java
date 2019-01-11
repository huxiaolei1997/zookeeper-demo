package com.xlh.zookeeper.javaclient.utils;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * @author xiaolei hu
 * @date 2019/1/11 21:52
 **/
public class AclUtils {
    public static String getDigestUserPwd(String id) throws Exception {
        return DigestAuthenticationProvider.generateDigest(id);
    }

    public static void main(String[] args) throws Exception {
        String id = "imooc:imooc";
        String idDigested = getDigestUserPwd(id);
        System.out.println(idDigested);
    }
}
