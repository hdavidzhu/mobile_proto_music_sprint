package com.hdavidzhu.mobileprotomusicsprint;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import java.io.*;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    SoundPool soundPool;
    public int streamId = 0;
    public int soundId = -1;
    public float pitch = 01f;


    private static final int NOTIFY_ID = 1;
    private final IBinder musicBind = new MusicBinder();
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private String songTitle;
    private boolean shuffle = false;
    private Random rand;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize position
        songPosn = 0;

        // Create player
        player = new MediaPlayer();

        rand = new Random();

        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public void setList(ArrayList<Song> theSongs) {
        songs = theSongs;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    //skip to next
    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }

    public void playSong() {
        player.reset();

        //get song
        Song playSong = songs.get(songPosn);

        //print song information
        Log.d("Playing song", songs.get(songPosn).getURI());

        songTitle = playSong.getTitle();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                currSong);
        Log.d("TAG", trackUri.toString());
        //soundId = soundPool.load(trackUri, 1);


//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
//                streamId = soundPool.play(soundId, 1, 1, 1, 3, pitch);
//                soundPool.setLoop(streamId, -1);
//                Log.e("TAG", String.valueOf(streamId));
//            }
//        });

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }


        final CountDownTimer cntr_aCounter = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                player.start();
            }

            public void onFinish() {
                //code fire after finish
                player.stop();

                //String filePath = Environment.getExternalStorageDirectory()+"\\Internal storage\\Music\\SingleLadies.mp3";
                //"/yourfolderNAme/yopurfile.mp3";
                player = new  MediaPlayer();
                player.setDataSource("/mnt/sdcard/Internal storage/Music/SingleLadies.mp3");
                player.prepare();
                player.start();

            }
        };
        cntr_aCounter.start();
        Log.d("SONG TAG", player.getTrackInfo().toString());
        Log.d("TIME TAG", cntr_aCounter.toString());
        player.prepareAsync();
    }

    public Song getCurrentSong() {
        return songs.get(songPosn);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        //start playback
        player.start();

        Intent notIntent = new Intent(this, MyActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    public void playPrev() {
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;
        playSong();
    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

}