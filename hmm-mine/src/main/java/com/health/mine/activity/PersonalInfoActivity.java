package com.health.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.mine.R;
import com.health.mine.contract.PersonalInfoContract;
import com.health.mine.presenter.PersonalInfoPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * @author Li
 * @date 2019/03/26 16:28
 * @des 个人信息界面
 */
@Route(path = MineRoutes.MINE_PERSONAL_INFO)
public class PersonalInfoActivity extends BaseActivity implements PersonalInfoContract.View, OnDateConfirmListener {

    private TopBar mTopBar;
    private StatusLayout mStatusLayout;
    private ImageView mIvAvatar;
    private SectionView mSectionNickname;
    private SectionView mSectionPhone;
    private SectionView mSectionStatus;
    private SectionView section_pay;
    private SectionView section_avatar;
    private PersonalInfoPresenter mPresenter;
    private UserInfoModel userInfoModel;
    private DateDialog mBornDateDialog;
    private SectionView mSectionBirthDay;
    private Map<String, Object> map = new HashMap<>();


    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_personal_info;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
    }

    private static final int RC_CHOOSE_IMG = 764;

    private void addImag() {
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
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);

        mIvAvatar = findViewById(R.id.iv_avatar);
        mSectionNickname = findViewById(R.id.section_nickname);
        mSectionPhone = findViewById(R.id.section_phone);
        mSectionStatus = findViewById(R.id.section_state);
        section_pay = findViewById(R.id.section_pay);
        section_avatar = findViewById(R.id.section_avatar);
        mSectionBirthDay = findViewById(R.id.section_birthday);
        mPresenter = new PersonalInfoPresenter(this, this);
