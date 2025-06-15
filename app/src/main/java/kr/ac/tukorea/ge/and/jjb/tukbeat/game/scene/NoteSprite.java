package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;

public class NoteSprite extends Sprite implements IRecyclable {
    protected Note note;
    protected static final float WIDTH = 200f;
    protected static final float HEIGHT = 100f;
    protected static final float SPEED = 450.0f;
    protected static final float LINE_Y = 725.0f;

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
        float y= note.time/10.0f;
        setPosition(x,y);
        return this;
    }

    @Override
    public void onRecycle() {
        note = null;
    }
    @Override
    public void update() {

    }

    public float getX() {
        return dstRect.centerX();
    }

    public float getY() {
        return dstRect.centerY();
    }
}
