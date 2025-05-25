package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class NoteSprite extends Sprite implements IRecyclable {
    protected Note note;
    private static final float WIDTH = 200f;
    private static final float HEIGHT = 100f;

    private static final float LINE_Y = Metrics.height-(50f/2f)-150f;

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

    private void init(Note note) {
        this.note = note;
        float x = note.startX;
        float y = note.time;
        setPosition(x, -y, WIDTH, HEIGHT);
    }

    @Override
    public void onRecycle() {
        note = null;
    }
    @Override
    public void update() {
        float musicTime = MainScene.scene.getMusicTime()-2.5f;
        float timeDiff = note.time - musicTime;
        float speed = 450.0f;
        float y = LINE_Y - timeDiff * speed;

        if(y> Metrics.height + HEIGHT) {
            MainScene.scene.remove(MainScene.Layer.note, this);
            return;
        }
        setPosition(note.startX, y, WIDTH, HEIGHT);
    }

}
