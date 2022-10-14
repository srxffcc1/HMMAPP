package com.health.servicecenter.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.health.servicecenter.R;
import com.healthy.library.widget.ImageTextView;

public abstract class StoreBlockChildHolder {
    public ConstraintLayout goodOrderSTopA;
    public RadioGroup checkGroup;
    public RadioButton checkA;
    public RadioButton checkB;
    public ConstraintLayout goodOrderSTopZ;
    public ConstraintLayout toStoreLL;
    public TextView toStorePoint;
    public TextView toStoreTitle;
    public ImageTextView toStoreName;
    public ImageView passToStore;
    public ImageTextView toStoreAddress;
    public TextView toStoreTimeTitle;
    public ImageTextView toStoreTime;
    public ImageView passToStoreTime;
    public TextView passToPointEnd;
    public ConstraintLayout toHomeLL;
    public LinearLayout addAddressLL;
    public  ImageTextView addAddress;
    public ConstraintLayout toHomeDetail;
    public TextView goodAddressDefaultFlag;
    public TextView homeCity;
    public  ImageView passToHome;
    public ImageView homeTip;
    public TextView homeAddress;
    public TextView homeMasterName;
    public TextView addressHasP;
    public LinearLayout addressShop;
    public ImageView goodsLimitUnder;
    public LinearLayout goodOrderSpace;
    public LinearLayout goodOrderSTopB;
    public LinearLayout goodOrderSpaceListA;
    public LinearLayout goodsListLL;
    public LinearLayout goodOrderSpaceListB;
    public TextView goodsListUnderDiv;
    public LinearLayout backLL;
    public LinearLayout underviewll;
    public ImageTextView underview;
    public ConstraintLayout goodOrderSTop;
    public TextView backDetail;
    public TextView receiveShop;
    public LinearLayout canReceivePass;

    public abstract View getView();

    public StoreBlockChildHolder() {
        canReceivePass= (LinearLayout) findViewById(R.id.canReceivePass);
        receiveShop = (TextView) findViewById(R.id.receiveShop);
        backDetail = (TextView) findViewById(R.id.backDetail);
        goodOrderSTop = (ConstraintLayout) findViewById(R.id.goodOrderSTop);
        underviewll = (LinearLayout) findViewById(R.id.underviewll);
        underview = (ImageTextView) findViewById(R.id.underview);
        goodOrderSTopA = (ConstraintLayout) findViewById(R.id.goodOrderSTopA);
        checkGroup = (RadioGroup) findViewById(R.id.checkGroup);
        checkA = (RadioButton) findViewById(R.id.checkA);
        checkB = (RadioButton) findViewById(R.id.checkB);
        goodOrderSTopZ = (ConstraintLayout) findViewById(R.id.goodOrderSTopZ);
        toStoreLL = (ConstraintLayout) findViewById(R.id.toStoreLL);
        toStorePoint = (TextView) findViewById(R.id.toStorePoint);
        toStoreTitle = (TextView) findViewById(R.id.toStoreTitle);
        toStoreName = (ImageTextView) findViewById(R.id.toStoreName);
        passToStore = (ImageView) findViewById(R.id.passToStore);
        toStoreAddress = (ImageTextView) findViewById(R.id.toStoreAddress);
        toStoreTimeTitle = (TextView) findViewById(R.id.toStoreTimeTitle);
        toStoreTime = (ImageTextView) findViewById(R.id.toStoreTime);
        passToStoreTime = (ImageView) findViewById(R.id.passToStoreTime);
        passToPointEnd = (TextView) findViewById(R.id.passToPointEnd);
        toHomeLL = (ConstraintLayout) findViewById(R.id.toHomeLL);
        addAddressLL = (LinearLayout) findViewById(R.id.addAddressLL);
        addAddress = (ImageTextView) findViewById(R.id.addAddress);
        toHomeDetail = (ConstraintLayout) findViewById(R.id.toHomeDetail);
        goodAddressDefaultFlag = (TextView) findViewById(R.id.goodAddressDefaultFlag);
        homeCity = (TextView) findViewById(R.id.homeCity);
        passToHome = (ImageView) findViewById(R.id.passToHome);
        homeTip = (ImageView) findViewById(R.id.homeTip);
        homeAddress = (TextView) findViewById(R.id.homeAddress);
        homeMasterName = (TextView) findViewById(R.id.homeMasterName);
        addressHasP = (TextView) findViewById(R.id.addressHasP);
        addressShop = (LinearLayout) findViewById(R.id.addressShop);
        goodsLimitUnder = (ImageView) findViewById(R.id.goodsLimitUnder);
        goodOrderSpace = (LinearLayout) findViewById(R.id.goodOrderSpace);
        goodOrderSTopB = (LinearLayout) findViewById(R.id.goodOrderSTopB);
        goodOrderSpaceListA = (LinearLayout) findViewById(R.id.goodOrderSpaceListA);
        goodsListLL = (LinearLayout) findViewById(R.id.goodsListLL);
        goodOrderSpaceListB = (LinearLayout) findViewById(R.id.goodOrderSpaceListB);
        goodsListUnderDiv = (TextView) findViewById(R.id.goodsListUnderDiv);
        backLL = (LinearLayout) findViewById(R.id.backLL);
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }
}

