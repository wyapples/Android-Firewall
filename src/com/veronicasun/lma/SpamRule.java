package com.veronicasun.lma;

import android.net.Uri;
import android.provider.BaseColumns;

public final class SpamRule {
	public static final String AUTHORITY = "com.veronica.lma.provider";

	private SpamRule() {
		// This class cannot be instantiated
	}

	public static class SpamRuleColumn implements BaseColumns {
		public SpamRuleColumn() {
			// This class cannot be instantiated
		};
		
		// define the uri string
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/spamrule");
		
		// define the MIME type
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.veronica.spamrule";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.veronica.spamrule";
		
		// define the sort order
		public static final String DEFAULT_SORT_ORDER = "extra DESC";
		
		// define the column name
		public static final String NUM = "num";
		public static final String EXTRA = "extra";
		public static final String MODE = "mode";
	}
}