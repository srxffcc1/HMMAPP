package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.R;
import com.healthy.library.model.VipGift;
import com.healthy.library.routes.DiscountRoutes;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;


public class GetCouponDialog extends DialogFragment {
    private String listString;
    VipGift vipGift;
    private ConstraintLayout dialogBg;
    private TextView dialogTopTxt;
    private TextView dialogTitleTxt;
    private LinearLayout coupLL;
    private TextView seeCouponBtn;
    private ImageView dialogCloseButtonTmp;
    private ImageView dialogCloseButton;


    private VipGift resolveData(String obj) {
        VipGift result = null;
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<VipGift>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static GetCouponDialog newInstance() {

        Bundle args = new Bundle();
        GetCouponDialog fragment = new GetCouponDialog();
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

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.4f;
        window.setAttributes(windowParams);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.index_dialog_get_coupon, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View dialog) {
        dialogBg = (ConstraintLayout) dialog.findViewById(R.id.dialog_bg);
        dialogTopTxt = (TextView) dialog.findViewById(R.id.dialogTopTxt);
        dialogTitleTxt = (TextView) dialog.findViewById(R.id.dialogTitleTxt);
        coupLL = (LinearLayout) dialog.findViewById(R.id.coupLL);
        seeCouponBtn = (TextView) dialog.findViewById(R.id.seeCouponBtn);
        dialogCloseButtonTmp = (ImageView) dialog.findViewById(R.id.dialog_close_button_tmp);
        dialogCloseButton = (ImageView) dialog.findViewById(R.id.dialog_close_button);
        dialog.findViewById(R.id.dialog_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.findViewById(R.id.seeCouponBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST)
                        .navigation();
                dismiss();
            }
        });

    }

    void ininData() {
        if (vipGift != null) {
            if (vipGift.getData() != null) {
                dialogTitleTxt.setText(vipGift.getData().title);
                if (vipGift.getData().array != null) {
                    List<VipGift.VipArray> vipArrays = vipGift.getData().array;
                    int needsize = vipArrays.size();
                    if (needsize > 2) {
                        needsize = 2;
                    }
                    for (int i = 0; i < needsize; i++) {
                        VipGift.VipArray vipArray = vipArrays.get(i);
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.index_dialog_coupons, coupLL, false);
                        TextView giftMoney;
                        TextView giftTitle;
                        TextView giftTime;
                        giftMoney = (TextView) view.findViewById(R.id.giftMoney);
                        giftTitle = (TextView) view.findViewById(R.id.giftTitle);
                        giftTime = (TextView) view.findViewById(R.id.giftTime);
                        if (Integer.parseInt(vipArray.Price.split("\\.")[0]) < 0) {
                            giftMoney.setText("￥" + Math.abs(Integer.parseInt(vipArray.Price.split("\\.")[0])));
                        } else {
                            giftMoney.setText("￥" + vipArray.Price.split("\\.")[0]);
                        }

                        giftTitle.setText(vipArray.GoodsName);
                        giftTime.setText(vipArray.StartDate + "至" + vipArray.EndDate);
                        coupLL.addView(view);
                    }
                }
            }

        }
    }

    public GetCouponDialog setListString(String listString) {
        this.listString = listString;
        //System.out.println("获得得list");
        vipGift = resolveData(listString);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ininData();
            }
        }, 500);
        return this;
    }

    public String getListString() {
        return listString;
    }

    private void initView() {


    }
}
