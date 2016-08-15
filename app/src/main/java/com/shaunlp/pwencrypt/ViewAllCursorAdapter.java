package com.shaunlp.pwencrypt;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.security.GeneralSecurityException;

/**
 * Created by shaunpanjabi on 7/9/16.
 */
public class ViewAllCursorAdapter extends CursorAdapter {
    AesCbcWithIntegrity.SecretKeys sKey;
    String mPassword;
    String mSalt;

    public ViewAllCursorAdapter(Context context, Cursor cursor, String pw, String salt) {
        super(context, cursor, 0);
        mPassword = pw;
        mSalt = salt;

        try {
            sKey = AesCbcWithIntegrity.generateKeyFromPassword(mPassword, mSalt);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pw_data, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvDesc = (TextView) view.findViewById(R.id.view_all_description_text_view);
        TextView tvUsername = (TextView) view.findViewById(R.id.view_all_username_text_view);

        String desc = cursor.getString(cursor.getColumnIndex(PwDataProvider.description));
        String username = cursor.getString(cursor.getColumnIndex(PwDataProvider.username));


        try {
            tvDesc.setText(desc);
            Log.d("ViewAllCursorAdapter", "2");
            AesCbcWithIntegrity.CipherTextIvMac civ = new AesCbcWithIntegrity.CipherTextIvMac(username);
            Log.d("ViewAllCursorAdapter", "3");
            tvUsername.setText(AesCbcWithIntegrity.decryptString(civ, sKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
