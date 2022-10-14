package com.healthy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
//增加内部层数的RadioGroup
public class DeepRadioGroup extends RadioGroup {
    private boolean mProtectFromCheckedChange;
    private List<View> mcheckviewchild = new ArrayList<View>();
    private int childid;
    private int index=0;
    private String tag="but1";
    private OnCheckedChangeListener onCheckedChangeListener;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;

    public DeepRadioGroup(Context context) {
        super(context);
        init();
    }

    public DeepRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setCheck(int index){
        this.index=index;
        if(mcheckviewchild.size()>index){
            ((Checkable)(mcheckviewchild.get(index))).setChecked(true);
        }
    }
    public void setOnCheckedChangeListener(
            OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        link();
    }
    public void link() {
        List<View> tmp= getAllChildViews(DeepRadioGroup.this);
        mcheckviewchild.clear();
        int childcount = tmp.size();
        for (int i = 0; i < childcount; i++) {
            if (tmp.get(i) instanceof CompoundButton) {
                ((CompoundButton) (tmp.get(i))).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
                mcheckviewchild.add(tmp.get(i));
            }
        }
        if(mcheckviewchild.size()>index){
            ((CompoundButton)(mcheckviewchild.get(index))).setChecked(true);
        }
    }
    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
    }
    public static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();

        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            allchildren.add(view);
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.addAll(getAllChildViews(viewchild));

            }
        } else {
            allchildren.add(view);
        }
        return allchildren;

    }



    class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            for (int i = 0; i < mcheckviewchild.size(); i++) {
                ((Checkable) (mcheckviewchild.get(i))).setChecked(false);
            }
            ((Checkable) (buttonView)).setChecked(true);
            if(onCheckedChangeListener!=null){
                onCheckedChangeListener.onCheckedChanged(DeepRadioGroup.this, buttonView.getId());
            }
            mProtectFromCheckedChange = false;
            childid = buttonView.getId();
            tag=buttonView.getTag()!=null?buttonView.getTag().toString():"but1";
        }
    }
}
