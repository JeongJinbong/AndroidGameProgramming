package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class Note {
    public boolean isHolding;
    public boolean isReleased;
    public boolean isJudged;

    public enum Type { TAP, HOLD, SLIDE }

    public Type type;
    public float time;
    public float endTime;
    public float startX;
    public ArrayList<Float> pathX;

    public boolean isLongNote(){
        return type ==Type.HOLD || type == Type.SLIDE;
    }
    public static Note parse(JsonReader reader) throws IOException {
        Note note = new Note();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "type":
                    note.type = Type.valueOf(reader.nextString());
                    break;
                case "time":
                    note.time = (float) reader.nextDouble();
                    break;
                case "endTime":
                    note.endTime = (float) reader.nextDouble();
                    break;
                case "startX":
                    note.startX = (float) reader.nextDouble();
                    break;
                case "pathX":
                    note.pathX = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        note.pathX.add((float) reader.nextDouble());
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        if (note.type == Type.TAP) {
            note.endTime = note.time;
        }
        return note;
    }
}
