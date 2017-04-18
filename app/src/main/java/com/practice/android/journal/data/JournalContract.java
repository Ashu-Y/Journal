package com.practice.android.journal.data;

import android.provider.BaseColumns;

/**
 * Created by Ashutosh on 4/18/2017.
 */

public final class JournalContract {

    public static abstract class JournalEntry implements BaseColumns {

        public static final String TABLE_NAME = "Journal";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_LOCATION = "Location";
        public static final String COLUMN_CONTENT = "Content";

    }

}
