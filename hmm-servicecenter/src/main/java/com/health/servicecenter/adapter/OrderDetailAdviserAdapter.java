package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.utils.MMiniPass;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;

public class OrderDetailAdviserAdapter extends BaseAdapter<TokerWorkerInfoModel> {

    public void setType(int type) {
        this.type = type;
    }

    public int type=-1;//-1 缺省 0显示的是导购 1显示的导购为公共顾问

    public OrderDetailAdviserAdapter() {
        this(R.layout.item_qwx_worker_order);
    }

    public OrderDetailAdviserAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        ImageView workBg = (ImageView) holder.itemView.findViewById(R.id.workBg);
        CornerImageView workImg = (CornerImageView) holder.itemView.findViewById(R.id.workImg);
        TextView workName = (TextView) holder.itemView.findViewById(R.id.workName);
        TextView workButton = (TextView) holder.itemView.findViewById(R.id.workButton);
        if(type==0){
            workButton.setText("点击可快速联系您的母婴顾问");
        }else {
            workButton.setText("点击进入专属母婴顾问通道");
        }
        final TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean bindingListBean = getModel().bindingList.get(0).bindingTokerWorker;
        com.healthy.library.businessutil.GlideCopy.with(getContext())
                .load(bindingListBean.professionalPhoto)
                .error(R.drawable.img_avatar_default)
                .placeholder(R.drawable.img_avatar_default)
                .into(workImg);
        if (bindingListBean.personName != null && !TextUtils.isEmpty(bindingListBean.personName)) {
            workName.setText(bindingListBean.personName);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==0){//
                    MMiniPass.passMini("gh_f9b4fbd9d3b8",String.format("pages/mine/servicer/jionGroup?merchantId=%s&shopId=%s&workcode=%s&type=%s&groupId=%s"
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC)
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP)
                            ,bindingListBean.referralCode
                            ,"2"
                            ,""
                    ));
                }else {//
                    MMiniPass.passMini("gh_f9b4fbd9d3b8",String.format("pages/mine/servicer/jionGroup?merchantId=%s&shopId=%s&workcode=%s&type=%s&groupId=%s"
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC)
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP)
                            ,bindingListBean.referralCode
                            ,"1"
                            ,""
                    ));
                }
            }
        });

    }
}
