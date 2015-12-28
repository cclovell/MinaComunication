import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * server线程
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-10-26 上午11:37:25 $
 */
public class ServerThread implements Runnable {
    ServerSocket serverSocket = null;
    Socket socket = null;
    BufferedReader br;
    List<Socket> socketArray;
    SocketInfo socketInfo;

    public ServerThread(SocketInfo socketInfo, Socket socket) {
        super();
        this.socketInfo = socketInfo;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "连接上来了");
        String msg = "";
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
            while (true) {

                System.out.println("开始收取消息");
                msg = br.readLine();// 方法是阻塞的（如果有数据就会读取否则一直等待）
                System.out.println("收到" + Thread.currentThread().getName() + ":" + msg);

                socketInfo.setNowSocket(socket);
                socketInfo.sendMsg(msg);
                if (msg.equals("bye")) {
                    System.out.println("服务器关闭！");
                    br.close();
                    socket.close();
                    break;
                }

            }
        }
        catch (IOException e) {
            try {
                br.close();
                socket.close();
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public synchronized void sendMsg(String msg) {

    }
}
