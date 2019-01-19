package com.xlh.zookeeper.service;

import org.springframework.stereotype.Service;

/**
 * @author xiaolei hu
 * @date 2019/1/19 14:26
 **/
@Service
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        System.out.println("测试");
    }
}
