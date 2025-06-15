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
import java.util.Locale;

public class Song {

    public String title;
    public String artist;
    public String thumbnail;
    public String media;
    public String note;
    public int demoStart, demoEnd;
    private Runnable demoLooper;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private static final String TAG = Song.class.getSimpleName();
    public static ArrayList<Song> songs;
    protected static Handler handler = new Handler(Looper.getMainLooper());
    public static int currentSongIndex = 0;
    public ArrayList<Note> notes;
    private float noteLength;
    protected  int noteIndex; // 어느 노트까지 만들었는지 기억


    @NonNull
    @Override
    public String toString() {
        return "Song:<" + title + "/" + artist + "/" + thumbnail + "/" + media + "/" + note + ">";
    }

    private void prepareMediaPlayer(Context context)throws IOException{
        AssetFileDescriptor afd = context.getAssets().openFd(media);
        Song.mediaPlayer.reset();
        FileDescriptor fd = afd.getFileDescriptor();
        mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
        afd.close();
        Song.mediaPlayer.prepare();
    }

    public void play(Context context)
    {
        stop();
        try{
            prepareMediaPlayer(context);
            Song.mediaPlayer.start();
            Log.d(TAG, "Play:" + this);
        }catch (IOException | IllegalStateException e) {
            Log.w(TAG, "MediaPlayer play failed", e);
        }
    }

    public void playDemo(Context context) {
        stop();
        try {
            prepareMediaPlayer(context);
            Song.mediaPlayer.seekTo(demoStart);
            Song.mediaPlayer.start();

            demoLooper = new Runnable() {
                @Override
                public void run() {
                    if (Song.mediaPlayer != null && Song.mediaPlayer.isPlaying()) {
                        if (Song.mediaPlayer.getCurrentPosition() >= demoEnd) {
                            Song.mediaPlayer.seekTo(demoStart);
                        }
                        handler.postDelayed(this, 200);
                    }
                }
            };
            handler.postDelayed(demoLooper, 200);
        } catch (IOException | IllegalStateException e) {
            Log.w(TAG, "MediaPlayer playDemo failed", e);
        }
    }
    public void stop() {
            if (demoLooper != null) {
                handler.removeCallbacks(demoLooper);
                demoLooper = null;
            }

            if (mediaPlayer != null) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
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

    public ArrayList<Note> loadNotes(Context context) {
        if (notes != null && !notes.isEmpty()) return notes;

        notes = new ArrayList<>();
        noteIndex = 0;
        String filename = String.format(Locale.ENGLISH, "note/note_%02d.json", currentSongIndex);
        float length = 0;

        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(filename);
            JsonReader jr = new JsonReader(new InputStreamReader(is));
            jr.beginArray();
            while (jr.hasNext()) {
                Note note = Note.parse(jr);
                if (note != null) {
                    Log.d(TAG, "Loaded Note: " + note);
                    notes.add(note);
                    if (length < note.time) {
                        length = note.time;
                    }
                }
            }
            jr.endArray();
            jr.close();
            this.noteLength = length;
        } catch (IOException e) {
            Log.e(TAG, "Failed to load notes", e);
        }
        return notes;
    }

    public Note popNoteBefore(float musicTime){
        if(noteIndex >= notes.size()) return null;
        Note note = notes.get(noteIndex);
        if(note.time > musicTime) return null;
        Log.d(TAG, "Popping nodeIndex=" + noteIndex);
        noteIndex++;
        return note;
    }

}
