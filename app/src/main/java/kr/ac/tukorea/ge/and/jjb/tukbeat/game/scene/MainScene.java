package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.os.Bundle;
import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.and.jjb.tukbeat.app.TUKBeatActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {
    private final Song song;
    float w = Metrics.width, h= Metrics.height;
    public enum Layer {
        bg,note;
        public static final int COUNT = values().length;
    }

    public MainScene() {
        initLayers(Layer.COUNT);
        int songIndex = 0;
        Bundle extras = GameActivity.activity.getIntent().getExtras();
        if (extras != null) {
            songIndex = extras.getInt(TUKBeatActivity.SONG_INDEX, 0);
        }
        song = Song.get(songIndex);


        add(Layer.bg, new Sprite(R.mipmap.bg,w/2,h/2,w,h));
        add(Layer.bg, new Sprite(R.mipmap.judgeline,w/2,h-(50f/2f)-150f,w,50f));
    }

    @Override
    public void onEnter() {
        super.onEnter();
        song.loadNotes();
        Note dummyNote = new Note();
        dummyNote.time = 0;
        dummyNote.startX = 0;
        add(Layer.note, new NoteSprite() {
            {
                setPosition(450, 800, 200, 100);
                Log.d("NoteSprite", "Manually added at (450, 800)");
            }
        });
       // for (Note note: song.notes) {
       //     add(Layer.note, NoteSprite.get(note));
       // }
        song.play();
    }

    @Override
    public void onExit() {
        song.stop();
        super.onExit();
    }

    @Override
    public void onPause() {
        song.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        song.resume();
        super.onResume();
    }

}