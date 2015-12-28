/* 
 * @(#)TestRedis.java    Created on 2015-11-20
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.redis;

import redis.clients.jedis.Jedis;

public class TestRedis {
    public static void main(String args[]) {
        // 创建连接
        String host = "192.168.0.79";
        int port = 6379;
        Jedis client = new Jedis(host, port);

        // 执行set指令
        String result = client.set("aa", "Hello, Redis!");
        System.out.println(String.format("set指令执行结果:%s", result));

        // 执行get指令
        String value = client.get("aa");
        System.out.println(String.format("get指令执行结果:%s", value));
    }
}
