package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class SlideNoteSprite extends Sprite implements IRecyclable {
    protected Note note;
    protected int index;
    protected float appearTime;
    protected boolean judged = false;

    private static final float WIDTH = 200f;
    private static final float HEIGHT = 100f;
    private static final float SPEED = 600.0f;
    private static final float LINE_Y = 725.0f;

    public SlideNoteSprite() {
        super(R.mipmap.slide_note);
        setPosition(0, 0, WIDTH, HEIGHT);
    }

    public static SlideNoteSprite get(Note note, int index) {
        return (SlideNoteSprite) Scene.top().getRecyclable(SlideNoteSprite.class).init(note, index);
    }

    private SlideNoteSprite init(Note note, int index) {
        this.note = note;
        this.index = index;
        this.judged = false;

        float start = note.time / 1000f;
        float end = note.endTime / 1000f;
        float ratio = (float) index / (note.pathX.length - 1);
        this.appearTime = start + (end - start) * ratio;

        float x = note.pathX[index];
        float y = LINE_Y - (appearTime - MainScene.scene.getMusicTime()) * SPEED;
        setPosition(x, y, WIDTH, HEIGHT);
        return this;
    }

    @Override
    public void update() {
        float musicTime = MainScene.scene.getMusicTime();
        float y = LINE_Y - (appearTime - musicTime) * SPEED;
        if (y > Metrics.height + HEIGHT) {
            if (!judged) MainScene.scene.removeNote(this);
            return;
        }
        setPosition(note.pathX[index], y);
    }

    @Override
    public void onRecycle() {
        note = null;
        judged = false;
        appearTime = 0f;
        index = 0;
    }

    public float getX() { return dstRect.centerX(); }
    public float getY() { return dstRect.centerY(); }
    public float getAppearTime() { return appearTime; }
    public boolean isJudged() { return judged; }
    public void markJudged() { this.judged = true; }
}
