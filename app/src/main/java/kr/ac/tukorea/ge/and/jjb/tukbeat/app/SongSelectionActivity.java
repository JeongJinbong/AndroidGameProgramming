package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.Adapter.SongImageAdapter;
import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.SongLoader;
import kr.ac.tukorea.ge.and.jjb.tukbeat.databinding.SelectionActivityMainBinding;

public class SongSelectionActivity extends AppCompatActivity {

    private @NonNull SelectionActivityMainBinding ui;
    private int currentSongIndex = 0;
    private Song currentSong;
    private ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ui = SelectionActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(ui.getRoot().findViewById(R.id.SongSelection), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        songs = new SongLoader(this).loadSongs();
        SongImageAdapter adapter = new SongImageAdapter(this, songs);
        ui.viewPager2.setAdapter(adapter);

        if (!songs.isEmpty()) {
            updateInfo(0);
        }

        ui.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                stopCurrentSong();
                updateInfo(position);
            }
        });
    }

    private void updateInfo(int position) {
        currentSongIndex = position;
        currentSong = songs.get(position);
        ui.textTitle.setText(currentSong.title);
        ui.textArtist.setText(currentSong.artist);
        // currentSong.playDemo();
    }

    private void stopCurrentSong() {
        if (currentSong != null) {
            currentSong.stop();
        }
    }

    @Override
    protected void onDestroy() {
        stopCurrentSong();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        stopCurrentSong();
        super.onPause();
    }

    public void onBtnStart(View view) {
        stopCurrentSong();
        Intent intent = new Intent(this, TUKBeatActivity.class);
        intent.putExtra(TUKBeatActivity.SONG_INDEX, currentSongIndex);
        startActivity(intent);
    }
}
