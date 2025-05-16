package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.os.Bundle;
import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.BuildConfig;
import kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TUKBeatActivity extends GameActivity {

    public static final String SONG_INDEX = "songIndex";
    private static final String TAG = TUKBeatActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        Metrics.setGameSize(1600,900);
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int index = extras.getInt(SONG_INDEX);
            Log.d(TAG, "Selected Song Index = " + index);
        }
        new MainScene().push();

    }


}