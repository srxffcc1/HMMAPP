package com.health.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.PersonalInfoContract;
import com.health.mine.presenter.PersonalInfoPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.interfaces.OnTopBarListener;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.NewInputView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * ??????????????????
 *
 * @author long
 * @date 2021-05-12
 */
@Route(path = MineRoutes.MINE_UPDATE_USER_INFO)
public class UpdateUserInfoActivity extends BaseActivity implements OnDateConfirmListener, PersonalInfoContract.View, IsNoNeedPatchShow {

    private final int RC_CHOOSE_IMG = 764;
    private long mBackTime;
    private TopBar mTopBar;
    private StatusLayout mStatusLayout;
    private DateDialog mBornDateDialog;
    private Map<String, Object> map = new HashMap<>();
    private NewInputView mNewInputNickName;
    private NewInputView mNewInputDate;
    private String mNickName;
    private PersonalInfoPresenter mPresenter;
    private String time;
    private CornerImageView ivAvatar;
    private String imgPath = "";
    private List<String> imgsList = new ArrayList<>();
    private int netWorkCount = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_user_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter = new PersonalInfoPresenter(this, this);
    }


    @Override
    protected void findViews() {
        super.findViews();
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
        setStatusLayout(mStatusLayout);

        ivAvatar = findViewById(R.id.iv_avatar);
        mNewInputNickName = findViewById(R.id.widget_input_nickName);
        mNewInputDate = findViewById(R.id.widget_input_date);
        imgPath = SpUtils.getValue(mContext, SpKey.USER_ICON);
        //???????????????????????????
        if (!TextUtils.isEmpty(imgPath)) {
            setAvatar();
        }
        //??????????????????
        mNewInputNickName.setRightEditBody(SpUtils.getValue(mContext, SpKey.USER_NICK));
        initListener();

    }

    private void initListener() {
        mTopBar.setTopBarListener(new OnTopBarListener() {
            @Override
            public void onBackBtnPressed() {
                onBackPressed();
            }
        });

        mNewInputNickName.getEditText().setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String regEx = "[a-zA-Z]|[\\u4E00-\\u9FA5]|[_\\\\\\-]";
                        Pattern p = Pattern.compile(regEx);
                        Matcher m = p.matcher(source);
                        if (m.find()) {
                            return null;
                        }
                        return "";
                    }
                }
        });
        mNewInputNickName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }
                if (s.toString().length() > 20) {
                    String substring = s.toString().substring(0, 20);
                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
                    mNewInputNickName.setRightEditBody(substring);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //????????????
//        ARouter.getInstance().build(AppRoutes.APP_LOGINTRANSFER)
//                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                        Intent.FLAG_ACTIVITY_NEW_TASK)
//                .navigation();
        finish();
//        super.onBackPressed();
    }
    private static final int RC_PERMISSION = 45;
    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    /**
     * ??????????????????
     *
     * @param view
     */
    public void updatePic(View view) {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .countable(true)
                    .maxSelectable(1)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .isCrop(true)
                    .forResult(RC_CHOOSE_IMG);
        }else {

            PermissionUtils.requestPermissions(this, 10009, mPermissions);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("??????????????????????????????????????????");
//                PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_IMG) {
                if (data != null) {
                    String cropPath = null;
                    //System.out.println("???????????????");
                    if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                        imgPath = cropPath;
                        uploadImgs();
                    }
                }
            }
        }
    }

    /**
     * ????????????
     */
    private void uploadImgs() {
        showLoading();
        imgsList.clear();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                //for (String filePath : filePaths) {
                String base64 = BitmapUtils.bitmap2Base64(imgPath);
                emitter.onNext(base64);
                //}
                emitter.onComplete();
            }
        })
                .compose(RxThreadUtils.<String>Obs_io_main())
                .to(RxLifecycleUtils.<String>bindLifecycle(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        imgPath = s;
                        imgsList.add(imgPath);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("??????????????????");
                    }

                    @Override
                    public void onComplete() {
                        //????????????
                        mPresenter.uploadFile(imgsList, 0);
                    }
                });
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    public void updateBirthday(View view) {
        if (mBornDateDialog == null) {
            Calendar startDay = Calendar.getInstance();
            startDay.add(Calendar.YEAR,5);
            long maxMill = startDay.getTimeInMillis();
            long currentMill = System.currentTimeMillis();
            long duration = 0;//0?????????1970???1???1???0???0???
            mBornDateDialog = DateDialog.newInstance(
                    currentMill, duration, maxMill, "??????"
            );
            mBornDateDialog.setOnConfirmClick(this);
        }
        mBornDateDialog.show(getSupportFragmentManager(), "bornDate");
    }

    @Override
    public void onConfirm(int pos, Date date) {
        time = TimestampUtils.timestamp2String(date.getTime(), "yyyy-MM-dd");
        mNewInputDate.setRightBody(DateUtils.getDateDay(time,"yyyy-MM-dd","yyyy???MM???dd???"));
        mNewInputDate.setRightColor(Color.parseColor("#333333"));
    }

    /**
     * ????????????????????????
     *
     * @param view
     */
    public void saveUserInfo(View view) {
        mNickName = mNewInputNickName.getEditText().getText().toString().trim();
        if(!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)){
            if (TextUtils.isEmpty(imgPath)) {
                showToast("???????????????");
                return;
            }

            if (TextUtils.isEmpty(mNickName)) {
                showToast("???????????????");
                return;
            }
            if (TextUtils.isEmpty(time)) {
                showToast("?????????????????????");
                return;
            }
            map.clear();
            map.put("faceUrl", imgPath);
            mPresenter.updateUserInfoPic(map);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String status = SpUtils.getValue(mContext, SpKey.STATUS);
                    MobclickAgent.onEvent(mContext, "event2LoginClick");
                    String birthday = SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY);

                    //??????????????????
                    if (Constants.STATUS_NONE.equals(status)) {
                        ARouter.getInstance().build(AppRoutes.APP_CHOOSE_SEX)
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation();
                    } else {
                        ARouter.getInstance().build(AppRoutes.APP_MAIN)
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation();
                    }
                }
            }, 800);
        }

    }

    /**
     * ?????? ????????????
     *
     * @param userInfoModel ????????????
     */
    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {

    }

    /**
     * ??????????????????
     *
     * @param urls
     * @param type
     */
    @Override
    public void onUpLoadSuccess(List<String> urls, int type) {

        if (ListUtil.isEmpty(urls)) {
            return;
        }

        imgPath = urls.get(0);
        setAvatar();
    }

    /**
     * ????????????
     */
    private void setAvatar() {
        com.healthy.library.businessutil.GlideCopy.with(ivAvatar.getContext())
                .load(imgPath)
                .error(R.drawable.update_info_avatar_default)
                .placeholder(R.drawable.update_info_avatar_default)
                .into(ivAvatar);
    }

    /**
     * ???????????????????????????????????????
     */
    @Override
    public void onupdateSucess() {
        netWorkCount++;
        if (netWorkCount == 2) {
            //??????????????????
            map.clear();
            map.put("nickName", mNickName);
            mPresenter.updateUserInfoNick(map);
        }
        if (netWorkCount == 3) {
            //??????????????????
            map.clear();
            map.put("birthday", time);
            mPresenter.updateUserBirthday(map);
        }
    }

    /**
     * ????????????????????????
     *
     * @param isSuccess ????????????
     */
    @Override
    public void onUpdateBirthday(boolean isSuccess) {
        netWorkCount = 1;
        //????????????????????????????????????
        if (isSuccess) {
            showToast("????????????????????????!");





            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String status = SpUtils.getValue(mContext, SpKey.STATUS);
                    MobclickAgent.onEvent(mContext, "event2LoginClick");
                    String birthday = SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY);

                    //??????????????????
                    if (Constants.STATUS_NONE.equals(status)) {
                        ARouter.getInstance().build(AppRoutes.APP_CHOOSE_SEX)
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation();
                    } else {
                        ARouter.getInstance().build(AppRoutes.APP_MAIN)
                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK)
                                .navigation();
                    }
                }
            }, 800);
        }
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        if (!mPresenter.isSuccess()) {
            netWorkCount = 1;
        }
    }
}