package com.healthy.library.presenter;

import android.content.Context;

import com.healthy.library.contract.LimitContract;

import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LimitPresenter implements LimitContract.Presenter {

    private Context mContext;
    private LimitContract.View mView;
    boolean isTestTrue=false;

    public LimitPresenter(Context context, LimitContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getMineLimit(Map<String, Object> map) {
        mView.onSucessGetLimit(true);
//        map.put("function", "10001");
//        map.put("memberPhone", SpUtils.getValue(mContext,SpKey.PHONE));
//        //System.out.println("获取开屏信息");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext,
//                        false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.onSucessGetLimit(true);
////                        try {
////                            if(!TextUtils.isEmpty(new JSONObject(obj).optString("data"))){
////                                mView.onSucessGetLimit(true);
////                            }else {
////                                if(ChannelUtil.getChannel(null).equals("release")||ChannelUtil.getChannel(null).equals("product")){
////                                    mView.onSucessGetLimit(false);
////                                }else {
////                                    mView.onSucessGetLimit(isTestTrue);
////                                }
////                            }
////                        } catch (Exception e) {
////                            if(ChannelUtil.getChannel(null).equals("release")||ChannelUtil.getChannel(null).equals("product")){
////                                mView.onSucessGetLimit(false);
////                            }else {
////                                mView.onSucessGetLimit(isTestTrue);
////                            }
////                            e.printStackTrace();
////                        }
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetLimit(true);
////                        if(ChannelUtil.getChannel(null).equals("release")||ChannelUtil.getChannel(null).equals("product")){
////                            mView.onSucessGetLimit(false);
////                        }else {
////                            mView.onSucessGetLimit(isTestTrue);
////                        }
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                    }
//                });
    }
}