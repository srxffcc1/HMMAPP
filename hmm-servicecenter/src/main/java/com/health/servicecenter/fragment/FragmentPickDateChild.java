package com.health.servicecenter.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.health.servicecenter.R;
import com.healthy.library.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentPickDateChild extends BaseFragment {
    private RadioButton checkA;
    private RadioButton checkB;

    public void setOnFragmentDateClickListener(OnFragmentDateClickListener onFragmentDateClickListener) {
        this.onFragmentDateClickListener = onFragmentDateClickListener;
    }

    OnFragmentDateClickListener onFragmentDateClickListener;


    public static FragmentPickDateChild newInstance() {
        FragmentPickDateChild fragment = new FragmentPickDateChild();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_time_select_child;
    }

    @Override
    protected void findViews() {
        initView();
    }

    private void initView() {
        checkA = (RadioButton) findViewById(R.id.checkA);
        checkB = (RadioButton) findViewById(R.id.checkB);
        checkA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if("notcheck".equals(checkA.getTag())){
                    Toast.makeText(getContext(), "时间已过不能选中", Toast.LENGTH_SHORT).show();
                    checkA.setChecked(false);
                    return;
                }
                if (isChecked && checkA.getVisibility() == View.VISIBLE && checkA.getTag()==null) {
                    if (onFragmentDateClickListener != null) {
                        onFragmentDateClickListener.onFragmentDateClick("上午");
                    }
                }
            }
        });
        checkB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if("notcheck".equals(checkB.getTag())){
                    Toast.makeText(getContext(), "时间已过不能选中", Toast.LENGTH_SHORT).show();
                    checkB.setChecked(false);
                    return;
                }
                if (isChecked && checkB.getVisibility() == View.VISIBLE && checkB.getTag()==null) {
                    if (onFragmentDateClickListener != null) {
                        onFragmentDateClickListener.onFragmentDateClick("下午");
                    }
                }
            }
        });
    }

    public void refresh(String time, String date) {
        try {
            if (TextUtils.isEmpty(time)) {
                checkA.setChecked(false);
                checkB.setChecked(false);
            } else {
                if ("上午".equals(time)) {
                    checkA.setChecked(true);
                } else {
                    checkB.setChecked(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            checkA.setTag(null);
            checkA.setVisibility(View.VISIBLE);
            checkB.setVisibility(View.VISIBLE);
            if (new SimpleDateFormat("yyyy-MM-dd").format(new Date()).equals(date)) {//说明选择的是今天 则需要判断是不是大于12点 上午不可选
                Calendar calendar12 = Calendar.getInstance();
                calendar12.set(Calendar.MINUTE, 0);
                calendar12.set(Calendar.HOUR_OF_DAY, 11);
                calendar12.set(Calendar.SECOND, 0);
                if (new Date().getTime() > calendar12.getTimeInMillis()) {//当前时间大于12点 算下午
                    checkA.setTag("notcheck");
                    ////System.out.println("设置不能选择");
                }
                checkA.setText("上午");
                checkB.setText("下午");
            } else {
                checkA.setText("上午");
                checkB.setText("下午");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface OnFragmentDateClickListener {
        void onFragmentDateClick(String result);
    }
}
