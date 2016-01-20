/* 
 * @(#)MyProtocalFilter.java    Created on 2016-1-18
 * Copyright (c) 2016 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.mytest.filter;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.file.FileRegion;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestWrapper;

import com.mytest.message.AbstractMessage;
import com.mytest.message.Item;
import com.mytest.message.MessageConstants;

public class MyProtocalFilter extends IoFilterAdapter {

    private static final IoBuffer EMPTY_BUFFER = IoBuffer.wrap(new byte[0]);

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }

    @Override
    public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(nextFilter, session, cause);
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (!(message instanceof IoBuffer)) {
            nextFilter.messageReceived(session, message);
            return;
        }

        IoBuffer in = (IoBuffer) message;

        while (in.hasRemaining()) {
            byte[] headBytes = new byte[MessageConstants.PK_HEAD_LENGTH];
            in.get(headBytes);
            ByteBuffer byteBuffer = ByteBuffer.allocate(headBytes.length);
            byteBuffer.put(headBytes);
            byteBuffer.flip();
            int length = byteBuffer.getInt();
            byteBuffer.position(6);
            int command = byteBuffer.getInt();

            byte[] allBytes = new byte[length];
            byte[] bodyBytes = new byte[length - headBytes.length];
            in.get(bodyBytes);

            System.arraycopy(headBytes, 0, allBytes, 0, headBytes.length);
            System.arraycopy(bodyBytes, 0, allBytes, headBytes.length, bodyBytes.length);

            if (command == MessageConstants.TOK_LOGIN) {
                AbstractMessage item = new Item();
                item.decode(allBytes);
                nextFilter.messageReceived(session, item);
            }
        }

    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (writeRequest instanceof EncodedWriteRequest) {
            return;
        }
        if (writeRequest instanceof MessageWriteRequest) {
            MessageWriteRequest wrappedRequest = (MessageWriteRequest) writeRequest;
            nextFilter.messageSent(session, wrappedRequest.getParentRequest());
        }
        else {
            nextFilter.messageSent(session, writeRequest);
        }
    }

    @Override
    public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        Object message = writeRequest.getMessage();

        // Bypass the encoding if the message is contained in a IoBuffer,
        // as it has already been encoded before
        if ((message instanceof IoBuffer) || (message instanceof FileRegion)) {
            nextFilter.filterWrite(session, writeRequest);
            return;
        }

        if (message instanceof AbstractMessage) {
            AbstractMessage m = (AbstractMessage) message;
            byte[] bytes = m.encode();
            IoBuffer outBuffer = IoBuffer.wrap(bytes);

            SocketAddress destination = writeRequest.getDestination();
            WriteRequest encodedWriteRequest = new EncodedWriteRequest(outBuffer, null, destination);

            nextFilter.filterWrite(session, encodedWriteRequest);
        }

        // Call the next filter
        nextFilter.filterWrite(session, new MessageWriteRequest(writeRequest));

    }

    private static class EncodedWriteRequest extends DefaultWriteRequest {
        public EncodedWriteRequest(Object encodedMessage, WriteFuture future, SocketAddress destination) {
            super(encodedMessage, future, destination);
        }

        @Override
        public boolean isEncoded() {
            return true;
        }
    }

    private static class MessageWriteRequest extends WriteRequestWrapper {
        public MessageWriteRequest(WriteRequest writeRequest) {
            super(writeRequest);
        }

        @Override
        public Object getMessage() {
            return EMPTY_BUFFER;
        }

        @Override
        public String toString() {
            return "MessageWriteRequest, parent : " + super.toString();
        }
    }

}
