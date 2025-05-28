package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class LongNoteSprite extends Sprite implements IRecyclable {
    protected Note note;
    private static final float WIDTH = 220f;
    private static final float SPEED = 450.0f;
    private static final float LINE_Y = Metrics.height - (50f / 2f) - 150f;

    public static LongNoteSprite get(Note note) {
        LongNoteSprite lns = Scene.top().getRecyclable(LongNoteSprite.class);
        if (lns == null) {
            lns = new LongNoteSprite();
        }
        lns.init(note);
        return lns;
    }

    public LongNoteSprite() {
        super(R.mipmap.long_note);
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

        float startDiff = note.time - musicTime;
        float endDiff = note.endTime - musicTime;

        float y1 = LINE_Y - startDiff * SPEED;
        float y2 = LINE_Y - endDiff * SPEED;

        float topY = Math.min(y1, y2);
        float bottomY = Math.max(y1, y2);
        float height = bottomY - topY;

        if (topY > Metrics.height + height) {
            MainScene.scene.remove(MainScene.Layer.note, this);
            return;
        }

        float centerY = topY + height / 2f;
        setPosition(note.startX, centerY, WIDTH, height);
    }
}
