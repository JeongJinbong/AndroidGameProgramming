package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;

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
    public String note;
    public int demoStart, demoEnd;

    private static final String TAG = Song.class.getSimpleName();
    public static ArrayList<Song> songs = new ArrayList<>();
    public static AssetManager assetManager;
    protected static Handler handler = new Handler(Looper.getMainLooper());
    private MediaPlayer mediaPlayer;


    @NonNull
    @Override
    public String toString() {
        return "Song:<" + title + "/" + artist + "/" + thumbnail + "/" + media + "/" + note + ">";
    }

    public void play() {
        stop();
        try {
            AssetFileDescriptor afd = assetManager.openFd(media);
            FileDescriptor fd = afd.getFileDescriptor();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                return true;
            });

            mediaPlayer.setOnCompletionListener(mp -> stop());

            mediaPlayer.start();
            Log.d(TAG, "Play: " + this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playDemo(Context context) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(media);            FileDescriptor fd = afd.getFileDescriptor();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.seekTo(demoStart);
            mediaPlayer.start();

            mediaPlayer.setOnSeekCompleteListener(mp -> {
                mp.start();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
            });

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        if (mediaPlayer.getCurrentPosition() >= demoEnd) {
                            mediaPlayer.seekTo(demoStart);
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(this, 200);
                    }
                }
            }, 200);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            Log.d(TAG,"Stopping " + this);
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public void pause() {
        if (mediaPlayer == null)
            return;
        mediaPlayer.pause();
    }

    public void resume() {
        if (mediaPlayer == null)
            return;
        mediaPlayer.start();
    }

    public static Song get(int index) {
        return songs.get(index);
    }

}
