package com.example.drivesafe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class DetailsFragment extends Fragment implements OnMapClickListener   {
	
	 private GoogleMap mMap;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
 
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        
        setUpMapIfNeeded();
        mMap.setOnMapClickListener(this);
        
        return rootView;
     
    }
    
    public void onResume() 
    {
        super.onResume();
        setUpMapIfNeeded();
    }
    
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
          
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera .
     */

	@Override
	public void onMapClick(LatLng pos) {
		
		mMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
	}
    
}