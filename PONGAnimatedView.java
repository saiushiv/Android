/******************************************************************************
 * The AnimatedView class is where the action is.  I have overridden the
 * onDraw method to do the real work  
 * 
 * Written by John Cole at The University of Texas at Dallas.
 ******************************************************************************/
package com.example.paddleball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AnimatedView extends ImageView
{
	private Context mContext;
	int ballX = -1;
	int ballY = -1;
	int ballCenter = -1;
	private int xVelocity = 12;
	private int yVelocity = -6;
	private Handler h;
	private int frameRate = 20;
	private int paddle1X = 0;
	private int paddle1Y = 0;
	private int paddle2X = 0;
	private int paddle2Y = 0;
	private int paddleWidth = 0;
	private int paddleHeight = 20;
	private int courtTop;
	private Rect paddle1;
	private Rect paddle2;
	private Paint paddleColor;
	private Paint textColor;
	private boolean bInMotion = false;
	private int score1 = 0;
	private int score2 = 0;
	private int prevScore = 0;
	boolean bHit = false;
	boolean bTwoPlayer = false;

	// Variables for sound.
	private final float duration = 0.3f; // seconds
	private final int sampleRate = 8000;
	private final int numSamples = (int) (duration * sampleRate);
	private final double sample[] = new double[numSamples];
	private final double freqOfTone = 220; // hz
	private final byte generatedSnd[] = new byte[2 * numSamples];

	/****************************************************************************
	 * This constructor takes a context and a set of attributes and calls the
	 * ImageView's constructor. Then do our initialization.
	 ****************************************************************************/
	public AnimatedView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		h = new Handler();
		paddleColor = new Paint();
		paddleColor.setColor(Color.GREEN);
		paddle1 = new Rect();
		paddle2 = new Rect();
		textColor = new Paint();
		textColor.setColor(Color.WHITE);
	}

	/****************************************************************************
	 * Set the paddle's X position.
	 ****************************************************************************/
	public void setPaddle1(int px)
	{
		paddle1X = px;
	}

	/****************************************************************************
	 * Set the paddle's X position.
	 ****************************************************************************/
	public void setPaddle2(int px)
	{
		paddle2X = px;
	}

	public void setSpeed(int speed)
	{
		frameRate = speed;
	}

	public int getCourtTop()
	{
		return courtTop;
	}

	public int getCourtHeight()
	{
		return this.getWidth();
	}

	/****************************************************************************
	 * If the ball isn't moving, start it and set the score to zero.
	 ****************************************************************************/
	public void setMotion(boolean pTwoPlayer)
	{
		if (!bInMotion)
		{
			bTwoPlayer = pTwoPlayer;
			bInMotion = true;
			score1 = 0;
			score2 = 0;
		}
		else
		{
			bInMotion = false;
		}
	}
	
	public void setTwoPlayer(boolean pTwoPlayer)
	{
		bTwoPlayer = pTwoPlayer;
		invalidate();
	}

	/****************************************************************************
	 * Show the score above the line.
	 ****************************************************************************/
	private void showScore(Canvas c)
	{
		textColor.setTextSize(30);
		String str = "Professor Cole's Pong.";
		c.drawText(str, 0, 30, textColor);
		str = "Player 1 Score: " + score1;
		float w = textColor.measureText(str);
		c.drawText(str, c.getWidth() - (int) w, 30, textColor);
		if (bTwoPlayer)
			{
			str = "Player 2 Score: " + score2;
			w = textColor.measureText(str);
			c.drawText(str, c.getWidth() - (int) w, 65, textColor);
			}
	}

	/****************************************************************************
	 * Create a new thread that will let us time the rate of the ball.
	 ****************************************************************************/
	private Runnable r = new Runnable() {
		@Override
		public void run()
		{
			invalidate();
		}
	};

	/****************************************************************************
	 * Draw everything. It may seem wasteful to re-draw the entire screen with
	 * every frame refresh, but it works. Get the ball from our resources, set up
	 * the paddle, and draw green lines around the court.
	 ****************************************************************************/
	protected void onDraw(Canvas c)
	{
		BitmapDrawable ball = (BitmapDrawable) mContext.getResources().getDrawable(
		    R.drawable.ic_launcher);
		int ballHeight = ball.getBitmap().getHeight();
		int ballWidth = ball.getBitmap().getWidth();
		ballCenter = ballX - (ballWidth / 2);
		paddleWidth = this.getWidth() / 5;
		courtTop = this.getHeight() - this.getWidth();
		paddle1Y = this.getHeight() - 100 - paddleHeight;
		paddle2Y = courtTop + 100 - paddleHeight;
		int xOffset = 0;
		bHit = false;
		// Draw green lines around the court.
		c.drawLine(0, courtTop, this.getWidth(), courtTop, paddleColor);
		c.drawLine(0, courtTop, 0, this.getHeight(), paddleColor);
		c.drawLine(this.getWidth() - 1, courtTop, this.getWidth() - 1,
		    this.getHeight(), paddleColor);
		c.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1,
		    paddleColor);

		// ??? c.drawRect(0, courtTop, this.getWidth(), this.getHeight(),
		// paddleColor);
		paddleColor.setTextSize(30);
		// ??? c.drawText("x: " + x + "  y=" + y + "  ballwidth: " + ballWidth +
		// "  PaddleX: " + paddleX, 20, 20, paint);;

		// If the Y position of the ball is past the paddle and the x position is
		// not within the range of the paddle, the ball has been dropped.
		if (bInMotion  && ballY > paddle1Y - 30)
			System.out.println("BallY: " + (ballY - ballHeight / 2 + Math.abs(yVelocity)) + "  BallX: " + ballX + " Paddle1X: " + paddle1X + "  PaddleWidth: " + paddleWidth + "  Ballwidth " + ballWidth);
		if ((ballY + ballHeight / 2 + Math.abs(yVelocity) > paddle1Y)
		    && (ballX + ballWidth < paddle1X || ballX /* + ballWidth / 2 */> paddle1X + paddleWidth))
		{
			bInMotion = false;
			yVelocity = Math.abs(yVelocity) * -1;
			xVelocity = Math.abs(xVelocity);
			prevScore = score1;
		}

		// If the Y position of the ball is within 10 pixels of the bottom paddle
		// and the X position of the ball is within the range of the paddle,
		// reverse the Y direction of the ball.
