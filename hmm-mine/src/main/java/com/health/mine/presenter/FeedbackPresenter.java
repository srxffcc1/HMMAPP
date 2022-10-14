package com.health.mine.presenter;

import android.content.Context;

import com.health.mine.contract.FeedbackContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/10 14:36
 * @des
 */
public class FeedbackPresenter implements FeedbackContract.Presenter {
    private Context mContext;
    private FeedbackContract.View mView;

    public FeedbackPresenter(Context context, FeedbackContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void commitFeedback(String content) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_FEEDBACK);
        map.put("content", content);
        map.put("userId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onCommitFeedbackSuccess();
                    }
                });
    }
}
