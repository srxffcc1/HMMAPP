package com.health.mine.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.health.mine.model.ServiceRecordModel;
import com.healthy.library.constant.Constants;

/**
 * @author Li
 * @date 2019/05/28 16:02
 * @des
 */

public class ServiceRecordAdapter extends BaseQuickAdapter<ServiceRecordModel, BaseViewHolder> {

    private String mCourseStyle;

    public ServiceRecordAdapter(int layoutResId, String courseStyle) {
        super(layoutResId);
        mCourseStyle = courseStyle;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceRecordModel item) {
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_date, item.getServiceDate());
        helper.setText(R.id.tv_name, item.getTechnicianName());

        TextView tvCount = helper.getView(R.id.tv_count);
        if (Constants.SERVICE_IN_TIME.equals(mCourseStyle)) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_count, item.getLeftCount());
        }

        if (position == 0) {
            helper.setTextColor(R.id.tv_date, Color.BLACK);
        } else {
            helper.setTextColor(R.id.tv_date, Color.parseColor("#444444"));
        }
    }

}
