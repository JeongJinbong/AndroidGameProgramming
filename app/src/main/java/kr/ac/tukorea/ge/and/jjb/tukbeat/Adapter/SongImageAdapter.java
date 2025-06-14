package kr.ac.tukorea.ge.and.jjb.tukbeat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import de.hdodenhof.circleimageview.CircleImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;

public class SongImageAdapter extends RecyclerView.Adapter<SongImageAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<Song> songs;

    public SongImageAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CircleImageView imageView = new CircleImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBorderWidth(8);
        imageView.setBorderColor(Color.BLACK);
        imageView.setElevation(8f);

        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String fileName = songs.get(position).thumbnail;
        try (InputStream is = context.getAssets().open(fileName)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            holder.imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
