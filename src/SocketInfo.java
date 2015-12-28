import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * socket 基本信息
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-10-27 下午04:21:58 $
 */
public class SocketInfo {
    private List<Socket> socketArray = new ArrayList<Socket>();
    private Socket nowSocket;

    public List<Socket> getSocketArray() {
        return socketArray;
    }

    public void setSocketArray(List<Socket> socketArray) {
        this.socketArray = socketArray;
    }

    public synchronized void sendMsg(String msg) {
        for (int i = 0; i < socketArray.size(); i++) {
            Socket client = socketArray.get(i);
            System.out.println("client:" + "[" + i + "]" + Thread.currentThread().getName() + "----" + client);
            try {
                if (client != nowSocket) {
                    PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
                    System.out.println("nowSocket:" + Thread.currentThread().getName() + "----" + nowSocket);
                    System.out.println("clientSocket:" + "[" + i + "]" + Thread.currentThread().getName() + "----"
                            + client);
                    pw.println("服务器回复所有客户端：" + Thread.currentThread().getName() + ":" + msg);
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Socket getNowSocket() {
        return nowSocket;
    }

    public void setNowSocket(Socket nowSocket) {
        this.nowSocket = nowSocket;
    }
}
