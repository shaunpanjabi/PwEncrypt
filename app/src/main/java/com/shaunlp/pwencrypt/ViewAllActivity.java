package com.shaunlp.pwencrypt;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ListView;

public class ViewAllActivity extends AppCompatActivity {
    public final static String descriptionExtra = "descriptionExtra";
    public final static String usernameExtra = "usernameExtra";
    public final static String pwExtra = "pwExtra";
    public final static String sourceExtra = "sourceExtra";
    public final static String notesExtra = "notesExtra";
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        mPassword = getIntent().getExtras().getString(MainActivity.pwExtra);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase sqlDb =  dbHelper.getWritableDatabase();
        final Cursor cursor = sqlDb.rawQuery(
                "SELECT * FROM " + PwDataProvider.TABLE_NAME +
                " ORDER BY " + PwDataProvider.description + " COLLATE NOCASE ASC;"
                , null);


        Log.d("CURSOR", DatabaseUtils.dumpCursorToString(cursor));

        ListView lv = (ListView) findViewById(R.id.listView);

        ViewAllCursorAdapter cursorAdapter = new ViewAllCursorAdapter(this, cursor, mPassword, 0);
        lv.setAdapter(cursorAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent detailIntent = new Intent(ViewAllActivity.this, PwDetailActivity.class);

                // Pass data
                detailIntent.putExtra(descriptionExtra, cursor.getString(cursor.getColumnIndex(PwDataProvider.description)));
                detailIntent.putExtra(usernameExtra, cursor.getString(cursor.getColumnIndex(PwDataProvider.username)));
                detailIntent.putExtra(pwExtra, cursor.getString(cursor.getColumnIndex(PwDataProvider.password)));
                detailIntent.putExtra(sourceExtra, cursor.getString(cursor.getColumnIndex(PwDataProvider.source)));
                detailIntent.putExtra(notesExtra, cursor.getString(cursor.getColumnIndex(PwDataProvider.notes)));
                startActivity(detailIntent);
            }
        });

    }
}
