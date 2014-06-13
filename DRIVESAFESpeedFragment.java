package com.example.drivesafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
 
public class SpeedFragment extends Fragment {
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_speed, container, false);
         
        final Button button1 = (Button)rootView.findViewById(R.id.b911);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Intent action = null;
                    action = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:111-111-1111"));
                    startActivity(action);
            }
        });
        
        final Button button2 = (Button)rootView.findViewById(R.id.user1);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Intent action = null;
                    action = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:972-849-2968"));
                    startActivity(action);
            }
        });

        final Button button3 = (Button)rootView.findViewById(R.id.user2);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Intent action = null;
                    action = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:972-878-4955"));
                    startActivity(action);
            }
        });

        final Button button4 = (Button)rootView.findViewById(R.id.user3);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Intent action = null;
                    action = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:206-650-6514"));
                    startActivity(action);
            }
        });
        
        final Button button5 = (Button)rootView.findViewById(R.id.user4);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                    Intent action = null;
                    action = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:469-767-4906"));
                    startActivity(action);
            }
        });

      
        
        return rootView;
    }
 
}