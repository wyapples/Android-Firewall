package com.veronicasun.lma;

import java.text.SimpleDateFormat;

import com.veronicasun.lma.Call.CallColumn;
import com.veronicasun.lma.CallList.ListObserver;
import com.veronicasun.lma.Sms.SmsColumn;
import com.veronicasun.lma.SpamRule.SpamRuleColumn;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SmsList extends Activity {
	int sms_id;
	String sms_numb;
	String sms_content;

	ListView smslist;
	CustomListAdapter smslistAdapter;
	AlertDialog mAlertDialog;
	AlertDialog.Builder mBuilder;
	ListObserver mListObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_list);
		
		mListObserver = new ListObserver(new Handler());
		getContentResolver().registerContentObserver(CallColumn.CONTENT_URI,
				true, mListObserver);

		initDialog();

		smslist = (ListView) findViewById(R.id.call_sms_list);
		smslistAdapter = new CustomListAdapter(getParent());
		smslist.setAdapter(smslistAdapter);
		smslist.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				smslistAdapter.cursor.moveToPosition(position);
				sms_id = smslistAdapter.cursor.getInt(smslistAdapter.idColumn);
				sms_numb = smslistAdapter.cursor
						.getString(smslistAdapter.numbColumn);
				sms_content = smslistAdapter.cursor
						.getString(smslistAdapter.contentColumn);

				mAlertDialog.setMessage(sms_content);
				mAlertDialog.show();
				return true;
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		updateList();
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateList();
	}

	public class CustomListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		int idColumn;
		int extraColumn;
		int contentColumn;
		int numbColumn;
		int dateColumn;

		Cursor cursor;
		Cursor extraCursor;

		public CustomListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			cursor = getContentResolver().query(SmsColumn.CONTENT_URI, null,
					null, null, null);
			idColumn = cursor.getColumnIndex(SmsColumn._ID);
			contentColumn = cursor.getColumnIndex(SmsColumn.CONTENT);
			numbColumn = cursor.getColumnIndex(SmsColumn.NUM);
			dateColumn = cursor.getColumnIndex(SmsColumn.DATE);
		}

		public int getCount() {
			return cursor.getCount();
		}

		public Object getItem(int position) {
			return cursor.getPosition();
		}

		public long getItemId(int position) {
			return cursor.getPosition();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.smslist_item, null);
				holder = new ViewHolder();
				holder.extra = (TextView) convertView
						.findViewById(R.id.sms_item_extra);
				holder.numb = (TextView) convertView
						.findViewById(R.id.sms_item_numb);
				holder.content = (TextView) convertView
						.findViewById(R.id.sms_item_content);
				holder.date = (TextView) convertView
						.findViewById(R.id.sms_item_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			cursor.moveToPosition(position);

			String query_str = SpamRuleColumn.NUM + " = "
					+ cursor.getString(numbColumn);
			extraCursor = getContentResolver().query(
					SpamRuleColumn.CONTENT_URI,
					new String[] { SpamRuleColumn.EXTRA }, query_str, null,
					null);
			extraCursor.moveToFirst();

			if (extraCursor.getCount() > 0) {
				extraColumn = extraCursor.getColumnIndex(SpamRuleColumn.EXTRA);
				holder.extra.setText(extraCursor.getString(extraColumn));
			} else {
				holder.extra.setText("Unknow");
			}

			SimpleDateFormat dFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");

			holder.numb.setText(cursor.getString(numbColumn));
			holder.date.setText(dFormat.format(cursor.getLong(dateColumn)));
			holder.content.setText(cursor.getString(contentColumn));

			return convertView;
		}

		class ViewHolder {
			TextView extra;
			TextView numb;
			TextView content;
			TextView date;
		}
	}

	public void updateList() {
		smslistAdapter.cursor = getContentResolver().query(
				SmsColumn.CONTENT_URI, null, null, null, null);
		smslistAdapter.notifyDataSetChanged();
	}

	public void initDialog() {
		mBuilder = new AlertDialog.Builder(getParent());

		mBuilder.setTitle("Information:")
				.setMessage(sms_content)
				.setPositiveButton("Reply",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked cancel so do some stuff */
								Uri suri = Uri.parse("smsto://" + sms_numb);
								startActivity(new Intent(Intent.ACTION_SENDTO,
										suri));
							}
						})
				.setNeutralButton("Delete",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Uri puri = ContentUris.withAppendedId(
										SmsColumn.CONTENT_URI, sms_id);
								getContentResolver().delete(puri, null, null);
								updateList();
							}
						})
				.setNegativeButton("Cancle",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked cancel so do some stuff */
								dialog.cancel();
							}
						});

		mAlertDialog = mBuilder.create();
	}
	
	class ListObserver extends ContentObserver {

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			updateList();
			super.onChange(selfChange);
		}

		public ListObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

	}
}
