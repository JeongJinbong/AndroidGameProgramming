package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class NoteSprite extends Sprite implements IRecyclable {
    protected Note note;
    protected static final float WIDTH = 200f;
    protected static final float HEIGHT = 100f;
    protected static final float SPEED = 600.0f;
    protected static final float LINE_Y = 725.0f;

    private boolean judged = false;

    public NoteSprite() {
        super(R.mipmap.black_note);
        setPosition(0,0,WIDTH,HEIGHT);
    }
    public static NoteSprite get(Note note) {
        return Scene.top().getRecyclable(NoteSprite.class).init(note);
    }

    private NoteSprite init(Note note) {
        this.note = note;
        float x = note.startX;
        float y= -note.time;
        setPosition(x,y);
        //Log.d("NoteSprite", "init(), msec=" + note.msec+ " x= " + x+ " y= " +y);
        return this;
    }

    public boolean logs;

    @Override
    public void update() {
        float musicTime = MainScene.scene.getMusicTime();
        float timeDiff = note.time / 1000.0f - musicTime;
        float y = LINE_Y - timeDiff * SPEED;
        if(y > Metrics.height+HEIGHT) {
            MainScene.scene.removeNote(this);
            return;
        }

        setPosition(x,y);
        if(logs)
            Log.d("NoteSprite", "x=" + x + "y=" + y + " t=" + musicTime + "scene=" + MainScene.scene);

    }

    public float getX() {
        return dstRect.centerX();
    }

    public float getY() {
        return dstRect.centerY();
    }

    public static float screenfulTime(){
        return Metrics.height / SPEED;
    }

    public void markJudged() {
        this.judged = true;
    }

    public boolean isJudged() {
        return judged;
    }

    @Override
    public void onRecycle() {
        note = null;
        judged = false;
    }

}
