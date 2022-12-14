package com.healthy.library.business;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.GridDropDownFaqAdapter;
import com.healthy.library.widget.PopupWindowCompat;

import java.util.List;


public class GridDropDownPopFaq extends PopupWindowCompat {
    RecyclerView rv;
    GridDropDownFaqAdapter adapter;
    private View contentView;

    public GridDropDownPopFaq(Context context, final ItemClickCallBack itemClickCallBack) {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_dropdown_pop, null);
        rv = contentView.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(context, 4));
        adapter = new GridDropDownFaqAdapter(R.layout.item_choose_type, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                adapter.setSelectId(adapter.getItem(position).getId());
//                adapter.setSelectName(adapter.getItem(position).getName());
//                adapter.notifyDataSetChanged();
                if (itemClickCallBack != null) {
                    itemClickCallBack.click(adapter.getSelectId(),adapter.getSelectName());
                }
                dismiss();
            }
        });
        rv.setAdapter(adapter);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (itemClickCallBack != null) {
                    itemClickCallBack.dismiss();
                }
            }
        });
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(contentView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
       /* this.setFocusable(true);
        this.setOutsideTouchable(true);*/
        this.update();
        // ???????????????ColorDrawable??????????????????
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // ???back??????????????????????????????,???????????????????????????OnDismisslistener ????????????????????????????????????
        this.setBackgroundDrawable(dw);
    }

    /**
     * ??????popupWindow
     *
     * @param v
     */
    public void showPopupWindow(View v) {
        if (!this.isShowing()) {
            this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            this.update();
            this.showAsDropDown(v);
        } else {
            this.dismiss();
        }
    }
    public void setSelectId(String selectId){
        adapter.setSelectId(selectId);
    }

    public void setData(List<DropDownType> list) {
        adapter.setNewData(list);
        adapter.setSelectId(list.get(0).getId());
        adapter.notifyDataSetChanged();
    }

    public interface ItemClickCallBack {
        void click(String id, String name);

        void dismiss();
    }
}
