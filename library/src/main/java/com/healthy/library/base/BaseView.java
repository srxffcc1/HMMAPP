package com.healthy.library.base;


import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author Li
 * @date 2019/03/01 11:35
 * @des
 */

public interface BaseView {
    /**
     * 展示对话框
     */
    void showLoading();

    /**
     * 提示
     *
     * @param msg 提示信息
     */
    void showToast(CharSequence msg);



    /**
     * 显示网络错误
     */
    void showNetErr();

    /**
     * 请求开始
     *
     * @param disposable 请求
     */
    void onRequestStart(Disposable disposable);

    /**
     * 显示内容
     */
    void showContent();

    /**
     * 显示空内容 适用于内容为列表时
     */
    void showEmpty();

    /**
     * 请求结束，不管结果
     */
    void onRequestFinish();

    /**
     * 获取数据
     */
    void getData();

    /**
     * 获取数据失败
     */
    void showDataErr();

}
