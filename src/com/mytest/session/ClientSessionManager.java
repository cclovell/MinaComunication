/* 
 * @(#)ClientSessionManager.java    Created on 2015-12-21
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.session;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

/**
 * 客户端IoSession管理器
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-12-21 上午10:44:58 $
 */
public class ClientSessionManager {
    private ConcurrentHashMap<String, IoSession> sessionMap;

}
