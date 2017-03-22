package com.veronicasun.lma;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Toggle extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toggletab);
		
		final Intent serviceIntent = new Intent(this, FirewallService.class);
		
		final ToggleButton mToggleButton = (ToggleButton) findViewById(R.id.toggle);

		if (isServiceRunning(this, "FirewallService")) {
			mToggleButton.setChecked(true);
		} else {
			mToggleButton.setChecked(false);
		}

		mToggleButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (startService(serviceIntent) != null) {
								Toast.makeText(Toggle.this, "Service Start Sucess", Toast.LENGTH_SHORT).show();
							}else {
								Toast.makeText(Toggle.this, "Service Start Fail", Toast.LENGTH_SHORT).show();
							}
						}else {
							if(stopService(serviceIntent)){
								Toast.makeText(Toggle.this, "Service Stoped Sucess", Toast.LENGTH_SHORT).show();
							}else {
								Toast.makeText(Toggle.this, "Service Stoped Fail", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});

	}

	public boolean isServiceRunning(Context context, String service_name) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(30);
		for (RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(
					"com.veronicasun.lma." + service_name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem menu_set = menu.add("Setting");
		MenuItem menu_about = menu.add("About");
		
		menu_set.setIcon(android.R.drawable.ic_menu_manage);
		menu_set.setIntent(new Intent(this, GlobalPreference.class));
		
		menu_about.setIcon(android.R.drawable.ic_menu_info_details);
		
		return super.onCreateOptionsMenu(menu);
	}
}
