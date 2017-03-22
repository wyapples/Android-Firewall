package com.veronicasun.lma;

import java.util.HashMap;

import com.veronicasun.lma.Call.CallColumn;
import com.veronicasun.lma.Sms.SmsColumn;
import com.veronicasun.lma.SpamRule.SpamRuleColumn;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DatabaseProvider extends ContentProvider {
	// Database define part
	private DBOHelper dboHelper;
	private static final String DB_NAME = "Main_db_3tb.db";
	private static final String TB_SPAM = "tb_spam";
	private static final String TB_SMS = "tb_sms";
	private static final String TB_CALL = "tb_call";
	private static final int DB_VERSION = 1;
	// Hash maps
	private static HashMap<String, String> SpamRulesProjetionMap;
	private static HashMap<String, String> SmsProjetionMap;
	private static HashMap<String, String> CallProjetionMap;
	static {
		SpamRulesProjetionMap = new HashMap<String, String>();
		SpamRulesProjetionMap.put(SpamRuleColumn._ID, SpamRuleColumn._ID);
		SpamRulesProjetionMap.put(SpamRuleColumn.NUM, SpamRuleColumn.NUM);
		SpamRulesProjetionMap.put(SpamRuleColumn.EXTRA, SpamRuleColumn.EXTRA);
		SpamRulesProjetionMap.put(SpamRuleColumn.MODE, SpamRuleColumn.MODE);

		SmsProjetionMap = new HashMap<String, String>();
		SmsProjetionMap.put(SmsColumn._ID, SmsColumn._ID);
		SmsProjetionMap.put(SmsColumn.NUM, SmsColumn.NUM);
		SmsProjetionMap.put(SmsColumn.CONTENT, SmsColumn.CONTENT);
		SmsProjetionMap.put(SmsColumn.DATE, SmsColumn.DATE);

		CallProjetionMap = new HashMap<String, String>();
		CallProjetionMap.put(CallColumn._ID, CallColumn._ID);
		CallProjetionMap.put(CallColumn.NUM, CallColumn.NUM);
		CallProjetionMap.put(CallColumn.DATE, CallColumn.DATE);
	}
	// Uri Matcher define part
	private static final int SPAMRULES = 1;
	private static final int SPAMRULE_ID = 2;
	private static final int SMSS = 3;
	private static final int SMS_ID = 4;
	private static final int CALLS = 5;
	private static final int CALL_ID = 6;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(SpamRule.AUTHORITY, "spamrule", SPAMRULES);
		uriMatcher.addURI(SpamRule.AUTHORITY, "spamrule/#", SPAMRULE_ID);
		uriMatcher.addURI(Sms.AUTHORITY, "sms", SMSS);
		uriMatcher.addURI(Sms.AUTHORITY, "sms/#", SMS_ID);
		uriMatcher.addURI(Call.AUTHORITY, "call", CALLS);
		uriMatcher.addURI(Call.AUTHORITY, "call/#", CALL_ID);
	}

	// Database open helper
	private static class DBOHelper extends SQLiteOpenHelper {
		private static final String CREAT_TB_SPAM = "CREATE TABLE " + TB_SPAM + " (" 
				+ SpamRuleColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ SpamRuleColumn.NUM + " TEXT," 
				+ SpamRuleColumn.EXTRA + " TEXT,"
				+ SpamRuleColumn.MODE + " INTEGER);";

		private static final String CREAT_TB_SMS = "CREATE TABLE " + TB_SMS + " (" 
				+ SmsColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ SmsColumn.NUM + " TEXT," 
				+ SmsColumn.CONTENT + " TEXT,"
				+ SmsColumn.DATE + " INTEGER);";

		private static final String CREAT_TB_CALL = "CREATE TABLE " + TB_CALL + " (" 
				+ CallColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ CallColumn.NUM + " TEXT," 
				+ CallColumn.DATE + " INTEGER);";

		DBOHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREAT_TB_SPAM);
			db.execSQL(CREAT_TB_SMS);
			db.execSQL(CREAT_TB_CALL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + TB_SPAM);
			db.execSQL("DROP TABLE IF EXISTS" + TB_SMS);
			db.execSQL("DROP TABLE IF EXISTS" + TB_CALL);
		}

	}

	@Override
	public boolean onCreate() {
		dboHelper = new DBOHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// Match the type of uri String and convert the uri to String
		switch (uriMatcher.match(uri)) {
		case SPAMRULES:
			return SpamRuleColumn.CONTENT_TYPE;
		case SPAMRULE_ID:
			return SpamRuleColumn.CONTENT_ITEM_TYPE;
		case SMSS:
			return SmsColumn.CONTENT_TYPE;
		case SMS_ID:
			return SmsColumn.CONTENT_ITEM_TYPE;
		case CALLS:
			return CallColumn.CONTENT_TYPE;
		case CALL_ID:
			return CallColumn.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		SQLiteDatabase db = dboHelper.getWritableDatabase();
		
		if (uriMatcher.match(uri) == SPAMRULES) {
			if (values.containsKey(SpamRuleColumn.EXTRA) == false) {
				values.put(SpamRuleColumn.EXTRA, "Unknow");
			}
			if (values.containsKey(SpamRuleColumn.MODE) == false) {
				values.put(SpamRuleColumn.MODE, 0);
			}

			long rowId = db.insert(TB_SPAM, null, values);
			if (rowId > 0) {
				Uri backUri = ContentUris.withAppendedId(
						SpamRuleColumn.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(backUri, null);
				return backUri;
			}
		}

		if (uriMatcher.match(uri) == SMSS) {
			if (values.containsKey(SmsColumn.DATE) == false) {
				values.put(SmsColumn.DATE,
						Long.valueOf(System.currentTimeMillis()));
			}

			long rowId = db.insert(TB_SMS, null, values);
			if (rowId > 0) {
				Uri backUri = ContentUris.withAppendedId(SmsColumn.CONTENT_URI,
						rowId);
				getContext().getContentResolver().notifyChange(backUri, null);
				return backUri;
			}
		}

		if (uriMatcher.match(uri) == CALLS) {
			if (values.containsKey(CallColumn.DATE) == false) {
				values.put(CallColumn.DATE,
						Long.valueOf(System.currentTimeMillis()));
			}

			long rowId = db.insert(TB_CALL, null, values);
			if (rowId > 0) {
				Uri backUri = ContentUris.withAppendedId(
						CallColumn.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(backUri, null);
				return backUri;
			}
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sq_bulder = new SQLiteQueryBuilder();
		String orderBy;

		switch (uriMatcher.match(uri)) {
		case SPAMRULES:
			sq_bulder.setTables(TB_SPAM);
			sq_bulder.setProjectionMap(SpamRulesProjetionMap);
			orderBy = SpamRuleColumn.DEFAULT_SORT_ORDER;
			break;
		case SPAMRULE_ID:
			sq_bulder.setTables(TB_SPAM);
			sq_bulder.setProjectionMap(SpamRulesProjetionMap);
			sq_bulder.appendWhere(SpamRuleColumn._ID + "="
					+ uri.getPathSegments().get(1));
			orderBy = SpamRuleColumn.DEFAULT_SORT_ORDER;
			break;
		case SMSS:
			sq_bulder.setTables(TB_SMS);
			sq_bulder.setProjectionMap(SmsProjetionMap);
			orderBy = SmsColumn.DEFAULT_SORT_ORDER;
			break;
		case SMS_ID:
			sq_bulder.setTables(TB_SMS);
			sq_bulder.setProjectionMap(SmsProjetionMap);
			sq_bulder.appendWhere(SmsColumn._ID + "="
					+ uri.getPathSegments().get(1));
			orderBy = SmsColumn.DEFAULT_SORT_ORDER;
			break;
		case CALLS:
			sq_bulder.setTables(TB_CALL);
			sq_bulder.setProjectionMap(CallProjetionMap);
			orderBy = CallColumn.DEFAULT_SORT_ORDER;
			break;
		case CALL_ID:
			sq_bulder.setTables(TB_CALL);
			sq_bulder.setProjectionMap(CallProjetionMap);
			sq_bulder.appendWhere(CallColumn._ID + "="
					+ uri.getPathSegments().get(1));
			orderBy = CallColumn.DEFAULT_SORT_ORDER;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (!TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = dboHelper.getReadableDatabase();
		Cursor result = sq_bulder.query(db, projection, selection,
				selectionArgs, null, null, orderBy);
		result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = dboHelper.getWritableDatabase();
		int count;
		String id;

		switch (uriMatcher.match(uri)) {
		case SPAMRULES:
			count = db.update(TB_SPAM, values, where, whereArgs);
			break;
		case SPAMRULE_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(TB_SPAM, values,
					SpamRuleColumn._ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case SMSS:
			count = db.update(TB_SMS, values, where, whereArgs);
			break;
		case SMS_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(TB_SMS, values,
					SmsColumn._ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case CALLS:
			count = db.update(TB_CALL, values, where, whereArgs);
			break;
		case CALL_ID:
			id = uri.getPathSegments().get(1);
			count = db.update(TB_SMS, values,
					CallColumn._ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dboHelper.getWritableDatabase();
		int count;
		String id;

		switch (uriMatcher.match(uri)) {
		case SPAMRULES:
			count = db.delete(TB_SPAM, where, whereArgs);
			break;
		case SPAMRULE_ID:
			id = uri.getPathSegments().get(1);
			count = db.delete(TB_SPAM,
					SpamRuleColumn._ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case SMSS:
			count = db.delete(TB_SMS, where, whereArgs);
			break;
		case SMS_ID:
			id = uri.getPathSegments().get(1);
			count = db.delete(TB_SMS,
					SmsColumn._ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		case CALLS:
			count = db.delete(TB_CALL, where, whereArgs);
			break;
		case CALL_ID:
			id = uri.getPathSegments().get(1);
			count = db.delete(TB_CALL,
					CallColumn._ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
