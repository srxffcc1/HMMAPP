package com.healthy.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.healthy.library.R;

/**
 * 自适应的选择框
 */
public class AutoFitCheckBox extends ImageView implements Checkable{
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private boolean mBroadcasting;
	private boolean mCheckedFromResource = false;
	public AutoFitCheckBox(Context context) {
		this(context, null);

		// TODO Auto-generated constructor stub
	}
	public AutoFitCheckBox(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		this.setScaleType(ScaleType.FIT_CENTER);
		// TODO Auto-generated constructor stub
	}

	public AutoFitCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr,0);
		this.setScaleType(ScaleType.FIT_CENTER);
	}

	@SuppressLint("NewApi")
	public AutoFitCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.setScaleType(ScaleType.FIT_CENTER);
		final TypedArray a = context.obtainStyledAttributes(
				attrs, R.styleable.AutoFitCheckBox, defStyleAttr, defStyleRes);
		final boolean checked = a.getBoolean(
				R.styleable.AutoFitCheckBox_checked, false);
		setChecked(checked);
		mCheckedFromResource = true;
		a.recycle();
	}

	private boolean mChecked;
	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		this.setClickable(true);
	}
	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mCheckedFromResource = false;
			mChecked = checked;
			refreshDrawableState();
		}
		
		if (mBroadcasting) {
            return;
        }
		
		mBroadcasting = true;
		if(mOnCheckedChangeListener!=null){
			
			mOnCheckedChangeListener.onCheckedChanged(this,mChecked);
		}
		if(mOnCheckedChangeWidgetListener !=null){
			mOnCheckedChangeWidgetListener.onCheckedChanged(this,mChecked);
		}
		mBroadcasting = false;
	}
	@Override
	public boolean isChecked() {
		return mChecked;
	}
	@Override
	public void toggle() {
		setChecked(!mChecked);
		
	}
    @Override
    public boolean performClick() {
    	if(this.isEnabled()){
    		toggle();
    	}
        return true;
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
    	
    	mOnCheckedChangeListener=onCheckedChangeListener;
    }
    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener monWidgetCheckedChangeListener){
    	this.mOnCheckedChangeWidgetListener = monWidgetCheckedChangeListener;
    }

	public static interface OnCheckedChangeListener {
		/**
		 * Called when the checked state of a compound button has changed.
		 *
		 * @param buttonView The compound button view whose state has changed.
		 * @param isChecked  The new checked state of buttonView.
		 */
		void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked);
	}

}