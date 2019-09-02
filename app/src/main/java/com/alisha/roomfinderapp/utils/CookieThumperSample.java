package com.alisha.roomfinderapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

/**
 * Created by Eugene Levenetc.
 */
public class CookieThumperSample {

    private Context context;

    public CookieThumperSample(Context context) {
        this.context = context;
    }

    public static void play(TextSurface textSurface, AssetManager assetManager) {

//        final Typeface robotoBlack = Typeface.createFromAsset(assetManager, "fonts/agency.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        paint.setTypeface(robotoBlack);

        Text textDaai = TextBuilder
                .create("Room Finder")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER).build();

        Text textBraAnies = TextBuilder
                .create("Your app for renting")
                .setPaint(paint)
                .setSize(24)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textDaai).build();

        Text textFokkenGamBra = TextBuilder
                .create(" rooms for,")
                .setPaint(paint)
                .setSize(24)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textBraAnies).build();

        Text textHaai = TextBuilder
                .create("Home,")
                .setPaint(paint)
                .setSize(34)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.RIGHT_OF, textFokkenGamBra).build();

        Text textAnd = TextBuilder
                .create("Hotels and")
                .setPaint(paint)
                .setSize(34)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textFokkenGamBra).build();

        Text textDaaiAnies = TextBuilder
                .create("Much More!!")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textHaai).build();

//        Text texThyLamInnie = TextBuilder
//                .create(" We are here for you.")
//                .setPaint(paint)
//                .setSize(24)
//                .setAlpha(0)
//                .setColor(Color.WHITE)
//                .setPosition(Align.BOTTOM_OF, textDaaiAnies).build();


        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textDaai, 1050, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textDaai, 600, SideCut.hide(Side.LEFT), false),
                                new Sequential(Delay.duration(300), ShapeReveal.create(textDaai, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(new TransSurface(500, textBraAnies, Pivot.CENTER), ShapeReveal.create(textBraAnies, 1300, SideCut.show(Side.LEFT), false)),
                        Delay.duration(500)
//                        new Parallel(new TransSurface(750, textFokkenGamBra, Pivot.CENTER), Slide.showFrom(Side.LEFT, textFokkenGamBra, 750), ChangeColor.to(textFokkenGamBra, 750, Color.WHITE)),
//                        Delay.duration(500),
//                        new Parallel(TransSurface.toCenter(textHaai, 500), Rotate3D.showFromSide(textHaai, 750, Pivot.TOP)),
//                        new Parallel(TransSurface.toCenter(textAnd, 500), Rotate3D.showFromSide(textAnd, 750, Pivot.TOP)),
//                        new Parallel(TransSurface.toCenter(textDaaiAnies, 500), Slide.showFrom(Side.TOP, textDaaiAnies, 500)),
////                        new Parallel(TransSurface.toCenter(texThyLamInnie, 750), Slide.showFrom(Side.LEFT, texThyLamInnie, 500)),
//                        Delay.duration(500)
//                        new Parallel(
//                                ShapeReveal.create(textThrowDamn, 1500, SideCut.hide(Side.LEFT), true),
//                                new Sequential(Delay.duration(250), ShapeReveal.create(textDevilishGang, 1500, SideCut.hide(Side.LEFT), true)),
//                                new Sequential(Delay.duration(500), ShapeReveal.create(textSignsInTheAir, 1500, SideCut.hide(Side.LEFT), true)),
//                                Alpha.hide(texThyLamInnie, 1500),
//                                Alpha.hide(textDaaiAnies, 1500)
//                        )
                )
        );

    }

}
