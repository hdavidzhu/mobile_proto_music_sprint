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
        Log.d("hey", "i got hit");
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) { //if this is new
            Log.d("uri", "boop");
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri); //wants to make sure that the credentials are correct
            Spotify spotify = new Spotify(response.getAccessToken()); //create a new spotify object
            Log.d("wop", "alksjdf;aslkjfd");
            mPlayer = spotify.getPlayer(this, "My Company Name", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    Log.d("hey", "imalive");
                    mPlayer.addConnectionStateCallback(MyActivity.this); //start a connection
                    mPlayer.addPlayerNotificationCallback(MyActivity.this); //initalize a player and its callback
                    mPlayer.play("spotify:track:7d5rvnXSMjYmzYruuUrMNS"); //start  playing music
                    Log.d("boop", "things are happening");
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());//if something goes wrong. Might wanna put a chance to
                    //log in again here
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
        //gets player status. Player state class tells us things like duration of song, if song active, etc
        //https://developer.spotify.com/android-sdk-docs/
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this); //This is super important because multiple fragments can access the player,
        //but there can only be one.If this is not called when with the player, then our app will leak resources.
        super.onDestroy();
    }
}