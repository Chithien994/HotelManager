package com.tcn.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MyPC on 14/03/2018.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final String SQL_CREATE_TABLE_ROOM =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.Room.TABLE_NAME + " (" +
                    FeedReaderContract.Room.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.Room.COLUMN_NUMBER + " VARCHAR(10)," +
                    FeedReaderContract.Room.COLUMN_TYPE + " INTEGER," +
                    FeedReaderContract.Room.COLUMN_RESERVE + " INTEGER," +
                    FeedReaderContract.Room.COLUMN_PLAN + " INTEGER,"+
                    FeedReaderContract.Room.COLUMN_CHECK_IN_DATE + " VARCHAR(50),"+
                    FeedReaderContract.Room.COLUMN_CHECK_OUT_DATE + " VARCHAR(50),"+
                    FeedReaderContract.Room.COLUMN_TOTAL_PRICE + " VARCHAR(50),"+
                    FeedReaderContract.Room.COLUMN_ROOM_CHARGE + " VARCHAR(50),"+
                    FeedReaderContract.Room.COLUMN_MONEY_SERVICE+ " VARCHAR(50),"+
                    FeedReaderContract.Room.COLUMN_NUM_PEOPLE + " INTEGER)";

    public static final String SQL_DELETE_TABLE_ROOM =
            "DROP TABLE IF EXISTS " + FeedReaderContract.Room.TABLE_NAME;

    public static final String SQL_CREATE_TABLE_CUSTOMER =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.Customer.TABLE_NAME + " (" +
                    FeedReaderContract.Customer.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.Customer.COLUMN_TEN + " VARCHAR(50)," +
                    FeedReaderContract.Customer.COLUMN_PHONE + " VARCHAR(20)," +
                    FeedReaderContract.Customer.COLUMN_CMT + " VARCHAR(10)," +
                    FeedReaderContract.Customer.COLUMN_ID_ROOM + " INTEGER)";

    public static final String SQL_CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.Users.TABLE_NAME + " (" +
                    FeedReaderContract.Users.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedReaderContract.Users.COLUMN_USER + " TEXT," +
                    FeedReaderContract.Users.COLUMN_PASS + " TEXT," +
                    FeedReaderContract.Users.COLUMN_PƠER + " TEXT)";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public boolean queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        try {
            database.execSQL(sql);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        try {
            return database.rawQuery(sql, null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}