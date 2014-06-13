/******************************************************************************
 * Detect touches to the screen.  This is used mostly for the motion of the
 * paddle.
 ******************************************************************************/

package com.example.paddleball;

import android.os.Vibrator;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

public class ActivitySwipeDetector implements View.OnTouchListener
	{

	static final String logTag = "ActivitySwipeDetector";
	private Main activity;
	static final int MIN_DISTANCE = 5;
	private float downX, downY, upX, upY;
	private int paddle1X = 0;
	private int paddle2X = 0;

	public ActivitySwipeDetector(Main activity)
		{
		this.activity = activity;
		}

	public void onRightToLeftSwipe()
		{
		// ?? Log.i(logTag, "RightToLeftSwipe!");
		// ??? activity.doSomething(finalX);
		// ??? activity.startball();
		}

	public void onLeftToRightSwipe()
		{
		// ??? Log.i(logTag, "LeftToRightSwipe!");
		// ??? activity.doSomething(finalX);
		// ??? activity.startball();
		}

	public void onTopToBottomSwipe()
		{
		// ??? Log.i(logTag, "onTopToBottomSwipe!");
		// ??? activity.doSomething(finalX);
		// ??? activity.startball();
		}

	public void onBottomToTopSwipe()
		{
		// ??? Log.i(logTag, "onBottomToTopSwipe!");
		// ??? activity.doSomething(finalX);
		// ??? activity.startball();
		}

	/******************************************************************************
	 * For any touch, set the current position of the paddle as the X position of
	 * the end of the touch. The Y position of the paddle is fixed.
	 ******************************************************************************/
	public boolean onTouch(View v, MotionEvent event)
		{
		// ??? dumpEvent(event);
		InputDevice dev = event.getDevice();
		Vibrator vibe = dev.getVibrator();
		if (vibe != null)
       vibe.vibrate(100);
		long etime = event.getEventTime();
		float press = event.getPressure();

		int courtTop = activity.getCourtTop();
		int courtHeight = activity.getCourtHeight();

		// Start of multi-touch code.
		int ptrCount = event.getPointerCount();
		int evtX, evtY;
		for (int ix = 0; ix < ptrCount; ix++)
			{
			evtX = (int) event.getX(ix);
			evtY = (int) event.getY(ix);
			if (evtY > courtTop)
				{
				if (evtY > courtTop + courtHeight / 2)
					{
					paddle1X = evtX;
					activity.movePaddle1(paddle1X);
					}
				else
					{
					paddle2X = evtX;
					activity.movePaddle2(paddle2X);
					}
				}
			// End of multi-touch code.
			}
		switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			return true;
			}
			case MotionEvent.ACTION_UP: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// swipe horizontal?
			if (Math.abs(deltaX) > MIN_DISTANCE)
				{
				// left or right
				if (deltaX < 0)
					{
					this.onLeftToRightSwipe();
					return true;
					}
				if (deltaX > 0)
					{
					this.onRightToLeftSwipe();
					return true;
					}
				}
			else
				{
				// ??? Log.i(logTag, "Swipe was only " + Math.abs(deltaX) +
				// " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
				}

			// swipe vertical?
			if (Math.abs(deltaY) > MIN_DISTANCE)
				{
				// top or down
				if (deltaY < 0)
					{
					this.onTopToBottomSwipe();
					return true;
					}
				if (deltaY > 0)
					{
					this.onBottomToTopSwipe();
					return true;
					}
				}
			else
				{
				// ??? Log.i(logTag, "Swipe was only " + Math.abs(deltaX) +
				// " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
				}

			return true;
			}
			}
		return true;
		}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event)
		{
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
		    "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
		    || actionCode == MotionEvent.ACTION_POINTER_UP)
			{
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
			}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++)
			{
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
			}
		sb.append("]");
		Log.d("Touch: ", sb.toString());
		}

	}