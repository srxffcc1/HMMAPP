package com.health.city.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.city.R;
import com.healthy.library.model.Topic;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class TipAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {

    String fragmentType="";

    public void setOnTipClickListener(OnTipClickListener onTipClickListener) {
        this.onTipClickListener = onTipClickListener;
    }

    OnTipClickListener onTipClickListener;

    public TipAdapter() {
        this(R.layout.city_item_tiplist);
    }

    private TipAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final Topic item) {
         TextView tipName;
         TextView tipJoinCount;
        tipName = (TextView) helper.itemView.findViewById(R.id.tipName);
        tipJoinCount = (TextView) helper.itemView.findViewById(R.id.tipJoinCount);
        tipName.setText(item.topicName);
        tipJoinCount.setText(item.partInNum+"人参与");
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTipClickListener.clickTip(view,item);
            }
        });

    }

    private void initView() {


    }

    public void setTipType(String fragmentType) {
        this.fragmentType=fragmentType;
    }
    public interface OnTipClickListener{
        void clickTip(View view, Topic topic);
    }
}
