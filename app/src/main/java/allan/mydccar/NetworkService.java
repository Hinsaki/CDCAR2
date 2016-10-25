package allan.mydccar;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkService implements Serializable {
    private static final String TAG = "NetworkService";
    private static final long serialVersionUID = -7060210544600464481L;
    InetAddress serverIp;
    int mServerPort = 0;

    public NetworkService(Context context)
    {

    }

    public int setServer(String strIp, int serverPort) {
        Socket socket;
        try {
            Log.i(TAG, "serverIp:" + strIp + ", port:" + serverPort);

            mServerPort = serverPort;
            serverIp = InetAddress.getByName(strIp);
            socket = new Socket(serverIp, mServerPort);
            if (socket == null)
                return -1;

        } catch (IOException e) {
            Log.i(TAG, "Setting Error!");
            return -1;
        }

        // outgoing stream redirect to socket
        Log.i(TAG, "Setting success!");
        return 1;
    }

    public void dataSend(String msg) {
        SocketAddress sc_addr;
        Socket socket;
        try {
            //設定Server IP位置
            sc_addr = new InetSocketAddress(serverIp, mServerPort);
            socket = new Socket();
            //與Server連線，timeout時間2秒
            socket.connect(sc_addr, 2000);
            //傳送資料
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            byte[] sendmsg = msg.getBytes();
            out.write(sendmsg);
            out.write('\n');
            socket.close();

        } catch (UnknownHostException e) {
            Log.i(TAG, "InetAddress物件建立失敗");
        } catch (SocketException e) {
            Log.i(TAG, "socket建立失敗");
        } catch (IOException e) {
            Log.i(TAG, "傳送失敗");
        }
    }

}
