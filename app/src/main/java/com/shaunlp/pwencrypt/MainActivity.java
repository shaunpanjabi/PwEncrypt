package com.shaunlp.pwencrypt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.support.design.widget.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private View promptView;
    private String mPassword;
    private EditText userInput;
    TextView pwDialog;
    private CheckBox showPwCheckBox;
    private LayoutInflater inflater;

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    private final String pwHash = "pwHash";
    public static final String sharedPrefs = "PwEncryptSP";
    public static final String changePw = "changePw";
    private final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inflater = LayoutInflater.from(this);

        // get shared prefs
        sp = getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE);
        spEditor = sp.edit();

        if (!sp.contains(changePw)) {
            // Add shared pref if not there and set to false
            spEditor.putBoolean(changePw, true);
            spEditor.commit();
        }

        Boolean pwFlag = sp.getBoolean(changePw, true);

        displayPwPrompt(pwFlag);
        addCheckBoxListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPwIntent = new Intent(MainActivity.this, AddPwActivity.class);
                startActivity(addPwIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        MenuItem searchItem = menu.findItem(R.id.action_spinner);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new CustomQueryTextListener());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_all_pwds_settings:
                startActivity(new Intent(MainActivity.this, ViewAllActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // Todo: probaly should make a class for this
    protected AlertDialog displayPwPrompt(final boolean newPwFlag) {
        promptView = inflater.inflate(R.layout.prompts, null);
        pwDialog = (TextView) promptView.findViewById(R.id.pw_dialog_message);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        userInput = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);
        userInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        if (newPwFlag) {
            pwDialog.setText("First run, please enter in a new password.");
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Empty because on click is overriden to avoid dialog from dismissing
                            }
                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword = computeSHA1(userInput.getText().toString());

                if (newPwFlag) {
                    // write new pw to shared prefs
                    spEditor.putString(pwHash, mPassword);
                    spEditor.putBoolean(changePw, false);
                    spEditor.commit();
                    Toast.makeText(MainActivity.this, R.string.pw_set_msg, Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                } else {
                    // check if pw matches shared prefs
                    String spPwHash = sp.getString(pwHash, null);
                    if (spPwHash.equals(mPassword)) {
                        Toast.makeText(MainActivity.this, R.string.pw_match, Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    } else {

                        pwDialog.setText(R.string.invalid_pw);
                        Toast.makeText(MainActivity.this, R.string.invalid_pw, Toast.LENGTH_SHORT).show();
                    }
                }
                TextView tv = (TextView) findViewById(R.id.main_text_view);
                tv.setText(mPassword);
            }
        });

        return alertDialog;
    }

    protected void addCheckBoxListener() {
        showPwCheckBox = (CheckBox) promptView.findViewById(R.id.checkBox);
        showPwCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    userInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    userInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }


    public String computeSHA1(String pw) {
        MessageDigest mdSha1 = null;
        String sha1String = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.d(LOG_TAG, "Error initializing SHA-1 message digest.");
        }
        try {
            mdSha1.update(pw.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            sha1String = convertToHex(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sha1String;
    }

    private static String convertToHex(byte[] data) throws IOException {
        StringBuffer sb = new StringBuffer();
        String hex;
        hex = Base64.encodeToString(data, 0, data.length, 0).trim();
        sb.append(hex);
        return sb.toString();
    }
}
