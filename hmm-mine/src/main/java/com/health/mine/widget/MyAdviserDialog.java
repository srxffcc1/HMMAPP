package com.health.mine.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.health.mine.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.widget.CornerImageView;

public class MyAdviserDialog extends BaseDialogFragment {
    //我的母婴顾问弹窗
    private ImageView dialog_close_button, myAdviserPhoneIcon;
    private TextView myAdviserName, myAdviserJobNum, myAdviserPhone, myAdviserAddress;
    private CornerImageView myAdviserImg;
    private onGetPayTypeListener listener;
    private TokerWorkerInfoModel model;

    public MyAdviserDialog setDate(TokerWorkerInfoModel model) {
        this.model = model;
        return this;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if(isAdded()){//避免标签重复
            try {
                manager.beginTransaction().remove(this).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyAdviserDialog newInstance() {

        MyAdviserDialog fragment = new MyAdviserDialog();
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
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.my_adviser_dialog_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        dialog_close_button = (ImageView) view.findViewById(R.id.dialog_close_button);
        myAdviserPhoneIcon = (ImageView) view.findViewById(R.id.myAdviserPhoneIcon);
        myAdviserName = (TextView) view.findViewById(R.id.myAdviserName);
        myAdviserJobNum = (TextView) view.findViewById(R.id.myAdviserJobNum);
        myAdviserPhone = (TextView) view.findViewById(R.id.myAdviserPhone);
        myAdviserAddress = (TextView) view.findViewById(R.id.myAdviserAddress);
        myAdviserImg = (CornerImageView) view.findViewById(R.id.myAdviserImg);
        dialog_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        initData();
    }

    private void initData() {
        if (model != null && model.bindingList != null) {
            final TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean bindingListBean = model.bindingList.get(0).bindingTokerWorker;
            if (bindingListBean == null) {
                return;
            }
            com.healthy.library.businessutil.GlideCopy.with(getContext())
                    .load(bindingListBean.professionalPhoto)
                    .error(R.drawable.img_avatar_default)
                    .placeholder(R.drawable.img_avatar_default)
                    .into(myAdviserImg);
            if (bindingListBean.personName != null && !TextUtils.isEmpty(bindingListBean.personName)) {
                myAdviserName.setText(bindingListBean.personName);
            }
            if (bindingListBean.personId != null && !TextUtils.isEmpty(bindingListBean.personId)) {
                myAdviserJobNum.setText("工号 " + bindingListBean.personId);
            }
            if (bindingListBean.personTel != null && !TextUtils.isEmpty(bindingListBean.personTel)) {
                myAdviserPhone.setVisibility(View.VISIBLE);
                myAdviserPhoneIcon.setVisibility(View.VISIBLE);
                myAdviserPhone.setText(bindingListBean.personTel);
                myAdviserPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.dial(getContext(), bindingListBean.personTel);
                    }
                });
                myAdviserPhoneIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtils.dial(getContext(), bindingListBean.personTel);
                    }
                });
            } else {
                myAdviserPhone.setVisibility(View.GONE);
                myAdviserPhoneIcon.setVisibility(View.GONE);
            }
            if (bindingListBean.departName != null && !TextUtils.isEmpty(bindingListBean.departName)) {
                myAdviserAddress.setText(bindingListBean.departName);
            }
        }
    }

    public interface onGetPayTypeListener {

        void setPayType(String type);
    }

    public void setPayTypeListener(onGetPayTypeListener getListener) {
        this.listener = getListener;
    }
}
