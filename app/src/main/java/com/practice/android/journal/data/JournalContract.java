package com.practice.android.journal.data;

import android.provider.BaseColumns;

/**
 * Created by Ashutosh on 4/18/2017.
 */

public final class JournalContract {

    //abstract

    public static class JournalEntry implements BaseColumns {

        public static final String TABLE_NAME = "Journal";
        public final static String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_LOCATION = "Location";
        public static final String COLUMN_IMAGE1 = "Image1";
        public static final String COLUMN_IMAGE2 = "Image2";
        public static final String COLUMN_IMAGE3 = "Image3";
        public static final String COLUMN_DESCRIPTION = "Description";

    }

}
