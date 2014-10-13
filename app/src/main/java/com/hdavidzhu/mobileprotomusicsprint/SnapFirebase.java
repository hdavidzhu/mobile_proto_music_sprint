package com.hdavidzhu.mobileprotomusicsprint;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pmc on 10/13/14.
 */
public class SnapFirebase {
    Firebase snapRef;

    public SnapFirebase(){
         this.snapRef = new Firebase("https://snaptunes.firebaseio.com/");

    }

    public void postSnap(Song song){
        Map<String, String> snapMap = new HashMap<String,String>();
        snapMap.put("uri", song.getURI());
        snapMap.put("formula", song.getFormula());

        snapRef.push().setValue(snapMap);

    }
}
