package com.healthy.library.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.contract.CardPackContract;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.presenter.CardPickLoopPresenter;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class CouponImgDialog extends BaseDialogFragment implements CardPackContract.View{

    private TextView couponTitle;
    private ImageView couponImg;
    private ImageView close;
    private String title;
    private Bitmap img;
    private CardPickLoopPresenter cardPickPresenter;
    private String couponId;
    private TextView couponFinish;
    private boolean isFinish=false;

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CouponImgDialog newInstance() {
        Bundle args = new Bundle();
        CouponImgDialog fragment = new CouponImgDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public CouponImgDialog setData(String title, Bitmap img) {
        this.title = title;
        this.img = img;
        return this;
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
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.item_coupon_img_dialog_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        couponTitle = (TextView) view.findViewById(R.id.couponTitle);
        couponImg = (ImageView) view.findViewById(R.id.couponImg);
        close = (ImageView) view.findViewById(R.id.close);
        couponFinish=view.findViewById(R.id.couponFinish);
        initData();
    }

    private void initData() {
        cardPickPresenter=new CardPickLoopPresenter(getActivity(),this);
        couponTitle.setText(title);
        Glide.with(getContext())
                .load(img)
                .into(couponImg);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFinish=true;
                dismiss();
            }
        });
        cardPickPresenter.getCouponDetail(new SimpleHashMapBuilder<>().puts("memberCouponId",couponId));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void onSucessGetCouponList(List<CouponInfoZ> adModels) {

    }

    @Override
    public void onSucessGetCoupon(CouponInfoZ adModels) {
        if(isFinish){
            return;
        }
        if(adModels.status==1){
            couponFinish.setText("该优惠券已核销");
            Toast.makeText(LibApplication.getAppContext(),"该优惠券已核销",Toast.LENGTH_SHORT).show();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardPickPresenter.getCouponDetail(new SimpleHashMapBuilder<>().puts("memberCouponId",couponId));
                }
            },1000);
        }
    }

    @Override
    public void onSucessGetInsert(Boolean hasinsert) {

    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponId() {
        return couponId;
    }
}
