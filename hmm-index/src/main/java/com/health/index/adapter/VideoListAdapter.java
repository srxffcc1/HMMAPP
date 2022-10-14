package com.health.index.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.IndexVideo;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DateUtils;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class VideoListAdapter extends BaseQuickAdapter<IndexVideo, BaseViewHolder> {


    public VideoListAdapter() {
        this(R.layout.index_onlinevedio_review);
    }

    private VideoListAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final IndexVideo item) {
         TextView videoTime;
         TextView videoTitle;
         TextView videoContent;
         TextView videoMan;
         TextView videoReviewButton;

        videoTime = (TextView) helper.itemView.findViewById(R.id.videoTime);
        videoTitle = (TextView) helper.itemView.findViewById(R.id.videoTitle);
        videoContent = (TextView) helper.itemView.findViewById(R.id.videoContent);
        videoMan = (TextView) helper.itemView.findViewById(R.id.videoMan);
        videoReviewButton = (TextView) helper.itemView.findViewById(R.id.videoReviewButton);

        videoTime.setText(DateUtils.formatLongAll(item.start_time*1000+""));
        videoTitle.setText(item.course_name);
        videoContent.setText("主讲人："+item.nickname+","+item.intro);
        if(TextUtils.isEmpty(item.intro)){

            videoContent.setText("主讲人："+item.nickname);
        }
        videoMan.setText(item.pbUv+"人已观看");

        videoReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withString("title", "憨妈直播")
                        .withBoolean("isinhome",false)
                        .withBoolean("leftnow",true)
                        .withBoolean("videoshop",true)
                        .withString("url", item.playbackUrl)
                        .navigation();
            }
        });

    }

    private void initView() {

    }
}
