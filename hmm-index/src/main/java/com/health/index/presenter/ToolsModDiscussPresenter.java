package com.health.index.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.health.index.contract.ToolsModDiscussContract;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:41
 * @des
 */
public class ToolsModDiscussPresenter implements ToolsModDiscussContract.Presenter {

    private Activity mContext;
    private ToolsModDiscussContract.View mView;

    public ToolsModDiscussPresenter(Activity context, ToolsModDiscussContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void see(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessSee(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getMine(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetMine(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getList(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessgetList(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void reView(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessReView(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void reDiscuss(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessReDiscuss(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void like(final Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if("0".equals(map.get("type"))){
                            Toast.makeText(mContext,"点赞成功",Toast.LENGTH_SHORT).show();
                        }
                        mView.onSucessLike(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void detail(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetDetail(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
}