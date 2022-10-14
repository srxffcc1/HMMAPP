package com.healthy.library.business;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.model.AdModel;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.SpUtils;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class ADDialogView extends BaseDialogFragment implements  AdContract.View, DataStatisticsContract.View{
    private ImageView dialogCloseButton;
    private ImageView imageAd;
    DataStatisticsPresenter dataStatisticsPresenter;

    public static ADDialogView newInstance() {

        Bundle args = new Bundle();
        ADDialogView fragment = new ADDialogView();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.index_ad_dialog, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
    }
    AdPresenter adPresenter;
    private void displayDialog(View view) {

        adPresenter = new AdPresenter(getActivity(), this);
        dataStatisticsPresenter = new DataStatisticsPresenter(getActivity(), this);

        dialogCloseButton = (ImageView) view.findViewById(R.id.dialog_close_button);
        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imageAd = (ImageView) view.findViewById(R.id.adImg);
        buildAd();
    }
     AdModel adModel;

    public ADDialogView setAdModel(AdModel adModel) {
        this.adModel = adModel;
        return this;
    }

    private void buildAd() {
        if(adModel==null){
            adModel=new Gson().fromJson(SpUtils.getValue(getActivity(), SpKey.USER_Main_AD_Bean), AdModel.class);
        }
        if(adModel==null||adModel.photoUrls==null){
            dismiss();
            return;
        }
        com.healthy.library.businessutil.GlideCopy.with(this)
                .load(adModel.photoUrls)

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        //System.out.println("广告载入失败直接跳转");
                        routePass();
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        com.healthy.library.businessutil.GlideCopy.with(imageAd.getContext()).load(resource).into(imageAd);

                    }
                });
        final String passString=adModel.getLink();
        imageAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MARouterUtils.passToTarget(getActivity(),adModel);
                dataStatisticsPresenter.bannerClickNum(new SimpleHashMapBuilder<>().puts("advertisingId",adModel.id));
                dismiss();
            }
        });
    }

    private void routePass() {
        dismiss();
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {

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
}
