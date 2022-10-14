package com.healthy.library.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.AutoFitCheckBox;

/**
 * @author liuwei
 * @des 隐私协议第一页
 */

public class YSXY1Dialog extends DialogFragment {
    private AlertDialog mAlertDialog;
    private TextView ysTitle1;
    private TextView topContent;
    private TextView bottomContent;
    private LinearLayout selectLayout;
    private AutoFitCheckBox selectImg;
    private TextView selectTxt;
    private TextView agreeBtn;
    private TextView noAgreeBtn;
    private int clickNum = 0;

    public void setOnConfirmClick(OnSelectConfirm onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    public interface OnSelectConfirm {
        void onClick(int result);
    }

    private OnSelectConfirm mOnConfirmClick;

    public static YSXY1Dialog newInstance() {
        Bundle args = new Bundle();
        YSXY1Dialog fragment = new YSXY1Dialog();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.ysxy_1_dialog_layout, null);
            ysTitle1 = (TextView) view.findViewById(R.id.ysTitle1);
            topContent = (TextView) view.findViewById(R.id.topContent);
            bottomContent = (TextView) view.findViewById(R.id.bottomContent);
            selectLayout = (LinearLayout) view.findViewById(R.id.selectLayout);
            selectImg = (AutoFitCheckBox) view.findViewById(R.id.selectImg);
            selectTxt = (TextView) view.findViewById(R.id.selectTxt);
            agreeBtn = (TextView) view.findViewById(R.id.agreeBtn);
            noAgreeBtn = (TextView) view.findViewById(R.id.noAgreeBtn);
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setCancelable(false)
                    .create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            /** 禁止物理返回键 */
            mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        return true;
                    }
                    return false;
                }
            });
            Window window = mAlertDialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.BottomDialogAnimation);
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                decorView.setBackgroundColor(Color.parseColor("#ffffff"));
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
            }
        }
        initData();
        return mAlertDialog;
    }

    private void initData() {
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectImg.isChecked()){
                    Toast.makeText(getActivity(),"为保障您的权益，您需同意憨妈妈协议⽅可继续使⽤憨妈妈",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mOnConfirmClick!=null){

                    mOnConfirmClick.onClick(1);
                }
                YSXY1Dialog.this.dismiss();
            }
        });
        noAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNum++;
                if (clickNum == 1) {
                    Toast.makeText(getContext(), "为保障您的权益，您需同意憨妈妈协议⽅可继续使⽤憨妈妈", Toast.LENGTH_SHORT).show();
                } else {
                    if(mOnConfirmClick!=null){

                        mOnConfirmClick.onClick(2);
                    }
                    YSXY1Dialog.this.dismiss();
                }
            }
        });
        bottomContent.setMovementMethod(LinkMovementMethod.getInstance());
        bottomContent.setText(SpanUtils.getBuilder(getContext(), "您可以通过阅读完整版").setForegroundColor(Color.parseColor("#333333"))
                .append("《憨妈妈⽤户协议》").setForegroundColor(Color.parseColor("#FF4D66")).setClickSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(Color.parseColor("#FF4D66"));
                        ds.setUnderlineText(true);
                    }

                    @Override
                    public void onClick(@NonNull View widget) {
                        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                                .withString("url", UrlKeys.URL_PROTOCOL)
                                .withString("title", "用户协议")
                                .navigation();
                    }
                }).append("及").setForegroundColor(Color.parseColor("#333333"))
                .append("《隐私政策》").setForegroundColor(Color.parseColor("#FF4D66")).setClickSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(Color.parseColor("#FF4D66"));
                        ds.setUnderlineText(true);
                    }
                    @Override
                    public void onClick(@NonNull View widget) {
                        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                                .withString("url", UrlKeys.URL_HMMPTYSZC)
                                .withString("title", "隐私政策")
                                .navigation();
                    }
                }).append("详细了解我们提供的服务以及所需的权限具体情况。 对于上述内容中免除我们责任和对您权利进⾏限制的条款，我们将以加粗字体及下划线的⽅式提醒您注意。")
                .create());
        selectTxt.setMovementMethod(LinkMovementMethod.getInstance());
        selectTxt.setText(SpanUtils.getBuilder(getContext(), "我已阅读并同意").setForegroundColor(Color.parseColor("#333333"))
                .append("《憨妈妈⽤户协议》").setForegroundColor(Color.parseColor("#FF4D66")).setClickSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(Color.parseColor("#FF4D66"));
                        ds.setUnderlineText(true);
                    }
                    @Override
                    public void onClick(@NonNull View widget) {
                        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                                .withString("url", UrlKeys.URL_PROTOCOL)
                                .withString("title", "⽤户协议")
                                .navigation();
                    }
                }).append("及").setForegroundColor(Color.parseColor("#333333"))
                .append("《隐私政策》").setForegroundColor(Color.parseColor("#FF4D66")).setClickSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(Color.parseColor("#FF4D66"));
                        ds.setUnderlineText(true);
                    }
                    @Override
                    public void onClick(@NonNull View widget) {
                        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                                .withString("url", UrlKeys.URL_HMMPTYSZC)
                                .withString("title", "隐私政策")
                                .navigation();
                    }
                }).create());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}