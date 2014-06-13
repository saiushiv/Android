/******************************************************************************
 * List all of the sensors on this device.
 * 
 * Written by John Cole and modified on January 9, 2014.
 ******************************************************************************/

package com.example.DeviceList;

import java.util.Iterator;
import java.util.List;

import com.example.DeviceList.R;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceList extends Activity
{
  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  private TextView tv;
  private Paint pBlue;
  float x, y, z;
  View mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acceler8);
    tv = new TextView(this);
    mainView = new View(this);
    tv.setTextSize(25);
//???    tv.setText("Sensors on this Device");
//???    setContentView(tv);
    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    
    // List all of the available sensors.  This list will go away as soon as
    // we create the new view, so comment that out if you want to see the list.
    List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
    Iterator<Sensor> i = sensorList.iterator();
    String sense = "Sensors on this Device:\n";
    while (i.hasNext())
    {
    	Sensor s = i.next();
    	sense += s.getName() + "\n";
    }
    tv.setText(sense);
    setContentView(tv);
	}
	
	@Override
  protected void onResume()
  {
		super.onResume();
//???    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.acceler8, menu);
		return true;
	}


}