//???		if (bInMotion)
//???			System.out.println("ballY=" + ballY + "    paddle1X, Y=" + paddle1X + "," + paddle1Y +
//???			    "   ballCenter=" + ballCenter);
	  if (bInMotion && ballY > paddle1Y - ballHeight && ballY < paddle1Y + paddleHeight)
		{
			// ??? c.drawText("YHit", 300, 20, paddleColor);
			if (ballCenter >= paddle1X && ballCenter <= paddle1X + paddleWidth)
			{
				// ??? c.drawText("Hit", 300, 20, paddleColor);
				yVelocity = yVelocity * -1;
				score1++;
				bHit = true;
				if (ballX < paddle1X / 3)
					xOffset = -1;
				else if (ballX > paddle1X / 3 * 2)
					xOffset = 1;
			}
		}

		// Two-player, determine if we hit the top paddle.
		if (bTwoPlayer)
		{
//???			System.out.println("Ball X=" + ballX + "  Ball Y=" + ballY);
			if (bInMotion && ballY < paddle2Y + paddleHeight && ballY > paddle2Y - paddleHeight)
			{
				// ??? c.drawText("YHit", 300, 20, paddleColor);
				if (ballX >= paddle2X && ballX <= paddle2X + paddleWidth)
				{
					// ??? c.drawText("Hit", 300, 20, paddleColor);
					yVelocity = yVelocity * -1;
					score2++;
					bHit = true;
				}
			}
		}

		// If the ball isn't moving and it isn't on top of the paddle, put it
		// there and center the paddle and ball. Center the other paddle.
		if (!bInMotion)
		{
			if (ballY - ballHeight == paddle1Y)
				return;
			else
			{
				ballY = paddle1Y - ballHeight;
				ballX = this.getWidth() / 2 - ballWidth / 2;
				paddle1X = this.getWidth() / 2 - paddleWidth / 2;
				paddle2X = this.getWidth() / 2 - paddleWidth / 2;
			}
		}
		// The ball is moving. Let's see if it hit anything.
		else
		{
			// If the ball has not been initialized, center it.
			if (ballX < 0 && ballY < 0)
			{
				ballX = this.getWidth() / 2;
				ballY = this.getHeight() / 2;
			}
			else
			{
				// Move the ball. If it hits a wall or the ceiling, reverse the
				// appropriate direction.
				ballX += xVelocity + xOffset;
				ballY += yVelocity;
				if ((ballX > this.getWidth() - ballWidth) || (ballX < 0))
				{
					xVelocity = xVelocity * -1;
				}
				if ((ballY > this.getHeight() - ballHeight) || (ballY < courtTop))
				{
					if (bTwoPlayer)
					{
						bInMotion = false;
						yVelocity = Math.abs(yVelocity) * -1;
						xVelocity = Math.abs(xVelocity);
					}
					else
					{
						yVelocity = yVelocity * -1;
					}
				}
			}
		}
		showScore(c);
		// Having recalculated various positions, display the ball and paddle.
		c.drawBitmap(ball.getBitmap(), ballX, ballY, null);
		paddle1.top = paddle1Y;
		paddle1.left = paddle1X;
		paddle1.right = paddle1X + paddleWidth;
		paddle1.bottom = paddle1Y + paddleHeight;
		c.drawRect(paddle1, paddleColor);
		
		if (bTwoPlayer)
		{
			paddle2.top = paddle2Y;
			paddle2.left = paddle2X;
			paddle2.right = paddle2X + paddleWidth;
			paddle2.bottom = courtTop + 100;
			c.drawRect(paddle2, paddleColor);
		}
		h.postDelayed(r, frameRate);
	}

	void genTone()
	{
		// fill out the array
		for (int i = 0; i < numSamples; ++i)
		{
			sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
		}

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalized.
		int idx = 0;
		for (final double dVal : sample)
		{
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

		}
	}

	void playSound()
	{
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
		    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
		    AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
		    AudioTrack.MODE_STATIC);
		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		audioTrack.play();
	}

	// Use a new tread as this can take a while
	Thread soundThread = new Thread(new Runnable() {
		public void run()
		{
			genTone();
			h.post(new Runnable() {

				public void run()
				{
					if (bHit)
					{
						playSound();
						bHit = false;
					}
				}
			});
		}
	});

}
