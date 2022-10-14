package com.health.client.shanyanutils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chuanglan.shanyan_sdk.listener.ShanYanCustomInterface;
import com.chuanglan.shanyan_sdk.tool.ShanYanUIConfig;
import com.health.client.R;
import com.healthy.library.routes.AppRoutes;


public class ConfigUtils {
    /**
     * 闪验三网运营商授权页配置类
     *
     * @param context
     * @return
     */

    //沉浸式竖屏样式
    public static ShanYanUIConfig getCJSConfig(final Context context) {
        /************************************************自定义控件**************************************************************/
        Drawable logBtnImgPath = context.getResources().getDrawable(R.drawable.selector_shanyan_login);
        Drawable backgruond = context.getResources().getDrawable(R.drawable.shanyan_login_bg);
        //Drawable returnBg = context.getResources().getDrawable(R.drawable.shanyan_demo_return_left_bg);
        //loading自定义加载框
//        LayoutInflater inflater = LayoutInflater.from(context);
//        RelativeLayout view_dialog = (RelativeLayout) inflater.inflate(R.layout.shanyan_demo_dialog_layout, null);
//        RelativeLayout.LayoutParams mLayoutParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        view_dialog.setLayoutParams(mLayoutParams3);
//        view_dialog.setVisibility(View.GONE);

//        //号码栏背景
//        LayoutInflater numberinflater = LayoutInflater.from(context);
//        RelativeLayout numberLayout = (RelativeLayout) numberinflater.inflate(R.layout.shanyan_phobackground_layout, null);
//        RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        numberParams.setMargins(0, 0, 0, AbScreenUtils.dp2px(context, 250));
//        numberParams.width = AbScreenUtils.getScreenWidth(context, true);
//        numberParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        numberParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        numberLayout.setLayoutParams(numberParams);
        //其他方式登录
        LayoutInflater inflater1 = LayoutInflater.from(context);
        RelativeLayout relativeLayout = (RelativeLayout) inflater1.inflate(R.layout.shanyan_other_login_item_layout, null);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOther.setMargins(0, AbScreenUtils.dp2px(context, ((int)(AbScreenUtils.getScreenHeight(context, true)*(500.0/750)))), 0, 0);
        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLayout.setLayoutParams(layoutParamsOther);
//        otherLogin(relativeLayout);

        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                //.setActivityTranslateAnim("shanyan_demo_fade_in_anim", "shanyan_dmeo_fade_out_anim")
                //授权页导航栏：
                .setNavColor(Color.parseColor("#ffffff"))  //设置导航栏颜色
                .setNavText("")  //设置导航栏标题文字
                .setNavReturnBtnWidth(35)
                .setNavReturnBtnHeight(35)
                .setAuthBGImgPath(backgruond)
                //.setLogoHidden(true)   //是否隐藏logo
                .setDialogDimAmount(0f)
                .setNavReturnImgPath(context.getResources().getDrawable(R.drawable.mine_ic_null))
                .setFullScreen(true)
                .setStatusBarHidden(false)

                //设置logo
                .setLogoImgPath(context.getResources().getDrawable(R.drawable.shanyan_login_logo))
                .setLogoWidth(140)   //设置logo宽度
                .setLogoHeight(55)   //设置logo高度
                .setLogoOffsetY((int)(AbScreenUtils.getScreenHeight(context, true)*(230.0/750)))//设置logo相对于标题栏下边缘y偏移
                .setLogoHidden(false)   //是否隐藏logo

                //授权页号码栏：
                .setNumberColor(Color.parseColor("#ffffff"))  //设置手机号码字体颜色
                .setNumFieldOffsetY((int)(AbScreenUtils.getScreenHeight(context, true)*(347.0/750)))     //设置号码栏相对于标题栏下边缘y偏移
                .setNumberSize(17)
                .setNumFieldHeight(50)

                //授权页slogan：
                .setSloganTextColor(0xffffffff)  //设置slogan文字颜色
                .setSloganOffsetY((int)(AbScreenUtils.getScreenHeight(context, true) *(390.0/750)))  //设置slogan相对于标题栏下边缘y偏移
                .setSloganTextSize(11)
                .setSloganHidden(false)
                .setShanYanSloganHidden(true)

                //授权页登录按钮：
                .setLogBtnText("本机号码一键登录")  //设置登录按钮文字
                .setLogBtnTextColor(0xffffffff)   //设置登录按钮文字颜色
                .setLogBtnImgPath(logBtnImgPath)   //设置登录按钮图片
                .setLogBtnTextSize(15)
                .setLogBtnHeight(45)
                .setLogBtnOffsetY((int)(AbScreenUtils.getScreenHeight(context, true) *(420.0/750)))
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 50)

                //授权页隐私栏：
                .setAppPrivacyOne("憨妈妈用户协议", "http://m.hanmama.com/appH5/page/agreement.html")  //设置开发者隐私条款1名称和URL(名称，url)
                .setAppPrivacyTwo("憨妈妈隐私协议", "http://m.hanmama.com/appH5/page/privacyPolicy.html")  //设置开发者隐私条款2名称和URL(名称，url)
                .setAppPrivacyColor(Color.parseColor("#ffffff"), Color.parseColor("#60C4FC"))    //	设置隐私条款名称颜色(基础文字颜色，协议文字颜色)
                .setPrivacyText("同意", "和", "、", "、", "并使用本手机号码登录")
                .setPrivacyOffsetBottomY(20)//设置隐私条款相对于屏幕下边缘y偏
                .setPrivacyState(true)
                .setPrivacyTextSize(11)
                .setPrivacyOffsetX(16)
                .setCheckBoxHidden(false)
                .setcheckBoxOffsetXY(0,12)
                .setPrivacyOffsetGravityLeft(true)
                .setUncheckedImgPath(context.getResources().getDrawable(R.drawable.shanyan_login_uncheck))
                .setCheckedImgPath(context.getResources().getDrawable(R.drawable.shanyan_login_check))
                .setShanYanSloganTextColor(Color.parseColor("#ffffff"))
                .setPrivacyGravityHorizontalCenter(false)
                .setPrivacyCustomToastText("请仔细阅读并勾选下方用户协议")
                //.addCustomView(numberLayout, false, false, null)
                //.setLoadingView(view_dialog)
                // 添加自定义控件:
                .addCustomView(relativeLayout, false, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                                .withString("shanyanType","0")
                                .navigation();
                    }
                })
                //标题栏下划线，可以不写
                .build();
        return uiConfig;
    }
//    private static void otherLogin(RelativeLayout relativeLayout) {
//        TextView shanyan_otherLogin = relativeLayout.findViewById(R.id.shanyan_otherLogin);
//        shanyan_otherLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance().build(AppRoutes.APP_LOGIN)
//                        .withString("shanyanType","0")
//                        .navigation();
//                //OneKeyLoginManager.getInstance().finishAuthActivity();
//            }
//        });
//    }
}
