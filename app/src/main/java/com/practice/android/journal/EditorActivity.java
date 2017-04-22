package com.practice.android.journal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.practice.android.journal.data.JournalContract.JournalEntry;
import com.practice.android.journal.data.JournalDbHelper;

public class EditorActivity extends AppCompatActivity {

    private JournalDbHelper mDbHelper;

    private EditText mTitle;
    private EditText mDate;
    private EditText mLocation;
    private EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDbHelper = new JournalDbHelper(this);

        mTitle = (EditText) findViewById(R.id.title);
        mDate = (EditText) findViewById(R.id.date);
        mLocation = (EditText) findViewById(R.id.location);
        mDescription = (EditText) findViewById(R.id.desc);

    }

    public void insertEntry(View view) {
        //Get the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String title = mTitle.getText().toString().trim();
        String date = mDate.getText().toString().trim();
        String location = mLocation.getText().toString().trim();
        String description = mDescription.getText().toString().trim();

        /*
        Create a ContentValues object where the column names are the keys,
        and the dummy data is the values.
         */
        ContentValues values = new ContentValues();
        values.put(JournalEntry.COLUMN_TITLE, title);
        values.put(JournalEntry.COLUMN_DATE, date);
        values.put(JournalEntry.COLUMN_LOCATION, location);
        values.put(JournalEntry.COLUMN_DESCRIPTION, description);

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

        if (newRowId == -1) {
            Toast.makeText(this, "Error in inserting data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data successfully inserted", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this, MainActivity.class));

    }
}
