package com.health.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.health.mall.R;
import com.healthy.library.model.GoodsModel;

/**
 * @author Li
 * @date 2019/03/06 09:29
 * @des 服务
 */

public class ServiceItemSection extends FrameLayout {

    private ImageView mImgItem;
    private TextView mTxtItemTitle;


    public ServiceItemSection(@NonNull Context context) {
        this(context, null);
    }

    public ServiceItemSection(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceItemSection(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_section_service_item, this);
        mTxtItemTitle = findViewById(R.id.txt_item_title);
        mImgItem = findViewById(R.id.img_item);
    }

    public void setImageResource(int resource) {
        mImgItem.setImageResource(resource);
    }

    public void setTxtItemTitle(CharSequence charSequence) {
        mTxtItemTitle.setText(charSequence);
    }

    public void setData(GoodsModel.ServiceItem model) {
        setTxtItemTitle(model.serviceName);
    }


}
