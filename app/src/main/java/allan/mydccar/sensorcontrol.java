package allan.mydccar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class sensorcontrol extends Activity implements SensorEventListener
{
	
	 private SensorManager sensorMgr;
	 private float x, y, z;
	 public String btAddress;
	 private TextView xyz_value;
	 private int nStatus;
		private static final String TAG = "sensorcontrol";
	    private static final boolean D = true;		
	 
 	 private static final int SENSOR_STOP 		= 0;
	 private static final int SENSOR_FORWARD 	= 1;
	 private static final int SENSOR_BACKWARD 	= 2;
	 private static final int SENSOR_LEFT 		= 3;
	 private static final int SENSOR_RIGHT 		= 4;
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState)
	 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.sensorcontrol);
		
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       	xyz_value = (TextView)findViewById(R.id.sensor_xyz);

	    sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
	    
	    nStatus = SENSOR_STOP;
	   	boolean accelSupported = sensorMgr.registerListener(
	   			this,
	   			sensorMgr.getDefaultSensor(SensorManager.SENSOR_ORIENTATION),
	   			SensorManager.SENSOR_DELAY_NORMAL);
     	if (!accelSupported) {
    	    // non accelerometer on this device
    	    sensorMgr.unregisterListener(this); 
     	}
  	
	 }

	 public void onAccuracyChanged(Sensor arg0, int arg1)
	 {
	 	// TODO Auto-generated method stub
	 }	 

	 public void onSensorChanged(SensorEvent event) {
	      	Sensor mySensor = event.sensor;
	      	if (mySensor.getType() == SensorManager.SENSOR_ORIENTATION) {
	  	    		
	  	    		x = event.values[SensorManager.DATA_X];
	  	    		y = event.values[SensorManager.DATA_Y];
	  	    		z = event.values[SensorManager.DATA_Z];
	  	    		
	  	    		xyz_value.setText("x: " + Math.round(x) + ", y:" + Math.round(y) + ", z:" + Math.round(z));
	  	    		if(x <= -3)
	  	    		{// Forward
	  	    			if(nStatus != SENSOR_FORWARD)
	  	    			{
	  	    				nStatus = SENSOR_FORWARD;
	  	    				rpiwifirobot.mNetworkService.dataSend("forward");
		  	    			Log.i("sensorcontrol", " Forward!");
	  	    			}
	  	    		}else if(x >= 3)
	  	    		{  // Backward
	  	    			if(nStatus != SENSOR_BACKWARD)
	  	    			{
	  	    				nStatus = SENSOR_BACKWARD;
	  	    				rpiwifirobot.mNetworkService.dataSend("back");
		  	    			Log.i("sensorcontrol", " Backward!");
	  	    			}
	  	    		}else if(y <= -3)
	  	    		{   // Left
	  	    			if(nStatus != SENSOR_LEFT)
	  	    			{
	  	    				nStatus = SENSOR_LEFT;
//	  	            		sendMessage(buttoncontrol.mCurrentBTService, "l"+1);
	  	    				rpiwifirobot.mNetworkService.dataSend("left");
		  	    			Log.i("sensorcontrol", " Left!");
	  	    			}
	  	    		}else if(y >= 3)
	  	    		{   // Right
	  	    			if(nStatus != SENSOR_RIGHT)
	  	    			{
	  	    				nStatus = SENSOR_RIGHT;
//	  	            		sendMessage(buttoncontrol.mCurrentBTService, "r"+1);
	  	    				rpiwifirobot.mNetworkService.dataSend("right");
		  	    			Log.i("sensorcontrol", " Right!");
	  	    			}
	  	    		}
	  	    		else
	  	    		{
	  	    			if(nStatus != SENSOR_STOP)
	  	    			{
	  	    				nStatus = SENSOR_STOP;
//	  	            		sendMessage(buttoncontrol.mCurrentBTService, "s"+1);
	  	    				rpiwifirobot.mNetworkService.dataSend("stop");
		  	    			Log.i("sensorcontrol", " Stop!");
	  	    			}
	  	    		}
	      	    }

	 }	
	
	 @Override
	public void onResume() {
			super.onResume();
			// register this class as a listener for the orientation and
			// accelerometer sensors
			sensorMgr.registerListener(this,
					sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),
					sensorMgr.SENSOR_DELAY_NORMAL);
		}

		@Override
		public void onPause() {
			super.onPause();
			// unregister listener
			if(D) Log.d(TAG, "+ ON PAUSE +");
			sensorMgr.unregisterListener(this);
		}	

		@Override
		protected void onStop() {
			super.onStop();
			// unregister listener
			if(D) Log.d(TAG, "+ ON STOP +");
			sensorMgr.unregisterListener(this);
		}

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	         menu.add(0, 0, 0, "Button");
	         menu.add(0, 1, 1, "Sensor");
	         return super.onCreateOptionsMenu(menu);
	    }
		
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch(item.getItemId()) {
	    	case 0:
	           // Button Control
	    			finish();
	                break;
	        case 1:
	          // Sensor Control
       		
	        		break;
	        default:
	            }
	      return super.onOptionsItemSelected(item);
	     }
	
}