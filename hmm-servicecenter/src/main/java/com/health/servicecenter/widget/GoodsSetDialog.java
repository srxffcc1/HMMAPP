package com.health.servicecenter.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.health.servicecenter.R;
import com.healthy.library.base.BaseDialogFragment;

public class GoodsSetDialog extends BaseDialogFragment {
    View dialogview = null;
    private ConstraintLayout setTitleTop;
    private TextView setNumber;
    private TextView setOldMoney;
    private TextView setNowMoney;
    private ImageView scrollImg;
    private LinearLayout setTitleUnder;
    private Spinner spinnerReal;
    private ConstraintLayout spinnerDetailLL;
    private TextView spinnerTitle;
    private TextView spinnerContext;
    private ImageView spinnerImg;
    private View setdivider;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        dialogview = LayoutInflater.from(getContext()).inflate(R.layout.service_dialog_set, null);
        initView(dialogview);
        buildView(dialogview);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消


        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomDialogAnimation);
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(com.healthy.library.R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
        }

        return dialog;
    }

    private void buildView(View dialogview) {
        spinnerDetailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerReal.performClick();
            }
        });
        spinnerReal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ////System.out.println("点击的位置:"+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerReal.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(spinnerReal.getWidth()!=0){
                    spinnerReal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    spinnerReal.setDropDownWidth(spinnerReal.getWidth()); //下拉宽度
                }
            }
        });
        String[] mItems = getResources().getStringArray(R.array.citys);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReal.setAdapter(adapter);

    }

    private void initView(View view) {

        setTitleTop = (ConstraintLayout) view.findViewById(R.id.setTitleTop);
        setNumber = (TextView) view.findViewById(R.id.setNumber);
        setOldMoney = (TextView) view.findViewById(R.id.setOldMoney);
        setNowMoney = (TextView) view.findViewById(R.id.setNowMoney);
        scrollImg = (ImageView) view.findViewById(R.id.scrollImg);
        setTitleUnder = (LinearLayout) view.findViewById(R.id.setTitleUnder);
        spinnerReal = (Spinner) view.findViewById(R.id.spinnerReal);
        spinnerDetailLL = (ConstraintLayout) view.findViewById(R.id.spinnerDetailLL);
        spinnerTitle = (TextView) view.findViewById(R.id.spinnerTitle);
        spinnerContext = (TextView) view.findViewById(R.id.spinnerContext);
        spinnerImg = (ImageView) view.findViewById(R.id.spinnerImg);
        setdivider = (View) view.findViewById(R.id.setdivider);
    }

    public static GoodsSetDialog newInstance() {

        Bundle args = new Bundle();
        GoodsSetDialog fragment = new GoodsSetDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
