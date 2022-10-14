package com.health.mine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.PersonalDetailContract;
import com.health.mine.model.UserInfoExModel;
import com.healthy.library.model.UserInfoModel;
import com.health.mine.presenter.PersonalDetailPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.DaysLimit;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.dialog.MenstruationDialog;
import com.healthy.library.dialog.SingleWheelDialog;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CommonSection;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/09 10:59
 * @des 个人信息详细界面
 */
@Route(path = MineRoutes.MINE_PERSONAL_INFO_DETAIL)
public class PersonalInfoDetailActivity extends BaseActivity implements PersonalDetailContract.View {
    @Autowired
    String id;
    String useStatus;
    String babyId = "";
    String stageStatus;
    String memberSex;
    int memberSexIndex;
    private TopBar mTopBar;
    private StatusLayout mStatusLayout;
    private PersonalDetailPresenter mPresenter;
    private SectionView mSectionStatus;

    private SectionView mSectionBirthDate;
    private SectionView mSectionBabySex;
    private CommonSection mSectionBabyName;
    private SectionView mSectionBirthType;

    private SectionView mSectionMenLastDate;
    private SectionView mSectionMenStartDate;
    private SectionView mSectionMenCycle;
    private TopBar topBar;
    private TextView saveNow;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_personal_info_detail;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
        mSectionStatus = findViewById(R.id.section_status);

        mSectionBirthDate = findViewById(R.id.section_baby_birth_date);
        mSectionBabySex = findViewById(R.id.section_baby_sex);
        mSectionBabyName = findViewById(R.id.section_baby_name);
        mSectionBirthType = findViewById(R.id.section_baby_birth_type);

        mSectionMenLastDate = findViewById(R.id.section_men_last_date);

