/* 
 * @(#)MyIoHandler.java    Created on 2015-11-3
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.mina;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ClinetHandler extends IoHandlerAdapter {
    private static final Logger log = Logger.getLogger(ClinetHandler.class);

    public ClinetHandler() {
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.info("出现了异常" + cause.toString());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info("client开始接受数据");
        // String msg = message.toString();
        log.info("收到的消息：" + message);
        // if (msg.equals("esc")) {
        // session.close(true);
        // return;
        // }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.info("client开始发送数据");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("client--session--关闭了");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("client创建一个session");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("client无数据传输session处于空闲状态");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("client打开一个session");
        // session.write("hellow 服务端");
    }

}
