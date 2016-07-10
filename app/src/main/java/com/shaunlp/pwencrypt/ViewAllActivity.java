package com.shaunlp.pwencrypt;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase sqlDb =  dbHelper.getWritableDatabase();
        final Cursor cursor = sqlDb.rawQuery(
                "SELECT * FROM " + PwDataProvider.TABLE_NAME +
                " ORDER BY " + PwDataProvider.description + " COLLATE NOCASE ASC;"
                , null);


        Log.d("CURSOR", DatabaseUtils.dumpCursorToString(cursor));

        ListView lv = (ListView) findViewById(R.id.listView);

        ViewAllCursorAdapter cursorAdapter = new ViewAllCursorAdapter(this, cursor, 0);
        lv.setAdapter(cursorAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                cursor.getString(cursor.getColumnIndex(PwDataProvider.description));
            }
        });

    }
}
