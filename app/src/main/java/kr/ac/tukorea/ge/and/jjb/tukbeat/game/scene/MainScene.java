package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.and.jjb.tukbeat.app.TUKBeatActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {

    public static MainScene scene;
    private float musicTime;
    private final Song song;
    float w = Metrics.width, h = Metrics.height;

    public enum Layer {
        bg, note;
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
        add(Layer.bg, new Sprite(R.mipmap.bg, w / 2, h / 2, w, h));
        add(Layer.bg, new Sprite(R.mipmap.judgeline, w / 2, h - (50f / 2f) - 150f, w, 50f));
    }

    @Override
    public void onEnter() {
        scene = this;
        super.onEnter();
        song.loadNotes();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                song.play();
            }
        }, 2500);
    }

    @Override
    public void onExit() {
        song.stop();
        super.onExit();
        scene = null;
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

    public float getMusicTime() {
        return musicTime;
    }

    @Override
    public void update() {
        super.update();
        float deltaTime = GameView.frameTime;
        musicTime += deltaTime;

        Note note = song.popNoteBefore(musicTime);
        if(note == null) return;
        add(Layer.note, NoteSprite.get(note));
    }
}