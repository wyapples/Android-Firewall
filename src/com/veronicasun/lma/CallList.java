package com.veronicasun.lma;

import java.text.SimpleDateFormat;

import com.veronicasun.lma.Call.CallColumn;
import com.veronicasun.lma.SpamRule.SpamRuleColumn;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CallList extends Activity {

	int call_id;

	ListView calllist;
	CustomListAdapter calllistAdapter;
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

		calllist = (ListView) findViewById(R.id.call_sms_list);
		calllistAdapter = new CustomListAdapter(getParent());
		calllist.setAdapter(calllistAdapter);
		calllist.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				calllistAdapter.cursor.moveToPosition(position);
				call_id = calllistAdapter.cursor
						.getInt(calllistAdapter.idColumn);

				mAlertDialog.show();
				return true;
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		updateList();
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		updateList();
		super.onResume();
	}

	public class CustomListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		int idColumn;
		int extraColumn;
		int numbColumn;
		int dateColumn;

		Cursor cursor;
		Cursor extraCursor;

		public CustomListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			cursor = getContentResolver().query(CallColumn.CONTENT_URI, null,
					null, null, null);
			idColumn = cursor.getColumnIndex(CallColumn._ID);
			numbColumn = cursor.getColumnIndex(CallColumn.NUM);
			dateColumn = cursor.getColumnIndex(CallColumn.DATE);
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
				convertView = inflater.inflate(R.layout.calllist_item, null);
				holder = new ViewHolder();
				holder.extra = (TextView) convertView
						.findViewById(R.id.call_item_extra);
				holder.numb = (TextView) convertView
						.findViewById(R.id.call_item_numb);
				holder.date = (TextView) convertView
						.findViewById(R.id.call_item_date);
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

			return convertView;
		}

		class ViewHolder {
			TextView extra;
			TextView numb;
			TextView date;
		}
	}

	public void updateList() {
		calllistAdapter.cursor = getContentResolver().query(
				CallColumn.CONTENT_URI, null, null, null, null);
		calllistAdapter.notifyDataSetChanged();
	}

	public void initDialog() {
		mBuilder = new AlertDialog.Builder(getParent());

		mBuilder.setTitle("Delete")
				.setMessage("Are you sure to delete this log?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked cancel so do some stuff */
								Uri puri = ContentUris.withAppendedId(
										CallColumn.CONTENT_URI, call_id);
								getContentResolver().delete(puri, null, null);
								updateList();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
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
