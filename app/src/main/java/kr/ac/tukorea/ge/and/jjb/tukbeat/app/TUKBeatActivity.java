package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.os.Bundle;

import kr.ac.tukorea.ge.and.jjb.tukbeat.BuildConfig;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class TUKBeatActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = true;
        super.onCreate(savedInstanceState);
    }
}