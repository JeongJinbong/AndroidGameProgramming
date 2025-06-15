package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {

    public static MainScene scene;
    private final Song song;
    private float musicTime;
    float w = Metrics.width, h = Metrics.height;

    public enum Layer {
        bg, note;
        public static final int COUNT = values().length;
    }

    public MainScene(Song song) {
        initLayers(Layer.COUNT);

        this.song = song;
        Context context = GameView.view.getContext();
        add(Layer.bg, new Sprite(R.mipmap.bg, w / 2, h / 2, w, h));
        add(Layer.bg, new Sprite(R.mipmap.judgeline, w / 2, h - (50f / 2f) - 150f, w, 50f));

        song.loadNotes(context);
    }

    public float getMusicTime(){
        return musicTime;
    }

    @Override
    public void update() {
        musicTime += GameView.frameTime;
        super.update();

        float timeOffset = NoteSprite.screenfulTime();
        while(true){
            Note note = song.popNoteBefore(musicTime+timeOffset);
            if(note == null) break;
            add(Layer.note,NoteSprite.get(note));
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        scene = this;
        Context context =GameView.view.getContext();
        song.play(context);
    }

    @Override
    public void onExit() {
        song.stop();
        scene = null;
        super.onExit();
    }

    @Override
    public void onPause() {
        super.onPause();
        song.pause();
    }

    @Override
    public void onResume() {
        song.resume();
        super.onResume();
    }



}