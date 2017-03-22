package com.veronicasun.lma;

import android.net.Uri;
import android.provider.BaseColumns;


public final class Call {
	public static final String AUTHORITY = "com.veronica.lma.provider";

	private Call() {
		// This class cannot be instantiated
	}

	public static class CallColumn implements BaseColumns {
		public CallColumn() {
			// This class cannot be instantiated
		};
		
		// define the uri string
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/call");
		
		// define the MIME type
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.veronica.call";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.veronica.call";
		
		// define the sort order
		public static final String DEFAULT_SORT_ORDER = "date DESC";
		
		// define the column name
		public static final String NUM = "num";
		public static final String DATE = "date";
	}
}