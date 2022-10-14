package com.healthy.library.interfaces;

import com.healthy.library.widget.RollNumberTextView;

public interface RollNumberBase {
    void start();

    RollNumberTextView withNumber(float number);

    RollNumberTextView withNumber(float number, boolean flag);

    RollNumberTextView withNumber(int number);

    RollNumberTextView setDuration(long duration);

    void setOnEnd(RollNumberTextView.EndListener callback);
}
