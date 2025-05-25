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

import kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene.NoteSprite;

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

    protected int noteIndex;

    public  ArrayList<Note> notes;
    private float noteLength;

    public static ArrayList<Song> load(Context context, String filename) {
        songs = new ArrayList<>();
        try {
            assetManager = context.getAssets();
            InputStream is = assetManager.open(filename);
            JsonReader jr = new JsonReader(new InputStreamReader(is));
            jr.beginArray();
            while (jr.hasNext()) {
                Song song = loadSong(jr);
                if (song != null) {
                    songs.add(song);
                    Log.d(TAG, "Songs count =" + songs.size() + " " + song);
                }
            }
            jr.endArray();
            jr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs;
    }

    private static Song loadSong(JsonReader jr) {
        Song song = new Song();
        try {
            jr.beginObject();
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (!JsonHelper.readProperty(song, name, jr)) {
                    jr.skipValue();
                }
            }
            jr.endObject();
        } catch (IOException e) {
            return null;
        }
        return song;
    }

    public void loadNotes() {
        if (notes != null && !notes.isEmpty()) return;

        notes = new ArrayList<>();
        if (note == null) return;

        float length = 0;
        try {
            InputStream is = assetManager.open(note);
            JsonReader jr = new JsonReader(new InputStreamReader(is));
            jr.beginArray();
            while (jr.hasNext()) {
                Note note = Note.parse(jr);
                if (note == null) continue;
                notes.add(note);
                if (length < note.time) {
                    length = note.time;
                }
            }
            jr.endArray();
            is.close();
            this.noteLength = length;
            Log.d(TAG, "Song loaded: " + notes.size() + " notes, " + noteLength + " ms.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void unload() {
        songs.clear();
        assetManager = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Song:<" + title + "/" + artist + "/" + thumbnail + "/" + media + "/" + note + ">";
    }

    public void play() {
        loadNotes();
        noteIndex=0;
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

    public void playDemo() {
        stop();
        try {
            AssetFileDescriptor afd = assetManager.openFd(media);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.seekTo(demoStart);

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error in demo: what=" + what + ", extra=" + extra);
                return true;
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                if (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.getCurrentPosition() >= demoEnd) {
                            mediaPlayer.seekTo(demoStart);
                            mediaPlayer.start();
                        }
                    } catch (IllegalStateException e) {
                    }
                }
            });

            mediaPlayer.start();
            Log.d(TAG, "Playing demo in loop: " + title + "/" + artist);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
            }
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "Stopped " + title + "/" + artist);
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

    public Note popNoteBefore(float MusicTime)
    {
        if(noteIndex >= notes.size()) return null;
        Note note = notes.get(noteIndex);
        if(note.time-NoteSprite.screenfulTime() > MusicTime) return null;
        Log.d(TAG,"Popping nodeIndex=" + noteIndex);
        noteIndex++;
        return note;
    }
}
