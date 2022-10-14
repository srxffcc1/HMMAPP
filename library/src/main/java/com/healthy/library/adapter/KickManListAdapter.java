package com.healthy.library.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.R;
import com.healthy.library.model.KickUser;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CornerImageView;


/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class KickManListAdapter extends BaseQuickAdapter<KickUser, BaseViewHolder> {




    public KickManListAdapter() {
        this(R.layout.dis_item_activity_disact_go_item);
    }

    private KickManListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final KickUser item) {
        if (item == null) {
            helper.getView(R.id.readlView).setVisibility(View.GONE);
            helper.getView(R.id.noMsgCon).setVisibility(View.VISIBLE);
            return;
        } else {
            helper.getView(R.id.readlView).setVisibility(View.VISIBLE);
            helper.getView(R.id.noMsgCon).setVisibility(View.GONE);
        }
         CornerImageView helpIcon;
         TextView helpName;
         TextView helpTip;
         TextView helpMoney;
        helpIcon = (CornerImageView) helper.getView(R.id.helpIcon);
        helpName = (TextView) helper.getView(R.id.helpName);
        helpTip = (TextView) helper.getView(R.id.helpTip);
        helpMoney = (TextView) helper.getView(R.id.helpMoney);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(item.friendFaceUrl)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into(helpIcon);
        helpName.setText(item.friendName);
;

        helpMoney.setText(SpanUtils.getBuilder(mContext,"砍掉").setForegroundColor(Color.parseColor("#444444"))
                .append(item.discountMoney+"").setForegroundColor(Color.parseColor("#F02846"))
                .append("元").setForegroundColor(Color.parseColor("#444444")).create());
        helpTip.setText(item.content);

    }

    private void initView() {

    }
}
