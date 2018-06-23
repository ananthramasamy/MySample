package com.anandharajr.mysample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.anandharajr.mysample.database.SQLiteDBHelper;

import org.junit.After;

import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_CAROUSEL_IMAGE_ARRAY;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_DATA_ID;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_FIRST_NAME;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_IMAGE_PATH;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_LAST_NAME;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_PAGE_ID;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_TOTAL_PAGE_COUNT;
import static com.anandharajr.mysample.database.SQLiteDBHelper.TABLE_USER;

public class SQLiteTest extends AndroidTestCase {

    private SQLiteDatabase dbWrite;
    private SQLiteDatabase dbRead;
    private ContentValues contentValues;

    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this.getContext());
        dbWrite = dbHelper.getWritableDatabase();
        dbRead = dbHelper.getReadableDatabase();
        contentValues = new ContentValues();
    }

    public void testReadWriteAuthTable() throws Exception {
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this.getContext());
        dbWrite = dbHelper.getWritableDatabase();
        contentValues.put(KEY_PAGE_ID, "1");
        contentValues.put(KEY_TOTAL_PAGE_COUNT, "4");
        contentValues.put(KEY_DATA_ID, "1");
        contentValues.put(KEY_FIRST_NAME, "George");
        contentValues.put(KEY_LAST_NAME, "Bluth");
        contentValues.put(KEY_IMAGE_PATH, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
        contentValues.put(KEY_CAROUSEL_IMAGE_ARRAY, "android.graphics.Bitmap@f1c1cd");
        dbWrite.insert(TABLE_USER, null, contentValues);

        String selectQuery = "SELECT * FROM " + TABLE_USER;
        Cursor c = dbRead.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();

            String pageID = c.getString(c.getColumnIndex(KEY_PAGE_ID));
            String TotalPageCount = c.getString(c.getColumnIndex(KEY_TOTAL_PAGE_COUNT));
            String dataID = c.getString(c.getColumnIndex(KEY_DATA_ID));
            String FirstName = c.getString(c.getColumnIndex(KEY_FIRST_NAME));
            String LastName = c.getString(c.getColumnIndex(KEY_LAST_NAME));
            String ImagePath = c.getString(c.getColumnIndex(KEY_IMAGE_PATH));
            String ImageArray = c.getString(c.getColumnIndex(KEY_CAROUSEL_IMAGE_ARRAY));

            assertEquals(pageID, "1");
            assertEquals(TotalPageCount, "4");
            assertEquals(dataID, "1");
            assertEquals(FirstName, "George");
            assertEquals(LastName, "Bluth");
            assertEquals(ImagePath, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
            assertEquals(ImageArray, "android.graphics.Bitmap@f1c1cd");
        }
    }


}