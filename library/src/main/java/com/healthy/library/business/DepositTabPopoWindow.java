package com.healthy.library.business;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.FlashBuyTab;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.PopupWindowCompat;

import java.util.List;


public class DepositTabPopoWindow extends PopupWindowCompat {
    private GridLayout popGrid;
    private RelativeLayout iconLL;
    private View contentView;
    private Context context;
    private ItemClickCallBack itemClickCallBack;

    public DepositTabPopoWindow(Context context, final ItemClickCallBack itemClickCallBack) {
        this.context = context;
        this.itemClickCallBack = itemClickCallBack;
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_deposit_popowindow, null);
        popGrid = contentView.findViewById(R.id.popGrid);
        iconLL = contentView.findViewById(R.id.iconLL);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (itemClickCallBack != null) {
                    itemClickCallBack.dismiss();
                }
            }
        });
        iconLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void setData(List<BalanceModel> list, int posion) {
        addFunctions(popGrid, list, posion);
    }

    public interface ItemClickCallBack {
        void click(int id, String name);

        void dismiss();
    }

    private void addFunctions(GridLayout gridLayout, final List<BalanceModel> modelList, int posion) {
        gridLayout.removeAllViews();
        {
            int row = 3;
            int mMargin = (int) TransformUtil.dp2px(context, 50);
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridlayoutparm.width = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3) * row;
            gridLayout.setLayoutParams(gridlayoutparm);
            gridLayout.setColumnCount(row);
            int w = ((ScreenUtils.getScreenWidth(context) - mMargin) / 3);
            for (int i = 0; i < modelList.size(); i++) {
                final BalanceModel model = modelList.get(i);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                View view = LayoutInflater.from(context).inflate(R.layout.item_deposit_pop_grid_layout, gridLayout, false);
                final TextView markingName = view.findViewById(R.id.markingName);
                markingName.setText("会员卡" + (i + 1));
                if (i == posion) {
                    markingName.setSelected(true);
                    markingName.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    markingName.setSelected(false);
                    markingName.setTextColor(Color.parseColor("#666666"));
                }
                final int j = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickCallBack != null) {
                            itemClickCallBack.click(j, markingName.getText().toString());
                        }
                        dismiss();
                    }
                });
                gridLayout.addView(view, params);
            }
        }

    }
}
