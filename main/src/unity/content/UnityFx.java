package unity.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Position;
import arc.util.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import unity.graphics.UnityPal;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static arc.math.Mathf.*;
import static mindustry.graphics.Drawf.*;

public class UnityFx{
    public static final Effect

    shootSmallBlaze = new Effect(22f, e -> {
        color(Pal.lightFlame, Pal.darkFlame, Pal.gray, e.fin());
        randLenVectors(e.id, 16, e.finpow() * 60f, e.rotation, 18f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.85f + e.fout() * 3.5f);
        });
    }),

    shootPyraBlaze = new Effect(32f, e -> {
        color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.gray, e.fin());
        randLenVectors(e.id, 16, e.finpow() * 60f, e.rotation, 18f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, 0.85f + e.fout() * 3.5f);
        });
    }),

    craftingEffect = new Effect(67f, 35f, e -> {
        float value = randomSeed(e.id);

        Tmp.v1.trns(value * 360f + ((value + 4f) * e.fin() * 80f), (randomSeed(e.id * 126) + 1f) * 34f * (1f - e.finpow()));

        color(Color.valueOf("ff9c5a"));
        Fill.square(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fslope() * 3f, 45f);
        color();
    }),

    orbHit = new Effect(12f, e -> {
        color(Pal.surge);
        stroke(e.fout() * 1.5f);
        randLenVectors(e.id, 8, e.finpow() * 17f, e.rotation, 360f, (x, y) -> {
            float ang = Mathf.angle(x, y);
            lineAngle(e.x + x, e.y + y, ang, e.fout() * 4f + 1f);
        });
    }),

    orbShoot = new Effect(21f, e -> {
        color(Pal.surge);
        for(int i = 0; i < 2; i++){
            int l = Mathf.signs[i];
            tri(e.x, e.y, 4f * e.fout(), 29f, e.rotation + 67 * l);
        }
    }),

    orbTrail = new Effect(43f, e -> {
        float originalZ = z();

        Tmp.v1.trns(randomSeed(e.id) * 360f, randomSeed(e.id * 341) * 12f * e.fin());

        z(Layer.bullet - 0.01f);
        light(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 4.7f * e.fout() + 3f, Pal.surge, 0.6f);

        color(Pal.surge);
        Fill.circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fout() * 2.7f);

        z(originalZ);
    }),

    orbShootSmoke = new Effect(26f, e -> {
        color(Pal.surge);
        randLenVectors(e.id, 7, 80f, e.rotation, 0f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
        });
    }),

    orbCharge = new Effect(38f, e -> {
        color(Pal.surge);
        randLenVectors(e.id, 2, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 1f);
        });
    }),

    orbChargeBegin = new Effect(71f, e -> {
        color(Pal.surge);
        Fill.circle(e.x, e.y, e.fin() * 3f);

        color();
        Fill.circle(e.x, e.y, e.fin() * 2f);
    }),

    currentCharge = new Effect(32f, e -> {
        color(Pal.surge, Color.white, e.fin());
        randLenVectors(e.id, 8, 420f + random(24f, 28f) * e.fout(), e.rotation, 4f, (x, y) -> {
            stroke(0.3f + e.fout() * 2f);
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 14f + 0.5f);
        });

        stroke(e.fin() * 1.5f);
        circle(e.x, e.y, e.fout() * 60f);
    }),

    currentChargeBegin = new Effect(260f, e -> {
        color(Pal.surge);
        Fill.circle(e.x, e.y, e.fin() * 7f);

        color();
        Fill.circle(e.x, e.y, e.fin() * 3f);
    }),

    plasmaCharge = new Effect(96f, e -> {
        color(Pal.surge);
        randLenVectors(e.id, 5, (1f - e.finpow()) * 24f, e.rotation, 360f, (x, y) -> {
            tri(e.x + x, e.y + y, e.fout() * 10f, e.fout() * 11f, e.rotation);
            tri(e.x + x, e.y + y, e.fout() * 8f, e.fout() * 9f, e.rotation);
        });
    }),

    plasmaChargeBegin = new Effect(250f, e -> {
        color(Pal.surge);
        tri(e.x, e.y, e.fin() * 16f, e.fout() * 20f, e.rotation);
    }),

    plasmaShoot = new Effect(36f, e -> {
        color(Pal.surge, Color.white, e.fin());

        randLenVectors(e.id, 8, e.fin() * 20f + 1f, e.rotation, 40f, (x, y) -> {
            tri(e.x + x, e.y + y, e.fout() * 14f, e.fout() * 15f, e.rotation);
            tri(e.x + x, e.y + y, e.fout() * 8f, e.fout() * 9f, e.rotation);
        });

        randLenVectors(e.id, 4, e.fin() * 20f + 1f, e.rotation, 40f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 18f + 3f);
        });
    }),

    plasmaTriangleHit = new Effect(30f, e -> {
        color(Pal.surge);

        stroke(e.fout() * 2.8f);
        Lines.circle(e.x, e.y, e.fin() * 60);
    }),

    plasmaFragAppear = new Effect(12f, e -> {
        z(Layer.bullet - 0.01f);

        color(Color.white);
        tri(e.x, e.y, e.fin() * 12f, e.fin() * 13f, e.rotation);

        z();
    }),

    plasmaFragDisappear = new Effect(12f, e -> {
        z(Layer.bullet - 0.01f);

        color(Pal.surge, Color.white, e.fin());
        tri(e.x, e.y, e.fout() * 10f, e.fout() * 11f, e.rotation);

        z();
    }),

    oracleChage = new Effect(30f, e -> {
        color(Pal.lancerLaser);
        Tmp.v1.trns(Mathf.randomSeed(e.id, 360f) + Time.time(), (1 - e.finpow()) * 20f);
        Fill.square(e.x + Tmp.v1.x, e.y + Tmp.v1.y, e.fin() * 4.5f, 45f);
    }),

    oracleChargeBegin = new Effect(40, e -> {
        color(Pal.lancerLaser);
        Fill.circle(e.x, e.y, e.fin() * 6f);
    }),

    effect = new Effect(60f, e -> {
        color(Pal.lancerLaser);
        float temp = (float) e.data;
        stroke(e.fout() * 3f * temp);
        circle(e.x, e.y, e.finpow() * 24f * temp);
    }),

    scarRailShoot = new Effect(24f, e -> {
        e.scaled(10f, b -> {
            color(Color.white, Color.lightGray, b.fin());
            stroke(b.fout() * 3f + 0.2f);
            circle(b.x, b.y, b.fin() * 50f);
        });
        for(int i = 0; i < 2; i++){
            int sign = Mathf.signs[i];
            color(UnityPal.scarColor);
            tri(e.x, e.y, 13 * e.fout(), 85f, e.rotation + 90f * sign);
            color(Color.white);
            tri(e.x, e.y, Math.max(13 * e.fout() - 4f, 0f), 81f, e.rotation + 90f * sign);
        }
    }),

    scarRailTrail = new Effect(16f, e -> {
        for(int i = 0; i < 2; i++){
            int sign = Mathf.signs[i];
            color(UnityPal.scarColor);
            tri(e.x, e.y, 10f * e.fout(), 24f, e.rotation + 90f + 90f * sign);
            color(Color.white);
            tri(e.x, e.y, Math.max(10f * e.fout() - 4f, 0f), 20f, e.rotation + 90f + 90f * sign);
        }
    }),

    scarRailHit = new Effect(18f, e -> {
        for(int i = 0; i < 2; i++){
            int sign = Mathf.signs[i];
            color(UnityPal.scarColor);
            tri(e.x, e.y, 10f * e.fout(), 60f, e.rotation + 90f + 90f * sign);
            color(Color.white);
            tri(e.x, e.y, Math.max(10 * e.fout() - 4f, 0f), 56f, e.rotation + 90f + 90f * sign);
        }
    }),

    falseLightning = new Effect(10f, 500f, e -> {
        if(e.data == null || !(e.data instanceof Float)) return;
        float length = (float) e.data;
        int lenInt = Mathf.round(length / 8f);
        stroke(3f * e.fout());
        color(e.color, Color.white, e.fin());
        //unity.Unity.print(lenInt,"  ",length);
        for(int i = 0; i < lenInt; i++){
            float offsetXA = i == 0 ? 0 : Mathf.randomSeed(e.id + i * 6413, -4.5f, 4.5f);
            float offsetYA = length / lenInt * i;
            int j = i + 1;
            float offsetXB = j == lenInt ? 0 : Mathf.randomSeed(e.id + j * 6413, -4.5f, 4.5f);
            float offsetYB = length / lenInt * j;
            Tmp.v1.trns(e.rotation, offsetYA, offsetXA);
            Tmp.v1.add(e.x, e.y);
            Tmp.v2.trns(e.rotation, offsetYB, offsetXB);
            Tmp.v2.add(e.x, e.y);
            line(Tmp.v1.x, Tmp.v1.y, Tmp.v2.x, Tmp.v2.y, false);
            Fill.circle(Tmp.v1.x, Tmp.v1.y, getStroke() / 2f);
        }
    }),

    forgeAbsorbEffect = new Effect(124f, e -> {
        float angle = e.rotation;
        float slope = (0.5f - Math.abs(e.finpow() - 0.5f)) * 2f;
        Tmp.v1.trns(angle, (1 - e.finpow()) * 110f);
        color(UnityPal.endColor);
        stroke(1.5f);
        lineAngleCenter(e.x + Tmp.v1.x, e.y + Tmp.v1.y, angle, slope * 8f);
    }),

    imberSparkCraftingEffect = new Effect(70f, e -> {
        color(UnityPal.imberColor, Color.valueOf("ffc266"), e.finpow());
        alpha(e.finpow());
        randLenVectors(e.id, 3, (1f - e.finpow()) * 24f, e.rotation, 360f, (x, y) -> {
            tri(e.x + x, e.y + y, e.fout() * 8f, e.fout() * 10f, e.rotation);
            tri(e.x + x, e.y + y, e.fout() * 4f, e.fout() * 6f, e.rotation);
        });
        color();
    }),

    imberCircleSparkCraftingEffect = new Effect(30f, e -> {
        color(Pal.surge);
        stroke(e.fslope());
        circle(e.x, e.y, e.fin() * 20f);
    }),

    healLaser = new Effect(60f, e -> {
        if(e.data == null || !(e.data instanceof Position[])) return;
        float[] reduction = new float[]{0f, 1.5f};
        Position[] temp = (Position[]) e.data;
        Position a = temp[0], b = temp[1];
        for(int i = 0; i < 2; i++){
            color(i == 0 ? Pal.heal : Color.white);
            stroke((3f - reduction[i]) * e.fout());
            line(a.getX(), a.getY(), b.getX(), b.getY());
            Fill.circle(a.getX(), a.getY(), (2.5f - reduction[i]) * e.fout());
            Fill.circle(b.getX(), b.getY(), (2.5f - reduction[i]) * e.fout());
        }
    }),

    coloredHitSmall = new Effect(14f, e -> {
        color(Color.white, e.color, e.fin());
        e.scaled(7f, s -> {
            stroke(0.5f + s.fout());
            circle(e.x, e.y, s.fin() * 5f);
        });
        stroke(0.5f + e.fout());
        randLenVectors(e.id, 5, e.fin() * 15f, (x, y) -> {
            lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 3f + 1f);
        });
    }),

    cutEffect = new Effect(3f * 60f, e -> {
        
    });
}
