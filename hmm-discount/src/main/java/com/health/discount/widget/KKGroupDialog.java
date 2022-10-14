package com.health.discount.widget;

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

import com.health.discount.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.ImageTextView;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class KKGroupDialog extends BaseDialogFragment {

    public String result;
    private LinearLayout kkLl;
    private TextView kkMoneyTitle;
    private TextView kkMoney;

    private TextView kkWeixin;
    private ImageView closeMessageDialog;

    private DialogShareclickListener onDialogShareclickListener;
    private ImageTextView tvWx;
    private ImageTextView tvTimeline;
    private ImageTextView tvQq;
    private ImageTextView tvQzone;
    private ImageTextView tvSina;
    private TextView tvMin;

    public boolean canfirst = true;
    private ImageView assemOk;

    public KKGroupDialog setCanfirst(boolean canfirst) {
        this.canfirst = canfirst;
        return this;
    }

    public KKGroupDialog setOnDialogShareclickListener(DialogShareclickListener onDialogShareclickListener) {
        this.onDialogShareclickListener = onDialogShareclickListener;
        return this;
    }

    public static KKGroupDialog newInstance() {
        Bundle args = new Bundle();
        KKGroupDialog fragment = new KKGroupDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dis_dialog_group_detail_min, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(View view) {
        kkLl = (LinearLayout) view.findViewById(R.id.kk_ll);
        kkMoneyTitle = (TextView) view.findViewById(R.id.kkMoneyTitle);
        kkMoney = (TextView) view.findViewById(R.id.kkMoney);
        closeMessageDialog = (ImageView) view.findViewById(R.id.closeMessageDialog);
        assemOk= (ImageView)view. findViewById(R.id.assemOk);

        tvWx = (ImageTextView) view.findViewById(R.id.tv_wx);
        tvTimeline = (ImageTextView) view.findViewById(R.id.tv_timeline);
        tvQq = (ImageTextView) view.findViewById(R.id.tv_qq);
        tvQzone = (ImageTextView) view.findViewById(R.id.tv_qzone);
        tvSina = (ImageTextView) view.findViewById(R.id.tv_sina);
        tvMin = (TextView) view.findViewById(R.id.tvMin);


        closeMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        if (!canfirst) {
//            assemOk.setImageResource(R.drawable.assem_ok);
//            kkMoneyTitle.setText("发送邀请成功");
//            kkMoney.setText(SpanUtils.getBuilder(getContext(), "邀请").setForegroundColor(Color.parseColor("#666666"))
//                    .append("3次").setForegroundColor(Color.parseColor("#FC2347"))
//                    .append("以上，拼团成功率高达").setForegroundColor(Color.parseColor("#666666"))
//                    .append("98%").setForegroundColor(Color.parseColor("#FC2347"))
//                    .append("哦！").setForegroundColor(Color.parseColor("#666666"))
//                    .create());
//            tvMin.setText("继续邀请");
//        } else {
//            assemOk.setImageResource(R.drawable.assem_ok2);
//            kkMoneyTitle.setText(SpanUtils.getBuilder(getContext(), "再邀请").setForegroundColor(Color.parseColor("#666666"))
//                    .append((result) + "").setForegroundColor(Color.parseColor("#FC2347"))
//                    .append("人，超值商品马上到手！").setForegroundColor(Color.parseColor("#666666"))
//                    .create());
//            kkMoney.setText(SpanUtils.getBuilder(getContext(), "去").setForegroundColor(Color.parseColor("#666666"))
//                    .append("大群").setForegroundColor(Color.parseColor("#FC2347"))
//                    .append("邀请，将提升").setForegroundColor(Color.parseColor("#666666"))
//                    .append("95%").setForegroundColor(Color.parseColor("#FC2347"))
//                    .append("拼团成功率哦！").setForegroundColor(Color.parseColor("#666666"))
//                    .create());
//            tvMin.setText("邀请好友拼团");
//        }
        assemOk.setImageResource(R.drawable.assem_ok);
        kkMoneyTitle.setText(SpanUtils.getBuilder(getContext(), "再邀请").setForegroundColor(Color.parseColor("#666666"))
                .append((result) + "").setForegroundColor(Color.parseColor("#FC2347"))
                .append("人，超值商品马上到手！").setForegroundColor(Color.parseColor("#666666"))
                .create());
            kkMoney.setText(SpanUtils.getBuilder(getContext(), "邀请").setForegroundColor(Color.parseColor("#666666"))
                    .append("3次").setForegroundColor(Color.parseColor("#FC2347"))
                    .append("以上，拼团成功率高达").setForegroundColor(Color.parseColor("#666666"))
                    .append("98%").setForegroundColor(Color.parseColor("#FC2347"))
                    .append("哦！").setForegroundColor(Color.parseColor("#666666"))
                    .create());
        tvMin.setText("继续邀请");

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

    public KKGroupDialog setResult(String result) {
        this.result = result;
        return this;
    }

    public interface DialogShareclickListener {
        void onClick(SHARE_MEDIA share_media);
    }
}
