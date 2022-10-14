package com.health.servicecenter.presenter;

import android.content.Context;

import com.health.servicecenter.contract.AddAddressContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import java.util.HashMap;
import java.util.Map;


public class AddAddressPresenter implements AddAddressContract.Presenter {

    private Context mContext;
    private AddAddressContract.View mView;

    public AddAddressPresenter(Context context, AddAddressContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void putAddress(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9060");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onPutAddressSuccess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void deleteAddress(String id) {
        Map<String, Object> map = new HashMap<>();
        String[] ids = new String[1];
        ids[0] = id;
        map.put("ids",ids);
        map.put(Functions.FUNCTION, "9062");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onDeleteAddressSuccess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

}
