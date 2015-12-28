/* 
 * @(#)SessionManage.java    Created on 2015-11-5
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.mina;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

/**
 * session管理器
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-11-5 下午02:47:58 $
 */
public class SessionManage {
    private Map<String, IoSession> sessionMap = new ConcurrentHashMap<String, IoSession>();

    public Map<String, IoSession> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(Map<String, IoSession> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
