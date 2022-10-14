package com.healthy.library.business;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.healthy.library.R;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.widget.PopupWindowCompat;
import com.healthy.library.widget.WarpLinearLayout;

import java.util.List;


public class VideoListTabPopWindow extends PopupWindowCompat {
    private View contentView;
    private Context context;
    private ItemClickCallBack itemClickCallBack;
    private LinearLayout llContent;
    private WarpLinearLayout lableLL;
    private ConstraintLayout upLL;

    public VideoListTabPopWindow(Context context, final ItemClickCallBack itemClickCallBack) {
        this.context = context;
        this.itemClickCallBack = itemClickCallBack;
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_videolist_popowindow, null);
        llContent = (LinearLayout) contentView.findViewById(R.id.ll_content);
        lableLL = (WarpLinearLayout) contentView.findViewById(R.id.lableLL);
        upLL = (ConstraintLayout) contentView.findViewById(R.id.upLL);
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
        this.setFocusable(true);
        this.setOutsideTouchable(true);
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

    public void setData(List<VideoCategory> list) {

        addFunctions(list);
    }

    public interface ItemClickCallBack {
        void click(List<VideoCategory> list);

        void dismiss();
    }

    private void addFunctions(List<VideoCategory> modelList) {
        upLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickCallBack != null) {
                    itemClickCallBack.dismiss();
                }
            }
        });
        buildLable(modelList);
    }

    private void buildLable(final List<VideoCategory> list) {
        lableLL.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_txt_lable_layout, null);
            TextView lable = view.findViewById(R.id.lable);
            lable.setText(list.get(i).categoryName);
            if (list.get(i).isSelect) {
                lable.setTextColor(Color.parseColor("#AC33BA"));
                lable.setBackgroundResource(R.drawable.shape_video_list_lable_bg_select);
            } else {
                lable.setTextColor(Color.parseColor("#333333"));
                lable.setBackgroundResource(R.drawable.shape_video_list_lable_bg_unselect);
            }
            final int pos = i;
            lable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setData(pos, list);
                }
            });
            lableLL.addView(view);
        }
    }

    private void setData(final int pos, final List<VideoCategory> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i == pos) {
                list.get(i).isSelect = true;
            } else {
                list.get(i).isSelect = false;
            }
        }
        buildLable(list);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemClickCallBack.click(list);
                itemClickCallBack.dismiss();
            }
        }, 100);
    }
}
