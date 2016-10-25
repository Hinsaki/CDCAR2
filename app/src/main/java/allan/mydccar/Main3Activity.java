package allan.mydccar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;



public class Main3Activity extends Activity {

    ImageButton button_left, button_right, button_forward, button_back;

    private static final String TAG = "buttoncontrol";
    private static final boolean D = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        if (D) Log.e(TAG, "+++ ON CREATE +++");

        // my button control
        button_forward = (ImageButton) findViewById(R.id.forward);
        button_back = (ImageButton) findViewById(R.id.backward);
        button_left = (ImageButton) findViewById(R.id.left);
        button_right = (ImageButton) findViewById(R.id.right);

        button_forward.setOnTouchListener(new OnTouchListener() {
            //            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.i(TAG, "FORWARD BUTTON UP =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("s");
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.i(TAG, "FORWARD BUTTON DOWN =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("f");
                }
                return true;

            }
        });

        button_back.setOnTouchListener(new OnTouchListener() {
            //            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.i(TAG, "BACK BUTTON UP =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("s");
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i(TAG, "BACK BUTTON DOWN =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("b");
                }
                return true;

            }
        });

        button_left.setOnTouchListener(new OnTouchListener() {
            //            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.i(TAG, "LEFT BUTTON UP =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("s");
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i(TAG, "LEFT BUTTON DOWN =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("l");
                }
                return true;
            }
        });

        button_right.setOnTouchListener(new OnTouchListener() {
            //            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Log.i(TAG, "RIGHT BUTTON UP =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("s");
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i(TAG, "RIGHT BUTTON DOWN =" + event.getAction());
                    rpiwifirobot.mNetworkService.dataSend("r");
                }
                return true;
            }
        });


    }

    //    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.i(TAG, "++ ON START ++");
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
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.i(TAG, "+ ON RESUME +");

//        String strIp = rpiwifirobot.mNetworkService.serverIp.toString();
//        int serverPort =  Integer.valueOf(rpiwifirobot.mNetworkService.mServerPort);
//        if(D) Log.d(TAG, "strIp = "+strIp);
//        if(D) Log.d(TAG, "serverPort = " + serverPort);
        //       int ret = rpiwifirobot.mNetworkService.setServer(strIp, serverPort);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // Sensor Control
                Intent myIntent = new Intent(Main3Activity.this, sensorcontrol.class);
                this.startActivity(myIntent);
                if (D) Log.d(TAG, "SensorControl Exit!");
                return true;

            case 1:  // Exit
                finish();
                break;

            case 2:  // About
                Toast.makeText(this, "Author: Allan", Toast.LENGTH_SHORT).show();
                break;
        }

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
