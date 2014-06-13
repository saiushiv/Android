package com.example.drivesafe;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class HomeFragment extends Fragment {
	
private View mDecorView;

private double mySpeed;		//used to store the user's detected speed
//private int waitCounter;	//used to wait for an interval after the user has apparently stopped moving before disabling the app

private static LocationManager locMan;
private static LocationListener locListener;

private TextView speedTextView;

public static boolean appEnabled;
private boolean isSilenced;

private static AudioManager audiomanage;

public static Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	
    	super.onCreateView(inflater, container, savedInstanceState);
        
		appEnabled = false;
		isSilenced = false;
		
    	View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    	
    	speedTextView=(TextView)rootView.findViewById(R.id.textView2);
    	Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/digital-7.ttf");
    	 speedTextView.setTypeface(typeFace);
        
     	mDecorView = getActivity().getWindow().getDecorView();
    	
         	
       mySpeed = 0;
        locMan = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locListener = new SpeedLocationListener();
        
        //Request location updates from either the GPS or Network provider (GPS preferred); should receive updates every half second at minimum
        if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locListener);
        } 
        else 
        {
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locListener);
        }
        return rootView;
    }
	
	
    public void toggleScreenFocus()
    {
    	if (appEnabled) 
    	{
            //The toggle is enabled
             mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY 
                  | View.INVISIBLE);  			 							
        } 
    	else 
    	{
            // The toggle is disabled
        	mDecorView.setSystemUiVisibility(0);     
        }
    }
    
	private class SpeedLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc)
		{
			if(loc!=null)
			{
					mySpeed = loc.getSpeed();
					mySpeed = mySpeed * (3600 / 1609.344);	//converting m/s to mph
					speedTextView.setText(String.format("%.1f",mySpeed));
					
					//if(appEnabled)
					//{
					if(mySpeed >= 20)
					{
						//waitCounter = 0;	//reset the counter
						
						if(!isSilenced)
						{	
							appEnabled = true;
							toggleScreenFocus();
							isSilenced = true;
							audiomanage = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                    		audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
						}
					} 		
					//}
					else
					{						
						if(isSilenced)
						{
							//waitCounter++;
							
							//if(waitCounter >= 120)	//Wait for at least 120 location updates before disabling the app.  Since the minimum interval between updates is half a second this should take at least 60 seconds.
							//{
							appEnabled = false;
							toggleScreenFocus();
							isSilenced = false;
							audiomanage = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                    		audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
							//}
						}
					}
			}
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{	
		}
		
		@Override
		public void onProviderEnabled(String provider)
		{
		}
		
		@Override
		public void onStatusChanged(String provider,int status,Bundle extras)
		{
		}
	}
	

}