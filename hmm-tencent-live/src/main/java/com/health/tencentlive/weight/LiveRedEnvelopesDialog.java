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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.health.tencentlive.R;
import com.healthy.library.base.BaseDialogFragment;


public class LiveRedEnvelopesDialog extends DialogFragment {

    //private LinearLayout topView;
    private ImageView closeImg;
    private TextView tips;
    private int lookNum;
    private OnRedClickListener onErrClickListener;
    private ImageView topImg;

    public LiveRedEnvelopesDialog setClickListener(OnRedClickListener onErrClickListener) {
        this.onErrClickListener = onErrClickListener;
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

    public LiveRedEnvelopesDialog setPopularity(int lookNum) {
        this.lookNum = lookNum;
        setLookNum(lookNum);
        return this;
    }

    private void setLookNum(int lookNum) {
        if(tips != null){
            tips.setText(String.format("共有%s人与您一起瓜分福利", lookNum));
        }
    }

    public static LiveRedEnvelopesDialog newInstance() {

        Bundle args = new Bundle();
        LiveRedEnvelopesDialog fragment = new LiveRedEnvelopesDialog();
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
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_red_envelopes_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        //topView = (LinearLayout) view.findViewById(R.id.topView);
        topImg = view.findViewById(R.id.topImg);
        closeImg = (ImageView) view.findViewById(R.id.closeImg);
        tips = (TextView) view.findViewById(R.id.tips);
        initData();
    }

    private void initData() {
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        topImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onErrClickListener != null) {
                    onErrClickListener.onClick();
                }
            }
        });
        setLookNum(lookNum);

    }

    public interface OnRedClickListener {
        void onClick();
    }

}
