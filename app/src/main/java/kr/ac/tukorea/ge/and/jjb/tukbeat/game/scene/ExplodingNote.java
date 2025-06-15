package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class ExplodingNote extends AnimSprite implements IRecyclable {

    protected static final float WIDTH = 200f;
    protected static final float HEIGHT = 100f;

    private float x, y;
    private float elapsedTime = 0f;

    public ExplodingNote() {
        super(R.mipmap.exploding_note, 10, 15); // 10줄 15칸짜리 애니메이션 시트
    }

    public static ExplodingNote get(Call.Type type, NoteSprite origin) {
        ExplodingNote ex = Scene.top().getRecyclable(ExplodingNote.class);
        return ex.init(origin.getX(), origin.getY());
    }

    private ExplodingNote init(float x, float y) {
        this.x = x;
        this.y = y;
        this.elapsedTime = 0f;
        setPosition(x,y,WIDTH,HEIGHT);
        return this;
    }

    @Override
    public void update() {
        super.update();

        elapsedTime += GameView.frameTime;
        if (elapsedTime > 0.25f) { // 0.25초 후 사라짐
            MainScene.scene.remove(MainScene.Layer.explosion, this);
        }
    }

    @Override
    public void onRecycle() {
    }
}


