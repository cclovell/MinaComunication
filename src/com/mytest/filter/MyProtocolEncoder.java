/* 
 * @(#)MyProtocolEncoder.java    Created on 2015-12-28
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.filter;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytest.message.Item;

public class MyProtocolEncoder extends ProtocolEncoderAdapter {
    private Logger logger = LoggerFactory.getLogger(MyProtocolEncoder.class);

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        Item item = (Item) message;
        int byteLen = item.getId().getBytes("GBK").length + item.getName().getBytes("GBK").length;
        logger.info("byteLen的值为：" + byteLen);
        IoBuffer buf = IoBuffer.allocate(byteLen);
        buf.putString(item.getId(), Charset.forName("GBK").newEncoder());
        // logger.info("name的长度为：" + item.getName().getBytes("GBK").length);
        // buf.putInt(item.getName().getBytes("GBK").length);
        buf.putString(item.getName(), Charset.forName("GBK").newEncoder());
        // buf.put(item.getName().getBytes("GBK"));
        buf.flip();
        out.write(buf);

    }
}
