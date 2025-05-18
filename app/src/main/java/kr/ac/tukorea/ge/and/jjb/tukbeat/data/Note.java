package kr.ac.tukorea.ge.and.jjb.tukbeat.data;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class Note {
    public enum Type { TAP, HOLD, SLIDE }

    public Type type;
    public float time; // ms 단위
    public float duration; // HOLD 전용
    public float startX, startY;
    public float endX, endY;

    public ArrayList<Point> curvePath; // SLIDE 전용

    public static class Point {
        public float x, y;
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
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
                case "duration":
                    note.duration = (float) reader.nextDouble();
                    break;
                case "startX":
                    note.startX = (float) reader.nextDouble();
                    break;
                case "startY":
                    note.startY = (float) reader.nextDouble();
                    break;
                case "endX":
                    note.endX = (float) reader.nextDouble();
                    break;
                case "endY":
                    note.endY = (float) reader.nextDouble();
                    break;
                case "curvePath":
                    note.curvePath = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        float x = 0, y = 0;
                        while (reader.hasNext()) {
                            String key = reader.nextName();
                            if (key.equals("x")) x = (float) reader.nextDouble();
                            else if (key.equals("y")) y = (float) reader.nextDouble();
                            else reader.skipValue();
                        }
                        reader.endObject();
                        note.curvePath.add(new Point(x, y));
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return note;
    }
}
