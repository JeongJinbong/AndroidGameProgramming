package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;

public class Score implements IGameObject {
    private final Bitmap numberBitmap;
    private final float right, top, dstCharWidth, dstCharHeight;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();
    private final int srcCharWidth, srcCharHeight;

    private int score = 0;

    public Score(int mipmapId, float right, float top, float width) {
        this.numberBitmap = BitmapPool.get(mipmapId);
        this.right = right;
        this.top = top;
        this.dstCharWidth = width;
        this.srcCharWidth = numberBitmap.getWidth() / 10;
        this.srcCharHeight = numberBitmap.getHeight();
        this.dstCharHeight = dstCharWidth * srcCharHeight / srcCharWidth;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Canvas canvas) {
        int temp = score;
        float x = right;
        if (temp == 0) {
            x -= dstCharWidth;
            drawDigit(canvas, 0, x, top);
            return;
        }

        while (temp > 0) {
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
