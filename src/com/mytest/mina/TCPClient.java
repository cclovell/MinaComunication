package com.mytest.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytest.filter.MyProtocalFilter;
import com.mytest.message.ActiveMessage;
import com.mytest.message.ActiveRespMessage;

/**
 * 
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-11-3 下午04:15:41 $
 */
public class TCPClient {
    private Logger logger = LoggerFactory.getLogger(TCPClient.class);

    private String clientName;
    private String password;
    private String serverAddress;
    private int port;
    private IoConnector connector;
    private IoSession session;

    public TCPClient(String clientName, String password, String serverAddress, int port) {
        this.clientName = clientName;
        this.password = password;
        this.serverAddress = serverAddress;
        this.port = port;
        init(clientName, password, serverAddress, port);
    }

    private void init(String clientName, String password, String serverAddress, int port) {
        connector = new NioSocketConnector();

        connector.setConnectTimeoutMillis(30000);

        // DemuxingProtocolCodecFactory

        // connector.getFilterChain().addLast(
        // "codec",
        // new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS
        // .getValue(), LineDelimiter.WINDOWS.getValue())));
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        // connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyProtocalFactory()));

        connector.getFilterChain().addLast("codec", new MyProtocalFilter());

        connector.setHandler(new ClinetHandler());
        // 心跳配置
        KeepAliveFilter keepAlivceFilter = new KeepAliveFilter(new KeepAliveMessageFactory() {

            @Override
            public boolean isResponse(IoSession session, Object message) {
                // logger.info((message instanceof ActiveRespMessage) + "*******");
                // logger.info(message + "");
                boolean isResponse = message instanceof ActiveRespMessage;
                logger.info("isActiveRespMessage:" + isResponse);
                return true;
            }

            @Override
            public boolean isRequest(IoSession session, Object message) {
                // logger.info("clientIsRequest");
                // logger.info(message.equals("456") + "************************");
                // logger.info((message instanceof ActiveRespMessage) + "-------");
                // boolean isResponse = message instanceof ActiveRespMessage;

                return false;

            }

            @Override
            public Object getResponse(IoSession session, Object request) {
                // logger.info("getResponse");
                // ActiveRespMessage arm = new ActiveRespMessage();
                return null;
            }

            @Override
            public Object getRequest(IoSession session) {
                logger.info("getRequest");
                ActiveMessage am = new ActiveMessage();
                return am;
            }
        }, IdleStatus.BOTH_IDLE, new KeepAliveRequestTimeoutHandlerImpl());

        keepAlivceFilter.setRequestInterval(5);
        keepAlivceFilter.setRequestTimeout(15);
        // connector.getFilterChain().addLast("keepAlive", keepAlivceFilter);

    }

    public synchronized ConnectFuture connet() {
        ConnectFuture connectFuture = null;
        try {
            connectFuture = connector.connect(new InetSocketAddress(serverAddress, port));
            connectFuture.await();
            session = connectFuture.getSession();
            logger.info("session是否为空：" + (session == null));
        }
        catch (Exception e) {
            logger.info(e.toString());
        }

        return connectFuture;
    }

    public WriteFuture sendMessage(Object message) {
        if (null == session) {
            logger.error("Session is null ,message not sent !");

            WriteFuture future = new DefaultWriteFuture(session);
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("session is null");
            future.setException(illegalArgumentException);
            return future;
        }
        return session.write(message);
    }

    private class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {

        @Override
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            logger.error("Heart Beat timeout!");
            // session.close(true);
        }

    }

}
