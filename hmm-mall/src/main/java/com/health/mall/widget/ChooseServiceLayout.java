package com.health.mall.widget;

import android.content.Context;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.mall.R;
import com.healthy.library.model.GoodsModel;
import com.healthy.library.widget.IncreaseDecreaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/05 16:15
 * @des 下单-选择服务
 */

public class ChooseServiceLayout extends FrameLayout {

    /**
     * 单选
     */
    public static final int MODE_RADIO = 1;
    /**
     * 复选
     */
    public static final int MODE_CHECK_BOX = 2;
    /**
     * 复选时-最大选中数量
     */
    private int mMaxSelectedNum = 0;
    private TextView mTxtServiceTitle;
    private LinearLayout mLayoutServices;
    private int mCurrentMode = MODE_RADIO;

    public IncreaseDecreaseView getmIncreaseDecreaseView() {
        return mIncreaseDecreaseView;
    }

    private IncreaseDecreaseView mIncreaseDecreaseView;
    private List<GoodsModel.ServiceItem> mServiceItemList;

    public ChooseServiceLayout(@NonNull Context context) {
        this(context, null);
    }

    public ChooseServiceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseServiceLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_choose_service, this);
        mTxtServiceTitle = findViewById(R.id.txt_service_title);
        mLayoutServices = findViewById(R.id.layout_services);
        mIncreaseDecreaseView = findViewById(R.id.increase_decrease);
    }

    public void setTxtServiceTitle(CharSequence charSequence) {
        mTxtServiceTitle.setText(charSequence);
    }

    public void setServiceItems(List<GoodsModel.ServiceItem> list) {
        mServiceItemList = list;
        for (GoodsModel.ServiceItem serviceItem : list) {
            final ServiceItemSection itemSection = new ServiceItemSection(getContext());
            itemSection.setData(serviceItem);
            int res = mCurrentMode == MODE_RADIO ?
                    R.drawable.selector_radio : R.drawable.selector_check_box;
            itemSection.setImageResource(res);
            mLayoutServices.addView(itemSection);
            //单选
            if (mCurrentMode == MODE_RADIO) {
                itemSection.setOnClickListener(mRadioListener);
            } else {
                //复选
                itemSection.setOnClickListener(mCheckListener);
            }
        }


    }

    public void setOnNumChangedListener(IncreaseDecreaseView.OnNumChangedListener onNumChangedListener) {
        mIncreaseDecreaseView.setOnNumChangedListener(onNumChangedListener);

    }

    public void setChangeVisable(boolean b) {
        mIncreaseDecreaseView.setChangeVisable(b);
    }

    @IntDef({MODE_RADIO, MODE_CHECK_BOX})
    @interface Mode {
    }

    /**
     * 复选最大选择数量
     *
     * @param num 数量
     */
    public void setMaxSelectedNum(int num) {
        mMaxSelectedNum = num;
    }

    public void setMode(@Mode int mode) {
        mCurrentMode = mode;
    }

    /**
     * 单选状态
     * 判断是否有选中的
     * 如果有选中的
     * 将选中的设为未选中，并且将该条选中
     * 如果没有选中的 直接将其设为选中即可
     */
    private OnClickListener mRadioListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            for (int j = 0; j < mLayoutServices.getChildCount(); j++) {
                ServiceItemSection section = (ServiceItemSection) mLayoutServices.getChildAt(j);
                if (section != v && section.isSelected()) {
                    section.setSelected(false);
                }
            }
            v.setSelected(!v.isSelected());
        }
    };

    /**
     * 复选状态
     * 如果是选中的话
     * 取消选中
     * 如果未选中的话
     * 判断已选中的数量是否
     * 大于最大可选中数量
     */
    private OnClickListener mCheckListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.isSelected()) {
                v.setSelected(false);
            } else {
                int count = 0;
                for (int i = 0; i < mLayoutServices.getChildCount(); i++) {
                    View view = mLayoutServices.getChildAt(i);
                    if (view.isSelected()) {
                        count++;
                    }
                }
                if (count < mMaxSelectedNum) {
                    v.setSelected(true);
                }
            }
        }
    };

    public int getNum() {
        return mIncreaseDecreaseView.getNum();
    }



    public List<GoodsModel.ServiceItem> getSelectedServiceItems() {
        List<GoodsModel.ServiceItem> list = new ArrayList<>();
        for (int i = 0; i < mLayoutServices.getChildCount(); i++) {
            View view = mLayoutServices.getChildAt(i);
            if (view.isSelected()) {
                list.add(mServiceItemList.get(i));
            }
        }
        return list;
    }

    public int getSelectedSize() {
        return getSelectedServiceItems().size();
    }
}
