package com.health.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.EnlistSignOffContract;
import com.health.mine.contract.ZxingScanContract;
import com.health.mine.presenter.EnListSignOffPresenter;
import com.health.mine.presenter.ZxingScanPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.EnlistActivityModel;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.ImageUtil;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Route(path = MineRoutes.MINE_ZXING_SCAN)
public class ZxingScanActivity extends BaseActivity implements IsFitsSystemWindows, ZxingScanContract.View, EnlistSignOffContract.View {
    private String[] mPermissions = new String[]{
            Manifest.permission.CAMERA};
    private CaptureFragment captureFragment;
    private static final int RC_PERMISSION = 45;
    private ImageView flashlight, photo;
    private ConstraintLayout scan_failLL;
    private TextView inputCode;
    private boolean isOpen = false;
    private static final int REQUEST_IMAGE = 666;
    private ZxingScanPresenter zxingScanPresenter;
    private EnListSignOffPresenter mEnlistSignOffPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_zxingscan;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        zxingScanPresenter = new ZxingScanPresenter(this, this);
        mEnlistSignOffPresenter = new EnListSignOffPresenter(this, this);
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            buildZxingMain();
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
        }

    }

    private void buildZxingMain() {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_my_zxingcamera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意权限");
//                PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
            } else {
                buildZxingMain();
            }
        }
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            scan_failLL.setVisibility(View.GONE);
            codeSee(result);
        }

        @Override
        public void onAnalyzeFailed() {
            zxingFail();
        }
    };

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            //System.out.println("解析成功");
                            scan_failLL.setVisibility(View.GONE);
                            codeSee(result);

                        }

                        @Override
                        public void onAnalyzeFailed() {
                            zxingFail();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void codeSee(String result) {
        if (result.contains("referral_code")) {//这里说明扫码成功并且格式正确
            String str = result.split("\\?").length > 1 ? result.split("\\?")[1] : result.split("\\?")[0];
            Map<String, String> map = new HashMap<>();
            String[] resultarray = str.split("&");
            for (int i = 0; i < resultarray.length; i++) {
                map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
            }
            String code = map.get("referral_code");

            if (TextUtils.isEmpty(code)) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_BINDINGZXINGRESULT)
                        .withString("type", "-1")
                        .navigation();
                finish();
            } else {
                zxingScanPresenter.getCodeInfo(code);
            }
        } else if (result.contains("courseId")) {//直播预告码

            String str = result.split("\\?").length > 1 ? result.split("\\?")[1] : result.split("\\?")[0];
            Map<String, String> map = new HashMap<>();
            String[] resultarray = str.split("&");
            for (int i = 0; i < resultarray.length; i++) {
                map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
            }
            String code = map.get("courseId");
            if (!TextUtils.isEmpty(code)) {
                LogUtils.e(str);
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveNotice)
                        .withString("courseId", code)
                        .navigation();
                finish();
            } else {
                showToast("未获取到直播Id");
            }
        } else if (result.contains("anchormanId")) {//主播详情码
            String str = result.split("\\?").length > 1 ? result.split("\\?")[1] : result.split("\\?")[0];
            Map<String, String> map = new HashMap<>();
            String[] resultarray = str.split("&");
            for (int i = 0; i < resultarray.length; i++) {
                map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
            }
            String code = map.get("anchormanId");
            if (!TextUtils.isEmpty(code)) {
                LogUtils.e(str);
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostMain)
                        .withString("anchormanId", code)
                        .navigation();
                finish();
            } else {
                showToast("未获取到主播Id");
            }
        } else if (result.contains("operation_code")) {
            String str = result.split("\\?").length > 1 ? result.split("\\?")[1] : result.split("\\?")[0];
            Map<String, String> map = new HashMap<>();
            String[] resultarray = str.split("&");
            for (int i = 0; i < resultarray.length; i++) {
                map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
            }
            String code = map.get("operation_code");
//                                https://capi.hanmama.com/cserver/public/actionHandler?operation_code=1
//                                https://capi.hanmama.com/cserver/public/actionHandler?operation_code=0
            if ("1".equals(code)) {
                SpUtils.store(mContext, SpKey.OPERATIONSTATUS, true);
                Toast.makeText(mContext, "已为您开启运营模式", Toast.LENGTH_SHORT).show();
            } else {
                SpUtils.store(mContext, SpKey.OPERATIONSTATUS, false);
                Toast.makeText(mContext, "已为您关闭运营模式", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else if (result.contains("LiveQrLogin")) {//## 扫码获取到的qrCode值  这里是扫码登录
            ARouter.getInstance()
                    .build(AppRoutes.APP_ZXINGLOGIN)
                    .withString("qrCode", resolveData(result))
                    .navigation();
            finish();
        } else if (result.contains("verificationCode") && result.contains("enlistActivityId")) {
            Map<String, Object> map = new HashMap<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                map.put("enlistActivityId", jsonObject.getString("enlistActivityId"));
                map.put("verificationCode", jsonObject.getString("verificationCode"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*String[] split = result.split("&&");
            if (split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    String[] param = split[i].split("=");
                    map.put(param[0], param[1]);
                }
            }*/
            mEnlistSignOffPresenter.getCodeInfo(map);
        } else if (result.contains("HMMTHM")) {//说明这里扫的是核销码
            ARouter.getInstance()
                    .build(SecondRoutes.SECOND_ORDERTICKET)
                    .withString("ticket", result.split(":", result.length())[1])
                    .navigation();
            finish();

        } else if (result.contains("veryCouponId")) {//说明这里扫的是优惠券
            String couponId = result.split("=")[1];
            if (!TextUtils.isEmpty(couponId)) {
                zxingScanPresenter.ticketCoupon(couponId);
            } else {
                showToast("扫码信息有误，请扫描正确的优惠券核销码！");
            }
        } else {//这里说明扫码成功但不是带导购信息的二维码
            if (result != null && result.startsWith("http")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(result);
                intent.setData(content_url);
                startActivity(intent);
            } else {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_BINDINGZXINGRESULT)
                        .withString("type", "-1")
                        .navigation();
            }

        }
    }

    private String resolveData(String data) {
        //content : "{"type":"LiveQrLogin","content":"7d77bf2b030f4bb98284004cb0e89b4c"}"
        if (data == null || TextUtils.isEmpty(data)) {
            return null;
        }
        String result = null;
        try {
            result = new JSONObject(data).optString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void zxingFail(String msg) {
        ARouter.getInstance()
                .build(MineRoutes.MINE_BINDINGZXINGRESULT)
                .withString("type", "-1")
                .navigation();
        finish();
    }

    private void zxingFail() {
        showToast("扫码失败");
        scan_failLL.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scan_failLL.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    protected void findViews() {
        super.findViews();
        flashlight = findViewById(R.id.flashlight);
        photo = findViewById(R.id.photo);
        scan_failLL = findViewById(R.id.scan_failLL);
        inputCode = findViewById(R.id.inputCode);
        onClick();
    }

    private void onClick() {
        flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {//开启或关闭手电筒
                    CodeUtils.isLightEnable(true);
                    flashlight.setImageResource(R.drawable.flashlight_open);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    flashlight.setImageResource(R.drawable.flashlight_close);
                    isOpen = false;
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        inputCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_BINDINGADVISER)
                        .navigation();
                finish();
            }
        });
    }

    @Override
    public void onGetCodeInfoSuccess(ZxingReferralCodeModel model) {
        if (model != null) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_BINDINGZXINGRESULT)
                    .withString("referral_code", model.referralCode)
                    .withString("id", model.id)
                    .withString("merchantId", model.merchantId)
                    .withString("type", "1")
                    .navigation();
            finish();
        } else {
            zxingFail("绑定失败");
        }
    }

    @Override
    public void onBindingSuccess(String result) {

    }

    @Override
    public void onGetTokerWorkerInfoSuccess(TokerWorkerInfoModel model) {

    }

    @Override
    public void ticketCoupon() {
        finish();
    }

    /**
     * 获取签核信息
     *
     * @param resultModel
     */
    @Override
    public void getCodeInfoSuccess(EnlistActivityModel resultModel) {
        ARouter.getInstance()
                .build(MineRoutes.MINE_ENLIST_SIGNOFF)
                .withString("verificationCode", resultModel.getVerificationCode())
                .withString("enlistActivityId", resultModel.getEnlistActivity().getId())
                .navigation();
        finish();
    }

    @Override
    public void confirmSignOffSuccess() {

    }
}
