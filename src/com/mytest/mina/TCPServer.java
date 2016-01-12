package com.mytest.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytest.filter.MyProtocalFactory;
import com.mytest.message.ActiveMessage;
import com.mytest.message.ActiveRespMessage;
import com.mytest.session.ServerSessionManager;

/**
 * 服务器端
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-11-3 下午04:15:41 $
 */
public class TCPServer extends Thread {
    private Logger logger = LoggerFactory.getLogger(TCPServer.class);
    private int port;

    public TCPServer(int port, String serverName) {
        this.port = port;
    }

    public static void main(String[] args) {
        new TCPServer(9123, "testServerName").start();
    }

    @Override
    public void run() {

        try {
            IoAcceptor acceptor = new NioSocketAcceptor();

            // acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            acceptor.getSessionConfig().setReadBufferSize(2048);

            // ProtocolEncoderAdapter 编码器实现接口
            // ProtocolDecoderAdapter 解码器实现接口
            // CumulativeProtocolDecoder 累积性的协议解码器（为了防止频繁调用解码器中的decode()方法），所有解码由这个类同意管理

            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyProtocalFactory()));

            acceptor.getFilterChain().addLast("logger", new LoggingFilter());

            // 心跳配置
            KeepAliveFilter keepAlivceFilter = new KeepAliveFilter(new KeepAliveMessageFactory() {

                @Override
                public boolean isResponse(IoSession session, Object message) {
                    // logger.info((message instanceof ActiveRespMessage) + "");
                    // boolean isResponse = message instanceof ActiveRespMessage;
                    return false;
                }

                @Override
                public boolean isRequest(IoSession session, Object message) {
                    // logger.info((message instanceof ActiveMessage) + "");
                    boolean isRequest = message instanceof ActiveMessage;
                    // return isRequest;
                    logger.info("isActiveMessage:" + isRequest);
                    return isRequest;
                }

                @Override
                public Object getResponse(IoSession session, Object request) {
                    logger.info("3");
                    ActiveRespMessage arm = new ActiveRespMessage();
                    return arm;
                }

                @Override
                public Object getRequest(IoSession session) {
                    //
                    // ActiveMessage am = new ActiveMessage();
                    return null;
                }
            }, IdleStatus.BOTH_IDLE, new KeepAliveRequestTimeoutHandlerImpl());

            keepAlivceFilter.setRequestInterval(5);
            keepAlivceFilter.setRequestTimeout(15);

            // acceptor.getFilterChain().addLast("keepAlivce", keepAlivceFilter);

            acceptor.setHandler(new ServerHandler(new ServerSessionManager()));

            acceptor.bind(new InetSocketAddress(port));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {

        @Override
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            logger.error("Heart Beat timeout!");
            // session.close(true);
        }

    }

}
