package com.xlh.zookeeper.controller;

import com.xlh.zookeeper.service.TestService;
import com.xlh.zookeeper.utils.ZkCurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author xiaolei hu
 * @date 2019/1/19 13:57
 **/
@RestController
public class ZkController {
    @Autowired
    private ZkCurator zkCurator;

    @Autowired
    private TestService testService;

    @RequestMapping("/isZkAlive")
    public String isZkAlive(HttpServletRequest request) {
        boolean isAlive = zkCurator.isZkAlive();
        String result = isAlive ? "连接" : "断开";
        System.out.println(request.getRequestURL() + ", uri=" + request.getRequestURI());
        return result;
    }
}
