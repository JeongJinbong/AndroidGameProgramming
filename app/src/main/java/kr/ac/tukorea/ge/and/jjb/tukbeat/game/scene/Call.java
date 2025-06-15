package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;


import android.view.animation.DecelerateInterpolator;


import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Call extends Sprite {

    public enum Type {
        charming, normal, miss, none
    }

    private boolean show = false;
    private ValueAnimator animator;
    private final Bitmap numberBitmap;
    private final Rect srcRect = new Rect();
    private final RectF dstRectDigit = new RectF();
    private final int digitWidth = 24, digitHeight = 32;
    private final float digitScale = 2.0f;
    private int currentCombo = 0;



    public static Type typeWithTimeDiff(float diff) {
        float abs = Math.abs(diff);
        if (abs <= 0.04f) return Type.charming;
        if (abs <= 0.10f) return Type.normal;
        return Type.miss;
    }

    public Call() {
        super(R.mipmap.charming);
        float width = 170f;
        float height = 70f;
        float x = 1550 - width / 2f;
        float y = Metrics.height / 2f;

        setPosition(x, y, width, height);
        set(Type.none);

        numberBitmap = BitmapPool.get(R.mipmap.number_24x32);
    }

    public void set(Type type) {
        if (type == Type.charming) {
            show = true;
            if (!getAnimator().isRunning()) {
                getAnimator().start();
            }
        } else {
            show = false;
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }
    }

    private ValueAnimator getAnimator() {
        if (animator != null) return animator;

        float y0 = dstRect.top;
        float y1 = y0 - 50f;

        animator = ValueAnimator.ofFloat(y0, y1);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float y = (Float) animation.getAnimatedValue();
            dstRect.offsetTo(dstRect.left, y);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                set(Type.none);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                set(Type.none);
            }
        });

        return animator;
    }

    @Override
    public void draw(Canvas canvas) {
        if (show) {
            super.draw(canvas);
        }

        if (currentCombo >= 2) {
            drawComboDigits(canvas);
        }
    }

    private void drawComboDigits(Canvas canvas) {
        String comboStr = String.valueOf(currentCombo);
        float digitW = digitWidth * digitScale;
        float digitH = digitHeight * digitScale;
        float totalWidth = comboStr.length() * digitW;

        float startX = dstRect.centerX() - totalWidth / 2f;
        float y = dstRect.top - digitH - 10f;

        for (int i = 0; i < comboStr.length(); i++) {
            int digit = comboStr.charAt(i) - '0';
            srcRect.set(digit * digitWidth, 0, (digit + 1) * digitWidth, digitHeight);
            dstRectDigit.set(startX, y, startX + digitW, y + digitH);
            canvas.drawBitmap(numberBitmap, srcRect, dstRectDigit, null);
            startX += digitW;
        }
    }

    public void setCombo(int combo) {
        this.currentCombo = combo;
    }
}

