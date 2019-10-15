package com.formichelli.dfbugmenot;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
        final TextView noResults = (TextView) mActivity.findViewById(R.id.no_results);
        final FloatingActionButton addFab = (FloatingActionButton) mActivity.findViewById(R.id.add);

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

                // hide noresults string and add FAB
                addFab.setVisibility(View.GONE);
                noResults.setVisibility(View.GONE);

                // start search
                final String url = searchEditText.getText().toString();
                if (url.isEmpty() || !url.contains(".")) {
                    searchEditText.setError(getString(R.string.invalid_url));
                    return;
                }
                new BugMeNotParserAsyncTask(getActivity(), bugMeNotAdapter) {
                    @Override
                    protected void onPostExecute(List<BugMeNotResult> bugMeNotResults) {
                        super.onPostExecute(bugMeNotResults);

                        if (bugMeNotResults == null) {
                            noResults.setText(getString(R.string.barred, url));
                            noResults.setVisibility(View.VISIBLE);
                        } else if (bugMeNotResults.size() == 0) {
                            noResults.setText(getString(R.string.no_results));
                            noResults.setVisibility(View.VISIBLE);
                            addFab.setVisibility(View.VISIBLE);
                        } else
                            addFab.setVisibility(View.VISIBLE);

                    }
                }.execute(url);
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://bugmenot.com/submit.php?seed=" + searchEditText.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Bundle extras = new Bundle();
                extras.putBinder("android.support.customtabs.extra.SESSION", null);
                i.putExtras(extras);
                i.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", R.color.action_bar);
                startActivity(i);

                // Snackbar.make(v, getActivity().getString(R.string.add_not_implemented), Snackbar.LENGTH_LONG).show();
            }
        });

        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        bugMeNotAdapter = new BugMeNotAdapter(getActivity());
        mRecyclerView.setAdapter(bugMeNotAdapter);
    }
}
