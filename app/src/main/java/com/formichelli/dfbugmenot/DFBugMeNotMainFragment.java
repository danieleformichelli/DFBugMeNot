package com.formichelli.dfbugmenot;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DFBugMeNotMainFragment extends Fragment {
    RecyclerView mRecyclerView;
    BugMeNotAdapter bugMeNotAdapter;

    public DFBugMeNotMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dfbug_me_not_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity mActivity = getActivity();

        final EditText searchEditText = (EditText) mActivity.findViewById(R.id.search_text);
        final ImageView searchButton = (ImageView) mActivity.findViewById(R.id.search_button);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchButton.callOnClick();
                return true;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // start search
                final String url = searchEditText.getText().toString();
                if (url.isEmpty() || !url.contains(".")) {
                    searchEditText.setError(getString(R.string.invalid_url));
                    return;
                }
                new BugMeNotParserAsyncTask(getActivity(), bugMeNotAdapter).execute(url);
            }
        });

        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        bugMeNotAdapter = new BugMeNotAdapter(getActivity());
        mRecyclerView.setAdapter(bugMeNotAdapter);
    }
}
