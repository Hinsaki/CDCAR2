package allan.mydccar;


import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;


public class buttoncontrol extends Activity {

    private ConnectivityManager cmgr ;
    private TextView mesg_one;
    private TextView mesg_two;
    private TextView mesg_three;
    private TextView mesg_four;
    private String data ;
    private UIHandler handler ;
    private StringBuffer sb_one ;
    private StringBuffer sb_two ;
    private StringBuffer sb_three ;
    private StringBuffer sb_four ;
    private ImageView img;
    private Timer mTimer;
    private MyTread mt1;


    private static final String TAG = "buttoncontrol";
    private static final boolean D = true;
    private WebView m_WV;
    private WebView m_WV1;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttoncontrol);
        mesg_one = (TextView) findViewById(R.id.json_hh);
        mesg_two = (TextView) findViewById(R.id.json_co2);
        mesg_three = (TextView) findViewById(R.id.json_e);
        mesg_four = (TextView) findViewById(R.id.json_tm);
        cmgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cmgr.getActiveNetworkInfo();
        handler = new UIHandler();
        img = (ImageView) findViewById(R.id.img_bad);
        mTimer = new Timer();
        setTimerTask();
        m_WV = (WebView) findViewById(R.id.webview);
        m_WV1 = (WebView) findViewById(R.id.webview1);
        WebSettings webSettings = m_WV.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebSettings webSettings1 = m_WV1.getSettings();
        webSettings1.setJavaScriptEnabled(true);

    }

    //    @Override
    public void onStart() {
        super.onStart();
        String strRbtUrl = "http://10.2.1.151:8080/?action=stream";
        String strRbtUrl1 = "https://mcs.mediatek.com/v2console/zh-TW/testdevices/DzDkkseX";
        m_WV.loadUrl(strRbtUrl);
        m_WV.setWebViewClient(new WebViewClientImpl());
        m_WV1.loadUrl(strRbtUrl1);
        m_WV1.setWebViewClient(new WebViewClientImpl());


    }

    private final class WebViewClientImpl extends WebViewClient {
        public void WebViewClientImpl() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.compareTo("about:blank") == 0) {
                finish();
            } else if (url.endsWith(".mp4")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                finish();
            }
            return false;
        }
    }

    //	@Override
    protected void onStop() {
        super.onStop();

        if (D) Log.i(TAG, "+ ON STOP +");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (D) Log.i(TAG, "+ ON DESTROY +");
//		if(rpiwifirobot.mNetworkService != null)
//			rpiwifirobot.mNetworkService.socketClose();
        // cancel timer
        mTimer.cancel();
    }
    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mt1 = new MyTread();
                mt1.start();
                Log.d("brad","Run as....");
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 0, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }
    private class MyTread extends Thread {

        @Override
        public void run() {
            try {
                URL url = new URL("https://api.thingspeak.com/channels/173441/feed/last.json?api_key=BTNMKI5MV1A8CJMM");
                HttpsURLConnection conn =  (HttpsURLConnection) url.openConnection();
                conn.connect();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                data = reader.readLine();
                reader.close();
                parseJSONA();
            }catch(Exception ee){
                Log.d("brad", "error");
            }
        }
    }
    private void parseJSONA(){
        sb_one = new StringBuffer();
        sb_two = new StringBuffer();
        sb_three = new StringBuffer();
        sb_four = new StringBuffer();

        try {
            String name = new JSONObject(data).getString("created_at");
            String field1 = new JSONObject(data).getString("field1");
            String field2 = new JSONObject(data).getString("field2");
            String field3 = new JSONObject(data).getString("field3");
            String field4 = new JSONObject(data).getString("field4");

            Log.d("brad", name + " -> " + field1+ " -> "+field2+ " -> "+field3+ " -> "+field4);
            sb_one.append(field1);
            sb_two.append(field2);
            sb_three.append(field3);
            sb_four.append(field4);
            handler.sendEmptyMessage(1);

        }catch(Exception ee){
            Log.d("brad", ee.toString());
        }


    }
    private  void changeState(){
        float dd = (float) 30.0;
        float ff = (float) 60.0;
        float gg = (float) 400.0;
        float hh = (float) 1000.0;
        float d = Float.valueOf(String.valueOf(sb_one)).floatValue();
        float f = Float.valueOf(String.valueOf(sb_two)).floatValue();
        float g = Float.valueOf(String.valueOf(sb_three)).floatValue();
        float h = Float.valueOf(String.valueOf(sb_four)).floatValue();



        Log.d("brad","float:"+d);
        if(d>dd){
            Log.d("brad","Warring");
            img.setVisibility(View.VISIBLE);
        }
        if(f>ff){
            Log.d("brad","Warring");
            img.setVisibility(View.VISIBLE);
        }
        if(g>gg){
            Log.d("brad","Warring");
            img.setVisibility(View.VISIBLE);
        }
        if(h>hh){
            Log.d("brad","Warring");
            img.setVisibility(View.VISIBLE);
        }

    }
    private class UIHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mesg_one.setText(sb_one);//溫度
            mesg_two.setText(sb_two);//濕度
            mesg_three.setText(sb_three);//PM2.5
            mesg_four.setText(sb_four);//瓦斯濃度
            if (sb_one != null) {
                changeState();
            }
        }
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.i(TAG, "+ ON RESUME +");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.i(TAG, "- ON PAUSE -");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Sensor");
        menu.add(0, 1, 0, "Exit");
        menu.add(0, 2, 0, "About");
        return true;
    }
    public void back(View v) {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

}
