/* 
 * @(#)SessionDto.java    Created on 2015-12-21
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.entity;

import org.apache.mina.core.session.IoSession;

public class SessionDto {
    private String name;
    private IoSession session;
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
