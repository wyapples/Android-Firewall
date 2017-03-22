package com.veronicasun.lma;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class Login extends Activity {
	public static final String PSW_EN = "password_enable";
	public static final String PSW_SET = "password_set";
	public static final String PREFERENCE_NAME = "com.veronicasun.lma_preferences";

	FrameLayout container;
	EditText input_box;
	Button login_button;
	SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mSharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);

		final Intent mIntent = new Intent(this, MainTags.class);

		if (mSharedPreferences.getBoolean(PSW_EN, false)) {
			setContentView(R.layout.login);

			input_box = (EditText) findViewById(R.id.login_input);
			login_button = (Button) findViewById(R.id.login_button);

			login_button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (GlobalPreference.getMD5Str(
							input_box.getText().toString()).equals(
							mSharedPreferences.getString(PSW_SET, ""))) {
						startMain();
					} else {
						Animation shake = AnimationUtils.loadAnimation(
								Login.this, R.anim.shake);
						findViewById(R.id.linearLayout1).startAnimation(shake);
					}
				}
			});
		} else {
			startMain();
		}
	}

	public void startMain() {
		container = (FrameLayout) ((ActivityGroup) getParent()).getWindow()
				.findViewById(R.id.a_container);
		container.removeAllViews();
		container.addView(((ActivityGroup) getParent())
				.getLocalActivityManager()
				.startActivity(
						"login",
						new Intent(this, MainTags.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}

}