//        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    public void checkStatus(View view) {
        MobclickAgent.onEvent(mContext, "event2ClassEnoughtDetail", new SimpleHashMapBuilder().puts("soure", "我的"));
        ARouter.getInstance()
                .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                .navigation();
    }


    /*
     * 跳转设置支付密码
     *
     * */
    public void sectionPay(View view) {

        if (userInfoModel != null) {
            ARouter.getInstance()
                    .build(FaqRoutes.FAQ_PAY_PASSWORD)
                    .withInt("type", userInfoModel.getIsSetPayPassword())
                    .navigation();
        }

    }

    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
        //修改成功并保存生日
        SpUtils.store(mContext, SpKey.USER_BIRTHDAY, userInfoModel.getBirthday());
        com.healthy.library.businessutil.GlideCopy.with(mIvAvatar.getContext())
                .load(userInfoModel.getFaceUrl())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into(mIvAvatar);
        mSectionNickname.setDes(userInfoModel.getNickname());
        mSectionPhone.setDes(userInfoModel.getPhone());
        mSectionStatus.setDes(userInfoModel.getStatusName());
        String mBirthDay = TextUtils.isEmpty(userInfoModel.getBirthday()) ? "请选择" :
                DateUtils.getDateDay(userInfoModel.getBirthday(),"yyyy-MM-dd","yyyy年MM月dd日");
        mSectionBirthDay.setDes(mBirthDay);
        if (userInfoModel.getStatus() == -1) {
            mSectionStatus.setDes("资料越全面，服务更贴心");
        }
        if (userInfoModel.getIsSetPayPassword() == 2) {
            //未设置
            section_pay.setDes("未设置");
            section_pay.setDesColor(R.color.color_ff6266);
        } else if (userInfoModel.getIsSetPayPassword() == 1) {
            //已设置
            section_pay.setDes("修改");
            section_pay.setDesColor(R.color.color_222222);
        }

    }

    @Override
    public void onUpLoadSuccess(List<String> urls, int type) {//头像上传成功
        map.clear();
        map.put("faceUrl", urls.get(0));
        mPresenter.updateUserInfoPic(map);

    }

    @Override
    public void onupdateSucess() {
        getData();
        EventBus.getDefault().post(new UpdateUserInfoMsg());
    }

    /**
     * 修改生日回调
     *
     * @param isSuccess
     */
    @Override
    public void onUpdateBirthday(boolean isSuccess) {
        if (isSuccess) {
            EventBus.getDefault().post(new UpdateUserInfoMsg());
            getData();
        }
    }

    @Override
    public void getData() {
        mPresenter.getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public static final int REQUEST_CODE_CROP = 3;

    /**
     * 裁剪拍摄的照片
     *
     * @param photoPath
     */
    public void cutPhoto(String photoPath) {
        File photoFile = new File(photoPath);
        File dir = new File(Environment.getExternalStorageDirectory(), "mmimg");
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date()) + "_clip.jpg";
        File cropFile = new File(dir.getAbsolutePath() + "/" + fileName);
        if (!cropFile.getParentFile().exists()) {
            cropFile.getParentFile().mkdirs();
        }
        if (!cropFile.exists()) {
            try {
                cropFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(FileProvider.getUriForFile(mActivity, mActivity.getApplicationInfo().packageName + ".fileprovider", photoFile), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 720);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserInfoMsg msg) {
        //System.out.println("我的收到了修改");
        getData();
    }

    private List<String> needuploadimgs = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_IMG) {
                if (data != null) {
                    String cropPath = null;
                    //System.out.println("裁剪进来了");
                    if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                        needuploadimgs.clear();
                        needuploadimgs.add(cropPath);
                        uploadImgs(needuploadimgs);


//                        com.healthy.library.businessutil.GlideCopy.with(this)
//                                .load(cropPath)
//                                .placeholder(R.drawable.img_avatar_default)
//                                .error(R.drawable.img_avatar_default)
//
//                                .into(mIvAvatar);
                    }
                }
            }
        }
    }

    private List<String> mBase64Imgs = new ArrayList<>();

    /**
     * 上传图片
     *
     * @param filePaths 图片路径集合
     */
    private void uploadImgs(final List<String> filePaths) {
        showLoading();
        mBase64Imgs.clear();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                for (String filePath : filePaths) {
                    String base64 = BitmapUtils.bitmap2Base64(filePath);
                    emitter.onNext(base64);
                }
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
                        mBase64Imgs.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败");
                    }

                    @Override
                    public void onComplete() {
                        mPresenter.uploadFile(mBase64Imgs, 0);
                    }
                });
    }

    public void updatePic(View view) {
        addImag();
    }

    public void updateNickName(View view) {
        if(userInfoModel==null){
            Toast.makeText(mContext,"服务器异常,稍后再试",Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.init(this);
        StyledDialog.buildMdInput("修改昵称", "输入昵称", "",
                userInfoModel.getNickname() != null ? userInfoModel.getNickname() : "", "", new InputFilter[]{
                        new InputFilter() {
                            @Override
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                                if(source.toString().getBytes().length>1){//是中文
//                                    return null;
//                                }
//                                if(source.toString().equals("_")||source.toString().equals("-")) {
//                                    return null;
//                                }
                                //[\u4E00-\u9FA5]|[\\w]|[_\\\-]
                                String regEx = "[a-zA-Z]|[\\u4E00-\\u9FA5]|[_\\\\\\-]";
                                Pattern p = Pattern.compile(regEx);
                                Matcher m = p.matcher(source);
                                if (m.find()) {
                                    return null;
                                }
                                return "";
                            }
                        }, new InputFilter.LengthFilter(20)}, null, new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }

                    @Override
                    public boolean onInputValid(CharSequence input1, CharSequence input2, EditText editText1, EditText editText2) {
//                        showToast("input1--input2:"+input1+"--"+input2 +"is not accepted!");
                        if (input1.toString().length() < 1) {
                            Toast.makeText(mContext, "昵称不得小于1个字", Toast.LENGTH_SHORT).show();
                        } else {
                            map.clear();
                            map.put("nickName", input1.toString());
                            mPresenter.updateUserInfoNick(map);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                    }
                })
                .setInput2HideAsPassword(true)
                .setCancelable(true, true)
                .show();
    }

    /**
     * 修改生日
     *
     * @param view
     */
    public void updateBirthday(View view) {
        try {
            String mBirthDay = TextUtils.isEmpty(userInfoModel.getBirthday()) ? "请选择" :
                    DateUtils.getDateDay(userInfoModel.getBirthday(),"yyyy-MM-dd","yyyy年MM月dd日");
            if(!TextUtils.isEmpty(mBirthDay)&&!mBirthDay.equals("1900年01月01日")){//当生日不为空 且不为 默认的1900时 才准许修改生日
                Toast.makeText(mContext,"如需修改生日请联系平台或者商家修改",Toast.LENGTH_LONG).show();
            }else {
                if (mBornDateDialog == null) {
                    Calendar startDay = Calendar.getInstance();
                    startDay.add(Calendar.YEAR,5);
                    long maxMill = startDay.getTimeInMillis();
                    long currentMill = System.currentTimeMillis();
                    long duration = 0;//0就是倒1970年1月1日0点0分
                    mBornDateDialog = DateDialog.newInstance(
                            currentMill, duration, maxMill, "生日"
                    );
                    mBornDateDialog.setOnConfirmClick(this);
                }
                mBornDateDialog.show(getSupportFragmentManager(), "bornDate");
            }
        } catch (Exception e) {
            Toast.makeText(mContext,"如需修改生日请联系平台或者商家修改",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onConfirm(int pos, Date date) {
        String time = TimestampUtils.timestamp2String(date.getTime(), "yyyy-MM-dd");
        map.clear();
        map.put("birthday", time);
        mPresenter.updateUserBirthday(map);
    }
}
