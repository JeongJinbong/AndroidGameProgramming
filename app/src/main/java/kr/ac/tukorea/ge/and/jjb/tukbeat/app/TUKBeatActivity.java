package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.os.Bundle;

import kr.ac.tukorea.ge.and.jjb.tukbeat.BuildConfig;
import kr.ac.tukorea.ge.and.jjb.tukbeat.app.scene.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TUKBeatActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        Metrics.setGameSize(1600,900);
        super.onCreate(savedInstanceState);
        new MainScene().push();
    }


}