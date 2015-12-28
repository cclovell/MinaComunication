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

import com.mytest.message.ActiveMessage;
import com.mytest.session.ServerSessionManager;
import com.mytest.util.UUIDUtils;

public class ServerHandler extends IoHandlerAdapter {
    private static Logger log = Logger.getLogger(ServerHandler.class);

    private ServerSessionManager sessionManager;

    public ServerHandler(ServerSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.info("出现了异常" + cause.toString());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info("server开始接受数据");
        if (message instanceof ActiveMessage) {
            ActiveMessage am = (ActiveMessage) message;
            log.info("收到的ActiveMessage消息：" + am);
        }
        String msg = message.toString();
        log.info("收到的消息：" + msg);
        if (msg.equals("esc")) {
            session.close(true);
            return;
        }
        // Collection<IoSession> ioSessions = session.getService().getManagedSessions().values();

        // for (IoSession is : ioSessions) {
        // if (is != session) {
        // is.write("向客户端" + is.getId() + ":发送消息：" + msg);
        // }
        //
        // }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.info("server开始发送数据");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("server--session--关闭了");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("server创建一个session");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("idle");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("server打开一个session");
        sessionManager.addSession(UUIDUtils.getUUID(), session);
    }

}
