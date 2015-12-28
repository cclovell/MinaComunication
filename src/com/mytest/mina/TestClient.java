/* 
 * @(#)TestClient.java    Created on 2015-12-18
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.mina;

import com.mytest.message.ActiveMessage;

public class TestClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        TCPClient tcpClient = new TCPClient("testClient", "123456", "192.168.0.79", 9123);
        tcpClient.connet();
        ActiveMessage am = new ActiveMessage();
        am.setMessageId("123456789");
        tcpClient.sendMessage(am);
    }

}
