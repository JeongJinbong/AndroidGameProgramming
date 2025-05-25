package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class NoteSprite extends Sprite implements IRecyclable {
    protected Note note;
    private static final float WIDTH = 200f;
    private static final float HEIGHT = 100f;

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
    }
    @Override
    public void update() {
        y += 50.0f * GameView.frameTime;
        setPosition(x, y, WIDTH, HEIGHT);
    }

}
