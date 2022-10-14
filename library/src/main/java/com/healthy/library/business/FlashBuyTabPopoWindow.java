package com.healthy.library.business;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.model.FlashBuyTab;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.PopupWindowCompat;

import java.util.List;


public class FlashBuyTabPopoWindow extends PopupWindowCompat {
    GridLayout popGrid;
    private View contentView;
    private Context context;
    private ItemClickCallBack itemClickCallBack;

    public FlashBuyTabPopoWindow(Context context, final ItemClickCallBack itemClickCallBack) {
        this.context = context;
        this.itemClickCallBack = itemClickCallBack;
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_flashbuy_popowindow, null);
        popGrid = contentView.findViewById(R.id.popGrid);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (itemClickCallBack != null) {
                    itemClickCallBack.dismiss();
                }
            }
        });
        this.setContentView(contentView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
       /* this.setFocusable(true);
        this.setOutsideTouchable(true);*/
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
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

    public void setData(List<FlashBuyTab> list, int posion) {
        addFunctions(popGrid, list, posion);
    }

    public interface ItemClickCallBack {
        void click(int id, String name);

        void dismiss();
    }

    private void addFunctions(GridLayout gridLayout, final List<FlashBuyTab> modelList, int posion) {
        gridLayout.removeAllViews();
        {
            int row = 4;
            int mMargin = (int) TransformUtil.dp2px(context, 10);
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 4) * row;
            gridLayout.setLayoutParams(gridlayoutparm);
            gridLayout.setColumnCount(row);
            int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 4);
            for (int i = 0; i < modelList.size(); i++) {
                final FlashBuyTab model = modelList.get(i);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                View view = LayoutInflater.from(context).inflate(R.layout.item_pop_grid_layout, gridLayout, false);
                TextView markingName = view.findViewById(R.id.markingName);
                markingName.setText(model.PopLabelName);
                if (i == posion) {
                    markingName.setTextColor(Color.parseColor("#FA3C5A"));
                } else {
                    markingName.setTextColor(Color.parseColor("#222222"));
                }
                final int j = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickCallBack != null) {
                            itemClickCallBack.click(j, model.PopLabelName);
                        }
                        dismiss();
                    }
                });
                gridLayout.addView(view, params);
            }
        }

    }
}
