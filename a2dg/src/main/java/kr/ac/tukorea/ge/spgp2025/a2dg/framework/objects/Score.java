package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Locale;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;

public class Score implements IGameObject {
    private final Bitmap numberBitmap;
    private final float right, top, dstCharWidth, dstCharHeight;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();
    private final int srcCharWidth, srcCharHeight;

    private float score;
    private float displayScore;

    private final Paint textPaint = new Paint();

    public Score(int mipmapId, float right, float top, float width) {
        this.numberBitmap = BitmapPool.get(mipmapId);
        this.right = right;
        this.top = top;
        this.dstCharWidth = width;
        this.srcCharWidth = numberBitmap.getWidth() / 10;
        this.srcCharHeight = numberBitmap.getHeight();
        this.dstCharHeight = dstCharWidth * srcCharHeight / srcCharWidth;

        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTextSize(dstCharHeight * 0.8f);
        this.textPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void setScore(float score) {
        this.score = this.displayScore = score;
    }

    public void updateScore(float newScore) {
        this.score = newScore;
    }

    @Override
    public void update() {
        float diff = score - displayScore;
        if (Math.abs(diff) < 0.1f) {
            displayScore = score;
        } else {
            displayScore += diff * 0.1f;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        int scoreInt = (int) displayScore;
        int scoreDecimal = (int) ((displayScore - scoreInt) * 10) % 10;

        float x = right;

        canvas.drawText("%", x, top + dstCharHeight, textPaint);
        x -= dstCharWidth * 0.8f;

        drawDigit(canvas, scoreDecimal, x - dstCharWidth, top);
        x -= dstCharWidth;

        canvas.drawText(".", x - dstCharWidth * 0.3f, top + dstCharHeight, textPaint);
        x -= dstCharWidth * 0.6f;

        int temp = scoreInt;
        while (temp > 0 || x >= right - dstCharWidth * 3) {
            int digit = temp % 10;
            temp /= 10;
            x -= dstCharWidth;
            drawDigit(canvas, digit, x, top);
        }
    }

    private void drawDigit(Canvas canvas, int digit, float x, float y) {
        srcRect.set(digit * srcCharWidth, 0, (digit + 1) * srcCharWidth, srcCharHeight);
        dstRect.set(x, y, x + dstCharWidth, y + dstCharHeight);
        canvas.drawBitmap(numberBitmap, srcRect, dstRect, null);
    }
}