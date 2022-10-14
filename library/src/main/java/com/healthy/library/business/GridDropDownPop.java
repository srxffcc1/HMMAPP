package com.healthy.library.business;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.healthy.library.R;
import com.healthy.library.adapter.DropDownType;
import com.healthy.library.adapter.GridDropDownAdapter;
import com.healthy.library.widget.PopupWindowCompat;

import java.util.List;


public class GridDropDownPop extends PopupWindowCompat {
    RecyclerView rv;
    GridDropDownAdapter adapter;
    private View contentView;



    public GridDropDownPop(Context context, int count,final ItemClickCallBack itemClickCallBack) {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_dropdown_pop, null);
        rv = contentView.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(context, count));
        adapter = new GridDropDownAdapter(R.layout.item_choose_type, new View.OnClickListener() {
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
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
    }


    public GridDropDownPop(Context context, final ItemClickCallBack itemClickCallBack) {
        this(context,4,itemClickCallBack);
    }

    /**
     * 显示popupWindow
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
        adapter.notifyDataSetChanged();
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
