package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.os.Bundle;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.VertScrollBackground;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.and.jjb.tukbeat.app.TUKBeatActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene{
    public enum Layer{
        bg;
        public static final int COUNT = values().length;
    }
    public MainScene()
    {
        initLayers(Layer.COUNT);
        int songIndex = 0;
        Bundle extras = GameActivity.activity.getIntent().getExtras();
        if (extras != null) {
            songIndex = extras.getInt(TUKBeatActivity.SONG_INDEX, 0);
        }
        Song song = Song.get(songIndex);

        Sprite bg = new Sprite(R.mipmap.bg);
        bg.setPosition(Metrics.width / 2, Metrics.height / 2, Metrics.width, Metrics.height);

        add(Layer.bg, bg);


    }

}