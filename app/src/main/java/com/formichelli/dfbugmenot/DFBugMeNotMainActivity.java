package com.formichelli.dfbugmenot;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class DFBugMeNotMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfbug_me_not_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.df_bugmenot_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.info_title)
                        .setMessage(R.string.info_msg)
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