        mSectionMenStartDate = findViewById(R.id.section_menstruation_date);
        mSectionMenCycle = findViewById(R.id.section_menstruation_cycle);
        initView();
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);

        mPresenter = new PersonalDetailPresenter(this, this);
        saveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userInfoModel==null){
                    finish();
                    return;
                }
                int status = userInfoModel.getStageStatus();
                if (status == 1) {
                    finish();
                } else if (status == 2) {

                    finish();
                } else {
                    if ("".equals(mSectionBabyName.getEtContent().getText().toString())) {

                    } else {
                        updateUserInfoWithFinish("babyName", mSectionBabyName.getEtContent().getText().toString());
                    }
                }
            }
        });
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                if (userInfoModel != null) {
                    if (userInfoModel.useStatus == 1) {
                        Toast.makeText(mContext, "当前状态已选中,请在首页切换", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StyledDialog.init(mContext);
                    StyledDialog.buildIosAlert("", "是否删除此状态?", new MyDialogListener() {
                        @Override
                        public void onFirst() {

                        }

                        @Override
                        public void onThird() {
                            super.onThird();
                        }

                        @Override
                        public void onSecond() {
                            Map<String, Object> clearmap = new HashMap<>();
                            clearmap.put("id", userInfoModel.id + "");
                            mPresenter.delete(clearmap);
                        }
                    }).setCancelable(true, true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
                }
            }
        });
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (id != null && !"".equals(id)) {
            mPresenter.getUserInfo(id);
        }
    }

    /**
     * 选择状态
     */
    public void chooseStatus(View view) {
        ARouter.getInstance()
                .build(AppRoutes.APP_CHOOSE_STATUS)
                .withString("id", id)
                .withString("sex", memberSex)
                .withString("babyId", babyId)
                .withString("useStatus", useStatus)
                .navigation(this, 1009);
    }

    private DateDialog mBornDateDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1009) {
            if (resultCode == Activity.RESULT_OK) {
                //System.out.println("更新状态");
                getData();
            }
        }
    }

    /**
     * 选择出生日期
     */
    public void chooseBirthDate(View view) {
        if (mBornDateDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration = currentMill - DaysLimit.DAYS_BORN * 24 * 60 * 60 * 1000L;
            mBornDateDialog = DateDialog.newInstance(
                    currentMill, duration, currentMill, "选择宝宝出生日期"
            );
            mBornDateDialog.setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    String time = DateUtils.formatTime2String("yyyy-MM-dd", date);
                    updateUserInfo("deliveryDate", time);
                }
            });
        }
        mBornDateDialog.show(getSupportFragmentManager(), "bornDate");
    }


    private SingleWheelDialog mSexDialog;

    /**
     * 选择宝宝性别
     */
    public void chooseBabySex(View view) {
        if (mSexDialog == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add("男孩");
            list.add("女孩");
            mSexDialog = SingleWheelDialog.newInstance(list);
            mSexDialog.setOnConfirmClick(new SingleWheelDialog.OnConfirmClickListener() {
                @Override
                public void onClick(int pos, String data) {
                    updateUserInfo("babySex", String.valueOf(pos + 1));
                }
            });
        }
        mSexDialog.show(getSupportFragmentManager(), "babySex");
    }

    private SingleWheelDialog mBornTypeDialog;

    /**
     * 选择分娩方式
     */
    public void chooseBabyBirthType(View view) {
        if (mBornTypeDialog == null) {
            ArrayList<String> list = new ArrayList<>();
            list.add("顺产");
            list.add("剖宫产");
            mBornTypeDialog = SingleWheelDialog.newInstance(list);
            mBornTypeDialog.setOnConfirmClick(new SingleWheelDialog.OnConfirmClickListener() {
                @Override
                public void onClick(int pos, String data) {
                    updateUserInfo("deliveryMode", String.valueOf(pos + 1));
                }
            });
        }
        mBornTypeDialog.show(getSupportFragmentManager(), "bornType");
    }


    private DateDialog mMenLastDialog;

    /**
     * 选择末次月经时间
     */
    public void chooseMenLastDate(View view) {
        if (mMenLastDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration;
            if (memberSexIndex == 2) {
                duration = currentMill - DaysLimit.DAYS_LAST_MEN * 24 * 60 * 60 * 1000L;
                mMenLastDialog = DateDialog.newInstance(
                        currentMill, duration, currentMill, "选择末次月经初潮时间"
                );
            } else {
                duration = currentMill - DaysLimit.DAYS_LAST_MEN * 24 * 60 * 60 * 1000L;
                long duration2 = currentMill + DaysLimit.DAYS_LAST_MEN * 24 * 60 * 60 * 1000L;
                mMenLastDialog = DateDialog.newInstance(
                        currentMill, duration, duration2, "选择预产期"
                );
            }

            mMenLastDialog.setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    String time = DateUtils.formatTime2String("yyyy-MM-dd", date);
                    if (memberSexIndex != 2) {
                        updateUserInfo("deliveryDate", time);
                    } else {
                        updateUserInfo("lastMensesDate", time);
                    }
                }
            });
        }
        mMenLastDialog.show(getSupportFragmentManager(), "menLast");
    }

    private DateDialog mMenStartDialog;

    /**
     * 选择月经开始时间
     */
    public void chooseMenStartDate(View view) {
        if (mMenStartDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration = currentMill - DaysLimit.DAYS_MEN_RECENT * 24 * 60 * 60 * 1000L;
            mMenStartDialog = DateDialog.newInstance(
                    currentMill, duration, currentMill, ""
            );
            mMenStartDialog.setOnConfirmClick(new OnDateConfirmListener() {
                @Override
                public void onConfirm(int pos, Date date) {
                    String time = DateUtils.formatTime2String("yyyy-MM-dd", date);
                    updateUserInfo("latelyMensesDate", time);
                }
            });
        }
        mMenStartDialog.show(getSupportFragmentManager(), "menStart");

    }

    private MenstruationDialog mCycleDurationDialog;

    /**
     * 选择月经周期 和时长
     */
    public void chooseMenCycle(View view) {
        if (mCycleDurationDialog == null) {
            mCycleDurationDialog = MenstruationDialog.newInstance("");
            mCycleDurationDialog.setOnConfirmListener(new MenstruationDialog.OnConfirmListener() {
                @Override
                public void onConfirm(int posCycle, int posDuration, String txtCycle, String txtDuration) {
                    Map<String, Object> map = new HashMap<>(3);
                    String duration = String.valueOf(posDuration + 1);
                    String cycle = String.valueOf(posCycle + 1);
                    map.put("useStatus", useStatus);
                    map.put("id", id);
                    map.put("stageStatus", stageStatus);
                    if (babyId != null && !"".equals(babyId)) {
                        map.put("babyId", babyId);
                    }
                    map.put("latelyMensesDate", mSectionMenStartDate.getTvDes().getText().toString());
                    map.put("menstrualCycle", cycle);
                    map.put("menstrualDays", duration);
                    mPresenter.updateUserInfoEx(map);
                }
            });
        }
        mCycleDurationDialog.show(getSupportFragmentManager(), "cycleDuration");
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {

    }

    @Override
    public void onDeleteUserInfoSuccess() {
        EventBus.getDefault().post(new UpdateUserInfoMsg());
        finish();
    }

    private int num = 5;
    UserInfoExModel userInfoModel;

    @Override
    public void onGetUserInfoExSuccess(UserInfoExModel userInfoModel) {
        if (userInfoModel != null) {
            this.userInfoModel = userInfoModel;
            int status = userInfoModel.getStageStatus();
            String statuseName = "";
            String dayvalue = "";
            memberSex = userInfoModel.memberSex == 1 ? "男" : "女";
            memberSexIndex = userInfoModel.memberSex;
            stageStatus = userInfoModel.stageStatus + "";
            if (status == 1) {
                statuseName = "备孕中";
                if (memberSexIndex != 2) {
                    statuseName = "备孕中";
                }
            }
            if (status == 2) {
                statuseName = "怀孕中";
            }
            if (status == 3) {
                statuseName = "宝宝已出生";
            }
            useStatus = userInfoModel.useStatus + "";
            mSectionStatus.setDes(statuseName);
            if (status == ParseUtils.parseInt(Constants.STATUS_FOR_NULL)) {
                //System.out.println("进入都不是中");
                mSectionBirthDate.setVisibility(View.GONE);
                mSectionBabySex.setVisibility(View.GONE);
                mSectionBabyName.setVisibility(View.GONE);
                mSectionBirthType.setVisibility(View.GONE);
                mSectionMenLastDate.setVisibility(View.GONE);
                mSectionMenLastDate.setVisibility(View.GONE);
                mSectionMenStartDate.setVisibility(View.GONE);
                mSectionMenCycle.setVisibility(View.GONE);
                mSectionBirthDate.setVisibility(View.GONE);
                mSectionBabySex.setVisibility(View.GONE);
                mSectionBabyName.setVisibility(View.GONE);
                mSectionBirthType.setVisibility(View.GONE);
                mSectionMenStartDate.setVisibility(View.GONE);
                mSectionMenCycle.setVisibility(View.GONE);
            } else if (status == ParseUtils.parseInt(Constants.STATUS_FOR_PREGNANCY)) {
                //System.out.println("进入备孕中");
                mSectionBirthDate.setVisibility(View.GONE);
                mSectionBabySex.setVisibility(View.GONE);
                mSectionBirthType.setVisibility(View.GONE);
                mSectionBabyName.setVisibility(View.GONE);

                mSectionMenLastDate.setVisibility(View.GONE);
                mSectionMenStartDate.setVisibility(View.VISIBLE);
                mSectionMenCycle.setVisibility(View.VISIBLE);
                mSectionMenStartDate.setDes(userInfoModel.getLatelyMensesDate());

                if (memberSexIndex != 2) {
                    mSectionMenStartDate.setVisibility(View.GONE);
                    mSectionMenCycle.setVisibility(View.GONE);
                    mSectionBirthDate.setVisibility(View.GONE);
                } else {
                    mSectionMenCycle.setDes(userInfoModel.getMenstrualCycle() + "," + userInfoModel.getMenstrualDays() + "");
                }
            } else if (status == ParseUtils.parseInt(Constants.STATUS_PREGNANCY)) {
                //System.out.println("进入怀孕中");
                mSectionBirthDate.setVisibility(View.GONE);
                mSectionBabySex.setVisibility(View.GONE);
                mSectionBirthType.setVisibility(View.GONE);

                mSectionBabyName.setVisibility(View.GONE);
                mSectionMenLastDate.setVisibility(View.VISIBLE);

                mSectionMenStartDate.setVisibility(View.GONE);
                mSectionMenCycle.setVisibility(View.GONE);
                if (memberSexIndex != 2) {
                    //System.out.println("进入怀孕中2");
                    mSectionMenLastDate.setVisibility(View.GONE);
                    mSectionBirthDate.setTitle("预产期");
                    mSectionBirthDate.setVisibility(View.VISIBLE);
                    mSectionBirthDate.setDes(userInfoModel.getDeliveryDate());
                } else {

                    mSectionMenLastDate.setDes(userInfoModel.getLastMensesDate());
                }
            } else {
                //System.out.println("进入宝宝中");
                mSectionBirthDate.setVisibility(View.VISIBLE);
                mSectionBabySex.setVisibility(View.VISIBLE);
                mSectionBirthType.setVisibility(View.VISIBLE);

                mSectionMenLastDate.setVisibility(View.GONE);

                mSectionMenStartDate.setVisibility(View.GONE);
                mSectionMenCycle.setVisibility(View.GONE);
                mSectionBabyName.setImeOptions(EditorInfo.IME_ACTION_SEND);
                mSectionBabyName.setVisibility(View.VISIBLE);
                mSectionBabyName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_SEND) {
                            if ("".equals(mSectionBabyName.getEtContent().getText().toString())) {
                                Toast.makeText(mContext, "宝宝昵称不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                updateUserInfo("babyName", mSectionBabyName.getEtContent().getText().toString());
                            }
                        }
                        return false;
                    }
                });
                mSectionBabyName.getEtContent().addTextChangedListener(new TextWatcher() {
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
                        selectionStart = mSectionBabyName.getEtContent().getSelectionStart();
                        selectionEnd = mSectionBabyName.getEtContent().getSelectionEnd();
                        if (temp.length() > num) {
                            showToast("昵称不得超过" + num + "个字");
//                            if (selectionStart > 0) {
//                                editable.delete(selectionStart - 1, selectionEnd);
//                            } else {
//                                editable.delete(selectionStart, selectionEnd);
//                            }
//                            int tempSelection = selectionStart;
//                            mSectionBabyName.getEtContent().setText(editable);
//                            mSectionBabyName.getEtContent().setSelection(tempSelection);//设置光标在最后
                        } else {
                            if (temp.length() > 0) {
                            }
                        }
                    }
                });
                mSectionBabyName.getEtContent().setText(userInfoModel.getBabyName());
                mSectionBabyName.getEtContent().setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                mSectionBabySex.setDes(Constants.SEX_BOY
                        .equals(String.valueOf(userInfoModel.getBabySex())) ?
                        "男" : "女");
                mSectionBirthType.setDes(Constants.BORN_TYPE_NORMAL
                        .equals(String.valueOf(userInfoModel.getDeliveryMode())) ?
                        "顺产" : "剖宫产");
                if (memberSexIndex != 2) {
                    mSectionBirthDate.setTitle("宝宝出生日期");
                } else {
                    mSectionBirthDate.setTitle("出生分娩日期");
                }
                babyId = userInfoModel.babyId + "";
                mSectionBirthDate.setDes(userInfoModel.getDeliveryDate());
