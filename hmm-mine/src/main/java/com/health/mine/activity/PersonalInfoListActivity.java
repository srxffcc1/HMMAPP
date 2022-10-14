package com.health.mine.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.PersonalInfoListContract;
import com.health.mine.model.UserInfoExModel;
import com.healthy.library.model.UserInfoModel;
import com.health.mine.presenter.PersonalInfoListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/26 16:28
 * @des 个人信息界面
 */
@Route(path = MineRoutes.MINE_PERSONAL_INFO_LIST)
public class PersonalInfoListActivity extends BaseActivity implements PersonalInfoListContract.View, View.OnClickListener {

    PersonalInfoListPresenter personalInfoListPresenter;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private android.widget.LinearLayout needInsert;
    private android.widget.LinearLayout addClass;
    private UserInfoModel userInfoModel;
    private List<UserInfoExModel> userInfoExModels;

    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
        if (userInfoModel.getStatus() == -1) {//说明是无状态 不可新增状态 只能修改现有无状态
            addClass.setVisibility(View.GONE);
        } else {
//                addClass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteUserInfoSuccess() {
        EventBus.getDefault().post(new UpdateUserInfoMsg());
        personalInfoListPresenter.getUserInfo();
        personalInfoListPresenter.getUserInfoList();
    }

    @Override
    public void onGetUserInfoListSuccess(List<UserInfoExModel> userInfoExModels) {
        this.userInfoExModels = userInfoExModels;
        addClasses(needInsert, userInfoExModels);
    }

    private void addClasses(LinearLayout needInsert, List<UserInfoExModel> userInfoExModels) {
        needInsert.removeAllViews();
        if (userInfoExModels != null && userInfoExModels.size() >= 5) {
            addClass.setVisibility(View.GONE);
        }
        for (int i = 0; i < userInfoExModels.size(); i++) {
            final UserInfoExModel userInfoExModel = userInfoExModels.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.mine_item_personinfo, needInsert, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_PERSONAL_INFO_DETAIL)
                            .withString("id", userInfoExModel.id + "")
                            .navigation();
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (userInfoExModel.useStatus == 1) {
                        Toast.makeText(mContext, "当前状态已选中,请在首页切换", Toast.LENGTH_SHORT).show();
                        return true;
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
                            clearmap.put("id", userInfoExModel.id + "");
                            personalInfoListPresenter.delete(clearmap);
                        }
                    }).setCancelable(true, true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();


                    return true;
                }
            });
            TextView stut = view.findViewById(R.id.stut);
            TextView day = view.findViewById(R.id.day);
            String statuseName = "";
            day.setVisibility(View.VISIBLE);
            int status = userInfoExModel.stageStatus;
            if (status == 1) {
                statuseName = "备孕中";
                if (userInfoExModel.memberSex != 2) {
                    statuseName = "备孕中";
                }
                day.setVisibility(View.GONE);
            } else if (status == 2) {
                statuseName = "怀孕中";
                day.setText(userInfoExModel.stageStatusStr);
            } else if (status == 3) {
                statuseName = userInfoExModel.babyName;
                day.setText(userInfoExModel.stageStatusStr.replace("出生", ""));
            } else {
                statuseName = "资料越全面，服务更贴心";//发现存在一个无状态 就不让他新增
                addClass.setVisibility(View.GONE);
                day.setVisibility(View.GONE);
            }
            stut.setText(statuseName);
            needInsert.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1008) {
            personalInfoListPresenter.getUserInfo();
            personalInfoListPresenter.getUserInfoList();
            EventBus.getDefault().post(new UpdateUserInfoMsg());
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        setTopBar(topBar);
        personalInfoListPresenter = new PersonalInfoListPresenter(mContext, this);

        addClass.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_personal_info_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        needInsert = (LinearLayout) findViewById(R.id.needInsert);
        addClass = (LinearLayout) findViewById(R.id.addClass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addClass.setVisibility(View.VISIBLE);
        personalInfoListPresenter.getUserInfo();
        personalInfoListPresenter.getUserInfoList();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addClass) {
            if (userInfoExModels != null) {
                if (userInfoExModels.size() >= 5) {
                    Toast.makeText(mContext, "只可添加5个状态", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    ARouter.getInstance()
                            .build(AppRoutes.APP_CHOOSE_STATUS)
                            .withString("isadd", "1")
                            .withString("sex", userInfoModel.getMemberSex() == 1 ? "男" : "女")
                            .navigation(this, 1008);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().post(new UpdateUserInfoMsg());
    }
}
