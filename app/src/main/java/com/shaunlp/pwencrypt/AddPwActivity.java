package com.shaunlp.pwencrypt;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

public class AddPwActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pw);

        Bundle extras = getIntent().getExtras();
        final String mSKey = extras.getString(MainActivity.sKeyExtra);

        final ContentValues values = new ContentValues();
        Button saveButton = (Button) findViewById(R.id.save_button);
        final EditText descField = (EditText) findViewById(R.id.editDescriptionText);
        final EditText usernameField = (EditText) findViewById(R.id.editUsernameText);
        final EditText pwField = (EditText) findViewById(R.id.editPwText);
        final EditText sourceField = (EditText) findViewById(R.id.editSourceText);
        final EditText notesField = (EditText) findViewById(R.id.editNotesText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descTxt = descField.getText().toString();
                String usernameTxt = usernameField.getText().toString();
                String pwTxt = pwField.getText().toString();
                String sourceTxt = sourceField.getText().toString();
                String notesTxt = notesField.getText().toString();
                boolean proceed = true;
                if (TextUtils.isEmpty(descTxt)){
                    descField.setError("Description cannot be empty.");
                    proceed = false;
                }
                if (TextUtils.isEmpty(usernameTxt)){
                    usernameField.setError("Username cannot be empty.");
                    proceed = false;
                }
                if (TextUtils.isEmpty(pwTxt)){
                    pwField.setError("Password cannot be empty.");
                    proceed = false;
                }

                if (proceed) {
                    // Todo: Encrypt data before adding
                    try {
                        AesCbcWithIntegrity.SecretKeys key = AesCbcWithIntegrity.keys(mSKey);

                        values.put(PwDataProvider.description, descTxt);
                        values.put(PwDataProvider.username, AesCbcWithIntegrity.encrypt(usernameTxt, key).toString());
                        values.put(PwDataProvider.password, AesCbcWithIntegrity.encrypt(pwTxt, key).toString());
                        if (!TextUtils.isEmpty(sourceTxt)){
                            values.put(PwDataProvider.source, sourceTxt);
                        }
                        if (!TextUtils.isEmpty(notesTxt)){
                            values.put(PwDataProvider.notes, notesTxt);
                        }
                        Uri uri = getContentResolver().insert(PwDataProvider.CONTENT_URL, values);
                        Toast.makeText(getBaseContext(), "Data was successfully saved", Toast.LENGTH_SHORT).show();
                    } catch (GeneralSecurityException e) {
                        Toast.makeText(getBaseContext(), "Something went wrong ERR_1.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(getBaseContext(), "Something went wrong ERR_2.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Something went wrong ERR_3.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
