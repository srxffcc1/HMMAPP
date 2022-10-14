package com.health.client.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.healthy.library.constant.Ids;
import com.healthy.library.model.PayResultModel;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

//import com.theweflex.react.WeChatModule;

/**
 * @author Li
 * @date 2019/03/13 09:43
 * @des 微信支付回调
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        com.theweflex.react.WeChatModule.handleIntent(getIntent());//在融合之后 这里注释掉
//        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Ids.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.e("tag", "onResp: " + resp.errCode);
            EventBus.getDefault().post(new PayResultModel(resp.errCode));
        }
        finish();
    }
}