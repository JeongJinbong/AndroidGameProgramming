package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.Adapter.SongAdapter;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;


public class SongSelectionActivity extends AppCompatActivity {

    private ViewPager2 viewpager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_activity_main);
        viewpager2 = findViewById(R.id.viewPager2);

        ArrayList<Song> songs = Song.load(this,"song.json");
        SongAdapter adapter = new SongAdapter(this, songs);
        viewpager2.setAdapter(adapter);

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