package com.hdavidzhu.mobileprotomusicsprint;

import com.firebase.client.Firebase;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class SnapFirebase {
    Firebase snapRef;
    String username;

    public SnapFirebase() {
        this.snapRef = new Firebase("https://snaptunes.firebaseio.com/");
    }

    public void postUser(String username) {
        this.username = username;
        snapRef.child(username).setValue("");
    }
    public void postSnap(Song song, String formula, String sendUser) {
        Map<String, String> snapMap = new HashMap<String, String>();
        snapMap.put("id", String.valueOf(song.getID()));
        snapMap.put("title", song.getTitle());
        snapMap.put("artist", song.getArtist());

        snapMap.put("uri", song.getURI());
        snapMap.put("formula", formula);

        snapRef.child(sendUser).setValue(snapMap);
//        snapRef.push().setValue(snapMap);

    }
}