package com.shaunlp.pwencrypt;

import android.util.Log;
import android.support.v7.widget.SearchView;

public class CustomQueryTextListener implements SearchView.OnQueryTextListener {
    private static final String LOG_TAG = "CustomQueryTextListener";

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(LOG_TAG, "onQueryTextSubmit");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(LOG_TAG, "onQueryTextChange");
        return false;
    }
}
