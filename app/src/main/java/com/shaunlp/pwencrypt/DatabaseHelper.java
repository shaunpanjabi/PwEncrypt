package com.shaunlp.pwencrypt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shaunpanjabi on 7/9/16.
 */
public class DatabaseHelper  extends SQLiteOpenHelper{

    DatabaseHelper(Context context) {
        super(context, PwDataProvider.DATABASE_NAME, null, PwDataProvider.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PwDataProvider.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PwDataProvider.TABLE_NAME);
        onCreate(db);
    }
}
