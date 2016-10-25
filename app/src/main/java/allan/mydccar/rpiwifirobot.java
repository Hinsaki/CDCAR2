package allan.mydccar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class rpiwifirobot extends Activity {
    /**
     * Called when the activity is first created.
     */
    Button btn_Connect;
    EditText edit_Server_IP, edit_Server_Port;
    TextView view_Message;
    //    InetAddress serverIp;
    int serverPort;
    public static NetworkService mNetworkService;
    public final static String SER_KEY = "allan.mydccar.NetService";
    private static final String TAG = "rpiwifirobot";
    private static final boolean D = true;
    private static Intent myIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mNetworkService = new NetworkService(this);
//        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);        

        edit_Server_IP = (EditText) findViewById(R.id.EditIPAddress);
        edit_Server_Port = (EditText) findViewById(R.id.EditPort);
        view_Message = (TextView) findViewById(R.id.ViewMessage);

        // my button control
        btn_Connect = (Button) findViewById(R.id.btnConnect);

        btn_Connect.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "Connect Server");

                // Set Server IP
                String strIp = edit_Server_IP.getText().toString();

                if (strIp.length() == 0) {
                    strIp = edit_Server_IP.getHint().toString();
                }

                // Set Server Port
                String strPort = edit_Server_Port.getText().toString();
                if (strPort.length() == 0) {// edit_Server_Port.getText().toString().equals("")
                    strPort = edit_Server_Port.getHint().toString();
                }
                serverPort = Integer.valueOf(strPort);

                Log.i(TAG, "serverIp:" + strIp + ", port:" + serverPort);


                int ret = mNetworkService.setServer(strIp, serverPort);
                if (ret == -1) {
                    if (D) Log.d(TAG, "connectServer error!");
                    Toast.makeText(rpiwifirobot.this, "Connect Error!", Toast.LENGTH_SHORT).show();

                    return;
                }

                myIntent = new Intent(rpiwifirobot.this, buttoncontrol.class);
                if (D) Log.d(TAG, "myIntent ok!");

                startActivity(myIntent);
                if (D) Log.d(TAG, "Go to startActivity!");


            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (D) Log.i(TAG, "+ ON DESTROY +");
//		if(rpiwifirobot.mNetworkService != null)
//			rpiwifirobot.mNetworkService.socketClose();

        finish();
    }
    public void back(View v) {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

}

