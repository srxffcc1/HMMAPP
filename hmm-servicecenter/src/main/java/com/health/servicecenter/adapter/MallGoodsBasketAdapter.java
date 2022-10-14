package com.health.servicecenter.adapter;

import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.IncreaseDecreaseView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MallGoodsBasketAdapter{

    OnGoodsChangeListener onGoodsChangeListener;
    OnGoodsCountChangeListener onGoodsCountChangeListener;

    public void setOnGoodsChangeListener(OnGoodsChangeListener onGoodsChangeListener) {
        this.onGoodsChangeListener = onGoodsChangeListener;
    }

    public void setOnGoodsCountChangeListener(OnGoodsCountChangeListener onGoodsCountChangeListener) {
        this.onGoodsCountChangeListener = onGoodsCountChangeListener;
    }


    public interface OnGoodsCountChangeListener {
        void onGoodsCountChange(GoodsBasketCell goodsBasketCell, int count);
    }

    public interface OnGoodsChangeListener {
        void onGoodsAdd();
        void onGoodsRemove();
    }
}
