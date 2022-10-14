package com.healthy.library.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.healthy.library.constant.Functions;
import com.healthy.library.contract.ActVipOnlineContract;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import java.util.Map;

/**
 * 创建日期：2021/11/19 15:37
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.healthy.library.presenter
 * 类说明：同城圈数据统计（包括浏览跟分享...）
 */

public class DataStatisticsPresenter implements DataStatisticsContract.Presenter {

    private Context mContext;
    private DataStatisticsContract.View mView;

    public DataStatisticsPresenter(Context context, DataStatisticsContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void shareAndLook(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "share_1000");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                });
    }

    @Override
    public void bannerClickNum(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9607");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                });
    }
}
