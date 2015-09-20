package com.formichelli.dfbugmenot;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class BugMeNotParserAsyncTask extends AsyncTask<String, Void, List<BugMeNotResult>> {
    final private Context mContext;
    final private BugMeNotAdapter bugMeNotAdapter;
    final private ProgressDialog dialog;

    public BugMeNotParserAsyncTask(Context mContext, BugMeNotAdapter bugMeNotAdapter) {
        this.mContext = mContext;
        dialog = new ProgressDialog(mContext);
        this.bugMeNotAdapter = bugMeNotAdapter;
    }

    @Override
    protected List<BugMeNotResult> doInBackground(String... url) {
        if (url == null)
            throw new NullPointerException("url cannot be null");
        if (url.length != 1)
            throw new IllegalArgumentException("url length must be 1");

        try {
            return BugMeNotParser.query(url[0]);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog.setTitle(mContext.getString(R.string.progress_dialog_title));
        dialog.setMessage(mContext.getString(R.string.progress_dialog_text));
        dialog.show();
    }

    @Override
    protected void onPostExecute(List<BugMeNotResult> bugMeNotResults) {
        super.onPostExecute(bugMeNotResults);

        dialog.dismiss();

        if (bugMeNotResults == null)
            Log.e("DFBugMeNot", "IOException");

        bugMeNotAdapter.replace(bugMeNotResults);
    }
}