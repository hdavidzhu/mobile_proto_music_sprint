package com.hdavidzhu.mobileprotomusicsprint;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

public class MyActivity extends Activity implements

        //super helpful tutorial:
        // https://developer.spotify.com/technologies/spotify-android-sdk/tutorial/

        PlayerNotificationCallback, ConnectionStateCallback{

    //connection statecallback is the connection to spotify?
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "2315f1ec631942d88177dcd8c0422e84";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "snaptunes://callback";

    private Player mPlayer;
    //thing that allows us to play songs
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Test","Test");

        super.onCreate(savedInstanceState);//save instance
        setContentView(R.layout.activity_my); //layout xml file
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"user-read-private", "streaming"}, null, this); //spotify authentication

        //We only want the user to grant us read private and streaming scope permissions.
        // Scopes let you specify exactly what types of data your application wants to access,
        // and the set of scopes you pass in your call determines what access permissions the user is asked to grant.
        //other options include access to private or public playlists.
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "My Company Name", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    mPlayer.addConnectionStateCallback(MyActivity.this);
                    mPlayer.addPlayerNotificationCallback(MyActivity.this);
                    mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onNewCredentials(String s) {
        Log.d("MainActivity", "User credentials blob received");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType) {
            // TODO: Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}