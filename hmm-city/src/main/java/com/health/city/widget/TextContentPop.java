package com.health.city.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.health.city.R;

public class TextContentPop extends PopupWindow {
    public TextContentPop(final Context context, int width, String text) {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(width);
        TextView textView=new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundResource(R.drawable.shape_chose);
        textView.setText(text);
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(textView);
    }
}
