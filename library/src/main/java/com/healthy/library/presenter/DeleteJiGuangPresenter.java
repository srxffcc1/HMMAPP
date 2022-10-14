package com.healthy.library.presenter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;

import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.DeleteJiGuangContract;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class DeleteJiGuangPresenter implements DeleteJiGuangContract.Presenter {

    private Context mContext;

    public DeleteJiGuangPresenter(Context context) {
        mContext = context;
    }


    @Override
    public void deleteJiGuang() {
        Map<String, Object> map =new HashMap<>();
        String id = SpUtils.getValue(mContext, SpKey.USER_ID);
        String status = SpUtils.getValue(mContext, SpKey.STATUS);
        String phone = SpUtils.getValue(mContext, SpKey.PHONE);
        int phoneed = 0;
        if (!TextUtils.isEmpty(phone)) {
            phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));
            //System.out.println("注册手机尾号：" + phoneed);
        }
        long JIGUANGALI=SpUtils.getValue(mContext, SpKey.JIGUANGALI,0L);
        //判断下极光注册 保持1小时注册一次
        if(System.currentTimeMillis()-JIGUANGALI<1800000 ){//大于一小时了 进行下重置
            return;
        }
        SpUtils.store(mContext,SpKey.JIGUANGALI,System.currentTimeMillis());
        map.put("function", "80014");
        System.out.println("重置别名信息-----------------------------------");
        final int finalPhoneed = phoneed;
        ObservableHelper.createObservableNoLife(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                JPushInterface.setAlias(mContext,
                                        finalPhoneed,
                                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                            }
                        },3000);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                JPushInterface.setAlias(mContext,
                                        finalPhoneed,
                                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                            }
                        },3000);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
    public void deleteJiGuangOnly() {
        Map<String, Object> map =new HashMap<>();
        String id = SpUtils.getValue(mContext, SpKey.USER_ID);
        String status = SpUtils.getValue(mContext, SpKey.STATUS);
        String phone = SpUtils.getValue(mContext, SpKey.PHONE);
        int phoneed = 0;
        if (!TextUtils.isEmpty(phone)) {
            phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));
            //System.out.println("注册手机尾号：" + phoneed);
        }
        SpUtils.store(mContext, SpKey.JIGUANGALI,0L);
        map.put("function", "80014");
        //System.out.println("获取开屏信息");
        final int finalPhoneed = phoneed;
        ObservableHelper.createObservableNoLife(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        try {
//                            JPushInterface.setAlias(mContext,
//                                    finalPhoneed,
//                                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        try {
//                            JPushInterface.setAlias(mContext,
//                                    finalPhoneed,
//                                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
}