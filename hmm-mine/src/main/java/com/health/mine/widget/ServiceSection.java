package com.health.mine.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.model.ServiceDetailModel;
import com.healthy.library.constant.Constants;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.TransformUtil;

import java.util.List;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/05/28 09:20
 * @des
 */
public class ServiceSection extends FrameLayout {
    private TextView mTvShopName;
    private LinearLayout mLayoutUnitService;

    public ServiceSection(@NonNull Context context) {
        this(context, null);
    }

    public ServiceSection(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceSection(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context)
                .inflate(R.layout.mine_section_service, this, true);
        mTvShopName = findViewById(R.id.tv_shop_name);
        mLayoutUnitService = findViewById(R.id.layout_service_unit);
    }

    public void setData(String shopName, List<ServiceDetailModel> list) {
        mTvShopName.setText(shopName);
        for (final ServiceDetailModel model : list) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.mine_service_unit, mLayoutUnitService, false);

            TextView tvServiceName = view.findViewById(R.id.tv_service_name);
            final TextView tvLeftCount = view.findViewById(R.id.tv_left_count);
            TextView tvNotice = view.findViewById(R.id.tv_notice);

            tvServiceName.setText(model.getServiceName());


            if (Constants.SERVICE_IN_NUMBER.equals(model.getCourseStyle())) {
                String content = String.format(Locale.CHINA, "剩 %d 次", model.getLeftCount());
                SpannableString spannableString = new SpannableString(content);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(13, true);
                int start = 1;
                int end = content.length() - 2;
                spannableString.setSpan(sizeSpan, start, end,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(styleSpan, start, end,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvLeftCount.setText(spannableString);
            } else {
                tvLeftCount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                String prefix = "截止";
                String suffix = DateUtils.formatTime2String("MM月dd日",
                        DateUtils.formatString2Time("yyyy-MM-dd HH:mm:ss",
                                model.getServiceEndDate()));
                tvLeftCount.setText(String.format("%s %s", prefix, suffix));
            }


            if (TextUtils.isEmpty(model.getDaysNotice())) {
                tvNotice.setVisibility(GONE);
            } else {
                tvNotice.setVisibility(VISIBLE);
                tvNotice.setText(model.getDaysNotice());
                int color;
                final int strokeWidth = (int) TransformUtil.dp2px(getContext(), 1);
                if (model.getDaysNumber() == 0 || model.getDaysNumber() == 1) {
                    color = Color.parseColor("#FF6266");
                    tvNotice.setBackground(new GradientDrawable() {
                        @Override
                        protected void onBoundsChange(Rect r) {
                            super.onBoundsChange(r);
                            float radius = Math.min(r.width(), r.bottom) >> 1;
                            float[] radii = new float[]{
                                    0, 0, radius, radius,
                                    radius, radius, radius, radius
                            };
                            setColor(Color.parseColor("#FFF1F1"));
                            setStroke(strokeWidth, Color.parseColor("#FF6266"));
                            setCornerRadii(radii);
                        }
                    });
                } else {
                    color = Color.parseColor("#18CDB3");
                    tvNotice.setBackground(new GradientDrawable() {
                        @Override
                        protected void onBoundsChange(Rect r) {
                            super.onBoundsChange(r);
                            float radius = Math.min(r.width(), r.bottom) >> 1;
                            float[] radii = new float[]{
                                    0, 0, radius, radius,
                                    radius, radius, radius, radius
                            };
                            setColor(Color.parseColor("#D5FFF9"));
                            setStroke(strokeWidth, Color.parseColor("#39E0C8"));
                            setCornerRadii(radii);
                        }
                    });
                }
                tvNotice.setTextColor(color);
            }
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_SERVICE_RECORD)
                            .withString("serviceId", model.getServiceId())
                            .withInt("shopId", model.getShopId())
                            .withInt("shopBrand", model.getShopBrand())
                            .withString("serviceName", model.getServiceName())
                            .withInt("totalCount", model.getTotalCount())
                            .withString("courseStyle", model.getCourseStyle())
                            .withString("endDate", tvLeftCount.getText().toString())
                            .navigation();
                }
            });
            mLayoutUnitService.addView(view);
        }
    }
}