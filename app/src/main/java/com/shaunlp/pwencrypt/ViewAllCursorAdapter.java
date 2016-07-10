package com.shaunlp.pwencrypt;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by shaunpanjabi on 7/9/16.
 */
public class ViewAllCursorAdapter extends CursorAdapter {

    public ViewAllCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
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

        tvDesc.setText(desc);
        tvUsername.setText(username);
    }
}
