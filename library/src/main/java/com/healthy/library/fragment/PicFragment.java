package com.healthy.library.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.healthy.library.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.utils.TransformUtil;

import java.util.Map;

public class PicFragment extends BaseFragment {


    private ImageView ivLoading;

    public static PicFragment newInstance(Map<String, Object> maporg) {
        PicFragment fragment = new PicFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pic;
    }

    @Override
    protected void findViews() {
        initView();
        if (get("fit") != null) {
            ivLoading.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivLoading.getLayoutParams();
            layoutParams.leftMargin = (int) TransformUtil.dp2px(mContext, 10f);
            layoutParams.rightMargin = (int) TransformUtil.dp2px(mContext, 10f);
//            layoutParams.topMargin=(int) TransformUtil.dp2px(mContext,56f);

        }
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(get("url").toString())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(ivLoading);

    }

    private void initView() {
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
    }
}
