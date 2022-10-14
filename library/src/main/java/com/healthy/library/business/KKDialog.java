package com.healthy.library.business;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.ImageTextView;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class KKDialog extends BaseDialogFragment {

    public String result;
    private TextView kkMoneyBottom;
    private TextView tvMin;

    public boolean canfirst = true;
    public KKDialog setCanfirst(boolean canfirst) {
        this.canfirst = canfirst;
        return this;
    }

    private DialogKickShareclickListener onDialogShareclickListener;
    private ImageTextView tvWx;
    private ImageTextView tvTimeline;
    private ImageTextView tvQq;
    private ImageTextView tvQzone;
    private ImageTextView tvSina;
//    private View.OnClickListener yellowOnclickListener;

    public KKDialog setRedOnclickListener(DialogKickShareclickListener redOnclickListener) {
        this.onDialogShareclickListener = redOnclickListener;
        return this;
    }

    //    public KKDialog setYellowOnclickListener(View.OnClickListener yellowOnclickListener) {
//        this.yellowOnclickListener = yellowOnclickListener;
//        return this;
//    }
    private LinearLayout kkLl;
    private TextView kkMoney;
    private ImageView kkIcon;
    private ImageView dissMissImg;

    public static KKDialog newInstance() {

        Bundle args = new Bundle();
        KKDialog fragment = new KKDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_dialog_kk, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(View view) {
        kkLl = (LinearLayout) view.findViewById(R.id.kk_ll);
        kkMoney = (TextView) view.findViewById(R.id.kkMoney);
        tvWx = (ImageTextView) view.findViewById(R.id.tv_wx);
        tvTimeline = (ImageTextView) view.findViewById(R.id.tv_timeline);
        tvQq = (ImageTextView) view.findViewById(R.id.tv_qq);
        tvQzone = (ImageTextView) view.findViewById(R.id.tv_qzone);
        tvSina = (ImageTextView) view.findViewById(R.id.tv_sina);
        kkIcon = (ImageView) view.findViewById(R.id.kk_icon);
        dissMissImg = (ImageView) view.findViewById(R.id.dissMissImg);
        kkMoneyBottom = (TextView) view.findViewById(R.id.kkMoneyBottom);
        tvMin= (TextView) view.findViewById(R.id.tvMin);
        if (!canfirst) {
            kkMoney.setText(SpanUtils.getBuilder(getContext(), "你又砍了").setForegroundColor(Color.parseColor("#222222"))
                    .append((result) + "").setForegroundColor(Color.parseColor("#FC2347")).setProportion(1.5f)
                    .append("元").setForegroundColor(Color.parseColor("#222222"))
                    .create());
            tvMin.setText("继续邀请");
            kkMoneyBottom.setVisibility(View.VISIBLE);
            kkMoneyBottom.setText("分享给新用户，帮忙砍价的力度更大哦～");
        } else {
            kkMoney.setText(SpanUtils.getBuilder(getContext(), "你已砍了").setForegroundColor(Color.parseColor("#222222"))
                    .append((result) + "").setForegroundColor(Color.parseColor("#FC2347")).setProportion(1.5f)
                    .append("元").setForegroundColor(Color.parseColor("#222222"))
                    .create());
            tvMin.setText("分享给好友");
            kkMoneyBottom.setVisibility(View.VISIBLE);
            kkMoneyBottom.setText("分享给好友，可以多砍一刀哦～");
        }

        tvMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onDialogShareclickListener != null) {
                    onDialogShareclickListener.onClick(SHARE_MEDIA.WEIXIN);
                }
            }
        });
        tvWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onDialogShareclickListener != null) {
                    onDialogShareclickListener.onClick(SHARE_MEDIA.WEIXIN);
                }
            }
        });
        tvTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onDialogShareclickListener != null) {
                    onDialogShareclickListener.onClick(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
            }
        });
        tvQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onDialogShareclickListener != null) {
                    onDialogShareclickListener.onClick(SHARE_MEDIA.QQ);
                }
            }
        });
        tvQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onDialogShareclickListener != null) {
                    onDialogShareclickListener.onClick(SHARE_MEDIA.QZONE);
                }
            }
        });
        tvSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onDialogShareclickListener != null) {
                    onDialogShareclickListener.onClick(SHARE_MEDIA.SINA);
                }
            }
        });
        dissMissImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        kkWeixin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                if(redOnclickListener!=null){
//                    redOnclickListener.onClick(v);
//                }
//            }
//        });
//        kkQQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                if(yellowOnclickListener!=null){
//                    yellowOnclickListener.onClick(v);
//                }
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    private void initView() {


    }

    public KKDialog setResult(String result) {
        this.result = result;
        return this;
    }

    public interface DialogKickShareclickListener {
        void onClick(SHARE_MEDIA share_media);
    }
}
