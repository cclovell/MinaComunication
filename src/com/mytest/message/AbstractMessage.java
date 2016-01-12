/* 
 * @(#)AbstractMessage.java    Created on 2012-2-24
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id: AbstractMessage.java 58001 2015-04-23 08:22:46Z zhaowj $
 */
package com.mytest.message;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 消息的顶级抽象父类（length、version、command、sequence合起来应该叫报头）
 * 
 * @author zhaowj
 * @version $Revision: 58001 $, $Date: 2015-04-23 16:22:46 +0800 (Thu, 23 Apr 2015) $
 */
public abstract class AbstractMessage implements Serializable {

    private static final long serialVersionUID = -3080009144186862046L;

    public static final int TOK_VERSION2 = 20;

    private int length; // 报文长度；4 byte
    private byte appVersion = 0; // 上层应用的版本号，byte
    private int wpcfVersion = TOK_VERSION2; // 版本；2 byte（编码的时候会转成short类型）
    private int command; // 命令代码；4 byte

    private byte[] sequence = new byte[16]; // 报文唯一编号 16 byte

    public int getLength() {
        return length;
    }

    public int getWpcfVersion() {
        return wpcfVersion;
    }

    public byte getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(byte appVersion) {
        this.appVersion = appVersion;
    }

    public void setWpcfVersion(int version) {
        this.wpcfVersion = version;
    }

    public byte[] getSequence() {
        return sequence;
    }

    /**
     * 设置报文唯一编号
     * 
     * @param sequence
     */
    public void setSequence(byte[] sequence) {
        if (sequence == null) {
            throw new IllegalArgumentException("sequence must not be null");
        }

        if (sequence.length == 16) {
            this.sequence = sequence;
        }
        else {
            // sequence小于16位长度
            byte[] newSequence = new byte[16];
            int i = 0;
            for (byte b : sequence) {
                newSequence[i++] = b;
            }

            this.sequence = newSequence;
        }
    }

    public CharsetDecoder getCharSetDecode() {
        return Charset.forName("GBK").newDecoder();
    }

    public CharsetEncoder getCharSetEncoder() {
        return Charset.forName("GBK").newEncoder();
    }

    @Override
    public String toString() {
        return getClass().getName() + "\t";
    }

    /**
     * 编码成字节数组（根据子类得到的报体拼合报头）
     * 
     * @return
     * @throws EncoderException
     */
    public byte[] encode() {
        try {
            byte[] body = retBody();
            length = MessageConstants.PK_HEAD_LENGTH + (body == null ? 0 : body.length);
            ByteBuffer bb = ByteBuffer.allocate(length);
            bb.position(4);

            // 消息版本
            bb.put(appVersion);
            bb.put((byte) wpcfVersion);

            // 命令代码
            if (0 == command) {
                command = retCommand();
            }
            bb.putInt(command);

            // 消息唯一标识
            bb.put(sequence);

            // 内容
            if (body != null) {
                bb.put(body);
            }

            byte[] all = new byte[length];

            // 报文长度
            bb.position(0);
            bb.putInt(length);

            bb.position(0);
            bb.get(all, 0, length);
            bb.clear();

            return all;
        }
        catch (Exception e) {
            // throw new EncoderException(e);
        }
        return sequence;
    }

    /**
     * 从字节数组解码成对象（解析出报头，报体交由子类实现自己解析）
     * 
     * @param msg
     * @throws DecoderException
     */
    public void decode(byte[] msg) {
        try {
            ByteBuffer bb = ByteBuffer.wrap(msg);

            // 报文长度
            length = bb.getInt();

            // 版本
            appVersion = bb.get();
            wpcfVersion = bb.get();

            // 命令代码
            command = bb.getInt();

            // 报文唯一编号
            byte[] seq = new byte[16];
            bb.get(seq); // get sequence
            sequence = seq;

            // if (this.length != msg.length) {
            // throw new DecoderException("TOCProtocol Header error,reason:lLength is not right!");
            // }

            // 内容
            byte[] bodyBytes = new byte[bb.remaining()];
            bb.get(bodyBytes);
            bb.clear();

            decodeBody(bodyBytes);
        }
        catch (Exception e) {
            // if (e instanceof DecoderException) {
            // throw (DecoderException) e;
            // }
            // else {
            // throw new DecoderException(e);
            // }
        }
    }

    /**
     * 子类实现，返回命令代码
     * 
     * @return
     */
    protected abstract int retCommand();

    /**
     * 子类实现，返回报文体的字节数组
     * 
     * @return
     */
    protected abstract byte[] retBody();

    /**
     * 子类实现，对报文体部分进行解码
     * 
     * @param bodyBytes
     */
    protected abstract void decodeBody(byte[] bodyBytes);

}
