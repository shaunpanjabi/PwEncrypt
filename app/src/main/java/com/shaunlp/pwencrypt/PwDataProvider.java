package com.shaunlp.pwencrypt;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by shaunpanjabi on 7/6/16.
 */
public class PwDataProvider extends ContentProvider{

    private SQLiteDatabase sqlDb;
    static final String DATABASE_NAME = "pwdEncryptDB";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "pwds";
    static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " description TEXT NOT NULL, " +
            " username TEXT NOT NULL, " +
            " pw TEXT NOT NULL, " +
            " source TEXT DEFAULT NULL, " +
            " notes TEXT DEFAULT NULL);";

    static final String id = "_id";
    static final String description = "description";
    static final String username = "username";
    static final String password = "pw";
    static final String source = "source";
    static final String notes = "notes";

    static final String PROVIDER_NAME = "com.shaunlp.pwencrypt.PwDataProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/pwds";
    static final Uri CONTENT_URL =  Uri.parse(URL);

    private static HashMap<String, String> values;

    static final int uriCode = 1;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "pwds", uriCode);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sqlDb = dbHelper.getWritableDatabase();

        if (sqlDb != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI" + uri);
        }

        Cursor cursor = queryBuilder.query(sqlDb, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case uriCode:
                return "vnd.android.cursor.dir/pwds";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowId = sqlDb.insert(TABLE_NAME, null, values);

        if(rowId > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        } else {
            Toast.makeText(getContext(), "Row Insert Failed",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;

        switch (uriMatcher.match(uri)){
            case uriCode:
                rowsDeleted = sqlDb.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        switch (uriMatcher.match(uri)){
            case uriCode:
                rowsUpdated = sqlDb.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

}
