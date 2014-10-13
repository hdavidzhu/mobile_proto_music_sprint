package com.hdavidzhu.mobileprotomusicsprint;

public class Song {

    private long id;
    private String title;
    private String artist;
    private String uri;
    private String formula;


    public Song(long songID, String songTitle, String songArtist, String songUri, String songFormula) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        uri = songUri;
        formula = songFormula;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}

    public String getURI(){return uri;}
    public String getFormula(){return formula;}

}