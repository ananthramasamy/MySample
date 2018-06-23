package com.anandharajr.mysample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.anandharajr.mysample.database.SQLiteDBHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_CAROUSEL_IMAGE_ARRAY;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_DATA_ID;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_FIRST_NAME;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_IMAGE_PATH;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_LAST_NAME;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_PAGE_ID;
import static com.anandharajr.mysample.database.SQLiteDBHelper.KEY_TOTAL_PAGE_COUNT;
import static com.anandharajr.mysample.database.SQLiteDBHelper.TABLE_USER;

@RunWith(MockitoJUnitRunner.class)
public class SQLiteDBHelperUnitTest extends AndroidTestCase {

    @Test
    public void insertImage() throws Exception {
        SQLiteDBHelper dbHelper = new SQLiteDBHelper(this.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //TODO Put dummy data to ContentValues
        ContentValues mContentValue = new ContentValues();
        mContentValue.put(KEY_PAGE_ID, "1");
        mContentValue.put(KEY_TOTAL_PAGE_COUNT, "4");
        mContentValue.put(KEY_DATA_ID, "1");
        mContentValue.put(KEY_FIRST_NAME, "George");
        mContentValue.put(KEY_LAST_NAME, "Bluth");
        mContentValue.put(KEY_IMAGE_PATH, "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
        mContentValue.put(KEY_CAROUSEL_IMAGE_ARRAY, "android.graphics.Bitmap@f1c1cd");
        db.insert(TABLE_USER, null, mContentValue);
        //verify getting Row Back
        //assertTrue(db. != -1);
          //TODO user cursors to query results
        Cursor repoCursor = db.query(
                TABLE_USER,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        // Check if Repo data returned
        if (!repoCursor.moveToFirst()) {
            fail("No Repo data returned!");
        }
        // Check if data on DB same with dummy data
        assertEquals(repoCursor.getInt(repoCursor.getColumnIndex(KEY_PAGE_ID)), 1);
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(KEY_TOTAL_PAGE_COUNT)), "4");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(KEY_DATA_ID)), "1");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(KEY_FIRST_NAME)), "George");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(KEY_LAST_NAME)), "Bluth");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(KEY_IMAGE_PATH)), "https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg");
        assertEquals(repoCursor.getString(repoCursor.getColumnIndex(KEY_CAROUSEL_IMAGE_ARRAY)), "android.graphics.Bitmap@f1c1cd");
        // Close DB connection
        repoCursor.close();
        dbHelper.close();

    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}