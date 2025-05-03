package kr.ac.tukorea.ge.and.jjb.tukbeat;

import android.app.Notification;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.util.AttributeSet;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameView extends View {
    private static final float SCREEN_WIDTH= 16.0f;
    private static final float SCREEN_HEIGHT= 9f;
    private final Matrix transformMatrix = new Matrix();
    private final RectF borderRect = new RectF(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
    private final Paint borderPaint = new Paint();

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(0.1f);
        borderPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
        float view_ratio = (float)w/ (float)h;
        float game_ratio = SCREEN_WIDTH / SCREEN_HEIGHT;

        transformMatrix.reset();
        if (view_ratio > game_ratio) {
            float scale = h / SCREEN_HEIGHT;
            transformMatrix.preTranslate((w - h * game_ratio) / 2, 0 );
            transformMatrix.preScale(scale, scale);
        }
        else{
            float scale = w/ SCREEN_WIDTH;
            transformMatrix.preTranslate(0,(h-w/game_ratio/2));
            transformMatrix.preScale(scale, scale);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.setMatrix(transformMatrix);
        canvas.drawRect(borderRect,borderPaint);
    }



}