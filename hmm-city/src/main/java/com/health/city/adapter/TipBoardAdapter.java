package com.health.city.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
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

public class TipBoardAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {

    public void setFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
    }

    String fragmentType="";

    public void setOnTipBoardClickListener(OnTipBoardClickListener onTipBoardClickListener) {
        this.onTipBoardClickListener = onTipBoardClickListener;
    }

    OnTipBoardClickListener onTipBoardClickListener;
    public TipBoardAdapter() {
        this(R.layout.city_item_activity_tip_board);
    }

    private TipBoardAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final Topic item) {

         TextView num;
         TextView tip;
         TextView tipCount;
         TextView manCount;
         ImageView isHot;


        num = (TextView) helper.itemView.findViewById(R.id.num);
        tip = (TextView) helper.itemView.findViewById(R.id.tip);
        tipCount = (TextView) helper.itemView.findViewById(R.id.tipCount);
        manCount = (TextView) helper.itemView.findViewById(R.id.manCount);
        isHot = (ImageView) helper.itemView.findViewById(R.id.isHot);
        isHot.setVisibility(View.GONE);
        if(helper.getPosition()==0){
            isHot.setVisibility(View.VISIBLE);
            num.setTextColor(Color.parseColor("#FF544F"));
        }
        else if(helper.getPosition()==1){

            num.setTextColor(Color.parseColor("#FA8800"));
        }
        else if(helper.getPosition()==2){

            num.setTextColor(Color.parseColor("#F7C700"));
        }else {

            num.setTextColor(Color.parseColor("#9596A4"));
        }
        tip.setText(item.topicName);
        manCount.setText(item.partInNum+"人参与");
        tipCount.setText(item.postingNum+"篇帖子");
        num.setText(String.format("%02d", (helper.getPosition()+1)));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onTipBoardClickListener!=null){
                    onTipBoardClickListener.clickTip(view,item);
                }
            }
        });
    }

    public interface OnTipBoardClickListener{
        void clickTip(View view, Topic topic);
    }
}
