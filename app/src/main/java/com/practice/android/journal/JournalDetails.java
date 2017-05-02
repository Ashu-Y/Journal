package com.practice.android.journal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.practice.android.journal.data.JournalContract.JournalEntry;
import com.practice.android.journal.data.JournalDbHelper;

public class JournalDetails extends AppCompatActivity {

    public JournalDbHelper mDbHelper;
    String title;
    TextView tv;
    ImageView iv;
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_details);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");

        tv = (TextView) findViewById(R.id.title);
        iv = (ImageView) findViewById(R.id.img);

        tv.setText(title);

        mDbHelper = new JournalDbHelper(this);

        displayDatabaseInfo();

    }


    private void displayDatabaseInfo() {
        //Create and/or open a database to read from it
        db = mDbHelper.getReadableDatabase();

        String[] selArgs = new String[] {title};

        /*
        Define a projection that specifies which columns from the database
        you will actually use after this query.
         */
        String[] projection = {
                JournalEntry._ID,
                JournalEntry.COLUMN_TITLE,
                JournalEntry.COLUMN_DATE,
                JournalEntry.COLUMN_LOCATION,
                JournalEntry.COLUMN_IMAGE1,
                JournalEntry.COLUMN_IMAGE2,
                JournalEntry.COLUMN_IMAGE3,
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

//        TextView displayView = (TextView) findViewById(R.id.tv);

        try {
//            displayView.setText("Number of rows = " + cursor.getCount() + ".\n\n");

            /* To access our database, we instantiate our subclass of SQLiteOpenHelper
             and pass the context, which is the current activity.
            */
//            displayView.append(JournalEntry._ID + " - " +
//                    JournalEntry.COLUMN_TITLE + " - " +
//                    JournalEntry.COLUMN_DATE + " - " +
//                    JournalEntry.COLUMN_LOCATION + " - " +
//                    JournalEntry.COLUMN_IMAGE1 + " - " +
//                    JournalEntry.COLUMN_IMAGE2 + " - " +
//                    JournalEntry.COLUMN_IMAGE3 + " - " +
//                    JournalEntry.COLUMN_DESCRIPTION + "\n");

            //Find the index of each column
            int idColumnIndex = cursor.getColumnIndex(JournalEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_DATE);
            int locationColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_LOCATION);
            int image1ColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_IMAGE1);
            int image2ColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_IMAGE2);
            int image3ColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_IMAGE3);
            int descriptionColumnIndex = cursor.getColumnIndex(JournalEntry.COLUMN_DESCRIPTION);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                /*
                Use the column index to extract the value of the word
                at the current row the user is on
                 */
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentLocation = cursor.getString(locationColumnIndex);
                String currentImage1 = cursor.getString(image1ColumnIndex);
                String currentImage2 = cursor.getString(image2ColumnIndex);
                String currentImage3 = cursor.getString(image3ColumnIndex);
                String currentDescription = cursor.getString(descriptionColumnIndex);

                //display the values from each column of the current row in the TextView
//                displayView.append(("\n" + currentID + " - " +
//                        currentTitle + " - " +
//                        currentDate + " - " +
//                        currentLocation + " - " +
//                        currentImage1 + " - " +
//                        currentImage2 + " - " +
//                        currentImage3 + " - " +
//                        currentDescription));

                tv.append(currentDate);
                if(currentImage1 != null && !currentImage1.isEmpty())
                    iv.setImageURI(Uri.parse(currentImage1));

            }
        } finally {
            //Always close the cursor when you are done reading from it.
            // This releases all its resources and makes it invalid.
            cursor.close();

        }
    }

}
