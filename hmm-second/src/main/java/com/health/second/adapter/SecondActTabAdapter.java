package com.health.second.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.legacy.widget.Space;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.SpanUtils;

public class SecondActTabAdapter extends BaseAdapter<String> {

    private RadioButton checkA;
    private RadioButton checkB;
    private RadioButton checkC;
    public int selectPostion=0;

    @Override
    public int getItemViewType(int position) {
        return 29;
    }

    public SecondActTabAdapter() {
        this(R.layout.item_second_acttab_bar);
    }

    private SecondActTabAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ImageView actsPost;
        RadioGroup actsRadioGroup;

        Space actsRadioSpace;
        LinearLayout actsRadioSpaceBottom;
        actsPost = (ImageView) baseHolder.itemView.findViewById(R.id.actsPost);
        actsRadioGroup = (RadioGroup) baseHolder.itemView.findViewById(R.id.actsRadioGroup);
        checkA = (RadioButton) baseHolder.itemView.findViewById(R.id.checkA);
        checkB = (RadioButton) baseHolder.itemView.findViewById(R.id.checkB);
        checkC = (RadioButton) baseHolder.itemView.findViewById(R.id.checkC);
        actsRadioSpace = (Space) baseHolder.itemView.findViewById(R.id.actsRadioSpace);
        actsRadioSpaceBottom = (LinearLayout) baseHolder.itemView.findViewById(R.id.actsRadioSpaceBottom);

        checkA.setText(SpanUtils.getBuilder(context, "秒抢").setProportion(1.8f).setBold().append("\n限时限量").create());
        checkB.setText(SpanUtils.getBuilder(context, "拼团").setProportion(1.8f).setBold().append("\n拼着买划算").create());
        checkC.setText(SpanUtils.getBuilder(context, "砍价").setProportion(1.8f).setBold().append("\n砍至低价").create());
        actsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (moutClickListener != null) {
                    int position = 0;
                    if (checkedId == R.id.checkA) {
                        position = 0;
                    } else if (checkedId == R.id.checkB) {
                        position = 1;
                    } else if (checkedId == R.id.checkC) {
                        position = 2;
                    }
                    moutClickListener.outClick("tabChange", position);
                }
            }
        });
    }

    public void setGroupChange(int position) {
        switch (position){
            case 0:
                checkA.setChecked(true);
                break;
            case 1:
                checkB.setChecked(true);
                break;
            case 2:
                checkC.setChecked(true);
                break;
        }
    }

    private void initView() {

    }
}
