package com.health.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.ZxingScanContract;
import com.health.mine.presenter.ZxingScanPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.business.LocMessageDialog;

@Route(path = MineRoutes.MINE_BINDINGZXINGRESULT)
public class BindingZxingResultActivity extends BaseActivity implements ZxingScanContract.View {
    @Autowired
    String type;
    @Autowired
    String id;
    @Autowired
    String referral_code;
    @Autowired
    String merchantId;

    private ImageView img_back;
    private LinearLayout successLiner, faildLiner;
    private TextView close, code_edit, bindingBtn;
    private TextView faildContent, faildTxt;
    private ZxingScanPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_binding_zxing_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        presenter = new ZxingScanPresenter(this, this);
        presenter.setMessageOkClickListener(new LocMessageDialog.MessageOkClickListener() {
            @Override
            public void onMessageOkClick(View view) {
                finish();
            }
        });
        if (type != null) {
            if ("-1".equals(type)) {//表示不是平台二维码
                successLiner.setVisibility(View.GONE);
                faildLiner.setVisibility(View.VISIBLE);
            } else if ("1".equals(type)) {//表示识别出了导购二维码
                successLiner.setVisibility(View.GONE);
                faildLiner.setVisibility(View.GONE);
                presenter.getTokerWorkerInfo();
            }
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        successLiner = findViewById(R.id.successLiner);
        faildLiner = findViewById(R.id.faildLiner);
        close = findViewById(R.id.close);
        code_edit = findViewById(R.id.code_edit);
        bindingBtn = findViewById(R.id.bindingBtn);
        faildContent = findViewById(R.id.faildContent);
        faildTxt = findViewById(R.id.faildTxt);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onGetCodeInfoSuccess(ZxingReferralCodeModel model) {

    }

    @Override
    public void onBindingSuccess(String result) {
        if (result != null && !result.contains("不可以绑定多个")) {
            if (result.contains("操作成功")) {
                showToast("绑定成功");
                SpUtils.store(mContext, "fun100001", null);
                finish();
            } else {
                showToast(result);//因为提示文案变化 现在只会出现一个提示 然后没有任务页面交互了
                finish();
            }
        } else {
            if (result.contains("不可以绑定多个")) {
                hasBinding();
            } else {
                showToast(result);
                finish();
            }

        }
    }

    public void hasBinding() {

        successLiner.setVisibility(View.GONE);
        faildLiner.setVisibility(View.VISIBLE);
        faildTxt.setText("已绑定");
        faildContent.setText("您已绑定母婴顾问，线上不可以绑定多个哦");
    }

    @Override
    public void onGetTokerWorkerInfoSuccess(TokerWorkerInfoModel model) {
        if (model != null) {
            if (model.bindingList != null && model.bindingList.size() > 0) {//绑定了
                boolean hasBindingLocal = false;
                for (int i = 0; i < model.bindingList.size(); i++) {
                    if (merchantId.equals(model.bindingList.get(i).bindingTokerWorker.memberId)) {//当前地绑定列表里有这个导购所属的merchantId
                        hasBindingLocal = true;
                    }
                }
                if (hasBindingLocal) {//本地已经绑定了导购
                    hasBinding();
                } else {//本地没有绑定导购尝试绑定
                    if (id != null) {
                        binding();
                    } else {
                        showToast("绑定导购员失败");
                    }
                }
            } else {// 没绑定
                binding();
            }
        }
    }

    @Override
    public void ticketCoupon() {

    }

    private void binding(){
        successLiner.setVisibility(View.VISIBLE);
        faildLiner.setVisibility(View.GONE);
        code_edit.setText(referral_code);
        bindingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != null) {
                    presenter.binding(referral_code, merchantId, "0","1");
                } else {
                    showToast("绑定导购员失败");
                }
            }
        });
    }
}