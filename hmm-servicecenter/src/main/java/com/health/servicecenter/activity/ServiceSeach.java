package com.health.servicecenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.ServiceRoutes;

@Route(path = ServiceRoutes.SERVICE_SEACH)
public class ServiceSeach extends BaseActivity implements TextView.OnEditorActionListener, IsFitsSystemWindows {
    @Autowired
    String tagSeach;
    private android.widget.EditText serarchKeyWord;
    private android.widget.ImageView clearEdit;
    private android.widget.ImageView imgBack;
    private android.widget.TextView cancelBack;

    @Override
    protected int getLayoutId() {
        return R.layout.service_activity_seach_flow;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serarchKeyWord.setOnEditorActionListener(this);
        clearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serarchKeyWord.setText("");
            }
        });
        serarchKeyWord.setText(tagSeach);
        serarchKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    clearEdit.setVisibility(View.VISIBLE);
                } else {
                    clearEdit.setVisibility(View.GONE);
                }
            }
        });
        cancelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serarchKeyWord.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                serarchKeyWord.requestFocus();
            }
        }, 300);
    }

    private void buildRecyclerView() { }

    private void initView() {
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        clearEdit = (ImageView) findViewById(R.id.clearEdit);
        imgBack = (ImageView) findViewById(R.id.img_back);
        cancelBack = (TextView) findViewById(R.id.cancel_back);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(serarchKeyWord.getText().toString())) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                        .withString("categoryId", 0 + "")
                        .withString("goodsTitle", serarchKeyWord.getHint().toString())
                        .navigation();
            } else {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_SERVICESORTGOODS)
                        .withString("categoryId", 0 + "")
                        .withString("goodsTitle", serarchKeyWord.getText().toString())
                        .navigation();
            }
            finish();
        }
        return false;
    }
}
