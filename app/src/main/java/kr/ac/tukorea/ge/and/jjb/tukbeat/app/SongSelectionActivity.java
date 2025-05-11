package kr.ac.tukorea.ge.and.jjb.tukbeat.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;


public class SongSelectionActivity extends AppCompatActivity {

    private static final String TAG = "SongSelectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 곡 데이터 로딩
        ArrayList<Song> songs = Song.load(this, "song.json");

        // 로드된 곡 정보 로그로 확인
        for (Song song : songs) {
            Log.d(TAG, "Loaded song: " + song.toString());
        }

        // 단순 TextView에 첫 번째 곡 정보 출력해보기
        TextView textView = new TextView(this);
        textView.setText(songs.isEmpty() ? "곡 없음" : songs.get(0).toString());
        textView.setTextSize(18);
        setContentView(textView);
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