package kr.ac.tukorea.ge.and.jjb.tukbeat.game.scene;


import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;

public class Call extends Sprite {

    public enum Type {
        perfect, great,good,miss,Count;
    }

    public static Type typeWithTimeDiff(float time)
    {
        if(time <0) time = -time;
        if(time <0.1f) return Type.perfect;
        if(time< 0.2f) return Type.great;
        if(time<0.3f) return Type.good;
        if(time<0.4f) return Type.miss;
        return Type.miss;
    }

    public Call()
    {
        super(0);
    }
}
