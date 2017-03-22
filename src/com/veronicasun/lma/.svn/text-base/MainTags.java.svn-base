package com.veronicasun.lma;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class MainTags extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		showTabs();

	}

	public void showTabs() {
		this.setContentView(R.layout.main_tab_view);
		
		InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if (mInputMethodManager.isActive()) {
			mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
		}
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		RelativeLayout toggleTab = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.tabstyle, null);
		TextView toggleTabLabel = (TextView) toggleTab
				.findViewById(R.id.tab_label);
		ImageView toggleTabImg = (ImageView) toggleTab
				.findViewById(R.id.tab_img);
		toggleTabImg.setImageDrawable(getResources().getDrawable(
				R.drawable.tog_icon));
		toggleTabLabel.setText("Toggle");

		RelativeLayout spamlistTab = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.tabstyle, null);
		TextView spamlistTabLabel = (TextView) spamlistTab
				.findViewById(R.id.tab_label);
		ImageView spamlistTabImg = (ImageView) spamlistTab
				.findViewById(R.id.tab_img);
		spamlistTabImg.setImageDrawable(getResources().getDrawable(
				R.drawable.spam_icon));
		spamlistTabLabel.setText("SpamList");

		RelativeLayout inboxTab = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.tabstyle, null);
		TextView inboxTabLabel = (TextView) inboxTab
				.findViewById(R.id.tab_label);
		ImageView inboxTabImg = (ImageView) inboxTab.findViewById(R.id.tab_img);
		inboxTabImg.setImageDrawable(getResources().getDrawable(
				R.drawable.sms_icon));
		inboxTabLabel.setText("InBox");

		RelativeLayout incallTab = (RelativeLayout) LayoutInflater.from(this)
				.inflate(R.layout.tabstyle, null);
		TextView incallTabLabel = (TextView) incallTab
				.findViewById(R.id.tab_label);
		ImageView incallTabImg = (ImageView) incallTab
				.findViewById(R.id.tab_img);
		incallTabImg.setImageDrawable(getResources().getDrawable(
				R.drawable.call_icon));
		incallTabLabel.setText("InCall");

		TabHost tabHost = getTabHost();
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(toggleTab)
				.setContent(new Intent(this, Toggle.class)));

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(spamlistTab)
				.setContent(new Intent(this, SpamList.class)));

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(inboxTab)
				.setContent(new Intent(this, SmsList.class)));

		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(incallTab)
				.setContent(new Intent(this, CallList.class)));

		tabHost.setCurrentTab(0);
		tabHost.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.bkg));
	}
}