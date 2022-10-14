package com.healthy.library.businessutil;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.HighlightOptions;
import com.app.hubert.guide.model.RelativeGuide;

public class GuideViewHelp {
    public static void showGuideRound(Activity activity, String label, boolean isdebug, View desview, int guideViewRes, OnGuideChangedListener onGuideChangedListener) {
        try {
            HighlightOptions options = new HighlightOptions.Builder()
                    .setRelativeGuide(new RelativeGuide(guideViewRes, Gravity.BOTTOM, 10))
                    .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                        @Override
                        public void onHighlightDrew(Canvas canvas, RectF rectF) {
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(4);
                            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                            RectF rectF1 = new RectF(rectF.left - 10, rectF.top - 10, rectF.right + 10, rectF.bottom + 10);
                            canvas.drawRoundRect(rectF1, 8, 8, paint);
                        }
                    })
                    .build();
            if (desview.getParent() == null) {
                onGuideChangedListener.onRemoved(null);
                return;
            }
            NewbieGuide.with(activity)
                    .setLabel(label)
                    //.setShowCounts(1)
                    .alwaysShow(isdebug)//总是显示，调试时可以打开
                    .setOnGuideChangedListener(onGuideChangedListener)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLightWithOptions(desview, HighLight.Shape.ROUND_RECTANGLE, 0, 5, options))
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGuideRoundRelativeSpecial(Activity activity, String label, boolean isdebug, View desview, int guideViewRes, int gravity, int padding, OnGuideChangedListener onGuideChangedListener) {
        try {
            HighlightOptions options = new HighlightOptions.Builder()
                    .setRelativeGuide(new RelativeGuide(guideViewRes, gravity, padding))
                    .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                        @Override
                        public void onHighlightDrew(Canvas canvas, RectF rectF) {
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(4);
                            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                            //                        canvas.drawRoundRect(rectF,50,50,paint);
                            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 20, paint);
                        }
                    })
                    .build();
            if (desview.getParent() == null) {
                onGuideChangedListener.onRemoved(null);
                return;
            }
            NewbieGuide.with(activity)
                    .setLabel(label)
                    .setShowCounts(1)
//                    .alwaysShow(true)//总是显示，调试时可以打开
                    .setOnGuideChangedListener(onGuideChangedListener)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLightWithOptions(desview, HighLight.Shape.CIRCLE, 50, 0, options)
                    )
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGuideRoundRelative(Activity activity, String label, boolean isdebug, View desview, int guideViewRes, int gravity, int padding, OnGuideChangedListener onGuideChangedListener) {
        try {
            HighlightOptions options = new HighlightOptions.Builder()
                    .setRelativeGuide(new RelativeGuide(guideViewRes, gravity, padding))
                    .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                        @Override
                        public void onHighlightDrew(Canvas canvas, RectF rectF) {
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(4);
                            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                            //                        canvas.drawRoundRect(rectF,50,50,paint);
                            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 12, paint);
                        }
                    })
                    .build();
            if (desview.getParent() == null) {
                onGuideChangedListener.onRemoved(null);
                return;
            }
            NewbieGuide.with(activity)
                    .setLabel(label)
                    .setShowCounts(1)
                    //                .alwaysShow(isdebug)//总是显示，调试时可以打开
                    .setOnGuideChangedListener(onGuideChangedListener)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLightWithOptions(desview, HighLight.Shape.CIRCLE, 50, 0, options)
                    )
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGuideRoundRectangleRelative(Activity activity, String label, boolean isdebug, View desview, int guideViewRes, int gravity, int padding, OnGuideChangedListener onGuideChangedListener) {
        try {
            HighlightOptions options = new HighlightOptions.Builder()
                    .setRelativeGuide(new RelativeGuide(guideViewRes, gravity, padding))
                    .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                        @Override
                        public void onHighlightDrew(Canvas canvas, RectF rectF) {
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(4);
                            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                            canvas.drawRoundRect(rectF, 50, 50, paint);
                            //                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                        }
                    })
                    .build();
            if (desview.getParent() == null) {
                onGuideChangedListener.onRemoved(null);
                return;
            }
            NewbieGuide.with(activity)
                    .setLabel(label)
                    .setShowCounts(1)
                    //                .alwaysShow(isdebug)//总是显示，调试时可以打开
                    .setOnGuideChangedListener(onGuideChangedListener)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLightWithOptions(desview, HighLight.Shape.ROUND_RECTANGLE, 50, 0, options)

                    )
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGuideRoundRectangleRelativeSpecial(Activity activity, String label, boolean isdebug, View desview, int guideViewRes, int gravity, int padding, OnGuideChangedListener onGuideChangedListener) {
        try {
            HighlightOptions options = new HighlightOptions.Builder()
                    .setRelativeGuide(new RelativeGuide(guideViewRes, gravity, padding))
                    .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                        @Override
                        public void onHighlightDrew(Canvas canvas, RectF rectF) {
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(4);
                            RectF rectFnew = new RectF(rectF.left - 5, rectF.top - 5, rectF.right + 5, rectF.bottom + 5);

                            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                            canvas.drawRoundRect(rectFnew, 50, 50, paint);
                            //                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                        }
                    })
                    .build();
            if (desview.getParent() == null) {
                onGuideChangedListener.onRemoved(null);
                return;
            }
            NewbieGuide.with(activity)
                    .setLabel(label)
                    .setShowCounts(1)
                    //                .alwaysShow(isdebug)//总是显示，调试时可以打开
                    .setOnGuideChangedListener(onGuideChangedListener)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLightWithOptions(desview, HighLight.Shape.ROUND_RECTANGLE, 50, 0, options)

                    )
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showGuideRoundRectangle(Activity activity, String label, boolean isdebug, View desview, int guideViewRes, OnGuideChangedListener onGuideChangedListener) {
        try {
            HighlightOptions options = new HighlightOptions.Builder()
                    .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                        @Override
                        public void onHighlightDrew(Canvas canvas, RectF rectF) {
                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(4);
                            paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                            canvas.drawRoundRect(rectF, 50, 50, paint);
                            //                                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                        }
                    })
                    .build();
            if (desview.getParent() == null) {
                onGuideChangedListener.onRemoved(null);
                return;
            }
            NewbieGuide.with(activity)
                    .setLabel(label)
                    .setShowCounts(1)
                    //                .alwaysShow(isdebug)//总是显示，调试时可以打开
                    .setOnGuideChangedListener(onGuideChangedListener)
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLightWithOptions(desview, HighLight.Shape.ROUND_RECTANGLE, 50, 0, options)
                            .setLayoutRes(guideViewRes))
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
