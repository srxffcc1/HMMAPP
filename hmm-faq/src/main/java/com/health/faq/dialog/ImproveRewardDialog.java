package com.health.faq.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.health.faq.R;
import com.healthy.library.utils.ScreenUtils;

/**
 * @author Li
 * @date 2019/07/03 17:58
 * @des 提高赏金
 */
public class ImproveRewardDialog extends DialogFragment {

    private EditText mEtPrice;
    private OnImproveClickListener mOnImproveClickListener;

    public static ImproveRewardDialog newInstance() {

        Bundle args = new Bundle();

        ImproveRewardDialog fragment = new ImproveRewardDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setOnImproveClickListener(OnImproveClickListener onImproveClickListener) {
        mOnImproveClickListener = onImproveClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_improve_reward, null);
        mEtPrice = view.findViewById(R.id.et_price);
        mEtPrice.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String normal = dest.toString();
                        if (".".contentEquals(source) && TextUtils.isEmpty(dest)) {
                            return "0.";
                        } else if (normal.contains(".") && normal.substring(
                                normal.indexOf(".") + 1).length() == 2) {
                            return "";
                        }
                        return null;
                    }
                }
        });
        view.findViewById(R.id.tv_improve)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (mOnImproveClickListener != null) {
                            mOnImproveClickListener.onImproveClick(mEtPrice.getText().toString());
                        }
                    }
                });
        builder.setView(view);
        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.faq_shape_improve_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (ScreenUtils.getScreenSize(getContext()).x * 0.86f);
        }

        return dialog;
    }

    public interface OnImproveClickListener {
        /**
         * 追加悬赏
         *
         * @param price 追加金额
         */
        void onImproveClick(String price);
    }
}
