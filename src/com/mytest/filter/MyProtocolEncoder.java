/* 
 * @(#)MyProtocolEncoder.java    Created on 2015-12-28
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytest.message.AbstractMessage;

public class MyProtocolEncoder extends ProtocolEncoderAdapter {
    private Logger logger = LoggerFactory.getLogger(MyProtocolEncoder.class);

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        AbstractMessage am = (AbstractMessage) message;
        // int byteLen = item.getId().getBytes("GBK").length + item.getName().getBytes("GBK").length;
        byte[] bytes = am.encode();
        // IoBuffer buf = IoBuffer.wrap(bytes);
        IoBuffer buf1 = IoBuffer.allocate(bytes.length);
        buf1.get(bytes);
        buf1.flip();
        out.write(buf1);

    }
}
