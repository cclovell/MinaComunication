import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * client端
 * 
 * @author chenchao
 * @version $Revision: 1.0 $, $Date: 2015-10-23 下午04:33:23 $
 */

public class Client {
    private static int port = 8808;

    public static void main(String args[]) {
        try {
            Socket socket = new Socket("127.0.0.1", port);

            final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                    "GBK")), true);
            // final PrintWriter pw = new PrintWriter(socket.getOutputStream());

            Thread sendThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // while (true) {
                    try {
                        pw.println("哈喽啊kit");
                        pw.flush();// 刷新缓存
                        // pw.println("用户名：root；密码：123456");
                        // socketOut.write(sysInbr.readLine().getBytes());
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // }

                }
            });

            sendThread.start();

            Thread receiveThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            String str = null;
                            str = br.readLine();
                            System.out.println(str);
                            if (str.equals("duankai")) {
                                break;
                            }
                        }
                        catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }
            });

            receiveThread.start();

            // pw.println("byebye");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
