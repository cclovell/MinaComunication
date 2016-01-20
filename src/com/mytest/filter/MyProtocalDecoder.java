/* 
 * @(#)MyProtocalDecoder.java    Created on 2015-12-28
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytest.message.AbstractMessage;
import com.mytest.message.Item;
import com.mytest.message.MessageConstants;

public class MyProtocalDecoder extends ProtocolDecoderAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MyProtocolEncoder.class);

    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        //
        int length = in.getInt();
        logger.debug("报文长度" + length);
        in.position(6);
        int command = in.getInt();
        logger.debug("command为：" + command);

        in.position(0);

        byte[] bodyBytes = new byte[length];
        in.get(bodyBytes);
        if (command == MessageConstants.TOK_LOGIN) {
            AbstractMessage item = new Item();
            item.decode(bodyBytes);
            out.write(item);
        }

    }
}
