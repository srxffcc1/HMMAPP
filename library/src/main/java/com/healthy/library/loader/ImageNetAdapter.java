package com.healthy.library.loader;

import android.view.ViewGroup;

import com.healthy.library.R;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.utils.ResUtil;
import com.healthy.library.widget.CornerImageView;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，网络图片
 */
public class ImageNetAdapter extends BannerAdapter<String, ImageHolder> {

    private float mRadius;

    public ImageNetAdapter(List<String> mDatas, float radius, List<ColorInfo> colorList) {
        super(mDatas);
        mRadius = radius;
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        CornerImageView cornerImageView = new CornerImageView(parent.getContext());
        cornerImageView.setCornerRadius(mRadius);
        cornerImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new ImageHolder(cornerImageView);
    }

    @Override
    public void onBindView(ImageHolder holder, String data, int position, int size) {
        if (data.contains("R.")) {
            GlideCopy.with(holder.itemView)
                    .load(ResUtil.getInstance().getResourceId(data))
                    .placeholder(R.drawable.img_690_260_default)
                    .error(R.drawable.img_690_260_default)
                    .into(holder.imageView);
        } else {
            GlideCopy.with(holder.itemView)
                    .load(data)
                    .placeholder(R.drawable.img_690_260_default)
                    .error(R.drawable.img_690_260_default)
                    .into(holder.imageView);
        }
    }
}
