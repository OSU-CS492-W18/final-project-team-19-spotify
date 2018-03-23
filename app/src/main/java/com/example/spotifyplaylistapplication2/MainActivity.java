package com.example.spotifyplaylistapplication2;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.spotify.sdk.android.player.Spotify;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class MainActivity extends AppCompatActivity implements Search.View, LoaderManager.LoaderCallbacks<String> {

    private final static String TAG = MainActivity.class.getSimpleName();

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private Search.ActionListener mActionListener;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private ScrollListener mScrollListener = new ScrollListener(mLayoutManager);
    private SearchResultsAdapter mAdapter;

    private final static int LOADER_ID = 0;

    private final static String SEARCH_URL_KEY = "SearchQuery";


    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_undo);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_like);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_skip);
                    return true;
            }
            return false;
        }
    };

    final SearchView searchView = ( SearchView ) findViewById(R.id.search_view);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mTextMessage = ( TextView ) findViewById(R.id.message);
        BottomNavigationView navigation = ( BottomNavigationView ) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        String token = intent.getStringExtra(EXTRA_TOKEN);

        mActionListener = new SearchPresenter(this, this);
        mActionListener.init(token);

        // Setup search field

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActionListener.search(query);
                searchView.clearFocus();
                Bundle args = new Bundle();
                args.putString(SEARCH_URL_KEY, query);
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Setup search results list
        mAdapter = new SearchResultsAdapter(this, new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Track item) {
                mActionListener.selectTrack(item);
            }
        });

        RecyclerView resultsList = ( RecyclerView ) findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        // If Activity was recreated wit active search restore it
        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY);
            mActionListener.search(currentQuery);
        }


    }

    public void doSearch(String data){
        mActionListener.search(data);
        searchView.clearFocus();
        Bundle args = new Bundle();
        args.putString(SEARCH_URL_KEY, data);
        getSupportLoaderManager().restartLoader(LOADER_ID, args, this);

        RecyclerView resultsList = ( RecyclerView ) findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        mActionListener.search(data);
    }

    @Override
    public void reset() {
        mScrollListener.reset();
        mAdapter.clearData();
    }

    @Override
    public void addData(List<Track> items) {
        mAdapter.addData(items);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionListener.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActionListener.getCurrentQuery() != null) {
            outState.putString(KEY_CURRENT_QUERY, mActionListener.getCurrentQuery());
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        mActionListener.destroy();
        super.onDestroy();
    }

    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, Bundle args) {
        String searchQuery = null;
        if (args != null) {
            searchQuery = args.getString(SEARCH_URL_KEY);
        }
        return new SearchLoader(this, searchQuery);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        Log.d(TAG, "loader finished loading");
        if (data != null) {
            doSearch(data);
        }
        else{

        }
    }
    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {
        // Nothing to do...
    }
}