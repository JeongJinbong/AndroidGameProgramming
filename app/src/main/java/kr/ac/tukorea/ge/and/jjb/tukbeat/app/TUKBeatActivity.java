package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.os.Bundle;
import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.BuildConfig;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TUKBeatActivity extends GameActivity {

    public static Song selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        Metrics.setGameSize(1600,900);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedSong = (Song) extras.getSerializable("song");
            Log.d("TUKBeat", "선택된 곡: " + selectedSong.title);
        }
        new MainScene().push();

    }


}