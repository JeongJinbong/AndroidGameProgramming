package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.Adapter.SongImageAdapter;
import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;

public class SongSelectionActivity extends AppCompatActivity {
    private int currentSongIndex = 0;
    private ViewPager2 viewPager2;
    private TextView textTitle, textArtist;
    private ArrayList<Song> songs;
    private Song currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_activity_main);

        viewPager2 = findViewById(R.id.viewPager2);
        textTitle = findViewById(R.id.textTitle);
        textArtist = findViewById(R.id.textArtist);

        songs = Song.load(this, "song.json");
        SongImageAdapter adapter = new SongImageAdapter(this, songs);
        viewPager2.setAdapter(adapter);

        if (!songs.isEmpty()) {
            updateInfo(0);
        }

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (currentSong != null) {
                    currentSong.stop();
                }
                updateInfo(position);
            }
        });
    }

    private void updateInfo(int position) {
        currentSongIndex = position;
        currentSong = songs.get(position);
        textTitle.setText(currentSong.title);
        textArtist.setText(currentSong.artist);
        currentSong.playDemo();
    }

    @Override
    protected void onDestroy() {
        if (currentSong != null) {
            currentSong.stop();
        }
        Song.unload();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (currentSong != null) {
            currentSong.stop();
        }
        super.onPause();
    }

    public void onBtnStart(View view) {
        if (currentSong != null) {
            currentSong.stop();
        }
        Intent intent = new Intent(this, TUKBeatActivity.class);
        intent.putExtra(TUKBeatActivity.SONG_INDEX, currentSongIndex);
        startActivity(intent);
    }
}
