package com.practice.android.journal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.practice.android.journal.data.JournalContract.JournalEntry;
import com.practice.android.journal.data.JournalDbHelper;

public class EditorActivity extends AppCompatActivity {

    private JournalDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        /* To access our database, we instantiate our subclass of SQLiteOpenHelper
         and pass the context, which is the current activity.
        */
        mDbHelper = new JournalDbHelper((this));

        //Get the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        /*
        Create a ContentValues object where the column names are the keys,
        and the dummy data is the values.
         */
        ContentValues values = new ContentValues();
        values.put(JournalEntry.COLUMN_TITLE, "The Hell");
        values.put(JournalEntry.COLUMN_DATE, "23");
        values.put(JournalEntry.COLUMN_LOCATION, "BML Munjal University");
        values.put(JournalEntry.COLUMN_DESCRIPTION, "Welcome to the hell");

        /**
         * Insert a new row for The Hell in the database, returning the ID of that new row.
         * The first argument for db.insert() is the pets table name.
         * The second argument provides the name of a column in which the framework
         * can insert NULL in the event that the ContentValues is empty (if
         * this is set to "null", then the framework will not insert a row when
         * there are no values).
         * The third argument is the ContentValues object containing the info for Toto.
         */
        long newRowId = db.insert(JournalEntry.TABLE_NAME, null, values);

    }
}
