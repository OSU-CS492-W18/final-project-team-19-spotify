package com.example.spotifyplaylistapplication2;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Noda Dragon on 3/23/2018.
 */

public class SearchLoader extends AsyncTaskLoader<String> {

    String mSearchResultsJSON;
    String mSearchQuery;

    private final static String TAG = SearchLoader.class.getSimpleName();

    public SearchLoader(Context context, String url) {
        super(context);
        mSearchQuery = url;
    }

    @Override
    public String loadInBackground() {
        if (mSearchQuery != null) {
            Log.d(TAG, "loading results from Spotify with search: " + mSearchQuery);
            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHTTPGet(mSearchQuery);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        } else {
            return null;
        }
    }

    @Override
    protected void onStartLoading() {
        if (mSearchQuery != null) {
            if (mSearchResultsJSON != null) {
                Log.d(TAG, "loader returning cached results");
                deliverResult(mSearchResultsJSON);
            } else {
                forceLoad();
            }
        }
    }

    @Override
    public void deliverResult(String data) {
        mSearchResultsJSON = data;
        super.deliverResult(data);
    }
}
