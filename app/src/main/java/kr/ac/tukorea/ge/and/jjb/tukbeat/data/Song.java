package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Log;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Song {

    public String title;
    public String artist;
    public String thumbnail;
    public String media;

    private static final String TAG = Song.class.getSimpleName();
    public static ArrayList<Song> songs = new ArrayList<>();
    public static AssetManager assetManager;
    private  MediaPlayer mediaPlayer;

    public static ArrayList<Song> load(Context context, String filename)
    {
        songs = new ArrayList<>();
        try{
            assetManager = context.getAssets();
            InputStream is = assetManager.open(filename);
            JsonReader jr = new JsonReader(new InputStreamReader(is));
            jr.beginArray();
            while(jr.hasNext()){
                Song song = loadSong(jr);
                if(song != null){
                    songs.add(song);
                    Log.d(TAG,"Songs count =" + songs.size() + " " + song);
                }
            }
            jr.endArray();
            jr.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return songs;

    }

    private static Song loadSong(JsonReader jr) {
        Song song = new Song();
        try{
            jr.beginObject();
            while(jr.hasNext()) {
                String name = jr.nextName();
                if (!JsonHelper.readProperty(song, name, jr)) {
                    jr.skipValue();
                }
            }
            jr.endObject();
        }catch (IOException e){
            return null;
        }
        return song;
    }

    public static void unload() {
        songs.clear();
        assetManager = null;
    }

    @NonNull
    @Override
    public String toString(){
        return "Song:<" + title + "/" + artist + "/" + thumbnail + "/" + media + ">";
    }

    public void playDemo() {
        try{
            AssetFileDescriptor afd = assetManager.openFd(media);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d(TAG,"Playing" + title + "/" + artist);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if(mediaPlayer != null)
        {
            Log.d(TAG, "Stopping" + title + "/" artist);
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}
