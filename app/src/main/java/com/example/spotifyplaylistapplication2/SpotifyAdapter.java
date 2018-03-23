package com.example.spotifyplaylistapplication2;

import android.util.Log;

import com.example.spotifyplaylistapplication2.models.Album;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by nabz7 on 3/22/2018.
 */

public class SpotifyAdapter {
    SpotifyApi api = new SpotifyApi();

// Most (but not all) of the Spotify Web API endpoints require authorisation.
// If you know you'll only use the ones that don't require authorisation you can skip this step
api.setAccessToken("myAccessToken");

    SpotifyService spotify = api.getService();

spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
        @Override
        public void success(Album album, Response response) {
            Log.d("Album success", album.name);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d("Album failure", error.toString());
        }
    });



