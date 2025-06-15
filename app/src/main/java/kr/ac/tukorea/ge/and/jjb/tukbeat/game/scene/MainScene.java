package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {

    public static MainScene scene;
    private final Song song;
    private float musicTime;
    float w = Metrics.width, h = Metrics.height;
    protected  Call call;

    public enum Layer {
        bg, note, explosion, ui, call;
        public static final int COUNT = values().length;
    }

    public MainScene(Song song) {
        initLayers(Layer.COUNT);

        this.song = song;
        Context context = GameView.view.getContext();
        add(Layer.bg, new Sprite(R.mipmap.bg, w / 2, h / 2, w, h));
        add(Layer.bg, new Sprite(R.mipmap.judgeline, w / 2, h - (50f / 2f) - 150f, w, 50f));

        song.loadNotes(context);
        add(Layer.call, call = new Call());

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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getActionMasked();
        int pointerCount = event.getPointerCount();
        int actionIndex = event.getActionIndex();

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            for (int i = 0; i < pointerCount; i++) {
                if (i == actionIndex &&
                        (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)) {
                    continue;
                }
                float tx = event.getX(i);
                float ty = event.getY(i);
                float[] pt = Metrics.fromScreen(tx, ty);
                judgeNoteAt(pt[0], pt[1]);  // 터치 좌표 기준 판정
            }
        }

        return true;
    }

    private void judgeNoteAt(float tx, float ty) {
        NoteSprite ns = findNearestNote(tx, ty);
        if (ns == null) return;

        float diff = ns.note.time / 1000f - musicTime;
        Call.Type type = Call.typeWithTimeDiff(diff);
        call.set(type);

        ExplodingNote ex = ExplodingNote.get(type, ns);
        add(Layer.explosion, ex);
        remove(Layer.note, ns);
    }

    private NoteSprite findNearestNote(float tx, float ty) {
        float maxDistance = 150f;
        float maxTimeDiff = 0.15f;

        NoteSprite nearest = null;
        float bestScore = Float.MAX_VALUE;

        for (IGameObject go : objectsAt(Layer.note)) {
            if (!(go instanceof NoteSprite)) continue;
            NoteSprite ns = (NoteSprite) go;

            float dx = Math.abs(ns.getX() - tx);
            float dy = Math.abs(ns.getY() - NoteSprite.LINE_Y);
            float dt = Math.abs(ns.note.time / 1000f - musicTime);

            if (dx > maxDistance || dy > maxDistance || dt > maxTimeDiff) continue;

            float score = dx + dy + dt * 1000;
            if (score < bestScore) {
                bestScore = score;
                nearest = ns;
            }
        }

        return nearest;
    }
}