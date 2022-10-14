package com.health.tencentlive.weight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.interfaces.IsNoNeedShowOnTop;
import com.healthy.library.routes.TencentLiveRoutes;


public class LiveErrorDialog extends BaseDialogFragment implements IsNoNeedShowOnTop {

    private LinearLayout dialogBg;
    private LinearLayout type1Layout;
    private TextView reconnecting;
    private LinearLayout type2Layout;
    private TextView netErr;
    private ImageView pullErrBtn;
    private LinearLayout type3Layout;
    private TextView netErrI;
    private ImageView pushErrBtn;
    private TextView checkNet;
    private LinearLayout type4Layout;
    private TextView closeErr;
    private TextView outActivity;

    private int type;
    private int midTime = 10;

    private OnErrClickListener onErrClickListener;

    public LiveErrorDialog setClickListener(OnErrClickListener onErrClickListener) {
        this.onErrClickListener = onErrClickListener;
        return this;
    }

    public LiveErrorDialog setType(int type) {
        this.type = type;
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

    public static LiveErrorDialog newInstance() {

        Bundle args = new Bundle();
        LiveErrorDialog fragment = new LiveErrorDialog();
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
        params.dimAmount = 0.6f;
        window.setAttributes(params);
        if (type == 5) {
            getDialog().setCancelable(false);
        } else {
            getDialog().setCancelable(true);
        }
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_error_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        dialogBg = (LinearLayout) view.findViewById(R.id.dialog_bg);
        type1Layout = (LinearLayout) view.findViewById(R.id.type1Layout);
        reconnecting = (TextView) view.findViewById(R.id.reconnecting);
        type2Layout = (LinearLayout) view.findViewById(R.id.type2Layout);
        netErr = (TextView) view.findViewById(R.id.netErr);
        pullErrBtn = (ImageView) view.findViewById(R.id.pullErrBtn);
        type3Layout = (LinearLayout) view.findViewById(R.id.type3Layout);
        netErrI = (TextView) view.findViewById(R.id.netErrI);
        pushErrBtn = (ImageView) view.findViewById(R.id.pushErrBtn);
        checkNet = (TextView) view.findViewById(R.id.checkNet);
        type4Layout = (LinearLayout) view.findViewById(R.id.type4Layout);
        closeErr = (TextView) view.findViewById(R.id.closeErr);
        outActivity = (TextView) view.findViewById(R.id.outActivity);
        initData();
    }

    private void initData() {
        if (type == 1) {
            type1Layout.setVisibility(View.VISIBLE);
            type2Layout.setVisibility(View.GONE);
            type3Layout.setVisibility(View.GONE);
            type4Layout.setVisibility(View.GONE);
            CountDownTimer timer = new CountDownTimer(10000, 1000) {
                public void onTick(long millisUntilFinished) {
                    reconnecting.setText("哦，网络好像出问题了正在为您重连···" + millisUntilFinished / 1000 + "s");
                }

                public void onFinish() {
                    dismiss();
                    onErrClickListener.onClick(1);
                }
            };
            timer.start();
        } else if (type == 2) {
            type1Layout.setVisibility(View.GONE);
            type2Layout.setVisibility(View.VISIBLE);
            type3Layout.setVisibility(View.GONE);
            type4Layout.setVisibility(View.GONE);
            pullErrBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onErrClickListener != null) {
                        onErrClickListener.onClick(2);
                    }
                }
            });
        } else if (type == 3) {
            type1Layout.setVisibility(View.GONE);
            type2Layout.setVisibility(View.GONE);
            type3Layout.setVisibility(View.VISIBLE);
            type4Layout.setVisibility(View.GONE);
            pushErrBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onErrClickListener != null) {
                        onErrClickListener.onClick(3);
                    }
                }
            });
            checkNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LIVESPEEDTEST)
                            .navigation();
                }
            });
        } else if (type == 4) {
            type1Layout.setVisibility(View.GONE);
            type2Layout.setVisibility(View.GONE);
            type3Layout.setVisibility(View.GONE);
            type4Layout.setVisibility(View.VISIBLE);
            outActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onErrClickListener != null) {
                        onErrClickListener.onClick(4);
                    }
                }
            });
        } else if (type == 5) {
            type1Layout.setVisibility(View.GONE);
            type2Layout.setVisibility(View.GONE);
            type3Layout.setVisibility(View.GONE);
            type4Layout.setVisibility(View.VISIBLE);
            closeErr.setText("当前直播未生成回放");
            outActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onErrClickListener != null) {
                        onErrClickListener.onClick(5);
                    }
                }
            });
        }
    }

    public interface OnErrClickListener {
        void onClick(int type);
    }
}
