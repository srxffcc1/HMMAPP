package com.health.mall.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.mall.R;
import com.health.mall.model.PeopleListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.widget.CornerFitCenterImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class PeopleListAdapter extends BaseAdapter<PeopleListModel> {

    public PeopleListAdapter() {
        this(R.layout.item_people_list_layout);
    }

    public PeopleListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        CornerFitCenterImageView peopleImg = holder.itemView.findViewById(R.id.peopleImg);
        TextView peopleName = holder.itemView.findViewById(R.id.peopleName);
        TextView peopleLable = holder.itemView.findViewById(R.id.peopleLable);
        TextView peopleYear = holder.itemView.findViewById(R.id.peopleYear);
        final PeopleListModel model = getDatas().get(position);
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(model.faceUrl)
                .into(peopleImg);
        peopleName.setText(model.realName);
        peopleLable.setText(model.tagTechnician);
        peopleYear.setText(model.positionTechnician);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "服务人员列表页-服务人员图片点击量");
                MobclickAgent.onEvent(context, "event2APPPeopleListImgClick", nokmap);
                ARouter.getInstance()
                        .build(MallRoutes.MALL_STORE_MAN_DETAIL)
                        .withString("userId", model.id)
                        .navigation();
            }
        });
    }
}
