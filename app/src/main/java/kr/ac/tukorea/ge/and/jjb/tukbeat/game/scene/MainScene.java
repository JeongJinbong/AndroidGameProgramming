package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;

import android.content.Context;
import android.view.MotionEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import kr.ac.tukorea.ge.and.jjb.tukbeat.R;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Song;
import kr.ac.tukorea.ge.and.jjb.tukbeat.data.Note;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Score;

public class MainScene extends Scene {
    public static MainScene scene;
    private final Song song;
    private float musicTime;
    private final float w = Metrics.width, h = Metrics.height;
    protected Call call;
    private Score scoreDisplay;
    private int charmingCount = 0;
    private int normalCount = 0;
    private int combo = 0;
    private final int totalNotes;

    private final Set<SlideNoteSprite> heldSlideNotes = new HashSet<>();
    public static final float JUDGMENT_RANGE_Y = 100f;

    public enum Layer {
        bg, note, explosion, ui, call;
        public static final int COUNT = values().length;
    }

    public MainScene(Song song) {
        initLayers(Layer.COUNT);
        this.song = song;

        Context context = GameView.view.getContext();
        add(Layer.bg, new Sprite(R.mipmap.bg, w / 2, h / 2, w, h));
        add(Layer.bg, new Sprite(R.mipmap.judgeline, w / 2, h - (50f / 2f) - 150f, w, 50f));

        song.loadNotes(context);
        this.totalNotes = song.getTotalNoteCount();

        add(Layer.call, call = new Call());

        scoreDisplay = new Score(R.mipmap.number_24x32, 150f, 20f, 30f);
        scoreDisplay.setScore(0f);
        add(Layer.ui, scoreDisplay);
    }

    public float getMusicTime() { return musicTime; }
    public int getCombo() { return combo; }
    public int getCharmingCount() { return charmingCount; }
    public int getNormalCount() { return normalCount; }
    public Call getCall() { return call; }

    @Override
    public void update() {
        musicTime += GameView.frameTime;
        super.update();

        while (true) {
            Note note = song.popNoteBefore(musicTime + NoteSprite.screenfulTime());
            if (note == null) break;

            if (note.isSlide()) {
                for (int i = 0; i < note.pathX.length; i++) {
                    SlideNoteSprite sns = SlideNoteSprite.get(note, i);
                    add(Layer.note, sns);
                }
            } else {
                add(Layer.note, NoteSprite.get(note));
            }
        }

        Iterator<SlideNoteSprite> heldIt = heldSlideNotes.iterator();
        while (heldIt.hasNext()) {
            SlideNoteSprite sns = heldIt.next();

            if (sns.isJudged()) {
                heldIt.remove();
                continue;
            }

            float y = sns.getY();
            float diff = sns.getAppearTime() - musicTime;
            Call.Type type = Call.typeWithTimeDiff(diff);

            if (Math.abs(y - NoteSprite.LINE_Y) <= JUDGMENT_RANGE_Y && type == Call.Type.charming) {
                charmingCount++;
                combo++;
                call.setCombo(combo);
                updateScore();
                call.set(Call.Type.charming);
                sns.markJudged();
                removeNote(sns);
                add(Layer.explosion, ExplodingNote.get(Call.Type.charming, sns));
                heldIt.remove();
            } else if (y > NoteSprite.LINE_Y + JUDGMENT_RANGE_Y && !sns.isJudged()) {
                combo = 0;
                call.setCombo(combo);
                call.set(Call.Type.miss);
                sns.markJudged();
                removeNote(sns);
                add(Layer.explosion, ExplodingNote.get(Call.Type.miss, sns));
                heldIt.remove();
            }
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        scene = this;
        song.play(GameView.view.getContext());
    }

    @Override
    public void onExit() {
        song.stop();
        scene = null;
        combo = 0;
        charmingCount = 0;
        normalCount = 0;
        heldSlideNotes.clear();
        super.onExit();
    }

    @Override
    public void onPause() {
        super.onPause();
        song.pause();
    }

    @Override
    public void onResume() {
        song.resume();
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerCount = event.getPointerCount();

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_MOVE) {
            for (int i = 0; i < pointerCount; i++) {
                float tx = event.getX(i);
                float ty = event.getY(i);
                float[] pt = Metrics.fromScreen(tx, ty);
                judgeAllNotesAt(pt[0], pt[1]);
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            heldSlideNotes.clear();
        }
        return true;
    }

    private void judgeAllNotesAt(float tx, float ty) {
        Iterator<IGameObject> it = objectsAt(Layer.note).iterator();
        while (it.hasNext()) {
            IGameObject go = it.next();

            if (go instanceof SlideNoteSprite) {
                SlideNoteSprite sns = (SlideNoteSprite) go;
                if (sns.isJudged()) continue;
                if (!isTouchingSlideNote(sns, tx, ty)) continue;
                heldSlideNotes.add(sns);
            } else if (go instanceof NoteSprite) {
                NoteSprite ns = (NoteSprite) go;

                float dx = Math.abs(ns.getX() - tx);
                float noteToLineYDiff = Math.abs(ns.getY() - NoteSprite.LINE_Y);

                if (dx > NoteSprite.WIDTH / 2 || noteToLineYDiff > JUDGMENT_RANGE_Y) {
                    continue;
                }

                float diff = ns.note.time / 1000f - musicTime;
                Call.Type type = Call.typeWithTimeDiff(diff);

                if (type == Call.Type.miss) {
                    combo = 0;
                    call.setCombo(combo);
                    call.set(type);
                } else {
                    if (type == Call.Type.charming) {
                        charmingCount++;
                    } else {
                        normalCount++;
                    }
                    combo++;
                    call.setCombo(combo);
                    updateScore();
                    call.set(type);
                }

                removeNote(ns);
                add(Layer.explosion, ExplodingNote.get(type, ns));
                return;
            }
        }
    }

    private void updateScore() {
        float weightedHit = charmingCount + normalCount * 0.5f;
        float total = totalNotes;
        float scorePercent = (20 * weightedHit * (weightedHit - 1)) / (total * (total - 1)) + (80 * weightedHit) / total;
        scoreDisplay.updateScore(scorePercent);
    }

    private boolean isTouchingSlideNote(SlideNoteSprite sns, float tx, float ty) {
        float dx = Math.abs(sns.getX() - tx);
        float dy = Math.abs(sns.getY() - NoteSprite.LINE_Y);
        return dx <= 100f && dy <= JUDGMENT_RANGE_Y;
    }

    public void removeNote(NoteSprite noteSprite) {
        remove(Layer.note, noteSprite);
    }

    public void removeNote(SlideNoteSprite sns) {
        remove(Layer.note, sns);
    }
}