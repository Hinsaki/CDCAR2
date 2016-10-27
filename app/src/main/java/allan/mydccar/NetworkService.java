package allan.mydccar;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.json.JSONObject;
public class NetworkService {
    InetAddress serverIp = InetAddress.getByName("10.2.1.151");
    private static Thread th_close;                //執行緒
    private static int serverport = 8080;
    private static ServerSocket serverSocket;    //伺服端的Socket
    private static ArrayList<Socket> socketlist=new ArrayList<Socket>();

    public NetworkService() throws UnknownHostException {
    }

    // 程式進入點
    public static void main(String[] args){
        try {
            serverSocket = new ServerSocket(serverport);    //啟動Server開啟Port接口
            System.out.println("Server開始執行");
            th_close=new Thread(Judge_Close);                //賦予執行緒工作(判斷socketlist內有沒有客戶端網路斷線)
            th_close.start();                                //讓執行緒開始執行
            // 當Server運作中時
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static Runnable Judge_Close=new Runnable(){    //讓執行緒每兩秒判斷一次SocketList內是否有客戶端強制斷線
        @Override
        public void run() {                                //在此抓取的是關閉wifi等斷線動作
            // TODO Auto-generated method stub
            try {
                while(true){
                    Thread.sleep(2000);                    //每兩秒執行一輪
                    for(Socket close:socketlist){
                        if(isServerClose(close))        //當該客戶端網路斷線時,從SocketList剔除
                            socketlist.remove(close);
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    private static Boolean isServerClose(Socket socket){    //判斷連線是否中斷
        try{
            socket.sendUrgentData(0);        //發送一個字節的緊急數據,默認情況下是沒有開啟緊急數據處理,不影響正常連線
            return false;                    //如正常則回傳false
        }catch(Exception e){
            return true;                      //如連線中斷則回傳true
        }
    }
    // 等待接受客戶端連接
    public static void waitNewSocket() {
        try {
            Socket socket = serverSocket.accept();
            // 呼叫創造新的使用者
            createNewThread(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 創造新的使用者
    public static void createNewThread(final Socket socket) {
        // 以新的執行緒來執行
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 增加新的使用者
                    socketlist.add(socket);
                    //取得網路輸出串流
                    BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                    // 取得網路輸入串流
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String tmp;
                    JSONObject json_read,json_write;
                    // 當Socket已連接時連續執行
                    while (socket.isConnected()) {
                        tmp = br.readLine();        //宣告一個緩衝,從br串流讀取值
                        // 如果不是空訊息
                        if(tmp!=null){
                            //將取到的String抓取{}範圍資料
                            tmp=tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                            json_read=new JSONObject(tmp);
                            //從客戶端取得值後做拆解,可使用switch做不同動作的處理與回應
                        }else{    //在此抓取的是使用使用強制關閉app的客戶端(會不斷傳null給server)
                            //當socket強制關閉app時移除客戶端
                            socketlist.remove(socket);
                            break;    //跳出迴圈結束該執行緒
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 啟動執行緒
        t.start();
    }
    public void dataSend(String msg) {
        SocketAddress sc_addr;
        Socket socket;
        try {
            //設定Server IP位置
            sc_addr = new InetSocketAddress(serverIp, serverport);
            socket = new Socket();
            //與Server連線，timeout時間2秒
            socket.connect(sc_addr, 2000);
            //傳送資料
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            byte[] sendmsg = msg.getBytes();
            out.write(sendmsg);
            out.write('\n');
            socket.close();

        } catch (Exception e){

        }
    }
}
