/* 
 * @(#)ActiveMessage.java    Created on 2015-12-22
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.message;

import java.util.Date;

public class ActiveMessage extends AbstractMessage {
    private static final long serialVersionUID = 5884393960229526691L;
    private String messageId;
    private Date creationTime;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    protected int retCommand() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected byte[] retBody() {
        // TODO Auto-generated method stub
        return null;
    }
}
