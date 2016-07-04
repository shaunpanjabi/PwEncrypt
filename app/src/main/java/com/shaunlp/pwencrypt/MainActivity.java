package com.shaunlp.pwencrypt;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private String mPassword;
    private LayoutInflater inflater;
    private CheckBox showPwCheckBox;
    private EditText userInput;
    private View promptView;
    private final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inflater = LayoutInflater.from(this);

        AlertDialog alertDialog = pwPromptBuilder();
        alertDialog.show();

        addCheckBoxListener();

    }

    protected AlertDialog pwPromptBuilder() {
        promptView = inflater.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        userInput = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);
        userInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPassword = computeSHA1(userInput.getText().toString());
                                TextView tv = (TextView) findViewById(R.id.main_text_view);
                                tv.setText(mPassword);
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
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
        hex = Base64.encodeToString(data, 0, data.length, 0);
        sb.append(hex);
        return sb.toString();
    }
}
