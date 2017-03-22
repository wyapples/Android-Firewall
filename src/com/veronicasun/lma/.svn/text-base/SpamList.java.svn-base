package com.veronicasun.lma;

import com.veronicasun.lma.SpamRule.SpamRuleColumn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SpamList extends Activity {
	// Data part
	int sp_id;
	int sp_mode;
	String sp_extra;
	String sp_numb;

	// The spamlist part
	Button creatnew_button;
	ListView spamlist;
	CustomListAdapter spamlistAdapter;

	// The dialog part
	AlertDialog.Builder dialog_builder;
	AlertDialog creat_dialog;
	AlertDialog edit_dialog;
	View creat_dialogView;
	EditText creat_sp_extra_et;
	EditText creat_sp_numb_et;
	Spinner creat_sp_mode_sp;
	View edit_dialogView;
	EditText edit_sp_extra_et;
	EditText edit_sp_numb_et;
	Spinner edit_sp_mode_sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spamlist);

		initDialogView();
		buildDialog();

		creatnew_button = (Button) findViewById(R.id.creatnew);
		creatnew_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				creat_sp_extra_et.setText("");
				creat_sp_extra_et.requestFocus();
				creat_sp_numb_et.setText("");
				creat_sp_mode_sp.setSelection(0);
				creat_dialog.show();
			}
		});

		spamlist = (ListView) findViewById(R.id.spamlist);
		spamlistAdapter = new CustomListAdapter(this);
		spamlist.setAdapter(spamlistAdapter);
		spamlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				spamlistAdapter.cursor.moveToPosition(position);
				sp_id = spamlistAdapter.cursor.getInt(spamlistAdapter.idColumn);
				sp_extra = spamlistAdapter.cursor
						.getString(spamlistAdapter.extraColumn);
				sp_numb = spamlistAdapter.cursor
						.getString(spamlistAdapter.numbColumn);
				sp_mode = spamlistAdapter.cursor
						.getInt(spamlistAdapter.modeColumn);

				edit_sp_extra_et.setText(sp_extra);
				edit_sp_extra_et.requestFocus();
				edit_sp_numb_et.setText(sp_numb);
				edit_sp_mode_sp.setSelection(sp_mode);
				edit_dialog.show();
				return true;
			}
		});
	}

	public class CustomListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Bitmap mode1;
		private Bitmap mode2;
		private Bitmap mode3;
		int idColumn;
		int extraColumn;
		int numbColumn;
		int modeColumn;

		Cursor cursor;

		public CustomListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			mode1 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.block_sms);
			mode2 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.block_call);
			mode3 = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.block_both);
			cursor = getContentResolver().query(SpamRuleColumn.CONTENT_URI,
					null, null, null, null);
			idColumn = cursor.getColumnIndex(SpamRuleColumn._ID);
			extraColumn = cursor.getColumnIndex(SpamRuleColumn.EXTRA);
			numbColumn = cursor.getColumnIndex(SpamRuleColumn.NUM);
			modeColumn = cursor.getColumnIndex(SpamRuleColumn.MODE);
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
				convertView = inflater.inflate(R.layout.spamlist_item, null);
				holder = new ViewHolder();
				holder.extra = (TextView) convertView
						.findViewById(R.id.sp_item_extra);
				holder.numb = (TextView) convertView
						.findViewById(R.id.sp_item_number);
				holder.mode = (ImageView) convertView
						.findViewById(R.id.sp_item_mode);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			cursor.moveToPosition(position);
			holder.extra.setText(cursor.getString(extraColumn));
			holder.numb.setText(cursor.getString(numbColumn));
			switch (cursor.getInt(modeColumn)) {
			case 0:
				holder.mode.setImageBitmap(mode1);
				break;
			case 1:
				holder.mode.setImageBitmap(mode2);
				break;
			case 2:
				holder.mode.setImageBitmap(mode3);
				break;
			default:
				break;
			}

			return convertView;
		}

		class ViewHolder {
			TextView extra;
			TextView numb;
			ImageView mode;
		}
	}

	public void updateList() {
		spamlistAdapter.cursor = getContentResolver().query(
				SpamRuleColumn.CONTENT_URI, null, null, null, null);
		spamlistAdapter.notifyDataSetChanged();
	}

	public void buildDialog() {
		dialog_builder = new AlertDialog.Builder(getParent());

		dialog_builder
				.setTitle("Creat new Rule")
				.setView(creat_dialogView)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								sp_extra = creat_sp_extra_et.getText()
										.toString();
								sp_numb = creat_sp_numb_et.getText().toString();

								ContentValues insert_value = new ContentValues();
								insert_value.put(SpamRuleColumn.NUM, sp_numb);
								insert_value
										.put(SpamRuleColumn.EXTRA, sp_extra);
								insert_value.put(SpamRuleColumn.MODE, sp_mode);

								Uri back_uri = getContentResolver().insert(
										SpamRuleColumn.CONTENT_URI,
										insert_value);
								updateList();
							}
						})
				.setNeutralButton("Pick",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Pick data from contact list
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
								startActivityForResult(intent, 1);
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

		creat_dialog = dialog_builder.create();

		dialog_builder
				.setTitle("Edit Rule")
				.setView(edit_dialogView)
				.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								sp_extra = edit_sp_extra_et.getText()
										.toString();
								sp_numb = edit_sp_numb_et.getText().toString();

								ContentValues insert_value = new ContentValues();
								insert_value.put(SpamRuleColumn.NUM, sp_numb);
								insert_value
										.put(SpamRuleColumn.EXTRA, sp_extra);
								insert_value.put(SpamRuleColumn.MODE, sp_mode);

								Uri puri = ContentUris.withAppendedId(
										SpamRuleColumn.CONTENT_URI, sp_id);
								getContentResolver().update(puri, insert_value,
										null, null);
								updateList();
							}
						})
				.setNeutralButton("Delete",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Uri puri = ContentUris.withAppendedId(
										SpamRuleColumn.CONTENT_URI, sp_id);
								getContentResolver().delete(puri, null, null);
								updateList();
							}
						});

		edit_dialog = dialog_builder.create();
	}

	public void initDialogView() {
		LayoutInflater inflater = LayoutInflater.from(getParent());
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getParent(), R.array.modes,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		creat_dialogView = inflater.inflate(R.layout.creat_new_rule_dialog,
				null);
		creat_sp_extra_et = (EditText) creat_dialogView
				.findViewById(R.id.spam_extra);
		creat_sp_numb_et = (EditText) creat_dialogView
				.findViewById(R.id.spam_num);
		creat_sp_mode_sp = (Spinner) creat_dialogView
				.findViewById(R.id.spam_mode);
		creat_sp_mode_sp.setAdapter(adapter);
		creat_sp_mode_sp
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						sp_mode = position;
					}

					public void onNothingSelected(AdapterView<?> parent) {
						sp_mode = 0;
					}
				});

		edit_dialogView = inflater
				.inflate(R.layout.creat_new_rule_dialog, null);
		edit_sp_extra_et = (EditText) edit_dialogView
				.findViewById(R.id.spam_extra);
		edit_sp_numb_et = (EditText) edit_dialogView
				.findViewById(R.id.spam_num);
		edit_sp_mode_sp = (Spinner) edit_dialogView
				.findViewById(R.id.spam_mode);
		edit_sp_mode_sp.setAdapter(adapter);
		edit_sp_mode_sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sp_mode = position;
			}

			public void onNothingSelected(AdapterView<?> parent) {
				sp_mode = 0;
			}
		});
	}
}
