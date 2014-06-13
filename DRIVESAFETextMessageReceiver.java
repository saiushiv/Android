package com.example.drivesafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class TextMessageReceiver extends BroadcastReceiver {
	
	String phoneNumber = null; 
	final static String sentmsg = "Driving right now. Will call you later !!";
	
	
 @Override
	public void onReceive(Context context, Intent intent) {
	
	    Bundle bundle=intent.getExtras();
		
		Object[] messages=(Object[])bundle.get("pdus");
		SmsMessage[] sms=new SmsMessage[messages.length];

		for(int n=0;n<messages.length;n++){
			sms[n]=SmsMessage.createFromPdu((byte[]) messages[n]);
		}

		for(SmsMessage msg:sms){
		
		phoneNumber = msg.getOriginatingAddress();
		
		}
		if(HomeFragment.appEnabled){
         SmsManager smsmesg = SmsManager.getDefault();
         smsmesg.sendTextMessage(phoneNumber, null, sentmsg, null, null);
		}
     
		
	}
}