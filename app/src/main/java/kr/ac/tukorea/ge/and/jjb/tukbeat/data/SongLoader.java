package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.JsonHelper;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;

public class SongLoader {
    private static final String TAG = SongLoader.class.getSimpleName();
    private final AssetManager assets;

    public SongLoader(Context context) {
        assets = context.getAssets();
    }

    @NonNull
    public ArrayList<Song> loadSongs() {
        ArrayList<Song> songs = new ArrayList<Song>();
        try {
            InputStream is = assets.open("song.json");
            InputStreamReader isr = new InputStreamReader(is);
            JsonReader jr = new JsonReader(isr);
            jr.beginArray();
            while (jr.hasNext()) {
                Song song = loadSong(jr);
                if (song != null) {
                    Log.d(TAG, "Loaded Song: " + song);
                    songs.add(song);
                }
            }
            jr.endArray();
            jr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs;
    }

    private Song loadSong(JsonReader jr) throws IOException {
        Song song = new Song();
        jr.beginObject();
        while (jr.hasNext()) {
            String name = jr.nextName();
            if (!JsonHelper.readProperty(song, name, jr)) {
                jr.skipValue();
            }
        }
        jr.endObject();
        return song;
    }
}
