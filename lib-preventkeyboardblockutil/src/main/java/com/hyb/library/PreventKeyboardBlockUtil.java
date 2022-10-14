package com.hyb.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * 类描述：防止软键盘弹出时挡住相关按钮或布局
 * 创建人：huangyaobin
 * 创建时间：2019/6/13
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class PreventKeyboardBlockUtil {

    private static PreventKeyboardBlockUtil preventKeyboardBlockUtil;
    private View mBtnView;
    private ViewGroup rootView;
    private boolean isMove;
    private int marginBottom = 0;
    private KeyboardHeightProvider keyboardHeightProvider;
    private int keyBoardHeight = 0;
    private int btnViewY = 0;
    private boolean isRegister = false;
    private AnimatorSet animSet = new AnimatorSet();
    private WeakReference<Activity> mWeakReference;

    public static PreventKeyboardBlockUtil getInstance() {
        if (preventKeyboardBlockUtil == null) {
            preventKeyboardBlockUtil = new PreventKeyboardBlockUtil();
        }
        return preventKeyboardBlockUtil;
    }

    public PreventKeyboardBlockUtil setContext(Activity activity) {
        try {
            if (mWeakReference == null || mWeakReference.get() != activity) {
                mWeakReference = new WeakReference<>(activity);
                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preventKeyboardBlockUtil;
    }

    private void initData() {
        mWeakReference.get().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        rootView = (ViewGroup) ((ViewGroup) mWeakReference.get().findViewById(android.R.id.content)).getChildAt(0);
        isMove = false;
        marginBottom = 0;
        if (keyboardHeightProvider != null) {
            keyboardHeightProvider.recycle();
            keyboardHeightProvider = null;
        }
        keyboardHeightProvider = new KeyboardHeightProvider(mWeakReference.get());
    }

    public PreventKeyboardBlockUtil setBtnView(View btnView) {
        mBtnView = btnView;
        return preventKeyboardBlockUtil;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startAnim(msg.arg1);
        }
    };

    void startAnim(int transY) {
        if (rootView == null) {
            return;
        }
        float curTranslationY = rootView.getTranslationY();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(rootView, "translationY", curTranslationY, transY);
        animSet.play(objectAnimator);
        animSet.setDuration(200);
        animSet.start();
    }

    public void register() {
        try {
            if (mWeakReference.get() == null) {
                return;
            }
            isRegister = true;

            keyboardHeightProvider.setKeyboardHeightObserver(new KeyboardHeightObserver() {
                @Override
                public void onKeyboardHeightChanged(int height, int orientation) {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        return;
                    }
                    if (!isRegister) {
                        return;
                    }

                    if (keyBoardHeight == height) {
                        return;
                    } else {
                        keyBoardHeight = height;
                    }

                    if (keyBoardHeight <= 0) {//键盘收起
                        if (isMove) {

                            sendHandlerMsg(0);

                            isMove = true;
                        }
                    } else {//键盘打开

                        int keyBorardTopY = ScreenUtils.getAppScreenHeight(mWeakReference.get()) - keyBoardHeight;
                        if (keyBorardTopY > (btnViewY + mBtnView.getHeight())) {
                            return;
                        }
                        int margin = keyBorardTopY - (btnViewY + mBtnView.getHeight());
                        Log.i("tag", "margin:" + margin);
                        sendHandlerMsg(margin);

                        isMove = true;
                    }

                }
            });

            mBtnView.post(new Runnable() {
                @Override
                public void run() {
                    btnViewY = getViewLocationYInScreen(mBtnView);
                    try {
                        keyboardHeightProvider.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void unRegister() {
        try {
            if (mWeakReference.get() == null) {
                return;
            }
            KeyboardUtils.hideSoftInput(mWeakReference.get());
        }catch (Exception e){
            e.printStackTrace();
        }
        isRegister = false;
        keyBoardHeight = 0;
        sendHandlerMsg(0);
        if (keyboardHeightProvider != null) {
            keyboardHeightProvider.setKeyboardHeightObserver(null);
            keyboardHeightProvider.recycle();
            keyboardHeightProvider.close();
            keyboardHeightProvider = null;
        }
        recycle();
    }

    public void recycle() {
        if (mBtnView != null) {
            mBtnView = null;
        }
        if (rootView != null) {
            rootView = null;
        }
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
        if (preventKeyboardBlockUtil != null) {
            preventKeyboardBlockUtil = null;
        }
    }

    private void sendHandlerMsg(int i) {
        Message message = new Message();
        message.arg1 = i;
        mHandler.sendMessage(message);
    }

    private int getViewLocationYInScreen(View view) {
        int[] location = new int[2];
        try {
            view.getLocationOnScreen(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location[1];
    }
}
