package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import androidx.annotation.NonNull;

public class Song {

    public String title;
    public String artist;
    //public String thumbnail;
    public int demoStart, demoEnd;

    @NonNull
    @Override
    public String toString(){
        return "<" + title + ">" + artist + "/" + artist;
    }

}
