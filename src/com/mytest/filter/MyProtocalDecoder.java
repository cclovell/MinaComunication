/* 
 * @(#)MyProtocalDecoder.java    Created on 2015-12-28
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.filter;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytest.message.Item;

public class MyProtocalDecoder extends ProtocolDecoderAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MyProtocolEncoder.class);

    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        CharsetDecoder cd = Charset.forName("GBK").newDecoder();
        String id = in.getString(10, cd);
        logger.info("id 为：" + id + "id的长度" + id.length());
        String name = in.getString(cd);
        // int len = in.getInt();
        // logger.info("name的长度为：" + len);
        // byte[] dst = new byte[len];
        //
        // in.get(dst);
        //
        // String name = new String(dst, "GBK");

        Item item = new Item();
        item.setId(id);
        item.setName(name);
        out.write(item);
    }
}
