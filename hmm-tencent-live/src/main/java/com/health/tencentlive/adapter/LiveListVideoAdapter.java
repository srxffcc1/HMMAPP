package com.health.tencentlive.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.health.tencentlive.R;
import com.healthy.library.LibApplication;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.business.LivePassWordDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

public class LiveListVideoAdapter extends BaseAdapter<LiveVideoMain> {


    @Override
    public int getItemViewType(int position) {
        return 77;
    }

    public LiveListVideoAdapter() {
        this(R.layout.item_t_live_list_squre);
    }

    private LiveListVideoAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
        helper.setMargin(6, 0, 6, 0);
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {

        final LiveVideoMain model = getDatas().get(i);
        CornerImageView videoIcon;
        ImageTextView videoBack;
        TextView videoMans;
        TextView videoTitle;
        TextView videoTime;
        ImageTextView videoPass;
        videoIcon = (CornerImageView) baseHolder.itemView.findViewById(R.id.videoIcon);
        videoBack = (ImageTextView) baseHolder.itemView.findViewById(R.id.videoBack);
        videoMans = (TextView) baseHolder.itemView.findViewById(R.id.videoMans);
        videoTitle = (TextView) baseHolder.itemView.findViewById(R.id.videoTitle);
        videoTime = (TextView) baseHolder.itemView.findViewById(R.id.videoTime);
        videoPass = (ImageTextView) baseHolder.itemView.findViewById(R.id.videoPass);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(model.picUrl)

                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into(videoIcon);
        videoTitle.setText(model.courseTitle);
        videoTime.setText("直播时间：" + model.beginTime);
        //洋洋说virtualTimesWatchedReplay不用乘以17
        videoMans.setText(ParseUtils.parseNumber(model.popularity * 17 + model.virtualTimesWatchedReplay + "", 10000, "w") + "观看");

        videoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.status == 1) {//预告

                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LiveNotice)
                            .withString("courseId", model.id)
                            .navigation();
                } else {
                    if (model.type == 2&&!SpUtils.getValue(LibApplication.getAppContext(),model.id+"Pass",false)) {
                        LivePassWordDialog.newInstance()
                                .setLivePassWordListener(new LivePassWordDialog.LivePassWordListener() {
                                    @Override
                                    public void onPassSucess() {
                                        ARouter.getInstance()
                                                .build(TencentLiveRoutes.LIVE_LOOK)
                                                .withString("courseId", model.id)
                                                .navigation();
                                    }
                                })
                                .setCourseId(model.id)
                                .show(((BaseActivity) context).getSupportFragmentManager(), "私密支付");
                    } else {
                        ARouter.getInstance()
                                .build(TencentLiveRoutes.LIVE_LOOK)
                                .withString("courseId", model.id)
                                .navigation();
                    }

                }

            }
        });
        videoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVEDETAIL)
                        .withString("courseId", model.id)
                        .navigation();
            }
        });
        videoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVEDETAIL)
                        .withString("courseId", model.id)
                        .navigation();
            }
        });
    }

    private void initView() {

    }
}
