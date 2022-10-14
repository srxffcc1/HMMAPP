//package com.healthy.library.utils;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;
//import com.healthy.library.R;
//import com.to.aboomy.banner.HolderCreator;
//
///**
// * auth aboom
// * date 2019-12-28
// */
//public class ImageRoundHolderCreator implements HolderCreator {
//    @Override
//    public View createView(final Context context, final int index, Object o) {
//        View inflate = View.inflate(context, R.layout.item_round_banner_image, null);
//        ImageView imageView = inflate.findViewById(R.id.img);
//        com.healthy.library.businessutil.GlideCopy.with(imageView).load(o).into(imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ToastUtils.showShort(index + "");
//            }
//        });
//        return inflate;
//    }
//}
