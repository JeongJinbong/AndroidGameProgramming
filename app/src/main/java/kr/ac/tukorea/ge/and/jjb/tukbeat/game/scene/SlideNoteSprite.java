package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class SlideNoteSprite extends Sprite implements IRecyclable {
    private static final float WIDTH = 100f;
    private static final float SPEED = 450f;
    private static final float LINE_Y = Metrics.height - (50f / 2f) - 150f;
    private Note note;
    private int index;

    public static SlideNoteSprite get(Note note, int index) {
        SlideNoteSprite sns = Scene.top().getRecyclable(SlideNoteSprite.class);
        if (sns == null) sns = new SlideNoteSprite();
        sns.init(note,index);
        return  sns;
    }

    public SlideNoteSprite() {
        super(R.mipmap.slide_note);
    }

    private void init(Note note, int index) {
        this.note = note;
        this.index = index;
        update();
    }

    @Override
    public void onRecycle(){
        note = null;
    }

}
