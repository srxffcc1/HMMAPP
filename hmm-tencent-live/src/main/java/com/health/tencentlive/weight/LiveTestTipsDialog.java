package com.health.tencentlive.weight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.health.tencentlive.R;
import com.healthy.library.base.BaseDialogFragment;


public class LiveTestTipsDialog extends BaseDialogFragment {

    private TextView content;
    private TextView submit;


    private OnClickListener onClickListener;

    public LiveTestTipsDialog setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LiveTestTipsDialog newInstance() {

        Bundle args = new Bundle();
        LiveTestTipsDialog fragment = new LiveTestTipsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0.5f;
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_test_tips_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        content = (TextView) view.findViewById(R.id.content);
        submit = (TextView) view.findViewById(R.id.submit);
        initData();
    }

    private void initData() {
        content.setText("1.直播调试模式与正式开播基本相同，但直播信息不会展示在直播列表中，普通用户无法进入您的直播间，也不会创建直播记录与录像。\n" +
                "\n" +
                "2.您可以通过分享功能，邀请您的同事或自己的小号进入调试直播，以观众的角度了解您的直播状态。\n" +
                "\n" +
                "3.直播调试会默认加载您最新的直播设置，以便您更好的调试，如果需要调整，请登录憨妈妈商家端，在直播设置或直播控制台功能中，调整您的配置。");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick();
                    dismiss();
                }
            }
        });
    }

    public interface OnClickListener {
        void onClick();
    }
}
