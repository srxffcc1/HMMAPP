package com.health.index.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.VaccineModel;

/**
 * @author Li
 * @date 2019/04/23 14:20
 * @des 疫苗
 */
public class VaccineAdapter extends BaseQuickAdapter<VaccineModel, BaseViewHolder> {
    public VaccineAdapter() {
        this(R.layout.index_item_vaccine);
    }

    private VaccineAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, VaccineModel item) {
        helper.setText(R.id.tv_date, item.getVaccinationTime());
        helper.setText(R.id.tv_title, item.getVaccineName());
        helper.setText(R.id.tv_age, item.getVaccineAge());
        helper.setText(R.id.tv_reason, item.getReason());
        if(TextUtils.isEmpty(item.getVaccinationTime())){
            helper.getView(R.id.tv_date).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
        }
    }
}
