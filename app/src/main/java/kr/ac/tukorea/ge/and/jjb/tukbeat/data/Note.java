package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Note {
  public String type;
  public float time;
  public float endTime;
  public int startX;
  public int[] pathX;

    @Override
    public String toString() {
        return "Note{" +
                "type='" + type + '\'' +
                ", time=" + time +
                ", endTime=" + endTime +
                ", startX=" + startX +
                ", pathX=" + Arrays.toString(pathX) +
                '}';
    }


    public static Note parse(JsonReader jr) throws IOException {
        Note note = new Note();
        jr.beginObject();
        while (jr.hasNext()) {
            String name = jr.nextName();
            if (!JsonHelper.readProperty(note, name, jr)) {
                jr.skipValue();
            }
        }
        jr.endObject();
        return note;
    }


}
