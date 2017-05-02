package com.practice.android.journal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.practice.android.journal.data.JournalContract.JournalEntry;
import com.practice.android.journal.data.JournalDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditorActivity extends AppCompatActivity {

    String check1 = null;
    String check2 = null;
    String check3 = null;
    int flag = 0;
    private JournalDbHelper mDbHelper;
    private EditText mTitle;
    private EditText mDate;
    private EditText mLocation;
    private EditText mDescription;
    //from Take photo
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect1;
    private Button btnSelect2;
    private Button btnSelect3;
    private ImageView ivImage1;
    private ImageView ivImage2;
    private ImageView ivImage3;
    private String userChosenTask;
    //For Date picker
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDbHelper = new JournalDbHelper(this);

        mTitle = (EditText) findViewById(R.id.title);
        mDate = (EditText) findViewById(R.id.date);
        mLocation = (EditText) findViewById(R.id.location);
        mDescription = (EditText) findViewById(R.id.desc);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);


        //from Take photo
        btnSelect1 = (Button) findViewById(R.id.btnSelectPhoto1);
        btnSelect1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 1;
                selectImage();
            }
        });
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);

        btnSelect2 = (Button) findViewById(R.id.btnSelectPhoto2);
        btnSelect2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 2;
                selectImage();
            }
        });
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);

        btnSelect3 = (Button) findViewById(R.id.btnSelectPhoto3);
        btnSelect3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = 3;
                selectImage();
            }
        });
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);

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
        Log.d("Editor Activity", "check1: " + check1);
        values.put(JournalEntry.COLUMN_IMAGE1, check1);
        Log.d("Editor Activity", "check2: " + check2);
        values.put(JournalEntry.COLUMN_IMAGE2, check2);
        Log.d("Editor Activity", "check3: " + check3);
        values.put(JournalEntry.COLUMN_IMAGE3, check3);
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
        long newRowId = -2;
        if(title != null && !title.isEmpty())
            newRowId = db.insert(JournalEntry.TABLE_NAME, null, values);

        if(newRowId == -2)
            Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();

        if (newRowId == -1) {
            Toast.makeText(this, "Error in inserting data", Toast.LENGTH_SHORT).show();
        }
        if(newRowId != -1 && newRowId != -2)
        {
            Toast.makeText(this, "Data successfully inserted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }



    }

    //from Take photo
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditorActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag == 1) {
            ivImage1.setImageBitmap(thumbnail);
            flag = 0;
        }
        if (flag == 2) {
            ivImage2.setImageBitmap(thumbnail);
            flag = 0;
        }
        if (flag == 3) {
            ivImage3.setImageBitmap(thumbnail);
            flag = 0;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                if (flag == 1) {
                    check1 = data.getData().toString().trim();
                }
                if (flag == 2) {
                    check2 = data.getData().toString().trim();
                }
                if (flag == 3) {
                    check3 = data.getData().toString().trim();
                }
                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (flag == 1) {
            ivImage1.setImageBitmap(bm);
            flag = 0;
        }
        if (flag == 2) {
            ivImage2.setImageBitmap(bm);
            flag = 0;
        }
        if (flag == 3) {
            ivImage3.setImageBitmap(bm);
            flag = 0;
        }
    }

    //For Date Picker
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        mDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


}
