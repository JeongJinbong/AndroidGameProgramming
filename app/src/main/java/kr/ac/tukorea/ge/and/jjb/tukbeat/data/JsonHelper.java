package kr.ac.tukorea.ge.and.jjb.tukbeat.data;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonHelper {
    private static final String TAG = JsonHelper.class.getSimpleName();

    public static boolean readProperty(Object object, String name, JsonReader reader) throws IOException {
        try {
            Field field = object.getClass().getField(name);
            Class<?> type = field.getType();
            if (type == int.class) {
                int value = reader.nextInt();
                Log.v(TAG, "Int " + name + ": " + value + " - " + object);
                field.setInt(object, value);
            } else if (type == String.class) {
                String value = reader.nextString();
                Log.v(TAG, "String " + name + ": " + value + " - " + object);
                field.set(object, value);
            } else if (type == boolean.class) {
                boolean value = reader.nextBoolean();
                Log.v(TAG, "boolean " + name + ": " + value + " - " + object);
                field.set(object, value);
            } else if (type == double.class) {
                double value = reader.nextDouble();
                Log.v(TAG, "double " + name + ": " + value + " - " + object);
                field.set(object, value);
            } else if (type == long.class) {
                long value = reader.nextLong();
                Log.v(TAG, "long " + name + ": " + value + " - " + object);
                field.set(object, value);
            } else if (type == int[].class) {
                int[] value = readIntArray(reader);
                Log.v(TAG, "int[] " + name + ": [" + value.length + "] - " + object);
                field.set(object, value);
            } else {
                Log.e(TAG, "Not handling " + name + ". type: " + type + " - " + object);
                return false;
            }
            return true;
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "No field \"" + name + "\" in " + object);
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
    private static int[] readIntArray(JsonReader reader) throws IOException {
        ArrayList<Integer> integers = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            int value = reader.nextInt();
            integers.add(value);
        }
        reader.endArray();

        int[] ints = new int[integers.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = integers.get(i);
        }

        return ints;
    }
}
