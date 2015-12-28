/* 
 * @(#)ServerSessionManageer.java    Created on 2015-12-21
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.session;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.mytest.entity.SessionDto;
import com.mytest.entity.TestConstants;

/**
 * 服务端IoSession管理器
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-12-21 上午10:45:31 $
 */
public class ServerSessionManager {
    private ConcurrentHashMap<String, SessionDto> sessionMap;

    public ServerSessionManager() {
        sessionMap = new ConcurrentHashMap<String, SessionDto>();
    }

    public void addSession(String uuid, IoSession session) {
        if (session == null) {
            return;
        }
        session.setAttribute(TestConstants.SESSION_KEY_SESSION_NAME, uuid);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUuid(uuid);
        sessionDto.setSession(session);

        sessionMap.put(uuid, sessionDto);
    }

    public IoSession getSession(String serverName) {
        return sessionMap.get(serverName).getSession();
    }

    public void removeSession(String serverName) {
        sessionMap.remove(serverName);
    }

}
