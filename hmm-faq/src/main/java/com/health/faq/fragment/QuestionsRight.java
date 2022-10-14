package com.health.faq.fragment;

import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.faq.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;

public class QuestionsRight extends BaseFragment {
    private TextView passGo;
    private ImageView rightImg;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_faq_question_right;
    }

    @Override
    protected void findViews() {
        initView();
        passGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MessageList";
                url = String.format(url,
                        new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef",true)
                        .withString("title", "在线医生")
                        .withBoolean("doctorshop",true)
                        .withBoolean("isinhome",false)
                        .withString("url", url)
                        .navigation();
            }
        });
    }

    private void initView() {
        passGo = (TextView) findViewById(R.id.passGo);
        rightImg = (ImageView) findViewById(R.id.right_img);
    }
}
