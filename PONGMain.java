/******************************************************************************
 * This is a Pong game, called Paddle Ball, that lets you play with a tennis
 * ball in a court that is the screen width wide and high.
 ******************************************************************************/

package com.example.paddleball;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class Main extends Activity
{
	AnimatedView lowestLayout;

	/****************************************************************************
	 * When the activity is created, load the main layout as the view.  Since
	 * there is a class associated with this view, that class, which is our
	 * AnimatedView class, will be created.  Thus we do not call the constructor
	 * here.
	 ****************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
		lowestLayout = (AnimatedView)this.findViewById(R.id.anim_view);
		lowestLayout.setOnTouchListener(activitySwipeDetector);
		SeekBar sb = (SeekBar)this.findViewById(R.id.speedBar);
		sb.setOnSeekBarChangeListener(new SpeedChange(this));
		sb.setProgress(20);
		CheckBox cb = (CheckBox)this.findViewById(R.id.twoPlayer);
//???		cb.setBackgroundColor(Color.WHITE);
	}

	/****************************************************************************
	 * Since we don't actually have the view above, create the "swipe detector"
	 * here and associate it with the view.  That lets us control the paddle.
	 ****************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/****************************************************************************
	 * Called with the new X position of the paddle.  The swipe detector calls
	 * us, and we, in turn, call the view's method.
	 ****************************************************************************/
	public void movePaddle1(int x)
	{
	AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
	v.setPaddle1(x);
	}

	public void movePaddle2(int x)
	{
	AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
	v.setPaddle2(x);
	}
	
	public void setSpeed(int speed)
	{
		AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
		v.setSpeed(speed);
	}
	
	public void onStart(View view)
	{
		CheckBox cb = (CheckBox)this.findViewById(R.id.twoPlayer);
		startball(cb.isChecked());
	}
	
	public int getCourtTop()
	{
		AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
		return v.getCourtTop();
	}
	
	public int getCourtHeight()
	{
		AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
		return v.getCourtHeight();
	}
	
	public boolean getTwoPlayerValue()
	{
		CheckBox cb = (CheckBox)this.findViewById(R.id.twoPlayer);
		AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
		v.setTwoPlayer(cb.isChecked());
		return cb.isChecked();
	}
	
	public void onTwoPlayerClicked(View view)
	{
		CheckBox cb = (CheckBox)this.findViewById(R.id.twoPlayer);
		AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
		v.setTwoPlayer(cb.isChecked());
	}
	
	/****************************************************************************
	 * A button starts the ball rolling, er, bouncing.
	 ****************************************************************************/
	public void startball(boolean bTwoPlayer)
	{
		AnimatedView v = (AnimatedView)this.findViewById(R.id.anim_view);
		v.setMotion(bTwoPlayer);
	}

}
