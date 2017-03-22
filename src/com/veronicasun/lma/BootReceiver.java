package com.veronicasun.lma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;

public class BootReceiver extends BroadcastReceiver {
	static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	public static final String SER_STAT = "ser_stat";
	public static final String PREFERENCE_NAME = "com.veronicasun.lma_preferences";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(BOOT_ACTION)){
			SharedPreferences serviceStat = context.getSharedPreferences(PREFERENCE_NAME, PreferenceActivity.MODE_PRIVATE);
			final Intent serviceIntent = new Intent(context, FirewallService.class);
			if(serviceStat.getBoolean(SER_STAT, false))
				context.startService(serviceIntent);
		}
	}

}
