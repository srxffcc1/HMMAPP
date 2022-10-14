package com.healthy.library.base;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.interfaces.IsFirstFragment;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.widget.StatusLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * @author Li
 * @date 2019/03/01 11:35
 * @des fragment
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener, BaseView,
        OnNetRetryListener, UMShareListener {

    private boolean mIsFirstVisible = true;
    protected Context mContext;
    public boolean isfragmenthow;
    protected boolean cantoast = true;

    public View.OnClickListener onClickListener;
    /**
     * 是否第一次加载
     */
    protected boolean isFirstLoad = false;

    //判断当前fragment是否可见
    public boolean isVisibleToUser = false;
    //判断当前fragment是否回调了resume
    private boolean isResume = false;
    private boolean isCallUserVisibleHint = false;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
    private AlertDialog mShareDialog;
    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;


    public Map<String, Object> getBasemap() {
        return basemap;
    }

    private Map<String, Object> basemap = new HashMap<>();
    protected View mContentView;
    protected Activity mActivity;
    private StatusLayout mStatusLayout;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }

    public static void bundleMap(Map<String, Object> maporg, Bundle args) {
        if (maporg != null) {
            for (Map.Entry<String, Object> entry : maporg.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer) {
                    args.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    args.putBoolean(key, (Boolean) value);
                } else if (value instanceof String) {
                    args.putString(key, (String) value);
                } else {
                    args.putSerializable(key, (Serializable) value);
                }
            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlatformMap.clear();
        mPlatformMap.put(ResUtils.getStrById(mContext, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(mContext, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(mContext, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(mContext, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(mContext, R.string.lib_share_sina), SHARE_MEDIA.SINA);
        Bundle args = getArguments();
        if (args != null) {
            Set<String> keySet = args.keySet();
            for (String key : keySet) {
                Object value = args.get(key);
                basemap.put(key, value);
            }
        }
        if(this instanceof IsFirstFragment){
            isfragmenthow=true;
        }


    }

    public void showShare() {
        if (this instanceof IsNeedShare) {
            IsNeedShare isNeedShare = (IsNeedShare) this;
            surl = isNeedShare.getSurl();
            stitle = isNeedShare.getStitle();
            sdes = isNeedShare.getSdes();
            if (TextUtils.isEmpty(sdes)) {
                sdes = " ";
            }
            if (sBitmap != null) {
                //稍作 bitmap优化
                sBitmap.recycle();
                sBitmap = null;
            }
            sBitmap = isNeedShare.getsBitmap();
            if (sBitmap == null) {
                sBitmap = BitmapUtils.changeColor(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.index_share_humb));
            }
            showShareBottomDialog();
        }
    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            share(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle, sBitmap);
            mShareDialog.dismiss();
        }
    };

    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void share(SHARE_MEDIA shareMedia, String url, String des, String title, Bitmap bitmap) {

//        BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb))
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(mContext, bitmap));
        web.setDescription(des);
        new ShareAction(getActivity())
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    private void showShareBottomDialog() {
        if (mShareDialog == null) {
            try {
                mShareDialog = new AlertDialog.Builder(mContext).create();
                View shareSheet = LayoutInflater.from(mContext)
                        .inflate(R.layout.lib_video_share_sheet2, null);
                shareSheet.findViewById(R.id.iv_share_close)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mShareDialog.dismiss();
                            }
                        });
                shareSheet.findViewById(R.id.tv_wx).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_timeline).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_qq).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_qzone).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_sina).setOnClickListener(mShareClick);

                mShareDialog.show();
                mShareDialog.setContentView(shareSheet);
                Window window = mShareDialog.getWindow();
                if (window != null) {
                    window.setWindowAnimations(R.style.DialogAnim);
                    View decorView = window.getDecorView();
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    params.gravity = Gravity.BOTTOM;
                    decorView.setPadding(0, 0, 0, 0);
                    window.setAttributes(params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            mShareDialog.show();
        }

    }

    //获取状态栏的高度
    public int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mContentView = view;
        mStatusLayout = mContentView.findViewById(R.id.layout_status);
        if (mStatusLayout != null) {
            mStatusLayout.setOnNetRetryListener(this);
        }
        if (onClickListener != null) {
            view.setOnClickListener(onClickListener);
        }
        //isFirstLoad = true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        onCreate();
        if(!isNeedLazy()){
            isFirstLoad=true;
            getData();
        }

    }
    public boolean isNeedLazy(){
        return true;
    }

    boolean isShowTime = true;

    @Override
    public void onResume() {
        super.onResume();
//        if(isResumed()&&getUserVisibleHint()){
//            //System.out.println("当前fragment显示Ok:"+getClass().getSimpleName());
////            isfragmenthow=true;
//        }
        // 视图可见且第一次加载 执行加载数据
        isResume = true;
        if (!isCallUserVisibleHint) isVisibleToUser = !isHidden();
        /*if (!isFirstLoad) {
            onLazyData();
            isFirstLoad = true;
        }*/
        onFirstData();

        Log.e(getClass().getSimpleName(), "onResume: ");
    }

    public final void onFirstData() {

        Log.e(getClass().getSimpleName(), "onFirstData: " + isFirstLoad+":"+isVisibleToUser+":"+isResume);
        if (!isFirstLoad && isVisibleToUser && isResume) {
            //懒加载。。。
            isFirstLoad = true;
            onLazyData();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(getClass().getSimpleName(), "onHiddenChanged: " + hidden);
        isVisibleToUser = !hidden;
        onFirstData();
        if (!hidden) {
            System.out.println("当前fragment显示方法2:"+getClass().getSimpleName());
            changeFragmentShow();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.e(getClass().getSimpleName(), "setUserVisibleHint: " + isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        isCallUserVisibleHint = true;
        onFirstData();
        if (getActivity() == null || getContext() == null || getView() == null) {
            return;
        }
        if (isVisibleToUser && mIsFirstVisible) {
            onFirstVisible();
            mIsFirstVisible = false;
        }
        if (isVisibleToUser) {
            System.out.println("当前fragment显示方法:"+getClass().getSimpleName());
            changeFragmentShow();
        }
        if (!isVisibleToUser) {
            changeFragmentHide();
        }
    }

    public void changeFragmentHide() {
        onInvisible();
        System.out.println("当前fragment不显示:"+getClass().getSimpleName());
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {//对内部的fragment进行设置状态跟随外部切换而改变
            Fragment fragment = fragments.get(i);
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                System.out.println("当前fragment子不显示:"+baseFragment.getClass().getSimpleName());
                baseFragment.changeFragmentHide();
            }
        }
        isfragmenthow = false;
    }

    public void changeFragmentShow() {
        onVisible();
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {//对内部的fragment进行设置状态跟随外部切换而改变
            Fragment fragment = fragments.get(i);
            if (fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;
                System.out.println("当前fragment显示子:"+baseFragment.getClass().getSimpleName());
                baseFragment.changeFragmentShow();
            }
        }
        if (isResumed() && getUserVisibleHint()) {
            System.out.println("当前fragment显示真实:"+getClass().getSimpleName());
            isfragmenthow = true;
        }
    }

    public void setStatusLayout(StatusLayout statusLayout) {
        mStatusLayout = statusLayout;
        mStatusLayout.setOnNetRetryListener(this);
    }

    /**
     * 获取内容布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 绑定控件
     */
    protected abstract void findViews();

    /**
     * 懒加载触发 (暂时只适用于ViewPager2)
     * 2021-09-03 统一都支持懒加载
     */
    protected void onLazyData() {
        Log.e(this.getClass().getSimpleName(), "onLazyData: ");
    }

    protected void onFirstVisible() {
    }

    protected void onVisible() {
    }

    protected void onInvisible() {
    }

    protected void onCreate() {

    }


    public View getContentView() {
        return mContentView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_LOADING);
        }

    }

    @Override
    public void showNetErr() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_NET_ERR);
        }
    }

    @Override
    public void showDataErr() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_DATA_ERR);
        }
    }

    private void changeStatus(long initialDelay) {
        Observable.intervalRange(0, 1, initialDelay, 0, TimeUnit.SECONDS, Schedulers.io())
                .to(RxLifecycleUtils.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        cantoast = true;
                    }
                });
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast roast = Toast.makeText(LibApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        if (cantoast) {
            cantoast = false;
            //System.out.println("展示Toast");
            roast.show();
            changeStatus(2);
        }
    }

    @Override
    public void showEmpty() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_EMPTY);
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onRequestStart(Disposable disposable) {
        addDisposable(disposable);
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return getContentView().findViewById(id);
    }

    @Override
    public void onDestroyView() {
        isFirstLoad = false;
        isCallUserVisibleHint = false;
        isVisibleToUser = false;
        isResume = false;
//        mActivity = null;
//        mContext = null;
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showContent() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CONTENT);
        }
    }

    @Override
    public void onRetryClick() {
        getData();
    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    public <T> T get(String key){
        try {
            return (T) basemap.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return (T) "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return (T) new Long(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return (T) new Integer(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return (T) new Boolean(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public final void onStart(SHARE_MEDIA shareMedia) {
    }

    @Override
    public final void onResult(SHARE_MEDIA shareMedia) {
//        Toast.makeText(LibApplication.getAppContext(), "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onError(SHARE_MEDIA shareMedia, Throwable throwable) {
        Toast.makeText(LibApplication.getAppContext(), "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onCancel(SHARE_MEDIA shareMedia) {
        Toast.makeText(LibApplication.getAppContext(), "分享已取消", Toast.LENGTH_SHORT).show();
    }
}