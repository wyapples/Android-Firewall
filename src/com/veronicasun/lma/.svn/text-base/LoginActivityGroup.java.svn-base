package com.veronicasun.lma;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class LoginActivityGroup extends ActivityGroup {

	FrameLayout container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_view_container);

		container = (FrameLayout) findViewById(R.id.a_container);
		set_as_login();
	}

	public void set_as_login() {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"login",
				new Intent(this, Login.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}
	
	public void set_as_main() {
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
				"login",
				new Intent(this, MainTags.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}
}
