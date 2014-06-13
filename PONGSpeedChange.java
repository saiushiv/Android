package com.example.paddleball;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SpeedChange implements OnSeekBarChangeListener
{
	private Main activity;
	
	public SpeedChange(Main a)
	{
		activity = a;
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
	{
		if (arg1 < 10)
			arg1 = 10;
		activity.setSpeed(arg0.getMax()- arg1);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0)
	{
		// TODO Auto-generated method stub

	}

}
