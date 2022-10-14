package com.healthy.library.misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

public class CenterImageSpan extends ImageSpan {

    public CenterImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    public CenterImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CenterImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public CenterImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }


    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // image to draw
        Drawable b = getDrawable();
        // font metrics of text to be replaced
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2
                - b.getBounds().bottom / 2;

        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
