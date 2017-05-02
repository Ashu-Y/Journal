package com.practice.android.journal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.practice.android.journal.data.JournalContract.JournalEntry;
import com.practice.android.journal.data.JournalDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public JournalDbHelper mDbHelper;
    SQLiteDatabase db;
    ArrayList items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new JournalDbHelper(this);
        items = new ArrayList();


        displayDatabaseInfo();


        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        ListView listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(itemsAdapter);



        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, items.get(position).toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, JournalDetails.class);
                intent.putExtra("title", items.get(position).toString());
                startActivity(intent);

            }
        });


    }


    private void displayDatabaseInfo() {
        //Create and/or open a database to read from it
        db = mDbHelper.getReadableDatabase();

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


                items.add(currentTitle);

            }
        } finally {
            //Always close the cursor when you are done reading from it.
            // This releases all its resources and makes it invalid.
            cursor.close();

        }
    }


//    public void check(View v) {
//        displayDatabaseInfo();
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        ListView listView = (ListView) findViewById(R.id.lvItems);
//        listView.setAdapter(itemsAdapter);
//    }

    public void editor(View view) {
        startActivity(new Intent(this, EditorActivity.class));
    }

    public void update(View view){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(JournalEntry.COLUMN_TITLE, "The Hell");

// Which row to update, based on the title
        String selection = JournalEntry.COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = { "abcd" };

        int count = db.update(
                JournalEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        Log.d("Main Activity", "update");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }


}
