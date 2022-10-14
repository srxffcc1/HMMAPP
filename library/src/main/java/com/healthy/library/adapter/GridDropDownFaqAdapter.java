package com.healthy.library.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.R;


/**
 * @author: luoyang
 * @description:
 */
public class GridDropDownFaqAdapter extends BaseQuickAdapter<DropDownType, BaseViewHolder> {

    private String selectId = "";
    private View.OnClickListener onClickListener;

    public GridDropDownFaqAdapter() {
        super(R.layout.item_choose_type);
    }



    public GridDropDownFaqAdapter(int layoutResId, View.OnClickListener onClickListener) {
        super(layoutResId);
        this.onClickListener=onClickListener;
    }
    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }

    private String selectName = "";

    public String getSelectId() {
        return selectId;
    }
    public String getSelectName() {
        return selectName;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DropDownType item) {
        helper.setText(R.id.tv_type, item.getName());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectId(item.getId());
                setSelectName(item.getName());
                notifyDataSetChanged();
                onClickListener.onClick(view);
            }
        });
        helper.setTextColor(R.id.tv_type, selectId.equals(item.getId()) ? Color.parseColor("#3E7E59"): Color.parseColor("#666666"));
        helper.getView(R.id.tv_type).setBackgroundResource(selectId.equals(item.getId()) ? R.drawable.shape_choose_type_selected_faq : R.drawable.shape_choose_type_normal);
    }

}
