package com.health.tencentlive.superlike;

import android.graphics.Bitmap;
import android.graphics.Paint;

public interface Element {

    int getX();

    int getY();

    Bitmap getBitmap();

    void evaluate(int start_x, int start_y, double time);

    Paint getPaint();
}
