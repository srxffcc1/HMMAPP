package com.health.mine.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.ZxingScanContract;
import com.health.mine.presenter.ZxingScanPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.routes.MineRoutes;

import org.greenrobot.eventbus.EventBus;

@Route(path = MineRoutes.MINE_BINDINGADVISER)
public class BindingAdviserActivity extends BaseActivity implements ZxingScanContract.View {
    private ImageView img_back;
    private EditText code_edit;
    private TextView bindingBtn;
    private ZxingScanPresenter zxingScanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_binding_adviser;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        zxingScanPresenter = new ZxingScanPresenter(this, this);
    }

    @Override
    public void getData() {
        super.getData();
        zxingScanPresenter.getCodeInfo(code_edit.getText().toString().trim());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        code_edit = findViewById(R.id.code_edit);
        bindingBtn = findViewById(R.id.bindingBtn);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        code_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    bindingBtn.setBackgroundResource(R.drawable.shape_binding_btn_selected);
                } else {
                    bindingBtn.setBackgroundResource(R.drawable.shape_binding_btn_unselected);
                }
            }
        });
        bindingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code_edit.getText().toString().trim() != null && !TextUtils.isEmpty(code_edit.getText().toString().trim())) {
                    if (code_edit.getText().toString().trim().length() >= 6) {
                        getData();
                    } else {
                        showToast("请输入正确的推荐码！");
                    }
                } else {
                    showToast("请输入推荐码！");
                }
            }
        });
    }

    @Override
    public void onBindingSuccess(String result) {

        showToast(result);
        if (result.contains("成功")) {

                    finish();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            },1000);
//            finish();
        } else {
        }
    }

    @Override
    public void onGetCodeInfoSuccess(ZxingReferralCodeModel model) {
        if (model != null) {
            zxingScanPresenter.binding(model.referralCode,model.merchantId,"0","1");
        } else {
            showToast("绑定导购员失败");
        }
    }

    @Override
    public void onGetTokerWorkerInfoSuccess(TokerWorkerInfoModel model) {

    }

    @Override
    public void ticketCoupon() {

    }
}