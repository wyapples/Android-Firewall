package com.veronicasun.lma;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.veronicasun.lma.Call.CallColumn;
import com.veronicasun.lma.Sms.SmsColumn;
import com.veronicasun.lma.SpamRule.SpamRuleColumn;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;

public class FirewallService extends Service {

	IncomingCallReceiver mIncomingReceiver;
	SMSContentObserver SmsContentObserver;
	private ITelephony mITelephony;
	private AudioManager mAudioManager;

	@Override
	public void onCreate() {
		super.onCreate();

		SmsContentObserver = new SMSContentObserver(new Handler());
		mIncomingReceiver = new IncomingCallReceiver();

		// Regist content observer
		getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, SmsContentObserver);

		// Set the intent filter
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction("android.intent.action.PHONE_STATE");
		registerReceiver(mIncomingReceiver, mIntentFilter);

		// Get audio manager
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// Use reflect method to get endcall method
		TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Method getITelephonyMethod = TelephonyManager.class
					.getDeclaredMethod("getITelephony", (Class[]) null);
			getITelephonyMethod.setAccessible(true);
			mITelephony = (ITelephony) getITelephonyMethod.invoke(
					mTelephonyManager, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	class SMSContentObserver extends ContentObserver {
		Cursor cursor;
		int idColumn;
		int thread_idColumn;
		int contentColumn;
		int numbColumn;

		public SMSContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// start the time analysis,block this in your public release
			//Debug.startMethodTracing("LMA_SmsTrace");
			
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			cursor = getContentResolver().query(
					Uri.parse("content://sms/inbox"),
					new String[] { "_id", "thread_id", "address", "read",
							"body" }, "read = 0", null, "date desc");

			if (cursor.getCount() > 0) {
				idColumn = cursor.getColumnIndex("_id");
				thread_idColumn = cursor.getColumnIndex("thread_id");
				contentColumn = cursor.getColumnIndex("body");
				numbColumn = cursor.getColumnIndex("address");

				cursor.moveToFirst();
				while (cursor.isLast()) {
					// Check if the number is in the database
					String address = cursor.getString(numbColumn);
					address = address.substring(address.length()-4,address.length());
					String query_str = SpamRuleColumn.NUM + " LIKE '%" + address
							+ "%' AND " + SpamRuleColumn.MODE + " <> 1";
					Cursor s_cursor = getContentResolver().query(
							SpamRuleColumn.CONTENT_URI, null, query_str, null,
							null);
					s_cursor.moveToFirst();

					// When matched
					if (s_cursor.getCount() > 0) {
						ContentValues to_sys_values = new ContentValues();
						to_sys_values.put("read", "1");

						Uri mUri = ContentUris.withAppendedId(
								Uri.parse("content://sms/inbox"),
								cursor.getInt(idColumn));
						getContentResolver().update(mUri, to_sys_values, null,
								null);

						ContentValues to_db_values = new ContentValues();

						to_db_values.put(SmsColumn.NUM, address);
						to_db_values.put(SmsColumn.CONTENT,
								cursor.getString(contentColumn));

						getContentResolver().insert(SmsColumn.CONTENT_URI,
								to_db_values);

						mUri = ContentUris.withAppendedId(
								Uri.parse("content://sms/conversations"),
								cursor.getInt(thread_idColumn));
						getContentResolver().delete(mUri, null, null);
						break;
					}

					cursor.moveToNext();
				}
			}
			// stop the time analysis,block this in your public release
			//Debug.stopMethodTracing();
		}
	}

	class IncomingCallReceiver extends BroadcastReceiver {

		Cursor cursor;

		@Override
		public void onReceive(Context context, Intent intent) {
			// start the time analysis,block this in your public release
			//Debug.startMethodTracing("LMA_CallTrace");
			
			// Disable Sound
			mAudioManager
					.setRingerMode(AudioManager.RINGER_MODE_SILENT);

			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			String in_call_numb = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			String in_call_numb_tmp = in_call_numb;
			in_call_numb = in_call_numb.substring(in_call_numb.length() - 4, in_call_numb.length());

			String query_str = SpamRuleColumn.NUM + " LIKE '%" + in_call_numb
					+ "%' AND " + SpamRuleColumn.MODE + " <> 0";

			cursor = getContentResolver().query(SpamRuleColumn.CONTENT_URI,
					null, query_str, null, null);
			cursor.moveToFirst();

			if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
				if (cursor.getCount() > 0) {
					// End call
					try {
						mITelephony.endCall();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Save Log to Database
					ContentValues insert_value = new ContentValues();
					insert_value.put(CallColumn.NUM, in_call_numb_tmp);
					getContentResolver().insert(CallColumn.CONTENT_URI,
							insert_value);
				}
			}
			
			// Enable Sound
			mAudioManager
					.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			
			// stop the time analysis,block this in your public release
			//Debug.stopMethodTracing();  
		}
	}
}
