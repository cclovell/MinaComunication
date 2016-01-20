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

    public static void main(String args[]) {
        IoBuffer buf = IoBuffer.allocate(18);
        buf.putInt(10000);
    }

    @Override
    protected byte[] retBody() {
        try {
            System.out.println("retBody////////////");
            CharsetEncoder ce = getCharSetEncoder();
            int totalLength = id.getBytes("GBK").length + name.getBytes("GBK").length;
            IoBuffer buf = IoBuffer.allocate(totalLength + 8);
            int idLength = id.getBytes("GBK").length;
            buf.putInt(idLength);
            buf.putString(id, ce);
            int nameLength = name.getBytes("GBK").length;
            buf.putInt(nameLength);
            buf.putString(name, ce);
            byte[] msg = new byte[totalLength + 8];
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
        System.out.println("decodeBody////////////");
        IoBuffer in = IoBuffer.wrap(bodyBytes);
        CharsetDecoder cd = getCharSetDecode();
        try {
            int idLength = in.getInt();
            id = in.getString(idLength, cd);
            int nameLength = in.getInt();
            name = in.getString(nameLength, cd);
        }
        catch (CharacterCodingException e) {
            e.printStackTrace();
        }

    }

    public String getCommand() {
        return MessageConstants.CMD_MESSAGE_ITEM;
    }

}
