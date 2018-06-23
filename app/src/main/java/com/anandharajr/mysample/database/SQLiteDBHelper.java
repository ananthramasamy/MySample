package com.anandharajr.mysample.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anandharajr.mysample.model.Datum;
import com.anandharajr.mysample.model.Users;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static SQLiteDBHelper mInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "USERSMASTER.db";
    public static final String TABLE_USER = "users";
    public static final String KEY_PAGE_ID = "page_id";
    public static final String KEY_TOTAL_PAGE_COUNT = "total_page_count";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_DATA_ID = "data_id";
    public static final String KEY_IMAGE_PATH = "avatar_image_path";
    public static final String KEY_CAROUSEL_IMAGE_ARRAY = "avatar_image_array";

    private static final String CREATE_USERS_IMAGES_TABLE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    KEY_PAGE_ID + " TEXT," +
                    KEY_TOTAL_PAGE_COUNT + " TEXT," +
                    KEY_DATA_ID + " TEXT," +
                    KEY_FIRST_NAME + " TEXT," +
                    KEY_LAST_NAME + " TEXT," +
                    KEY_IMAGE_PATH + " TEXT," +
                    KEY_CAROUSEL_IMAGE_ARRAY + " BLOB  );";



    public SQLiteDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mInstance=SQLiteDBHelper.this;
        db.execSQL(CREATE_USERS_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    // Insert the image to the SQLite DB
    public void insertImage(Users imageObjectList) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            for (int i = 0; i < imageObjectList.getData().size(); i++) {
                ContentValues mContentValue = new ContentValues();
                mContentValue.put(KEY_PAGE_ID, imageObjectList.getPage());
                mContentValue.put(KEY_TOTAL_PAGE_COUNT, imageObjectList.getTotal_pages());
                mContentValue.put(KEY_DATA_ID, imageObjectList.getData().get(i).getId());
                mContentValue.put(KEY_FIRST_NAME, imageObjectList.getData().get(i).getFirst_name());
                mContentValue.put(KEY_LAST_NAME, imageObjectList.getData().get(i).getLast_name());
                mContentValue.put(KEY_IMAGE_PATH, imageObjectList.getData().get(i).getAvatar());
                mContentValue.put(KEY_CAROUSEL_IMAGE_ARRAY, String.valueOf(imageObjectList.getData().get(i).getBitmapAvatar()));
                db.insert(TABLE_USER, null, mContentValue);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Users getUserRecords(int pageNo) throws OperationApplicationException {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + KEY_PAGE_ID + "='" + pageNo + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Datum> datumList = new ArrayList<>();
        Users UsersModel = new Users();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                UsersModel.setPage(Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_PAGE_ID))));
                UsersModel.setTotal_pages(Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_PAGE_COUNT))));
                Datum datum = new Datum();
                datum.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_DATA_ID))));
                datum.setFirst_name(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                datum.setLast_name(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                datum.setAvatar(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
                Bitmap mBitMap = getPhoto(cursor.getBlob(cursor.getColumnIndex(KEY_CAROUSEL_IMAGE_ARRAY)));
                datum.setBitmapAvatar(mBitMap);
                datumList.add(datum);
                UsersModel.setData(datumList);
                cursor.moveToNext();
            }
        } else {
            throw new OperationApplicationException("Internet is required for first run");
        }
        cursor.close();
        db.close();
        return UsersModel;
    }

    private static Bitmap getPhoto(byte[] image) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (OutOfMemoryError e) {
            bitmap = null;
            e.printStackTrace();
        }
        return bitmap;
    }

    public void deleteRecords() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(CREATE_USERS_IMAGES_TABLE);

    }

    public static SQLiteDBHelper getInstance(Context ctx){
        if(mInstance==null){
            mInstance = new SQLiteDBHelper(ctx);
        }
        return mInstance;
    }

}
