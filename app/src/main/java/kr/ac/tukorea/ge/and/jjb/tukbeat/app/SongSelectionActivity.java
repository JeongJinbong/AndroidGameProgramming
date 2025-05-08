package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.app.data.Song;

public class SongSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Song.load(this,"song.json");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            startActivity(new Intent(this, TUKBeatActivity.class));
        }
        return super.onTouchEvent(event);
    }
}