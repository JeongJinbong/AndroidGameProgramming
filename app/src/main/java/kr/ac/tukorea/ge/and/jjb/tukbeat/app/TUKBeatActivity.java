package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import kr.ac.tukorea.ge.and.jjb.tukbeat.BuildConfig;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class TUKBeatActivity extends GameActivity {

    private static final String TAG = TUKBeatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        Metrics.setGameSize(1600,900);
        int songIndex = Song.currentSongIndex;
        super.onCreate(savedInstanceState);
        Song song = Song.songs.get(songIndex);
        Log.d(TAG,"Song Index = " + songIndex);
        new MainScene(song).push();
    }


}