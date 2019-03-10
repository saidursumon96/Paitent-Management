package com.jarvis.patientmanagement.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.HashMap;

public class SQLiteHandler_D extends SQLiteOpenHelper {

    private static final String TAG_D = SQLiteHandler_D.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "patientmanage";
    private static final String TABLE_USER_D = "userd";
    private static final String KEY_ID = "id";

    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_EXPERTISE = "expertise";
    private static final String KEY_CHAMBER = "chamber";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_BLOOD = "blood";

    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler_D(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE_D = "CREATE TABLE " + TABLE_USER_D + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_EXPERTISE + " TEXT," + KEY_CHAMBER + " TEXT,"
                + KEY_START + " TEXT," + KEY_END + " TEXT,"
                + KEY_MOBILE + " TEXT," + KEY_BLOOD + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE_D);
        Log.d(TAG_D, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_D);
        onCreate(db);
    }

    public void addUser(String name, String address, String email, String expertise,
            String chamber, String start, String end, String mobile, String blood, String uid, String created_at) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_d = new ContentValues();

        values_d.put(KEY_NAME, name);
        values_d.put(KEY_ADDRESS, address);
        values_d.put(KEY_EMAIL, email);
        values_d.put(KEY_EXPERTISE, expertise);
        values_d.put(KEY_CHAMBER, chamber);
        values_d.put(KEY_START, start);
        values_d.put(KEY_END, end);
        values_d.put(KEY_MOBILE, mobile);
        values_d.put(KEY_BLOOD, blood);
        values_d.put(KEY_UID, uid);
        values_d.put(KEY_CREATED_AT, created_at);

        long id_d = db.insert(TABLE_USER_D, null, values_d);
        db.close();
        Log.d(TAG_D, "New user inserted into sqlite: " + id_d);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user_d = new HashMap<String, String>();

        String selectQuery_d = "SELECT  * FROM " + TABLE_USER_D;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery_d, null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            user_d.put("name", cursor.getString(1));
            user_d.put("address", cursor.getString(2));
            user_d.put("email", cursor.getString(3));
            user_d.put("expertise", cursor.getString(4));
            user_d.put("chamber", cursor.getString(5));
            user_d.put("start", cursor.getString(6));
            user_d.put("end", cursor.getString(7));
            user_d.put("mobile", cursor.getString(8));
            user_d.put("blood", cursor.getString(9));
            user_d.put("uid", cursor.getString(10));
            user_d.put("created_at", cursor.getString(11));
        }
        cursor.close();
        db.close();
        Log.d(TAG_D, "Fetching user from Sqlite: " + user_d.toString());
        return user_d;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_D, null, null);
        db.close();
        Log.d(TAG_D, "Deleted all user info from sqlite");
    }
}