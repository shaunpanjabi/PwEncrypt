package com.shaunlp.pwencrypt;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class PwDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_detail);

        Bundle extras = getIntent().getExtras();
        String descriptionExtra = extras.getString(ViewAllActivity.descriptionExtra);
        String usernameExtra = extras.getString(ViewAllActivity.usernameExtra);
        String pwExtra = extras.getString(ViewAllActivity.pwExtra);
        String sourceExtra = extras.getString(ViewAllActivity.sourceExtra);
        String notesExtra = extras.getString(ViewAllActivity.notesExtra);

        EditText description = (EditText) findViewById(R.id.detail_description);
        EditText usr = (EditText) findViewById(R.id.detail_username);
        EditText pw = (EditText) findViewById(R.id.detail_pw);
        EditText source = (EditText) findViewById(R.id.detail_source);
        EditText notes = (EditText) findViewById(R.id.detail_notes);

        description.setText(descriptionExtra);
        usr.setText(usernameExtra);
        pw.setText(pwExtra);
        source.setText(sourceExtra);
        notes.setText(notesExtra);

    }
}
