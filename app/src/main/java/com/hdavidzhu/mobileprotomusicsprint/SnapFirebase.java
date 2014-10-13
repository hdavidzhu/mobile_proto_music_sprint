package com.hdavidzhu.mobileprotomusicsprint;

import com.firebase.client.Firebase;

/**
 * Created by pmc on 10/13/14.
 */
public class SnapFirebase {
    Firebase snapRef;

    public SnapFirebase {
         this.snapRef = new Firebase("https://snaptunes.firebaseio.com/");

    }

    public void postSnap(Song song){

    }
}
