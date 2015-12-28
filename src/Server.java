import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * 
 * client端
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-10-23 下午04:33:23 $
 */

public class Server {
    private static int port = 8808;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            SocketInfo socketInfo = new SocketInfo();
            List<Socket> socketArray = socketInfo.getSocketArray();
            while (true) {
                System.out.println("等待客户端连接！！");
                Socket clientSocket = serverSocket.accept();// 方法是阻塞的（如果有数据就会读取否则一直等待）
                
                socketArray.add(clientSocket);
                Thread thread = new Thread(new ServerThread(socketInfo, clientSocket));
                thread.start();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("close the Server socket and the io.");

            try {
                serverSocket.close();
            }
            catch (IOException e) {
                System.out.println("服务器异常！！");
                e.printStackTrace();
            }

        }
    }

}
