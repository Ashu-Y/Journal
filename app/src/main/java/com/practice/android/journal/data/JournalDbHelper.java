package com.practice.android.journal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.practice.android.journal.data.JournalContract.JournalEntry;

import static android.R.attr.version;

/**
 * Created by Ashutosh on 4/18/2017.
 */

public class JournalDbHelper extends SQLiteOpenHelper {

    //Name of the database file
    private static final String DATABASE_NAME = "journal.db";

    /**
     * Database version. When changing database schema, increment the version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs new instance of {@link JournalDbHelper}
     *
     * @param context of the app
     */

    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    /**
     * Called when database is created  for the first time.
     *
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String containing SQL statement to create the Journal table
        String SQL_CREATE_JOURNAL_TABLE = "CREATE TABLE" + JournalEntry.TABLE_NAME + "("
                + JournalEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + JournalEntry.COLUMN_TITLE + "TEXT NOT NULL"
                + JournalEntry.COLUMN_DATE + "TEXT"
                + JournalEntry.COLUMN_LOCATION + "TEXT"
                + JournalEntry.COLUMN_CONTENT + "TEXT";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_JOURNAL_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
