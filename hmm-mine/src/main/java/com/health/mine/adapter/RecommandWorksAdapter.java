package com.health.mine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.QiYeWeXinWorker;
import com.healthy.library.utils.MMiniPass;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;

import java.util.List;

public class RecommandWorksAdapter extends BaseAdapter<QiYeWeXinWorkShop> {




    public RecommandWorksAdapter() {
        this(R.layout.mine_adapter_recommandworks);
    }


    private RecommandWorksAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView shopName;
        LinearLayout recommandQWX;
        shopName = (TextView) holder.getView(R.id.shopName);
        QiYeWeXinWorkShop qiYeWeXinWorkShop = getDatas().get(position);
        shopName.setText(qiYeWeXinWorkShop.shopName);
        recommandQWX = (LinearLayout) holder.getView(R.id.recommandQWX);
        buildList(recommandQWX, qiYeWeXinWorkShop.tokerWorkerList);
    }

    public void buildList(LinearLayout linearLayout, List<QiYeWeXinWorker> qiYeWeXinWorkers) {
        linearLayout.removeAllViews();
        for (int i = 0; i < qiYeWeXinWorkers.size(); i++) {
            final QiYeWeXinWorker qiYeWeXinWorker=qiYeWeXinWorkers.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_qwx_worker_layout, linearLayout, false);
             CornerImageView workImg;
             TextView workName;
            workImg = (CornerImageView)view. findViewById(R.id.workImg);
            workName = (TextView) view.findViewById(R.id.workName);
            GlideCopy.with(context)
                    .load(qiYeWeXinWorker.professionalPhoto)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    .into(workImg);
            workName.setText(qiYeWeXinWorker.personName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MMiniPass.passMini("gh_f9b4fbd9d3b8",String.format("pages/mine/servicer/jionGroup?merchantId=%s&shopId=%s&workcode=%s&type=%s&groupId=%s"
                            , SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC)
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP)
                            ,qiYeWeXinWorker.referralCode
                            ,"2"
                            ,""
                    ));
                }
            });
            linearLayout.addView(view);
        }
    }

    private void initView() {

    }
}
