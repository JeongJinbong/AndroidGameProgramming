package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
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
    private boolean isPlaying =false;
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
                isPlaying= true;
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
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean handled = super.onTouchEvent(event);
        if(handled) return true;

        float [] pt = Metrics.fromScreen(event.getX(),event.getY());
        float x= pt[0], y = pt[1];

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return handleTouchDown(x,y);
            case MotionEvent.ACTION_UP:
                return handleTouchUp(x,y);
        }
        return false;
    }

    private boolean handleTouchUp(float x, float y) {
        ArrayList<IGameObject> notes = objectsAt(Layer.note);
        for (IGameObject go : notes) {
            if (!(go instanceof LongNoteSprite)) continue;
            LongNoteSprite lns = (LongNoteSprite) go;
            if (!lns.note.isHolding) continue;

            float timeDiff = lns.note.endTime - musicTime;
            if (Math.abs(timeDiff) < 0.15f) {
                lns.note.isReleased = true;
                lns.note.isHolding = false;
                remove(Layer.note, lns);
                return true;
            } else {
                lns.note.isHolding = false;
                lns.note.isReleased = false;
                remove(Layer.note, lns);
                return true;
            }
        }
        return false;
    }
    private boolean handleTouchDown(float x, float y) {
        Sprite noteSprite = findNearestNote(x, y);
        if (noteSprite == null) return false;

        if (noteSprite instanceof NoteSprite) {
            NoteSprite ns = (NoteSprite) noteSprite;
            float timeDiff = ns.note.time - musicTime;
            Call.Type type = Call.typeWithTimeDiff(timeDiff);
            remove(Layer.note, ns);
            return true;
        }

        if (noteSprite instanceof LongNoteSprite) {
            LongNoteSprite lns = (LongNoteSprite) noteSprite;
            float timeDiff = lns.note.time - musicTime;
            if (Math.abs(timeDiff) < 0.15f) {
                lns.note.isHolding = true;
                lns.note.isJudged = true;
                return true;
            }
        }
        return false;
    }



    private Sprite findNearestNote(float x, float y) {
        float maxDistance = 100f;
        float timeThreshold = 0.3f;

        Sprite nearest = null;
        float bestScore = Float.MAX_VALUE;

        ArrayList<IGameObject> notes = objectsAt(Layer.note);
        for (IGameObject go : notes) {
            if (!(go instanceof Sprite)) continue;
            Sprite s = (Sprite) go;

            float dx = x - s.getX();
            float dy = y - s.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            float noteTime = 0f;
            if (s instanceof NoteSprite) {
                noteTime = ((NoteSprite) s).note.time;
            } else if (s instanceof LongNoteSprite) {
                noteTime = ((LongNoteSprite) s).note.time;
            } else continue;

            float timeDiff = Math.abs(noteTime - musicTime);

            if (distance < maxDistance && timeDiff < timeThreshold) {
                float score = distance + timeDiff * 100;
                if (score < bestScore) {
                    bestScore = score;
                    nearest = s;
                }
            }
        }

        return nearest;
    }


    @Override
    public void update() {
        super.update();
        if(!isPlaying) return;

        float deltaTime = GameView.frameTime;
        musicTime += deltaTime;

        Note note = song.popNoteBefore(musicTime);
        if(note != null) {

            switch(note.type){
                case TAP:
                add(Layer.note, NoteSprite.get(note));
                break;
                case HOLD:
                    add(Layer.note,LongNoteSprite.get(note));
                    break;
            }
        }
    }
}