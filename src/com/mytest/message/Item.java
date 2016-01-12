/* 
 * @(#)Item.java    Created on 2015-12-28
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.message;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;

public class Item extends AbstractMessage {
    private static final long serialVersionUID = -4076944799526176830L;
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected int retCommand() {
        return MessageConstants.TOK_LOGIN;
    }

    @Override
    protected byte[] retBody() {
        try {
            CharsetEncoder ce = getCharSetEncoder();
            int totalLength = id.getBytes("GBK").length + name.getBytes("GBK").length;
            IoBuffer buf = IoBuffer.allocate(totalLength);
            buf.putString(id, ce);
            buf.putString(name, ce);
            byte[] msg = new byte[totalLength];
            buf.flip();
            buf.get(msg);
            buf.clear();
            return msg;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void decodeBody(byte[] bodyBytes) {
        IoBuffer in = IoBuffer.wrap(bodyBytes);
        CharsetDecoder cd = getCharSetDecode();
        try {
            id = in.getString(10, cd);
            name = in.getString(cd);
        }
        catch (CharacterCodingException e) {
            e.printStackTrace();
        }

    }

    public String getCommand() {
        return MessageConstants.CMD_MESSAGE_ITEM;
    }

}
