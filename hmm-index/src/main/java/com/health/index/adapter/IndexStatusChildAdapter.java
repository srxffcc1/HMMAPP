package com.health.index.adapter;

import android.graphics.Bitmap;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.text.HtmlCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.constants.Changes;
import com.health.index.model.IndexStatusRecyclerBean;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.TimestampUtils;

import java.util.Date;

public class IndexStatusChildAdapter extends BaseQuickAdapter<IndexStatusRecyclerBean, BaseViewHolder> {
    public void setClasszz(String classzz) {
        this.classzz = classzz;
    }

    public String classzz="备孕";//备孕 孕期 产后
    public Date getDate() {
        return mDate == null ? new Date() : mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    private Date mDate;


    @Override
    public int getItemViewType(int position) {
        return 8;
    }
    public IndexStatusChildAdapter() {
        super(R.layout.index_mon_header_for_pregnancy_child);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IndexStatusRecyclerBean item) {

        String content = "";
        if(item.mon2son == 1){
            content = "<strong><font color=\"#333333\">宝宝变化:</font></strong>";
        } else if(item.mon2son == 0){
            content = "<strong><font color=\"#333333\">妈妈变化:</font></strong>";
        }
        content += item.content;
        helper.setText(R.id.tv_pregnancy_content, HtmlCompat.fromHtml(content,HtmlCompat.FROM_HTML_MODE_COMPACT));

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("孕期".equals(classzz)){//孕期
                    if(item.mon2son==0){
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_CHANGES)
                                .withInt("type", Changes.CHANGE_TYPE_MOM)
                                .withInt("period", Changes.CHANGE_PERIOD_PREGNANCY)
                                .withString("queryDate", TimestampUtils.timestamp2String(getDate().getTime(),
                                        "yyyy-MM-dd"))
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_CHANGES)
                                .withInt("type", Changes.CHANGE_TYPE_BABY)
                                .withInt("period", Changes.CHANGE_PERIOD_PREGNANCY)
                                .withString("queryDate", TimestampUtils.timestamp2String(getDate().getTime(),
                                        "yyyy-MM-dd"))
                                .navigation();
                    }
                }else {//产后
                    if(item.mon2son==0){
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_CHANGES)
                                .withInt("type", Changes.CHANGE_TYPE_MOM)
                                .withInt("period", Changes.CHANGE_PERIOD_PARENTING)
                                .withString("queryDate", TimestampUtils.timestamp2String(getDate().getTime(),
                                        "yyyy-MM-dd"))
                                .navigation();
                    }else {
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_CHANGES)
                                .withInt("type", Changes.CHANGE_TYPE_BABY)
                                .withInt("period", Changes.CHANGE_PERIOD_PARENTING)
                                .withString("queryDate", TimestampUtils.timestamp2String(getDate().getTime(),
                                        "yyyy-MM-dd"))
                                .navigation();
                    }
                }
            }
        });

    }


}