package com.health.client.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.contract.UpdateStatusContract;
import com.health.client.presenter.UpdateStatusPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.DaysLimit;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.dialog.SingleWheelDialog;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/19 09:57
 * @des 产后期 1.选择分娩日期  2.选择分娩方式  3.选择性别
 */
@Route(path = AppRoutes.APP_STATUS_PARENTING)
public class StatusParentingActivity extends BaseActivity implements OnDateConfirmListener,
        SingleWheelDialog.OnConfirmClickListener, UpdateStatusContract.View , IsNoNeedPatchShow {
    @Autowired
    String sex = "女";
    @Autowired
    String id;
    @Autowired
    String useStatus;
    @Autowired
    String babyId;
    @Autowired
    String isadd;
    private Group mGroupStep1;
    private Group mGroupStep2;
    private Group mGroupStep3;
    private Group mGroupStep4;
    private EditText et_Name;
    private TextView finishname;

    private DateDialog mBornDateDialog;
    private SingleWheelDialog mBornTypeDialog;

    private TextView mTvBornDate;
    private TextView mTvBornType;

    @Autowired(name = Constants.STATUS)
    String mStatus;
    String baby_sex;
    private UpdateStatusPresenter mUpdateStatusPresenter;
    private String mBornDate;
    private String mBornType;
    private StatusLayout mStatusLayout;
    private TextView tv_title_step_1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_status_parenting;
    }

    @Override
    protected void findViews() {
        mGroupStep1 = findViewById(R.id.group_step_1);
        mGroupStep2 = findViewById(R.id.group_step_2);
        mGroupStep3 = findViewById(R.id.group_step_3);
        mGroupStep4 = findViewById(R.id.group_step_4);
        mTvBornDate = findViewById(R.id.tv_step_1);
        mTvBornType = findViewById(R.id.tv_step_2);
        et_Name = findViewById(R.id.et_Name);
        finishname = findViewById(R.id.finishname);
        mStatusLayout = findViewById(R.id.layout_status);
        tv_title_step_1 = findViewById(R.id.tv_title_step_1);
        mTvBornDate.performClick();

    }

    private int num = 5;

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setStatusLayout(mStatusLayout);
        mUpdateStatusPresenter = new UpdateStatusPresenter(mContext, this);
        et_Name.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        et_Name.setImeOptions(EditorInfo.IME_ACTION_GO);
//        et_Name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId== EditorInfo.IME_ACTION_GO){
//                    if("".equals(et_Name.getText().toString())){
//                        Toast.makeText(mContext,"宝宝昵称不能为空",Toast.LENGTH_SHORT).show();
//                    }else {
//                        save();
//                    }
//                }
//                return false;
//            }
//        });
        et_Name.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = editable.length();
                selectionStart = et_Name.getSelectionStart();
                selectionEnd = et_Name.getSelectionEnd();
                if (temp.length() > num) {
                    showToast("昵称不得超过" + num + "个字");
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    et_Name.setText(editable);
                    et_Name.setSelection(tempSelection);//设置光标在最后
                    finishname.setVisibility(View.GONE);
                } else {
                    finishname.setVisibility(View.VISIBLE);
                    if (temp.length() > 0) {
                        finishname.setVisibility(View.VISIBLE);
                    }
                }


//                if(number>0){
//                    if(number>5){
//                        Toast.makeText(mContext,"",Toast.LENGTH_SHORT).show();
//
//
//                    }
//
//                }else {
//
//                }
            }
        });
        mGroupStep1.setVisibility(View.VISIBLE);
        finishname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        if ("女".equals(sex)) {

            mTvBornDate.setText("点此选择分娩日期");
        } else {
            tv_title_step_1.setText("请选择宝宝出生日期");
            mTvBornDate.setText("点此选择宝宝出生日期");
        }
    }

    private void showReviewBoard(EditText editText) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.4f;
