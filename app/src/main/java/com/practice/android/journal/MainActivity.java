package com.practice.android.journal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.practice.android.journal.data.JournalContract.JournalEntry;
import com.practice.android.journal.data.JournalDbHelper;

public class MainActivity extends AppCompatActivity {

    public JournalDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new JournalDbHelper(this);

    }


    private void displayDatabaseInfo() {
        //Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        /*
        Define a projection that specifies which columns from the database
        you will actually use after this query.
         */
        String[] projection = {
                JournalEntry._ID,
                JournalEntry.COLUMN_TITLE,
                JournalEntry.COLUMN_DATE,
                JournalEntry.COLUMN_LOCATION,
                JournalEntry.COLUMN_DESCRIPTION};

        //Perform a query on Journal table
        Cursor cursor = db.query(
                JournalEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.tv);

        try {
            displayView.setText("Number of rows = " + cursor.getCount() + ".\n\n");

            /* To access our database, we instantiate our subclass of SQLiteOpenHelper
             and pass the context, which is the current activity.
            */
            displayView.append(JournalEntry._ID + " - " +
                    JournalEntry.COLUMN_TITLE + " - " +
                    JournalEntry.COLUMN_DATE + " - " +
                    JournalEntry.COLUMN_LOCATION + " - " +
                    JournalEntry.COLUMN_DESCRIPTION + "\n");

            //Find the index of each column
            int idColumnIndex = cursor.getColumnIndex(JournalEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_DATE);
            int locationColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_LOCATION);
            int descriptionColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_DESCRIPTION);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                /*
                Use the column index to extract the value of the word
                at the current row the user is on
                 */
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                int currentDate = cursor.getInt(dateColumnIndex);
                String currentLocation = cursor.getString(locationColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);

                //display the values from each column of the current row in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentTitle + " - " +
                        currentDate + " - " +
                        currentLocation + " - " +
                        currentDescription));

            }
        } finally {
            //Always close the cursor when you are done reading from it.
            // This releases all its resources and makes it invalid.
            cursor.close();

        }
    }

    public void insertEntry() {
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

        if (newRowId == -1) {
            Toast.makeText(this, "Error in inserting data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data successfully inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public void check(View v) {
        insertEntry();
        displayDatabaseInfo();
    }
}