//                mSectionBabyName.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showReviewBoard(mSectionBabyName.getEtContent());
//                    }
//                });
            }
        }

    }

    private void showReviewBoard(EditText editText) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.4f;
//        getWindow().setAttributes(lp);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editText.requestFocus();
    }

    @Override
    public void onUpdateUserInfoSuccess() {
        getData();
        //System.out.println("个人刷新开始");
        EventBus.getDefault().post(new UpdateUserInfoMsg());
    }

    @Override
    public void onUpdateUserInfoSuccessEx() {
        getData();
        //System.out.println("个人刷新开始");
        EventBus.getDefault().post(new UpdateUserInfoMsg());
    }

    @Override
    public void onUpdateUserInfoSuccessF() {
        finish();
    }

    @Override
    public void onUpdateUserInfoSuccessExF() {
        finish();

    }

    private void updateUserInfoWithFinish(String key, String value) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("deliveryDate", mSectionBirthDate.getTvDes().getText().toString());
        map.put("deliveryMode", "顺产".equals(mSectionBirthType.getTvDes().getText().toString()) ? "1" : "2");
        map.put("babyName", mSectionBabyName.getEtContent().getText().toString());
        map.put("lastMensesDate", mSectionMenLastDate.getTvDes().getText().toString());
        map.put("latelyMensesDate", mSectionMenStartDate.getTvDes().getText().toString());
        map.put("babySex", "男".equals(mSectionBabySex.getTvDes().getText().toString()) ? "1" : "0");

        if (!"男".equals(memberSex)) {
            map.put("menstrualCycle", userInfoModel.menstrualCycle);
            map.put("menstrualDays", userInfoModel.menstrualDays);
        }


        map.put(key, value);
        if (id != null && !"".equals(id)) {
            map.put("memberSex", memberSexIndex + "");
            map.put("useStatus", useStatus);
            map.put("id", id);
            map.put("stageStatus", stageStatus);
            if (babyId != null && !"".equals(babyId)) {
                map.put("babyId", babyId);
            }
            mPresenter.updateUserInfoExWithF(map);
        } else {
            map.put("memberSex", memberSexIndex + "");
            mPresenter.updateUserInfoWithF(map);
        }
    }

    private void updateUserInfo(String key, String value) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("deliveryDate", mSectionBirthDate.getTvDes().getText().toString());
        map.put("babyName", mSectionBabyName.getEtContent().getText().toString());
        map.put("deliveryMode", "顺产".equals(mSectionBirthType.getTvDes().getText().toString()) ? "1" : "2");
        map.put("lastMensesDate", mSectionMenLastDate.getTvDes().getText().toString());
        map.put("latelyMensesDate", mSectionMenStartDate.getTvDes().getText().toString());
        map.put("babySex", "男".equals(mSectionBabySex.getTvDes().getText().toString()) ? "1" : "0");
        if (!"男".equals(memberSex)) {
            try {
                map.put("menstrualCycle", userInfoModel.menstrualCycle);
                map.put("menstrualDays", userInfoModel.menstrualDays);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put(key, value);
        if (id != null && !"".equals(id)) {
            map.put("memberSex", memberSexIndex + "");
            map.put("useStatus", useStatus);
            map.put("id", id);
            map.put("stageStatus", stageStatus);
            if (babyId != null && !"".equals(babyId)) {
                map.put("babyId", babyId);
            }
            mPresenter.updateUserInfoEx(map);
        } else {
            map.put("memberSex", memberSexIndex + "");
            mPresenter.updateUserInfo(map);
        }
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        saveNow = (TextView) findViewById(R.id.saveNow);
    }
}