//        getWindow().setAttributes(lp);
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                editText.requestFocus();
            }
        }, 300);
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        editText.requestFocus();
//        editText.setCursorVisible(true);
    }


    /**
     * 选择分娩日期
     *
     * @param view view
     */
    public void chooseBornDate(View view) {
        if (mBornDateDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration = currentMill - DaysLimit.DAYS_BORN * 24 * 60 * 60 * 1000L;
            mBornDateDialog = DateDialog.newInstance(
                    currentMill, duration, currentMill, ""
            );
            mBornDateDialog.setOnConfirmClick(this);
        }
        mBornDateDialog.show(getSupportFragmentManager(), "bornDate");


    }

    /**
     * 选择分娩方式
     *
     * @param view view
     */
    public void chooseBornType(View view) {
        if (mBornTypeDialog == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add("顺产");
            list.add("剖宫产");
            mBornTypeDialog = SingleWheelDialog.newInstance(list);
            mBornTypeDialog.setOnConfirmClick(this);
        }
        mBornTypeDialog.show(getSupportFragmentManager(), "bornType");
    }

    @Override
    public void onConfirm(int pos, Date data) {
        String time = TimestampUtils.timestamp2String(data.getTime(), "yyyy-MM-dd");
        mTvBornDate.setText(time);
        mBornDate = time;
        mGroupStep1.setVisibility(View.GONE);
        mGroupStep2.setVisibility(View.VISIBLE);
        mGroupStep3.setVisibility(View.GONE);
        mGroupStep4.setVisibility(View.GONE);
        mTvBornType.performClick();
    }

    @Override
    public void onClick(int pos, String data) {
        mTvBornType.setText(data);
        mBornType = String.valueOf(pos + 1);
        mGroupStep1.setVisibility(View.GONE);
        mGroupStep2.setVisibility(View.GONE);
        mGroupStep3.setVisibility(View.VISIBLE);
        mGroupStep4.setVisibility(View.GONE);
    }

    public void chooseSex(View view) {
        baby_sex = view.getId() == R.id.iv_boy ? Constants.SEX_BOY : Constants.SEX_GIRL;
        mGroupStep1.setVisibility(View.GONE);
        mGroupStep2.setVisibility(View.GONE);
        mGroupStep3.setVisibility(View.GONE);
        mGroupStep4.setVisibility(View.VISIBLE);
        showReviewBoard(et_Name);
    }

    public void save() {
        Map<String, Object> map = new HashMap<>();
        map.put("babyName", et_Name.getText().toString());
        map.put("memberSex", "女".equals(sex) ? "2" : "1");
        map.put("currentStatus", mStatus);
        map.put("stageStatus", mStatus);
        map.put("deliveryDate", mBornDate);
        map.put("deliveryMode", mBornType);
        map.put("babySex", baby_sex);
        if (id != null && !"".equals(id)) {
            map.put("id", id);
            map.put("useStatus", useStatus);
            mUpdateStatusPresenter.updateStatusEx(map);
        } else {

            mUpdateStatusPresenter.updateStatus(map);
        }
    }

    @Override
    public void onUpdateSuccess() {
        EventBus.getDefault().post(new UpdateUserInfoMsg());
        if (id != null && !"".equals(id)) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            if (isadd != null && !"".equals(isadd)) {
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mContext, MainActivity.class);
                startActivity(intent);
//                ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO).navigation();
            }

        }

    }

    @Override
    public void onUpdateSuccessEx() {
        EventBus.getDefault().post(new UpdateUserInfoMsg());
        if (id != null && !"".equals(id)) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            if (isadd != null && !"".equals(isadd)) {
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mContext, MainActivity.class);
                startActivity(intent);
//                ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO).navigation();
            }

        }
    }

    @Override
    public void onUpdateFail() {

    }

    @Override
    public void onRequestFinish() {
        mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    @Override
    public void showNetErr() {
    }

    @Override
    public void showDataErr() {

    }

    public void back(View view) {
        finish();
    }
}
