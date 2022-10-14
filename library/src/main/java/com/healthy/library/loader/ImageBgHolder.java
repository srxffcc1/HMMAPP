package com.healthy.library.loader;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.widget.CornerImageView;
import com.lihang.ShadowLayout;

public class ImageBgHolder extends RecyclerView.ViewHolder {
    public CornerImageView imageView;
    public ShadowLayout mShadowLayout;
    private float mRadius;

    public ImageBgHolder(@NonNull View view, float radius) {
        super(view);
        mRadius = radius;
        this.imageView =  view.findViewById(R.id.imageview);
        this.mShadowLayout=view.findViewById(R.id.mShadowLayout);
        imageView.setCornerRadius(mRadius);
        mShadowLayout.setmCornerRadius((int) mRadius);
    }
}