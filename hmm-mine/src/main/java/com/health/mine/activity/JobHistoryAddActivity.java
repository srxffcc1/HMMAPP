package com.health.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.TopBar;

@Route(path = MineRoutes.MINE_JOB_ADDWANT)
public class JobHistoryAddActivity extends BaseActivity {
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.EditText chatContent;
    private android.widget.TextView chatCount;
    @Autowired
    public String teachTitle;
    @Autowired
    public String teachTmp;
    private int limitnum = 300;
    private boolean lengthlimit = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_jobaddnormal;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if(!TextUtils.isEmpty(teachTitle)){
            topBar.setTitle(teachTitle);
            if("个人专长".equals(teachTitle)){
                limitnum=100;
            }
        }
        if(!TextUtils.isEmpty(teachTmp)){
            if (teachTmp.length() >= 10) {
                lengthlimit = true;
            } else {
                lengthlimit = false;
            }
        }
        chatCount.setText((TextUtils.isEmpty(teachTmp)?0:teachTmp.length())+"/"+limitnum);
        chatContent.setText(teachTmp);
        chatContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                //System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = limitnum - s.length();
//                tv_num.setText("" + number);
                selectionStart = chatContent.getSelectionStart();
                selectionEnd = chatContent.getSelectionEnd();
                ////System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > limitnum) {
                    showToast("限制输入" + limitnum + "个字");
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    chatContent.setText(s);
                    chatContent.setSelection(tempSelection);//设置光标在最后
                }else {
                    chatCount.setText(temp.length()+"/"+limitnum+"字");
                }

                if (temp.length() >= 10) {
                    lengthlimit = true;
                } else {

                    lengthlimit = false;
                }
            }
        });
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                if(!lengthlimit){
                    Toast.makeText(mContext,"至少输入10个字",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent();
                    intent.putExtra("detail",chatContent.getText().toString());
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        chatContent = (EditText) findViewById(R.id.chatContent);
        chatCount = (TextView) findViewById(R.id.chatCount);
    }
}
