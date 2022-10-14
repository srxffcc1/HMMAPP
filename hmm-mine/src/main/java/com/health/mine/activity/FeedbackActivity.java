package com.health.mine.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.mine.R;
import com.health.mine.contract.FeedbackContract;
import com.health.mine.presenter.FeedbackPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.InputFilterUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.Locale;

/**
 * @author Li
 * @date 2019/05/10 13:58
 * @des 意见反馈
 */
@Route(path = MineRoutes.MINE_FEEDBACK)
public class FeedbackActivity extends BaseActivity implements TextWatcher, FeedbackContract.View {

    private EditText mEtFeedback;
    private TextView mTvNum;
    private TopBar mTopBar;
    private StatusLayout mStatusLayout;
    private FeedbackPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_feedback;
    }

    @Override
    protected void findViews() {
        mEtFeedback = findViewById(R.id.et_feedback);
        mTvNum = findViewById(R.id.tv_num);
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mEtFeedback.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(300),
                new InputFilterUtils.NoEmojiFilter()});
        mEtFeedback.addTextChangedListener(this);
        mEtFeedback.setText("");
        mPresenter = new FeedbackPresenter(this, this);
    }

    @Override
    public void showNetErr() {
    }

    @Override
    public void showDataErr() {

    }

    /**
     * 提交意见反馈
     */
    public void commit(View view) {
        if (mEtFeedback.getText().toString().length() == 0) {
            //修改提示文案样式
            showToast("请输入您的宝贵意见");
        } else {
            mPresenter.commitFeedback(mEtFeedback.getText().toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTvNum.setText(String.format(Locale.CHINA, "%d/%d", s.length(), 300));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCommitFeedbackSuccess() {
        finish();
    }
}
