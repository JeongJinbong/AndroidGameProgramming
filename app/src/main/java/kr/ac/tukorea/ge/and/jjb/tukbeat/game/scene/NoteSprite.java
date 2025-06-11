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
    protected static final float SPEED = 450.0f;
    protected static final float LINE_Y = Metrics.height-(50f/2f)-150f;

    public static NoteSprite get(Note note) {
        NoteSprite ns = Scene.top().getRecyclable(NoteSprite.class);
        if (ns == null)
            ns = new NoteSprite();
        ns.init(note);
        return ns;
    }

    public NoteSprite() {
        super(R.mipmap.black_note);
    }

    public static float screenfulTime() {
        float distance = LINE_Y +HEIGHT / 2f;
        return distance  / SPEED;
    }

    private void init(Note note) {
        this.note = note;
        update();
    }

    @Override
    public void onRecycle() {
        note = null;
    }
    @Override
    public void update() {
        float musicTime = MainScene.scene.getMusicTime();
        float timeDiff = note.time - musicTime;
        float y = LINE_Y - timeDiff * SPEED;

        if(y> Metrics.height + HEIGHT) {
            MainScene.scene.remove(MainScene.Layer.note, this);
            return;
        }
        setPosition(note.startX, y, WIDTH, HEIGHT);
    }

    public float getX() {
        return dstRect.centerX();
    }

    public float getY() {
        return dstRect.centerY();
    }
}
