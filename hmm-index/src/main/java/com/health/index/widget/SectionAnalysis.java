package com.health.index.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.index.R;
import com.health.index.model.AnalyzeModel;
import com.healthy.library.utils.TransformUtil;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/25 17:49
 * @des B超解读项
 */
public class SectionAnalysis extends LinearLayout {
    public SectionAnalysis(Context context) {
        this(context, null);
    }

    public SectionAnalysis(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionAnalysis(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setData(List<AnalyzeModel> list) {
        removeAllViews();
        setShowDividers(SHOW_DIVIDER_MIDDLE);
        setDividerDrawable(getContext().getResources().
                getDrawable(R.drawable.shape_analysis_divider));
        for (AnalyzeModel model : list) {
            ConstraintLayout layout = new ConstraintLayout(getContext());

            TextView tvName = new TextView(getContext());
            TextView tvValue = new TextView(getContext());

            tvName.setId(R.id.index_analysis_name);
            tvValue.setId(R.id.index_analysis_value);

            tvName.setLines(1);
            tvName.setEllipsize(TextUtils.TruncateAt.END);

            tvName.setTextColor(Color.parseColor("#222222"));
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    TransformUtil.dp2px(getContext(), 13));
            tvName.setTypeface(Typeface.DEFAULT);

            tvValue.setTextColor(Color.parseColor("#222222"));
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    TransformUtil.dp2px(getContext(), 14));
            tvValue.setTypeface(Typeface.DEFAULT_BOLD);


            ConstraintLayout.LayoutParams valueParams = new Constraints.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            valueParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            valueParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            valueParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.addView(tvValue, valueParams);

            ConstraintLayout.LayoutParams nameParams = new Constraints.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            nameParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            nameParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            nameParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            nameParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            layout.addView(tvName, nameParams);

            tvName.setText(model.getName());
            tvValue.setText(model.getValue());
            addView(layout);
        }
    }
}
