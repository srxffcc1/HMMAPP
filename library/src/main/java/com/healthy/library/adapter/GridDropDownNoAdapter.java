package com.healthy.library.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.R;


/**
 * @author: luoyang
 * @description:
 */
public class GridDropDownNoAdapter extends BaseQuickAdapter<DropDownType, BaseViewHolder> {

    private String selectId = "";
    private View.OnClickListener onClickListener;

    public GridDropDownNoAdapter() {
        super(R.layout.item_choose_type);
    }



    public GridDropDownNoAdapter(int layoutResId, View.OnClickListener onClickListener) {
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
        if(selectId==null){
            selectId="";
        }
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
        TextView tv_typet=helper.getView(R.id.tv_type);
        if(helper.getPosition()==0){
            tv_typet.getPaint().setFakeBoldText(true);
            helper.setTextColor(R.id.tv_type, selectId.equals(item.getId()) ? Color.parseColor("#FF6266"): Color.parseColor("#222222"));
        }else {

            tv_typet.getPaint().setFakeBoldText(false);
            helper.setTextColor(R.id.tv_type, selectId.equals(item.getId()) ? Color.parseColor("#FF6266"): Color.parseColor("#666666"));
        }

        helper.getView(R.id.tv_type).setBackgroundResource(selectId.equals(item.getId()) ? R.drawable.shape_choose_type_selected : R.drawable.shape_choose_type_normal);
    }

}
