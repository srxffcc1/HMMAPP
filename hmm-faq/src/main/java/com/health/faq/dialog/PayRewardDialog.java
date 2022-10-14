package com.health.faq.dialog;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.faq.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Li
 * @date 2019/07/01 11:32
 * @des 悬赏支付
 */

public class PayRewardDialog extends DialogFragment implements View.OnClickListener {

    private TextView mTvTitle;
    private TextView mTvPrice;
    private Group mGroupBalance;
    private TextView mTvBalance;
    private ImageView mIvWx;
    private ImageView mIvZfb;
    private ImageView mIvHdd;
    private List<ImageView> mPayWayIvs;
    private View mWxView;
    private View mZfbView;
    private View mHddView;
    private OnPayClickListener mOnPayClickListener;

    public static PayRewardDialog newInstance(String title, String money, String leftMoney,
                                              boolean isBalanceEnough) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("money", money);
        args.putString("leftMoney", leftMoney);
        args.putBoolean("enough", isBalanceEnough);
        PayRewardDialog fragment = new PayRewardDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pay_reward, null);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvPrice = view.findViewById(R.id.tv_price);
        mGroupBalance = view.findViewById(R.id.group_balance);
        mTvBalance = view.findViewById(R.id.tv_pay_hdd);
        mIvWx = view.findViewById(R.id.iv_wx);
        mIvZfb = view.findViewById(R.id.iv_zfb);
        mIvHdd = view.findViewById(R.id.iv_hdd);

        mWxView = view.findViewById(R.id.view_wx_click);
        mZfbView = view.findViewById(R.id.view_ali_click);
        mHddView = view.findViewById(R.id.view_hdd_click);


        mWxView.setOnClickListener(this);
        mZfbView.setOnClickListener(this);
        mHddView.setOnClickListener(this);

        mPayWayIvs = new ArrayList<>();
        mPayWayIvs.add(mIvWx);
        mPayWayIvs.add(mIvZfb);
        mPayWayIvs.add(mIvHdd);


        view.findViewById(R.id.tv_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPayClickListener != null) {
                    mOnPayClickListener.onPay(getPayType());
                }
            }
        });


        mIvWx.setSelected(true);
        initContent();
        builder.setView(view);
        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(com.healthy.library.R.style.BottomDialogAnimation);
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(com.healthy.library.R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
        }

        return dialog;
    }

    private void initContent() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        String prefix = "悬赏求助：";
        SpannableString spannableString = new SpannableString(
                String.format("%s%s", prefix, bundle.getString("title")));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan, 0, prefix.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvTitle.setText(spannableString);
        mTvPrice.setText(String.format("¥ %s", bundle.getString("money")));

        if (bundle.getBoolean("enough")) {
            mGroupBalance.setVisibility(View.VISIBLE);
            mTvBalance.setText(String.format("憨豆豆余额 %s", bundle.getString("leftMoney")));
        } else {
            mGroupBalance.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        mIvWx.setSelected(v == mWxView);
        mIvZfb.setSelected(v == mZfbView);
        mIvHdd.setSelected(v == mHddView);
    }

    public void setOnPayClickListener(OnPayClickListener onPayClickListener) {
        mOnPayClickListener = onPayClickListener;
    }

    public interface OnPayClickListener {
        /**
         * 支付
         *
         * @param type 支付方式
         */
        void onPay(int type);
    }

    private int getPayType() {
        for (ImageView payWayIv : mPayWayIvs) {
            if (payWayIv.isSelected()) {
                return mPayWayIvs.indexOf(payWayIv);
            }
        }
        return 0;
    }
}
