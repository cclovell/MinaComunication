/* 
 * @(#)TestClient.java    Created on 2015-12-18
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.mina;

import com.mytest.message.Item;

public class TestClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TCPClient tcpClient = new TCPClient("testClient", "123456", "192.168.0.79", 9123);
        tcpClient.connet();
        Item item = new Item();
        item.setName("张三");
        item.setId("5554646464");
        tcpClient.sendMessage(item);
    }

}
