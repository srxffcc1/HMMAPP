package com.health.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dilusense.customkeyboard.BaseKeyboard;
import com.dilusense.customkeyboard.KeyboardIdentity;
import com.dilusense.customkeyboard.KeyboardUtils;
import com.dilusense.customkeyboard.utils.IdentityUtils;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.R;
import com.health.mine.adapter.MineAddImgAdapter;
import com.health.mine.adapter.MineAddLocAdapter;
import com.health.mine.contract.JobContract;
import com.health.mine.model.JobArea;
import com.health.mine.model.JobDetail;
import com.health.mine.model.JobType;
import com.health.mine.presenter.JobPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.SingleWheelDialog;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.decoration.AddImgDecoration;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


@Route(path = MineRoutes.MINE_JOB_WANT)
public class JobWantActivity extends BaseActivity implements JobContract.View, OnSubmitListener {

    @Autowired
    public String technicianTypeId;

    @Autowired
    public String technicianTypeName;

    private JDCityPicker cityPicker;
    private JDCityConfig jdCityConfig;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.CommonInsertSection jobName;
    private com.healthy.library.widget.CommonInsertSection jobPhone;
    private com.healthy.library.widget.CommonInsertSection jobCode;
    private com.healthy.library.widget.CommonInsertSection jobId;
    private com.healthy.library.widget.CommonInsertSection jobAge;
    private com.healthy.library.widget.CommonInsertSection jobSex;
    private com.healthy.library.widget.CommonInsertSection jobLoc;
    private com.healthy.library.widget.CommonInsertSection jobNowLoc;
    //    private com.healthy.library.widget.CommonInsertSection jobWantLoc;
    private com.healthy.library.widget.CommonInsertSection jobYear;
    private com.healthy.library.widget.CommonInsertSection jobHistory;
    private com.healthy.library.widget.CommonInsertSection jobTeachHistory;
    private com.healthy.library.widget.CommonInsertSection jobNow;
    private com.healthy.library.widget.CommonInsertSection jobGoodAt;
    private RecyclerView jobRecyclerCertPager;
    private RecyclerView jobRecyclerHealthPager;
    private SectionView jobTitleId;
    private CommonInsertSection jobWantLoc;
    private SectionView jobTitleGood;
    private SectionView jobTitlePaper;
    private SectionView jobTitleOther;
    private android.widget.EditText jobOtherEt;


    private RecyclerView jobRecyclerWantLoc;
    private android.widget.TextView jobHide;
    private MineAddImgAdapter cretAdapter;
    private MineAddImgAdapter healthAdapter;
    private MineAddImgAdapter nowadapter;
    private MineAddLocAdapter addLocAdapter;
    private static final int PASS_INSERT_JOBHISTORY = 17173;
    private static final int PASS_INSERT_JOBTEACHHISTORY = 17174;
    private static final int PASS_INSERT_JOBGOODAT = 17175;
    private static final int PASS_INSERT_JOBWANTLOC = 17176;
    private JobPresenter jobPresenter;
    private List<String> adapterLocList = new ArrayList<>();//从adapter中获得的list
    private List<String> adapterCertPaperList = new ArrayList<>();//从adapter中获得的list
    private List<String> adapterHealthPaperList = new ArrayList<>();//从adapter中获得的list
    private TextView jobOtherEtCount;
    private KeyboardIdentity keyboardIdentity;


    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_job_detail;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();

    }

    private String[] mPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int RC_CHOOSE_IMG = 764;
    private static final int RC_CHOOSE_AREA = 769;
    private static final int RC_CHOOSE_TIP = 709;
    private static final int RC_UPDATE_IMG = 765;
    private static final int RC_PERMISSION = 45;
    private static final int RC_PERMISSION_VIDEO = 46;
    //    private static final int MAX_IMG_NUM = 8 ;
    private static final int VIDEO_SELECT = 13197;
    private static final int VIDEO_SELECT_UPDATE = 13190;
    private int nowIntent = 0;//0为一般选择 1为意向选择

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void onUpdateImp(int pos) {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            mPos = pos;
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .countable(false)
                    .maxSelectable(1)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(RC_UPDATE_IMG);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
        }
    }

    public void onAddImgImp() {

        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            if (0 == 1) {
//                if (cityAddImgAdapter.getData().size() == MAX_IMG_NUM) {
//                    showToast("最多上传8张图片和1个视频");
//                    return;
//                }
            } else {
//                if (nowadapter.getData().size() == nowadapter.getLimitCount()) {
//                    if(TextUtils.isEmpty(nowadapter.getData().get(nowadapter.getLimitCount()-1))){
//                        showToast("最多选"+nowadapter.getLimitCount()+"图片");
//                    }
//                    return;
//                }
            }
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .countable(true)
                    .maxSelectable(nowadapter.getLimitCount() - nowadapter.getData().size() + 1)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(RC_CHOOSE_IMG);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
        }
    }

    private int limitnum = 100;

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);


        jobOtherEtCount.setText((0) + "/" + limitnum);
        jobOtherEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                ////System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = limitnum - s.length();
                selectionStart = jobOtherEt.getSelectionStart();
                selectionEnd = jobOtherEt.getSelectionEnd();
                if (temp.length() > limitnum) {
                    showToast("限制输入" + limitnum + "个字");
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    jobOtherEt.setText(s);
                    jobOtherEt.setSelection(tempSelection);//设置光标在最后
                } else {
                    jobOtherEtCount.setText(temp.length() + "/" + limitnum + "字");
                }
            }
        });


        jobRecyclerWantLoc.setNestedScrollingEnabled(false);
        jobRecyclerCertPager.setNestedScrollingEnabled(false);
        jobRecyclerHealthPager.setNestedScrollingEnabled(false);
        jobPresenter = new JobPresenter(mContext, this);
        List<JobArea> data0 = new ArrayList<>();
        data0.add(null);
        addLocAdapter = new MineAddLocAdapter(this);
        addLocAdapter.setData(data0);
        addLocAdapter.setLimitCount(3);
        FlexboxLayoutManager flexlayoutManager = new FlexboxLayoutManager(mContext);
        flexlayoutManager.setFlexWrap(FlexWrap.WRAP); //设置是否换行
        jobRecyclerWantLoc.setLayoutManager(flexlayoutManager);
        jobRecyclerWantLoc.setAdapter(addLocAdapter);
        keyboardIdentity = new KeyboardIdentity(this);
        cretAdapter = new MineAddImgAdapter(this);
        cretAdapter.setLimitCount(6);
        jobRecyclerCertPager.setLayoutManager(new GridLayoutManager(mContext, 3));
        jobRecyclerCertPager.setAdapter(cretAdapter);
        jobRecyclerCertPager.addItemDecoration(new AddImgDecoration(mContext));

        addLocAdapter.setListener(new MineAddLocAdapter.OnTipChangeListener() {
            @Override
            public void onAddTip() {
                nowIntent = 1;
                showCityPick();
            }
        });


        cretAdapter.setListener(new MineAddImgAdapter.OnImgChangeListener() {
            @Override
            public void onAddImg() {
                nowadapter = cretAdapter;
                onAddImgImp();
            }

            @Override
            public void onAddVideo(int pos) {
                nowadapter = cretAdapter;
                onUpdateImp(pos);
            }

            @Override
            public void onUpdate(int pos) {

            }

            @Override
            public void onVideoUpdate(int pos) {

            }
        });

        List<String> data = new ArrayList<>();
        data.add(null);
        cretAdapter.setData(data);

        healthAdapter = new MineAddImgAdapter(this);
        healthAdapter.setLimitCount(1);
        jobRecyclerHealthPager.setLayoutManager(new GridLayoutManager(mContext, 3));
        jobRecyclerHealthPager.setAdapter(healthAdapter);
        jobRecyclerHealthPager.addItemDecoration(new AddImgDecoration(mContext));

        healthAdapter.setListener(new MineAddImgAdapter.OnImgChangeListener() {
            @Override
            public void onAddImg() {
                nowadapter = healthAdapter;
                onAddImgImp();
            }

            @Override
            public void onAddVideo(int pos) {
                nowadapter = healthAdapter;
                onUpdateImp(pos);
            }

            @Override
            public void onUpdate(int pos) {

            }

            @Override
            public void onVideoUpdate(int pos) {

            }
        });

        List<String> data2 = new ArrayList<>();
        data2.add(null);
        healthAdapter.setData(data2);

        cityPicker = new JDCityPicker();
        jdCityConfig = new JDCityConfig.Builder().build();
        jdCityConfig.setShowType(JDCityConfig.ShowType.PRO_CITY_DIS);
        cityPicker.init(this);
        cityPicker.setConfig(jdCityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if (nowIntent == 0) {
                    jobNow.getTxtContent().setText(province.getName() + "-" + city.getName() + "-" + district.getName());
                    jobNow.getEtContent().setText(province.getId() + "-" + city.getId() + "-" + district.getId());
                } else {
                    if (!checkAddress(province.getName() + "-" + city.getName() + "-" + district.getName())) {
                        addLocAdapter.addData(new JobArea(province.getName() + "-" + city.getName() + "-" + district.getName(), province.getId() + "-" + city.getId() + "-" + district.getId()));

                    } else {
                        Toast.makeText(mContext, "地址已存在", Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onCancel() {
            }
        });
        jobPhone.getRightEnd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        jobLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowIntent = 0;
                jobNow = jobLoc;
                showCityPick();
            }
        });
        jobNowLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowIntent = 0;
                jobNow = jobNowLoc;
                showCityPick();
            }
        });
        jobYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseYear();
            }
        });
        jobPhone.getEtContent().setInputType(InputType.TYPE_CLASS_NUMBER);
        jobCode.getEtContent().setInputType(InputType.TYPE_CLASS_NUMBER);
//        jobId.getEtContent().setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        KeyboardUtils.bindEditTextEvent(keyboardIdentity, jobId.getEtContent());
//        .setKeyListener(DigitsKeyListener.getInstance("0123456789xX"));

        jobId.getEtContent().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = s.length();
                ////System.out.println("身份证输入字数:"+number);
                if (number >= 18) {
                    keyboardIdentity.hideKeyboard();
                    checkId();
                }
            }
        });
        keyboardIdentity.setOnOkClick(new BaseKeyboard.OnOkClick() {
            @Override
            public void onOkClick() {
                checkId();
            }
        });
        jobHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_JOB_ADDWANT)
                        .withString("teachTmp", jobHistory.getTxtContent().getText().toString())
                        .withString("teachTitle", "从业经历")
                        .navigation(JobWantActivity.this, PASS_INSERT_JOBHISTORY);
            }
        });
        jobTeachHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_JOB_ADDWANT)
                        .withString("teachTmp", jobTeachHistory.getTxtContent().getText().toString())
                        .withString("teachTitle", "培训经历")
                        .navigation(JobWantActivity.this, PASS_INSERT_JOBTEACHHISTORY);
            }
        });
        jobGoodAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(MineRoutes.MINE_JOB_ADDWANT)
                        .withString("teachTmp", jobGoodAt.getTxtContent().getText().toString())
                        .withString("teachTitle", "个人专长")
                        .navigation(JobWantActivity.this, PASS_INSERT_JOBGOODAT);
            }
        });
        jobTitleOther.getTvTitle().getPaint().setFakeBoldText(true);
        jobTitleId.getTvTitle().getPaint().setFakeBoldText(true);
        jobTitleGood.getTvTitle().getPaint().setFakeBoldText(true);
        jobTitlePaper.getTvTitle().getPaint().setFakeBoldText(true);
        topBar.setSubmitListener(this);
        buildTmpJob();
    }

    private void checkId() {
        IdentityUtils identityUtils = new IdentityUtils();
        if (!identityUtils.isValidatedAllIdcard(jobId.getEtContent().getText().toString())) {
            Toast.makeText(getApplicationContext(), "输入的身份证号不合法", Toast.LENGTH_SHORT).show();
        } else {
            successGetManId();
        }
    }

    private boolean checkAddress(String loc) {

        List adapterLocList = addLocAdapter.getResultName();
        for (int i = 0; i < adapterLocList.size(); i++) {
            if (adapterLocList.get(i) != null) {
                if (loc.equals(adapterLocList.get(i))) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public void finish() {
        if (checkIllegalNeedStore()) {
            StyledDialog.init(this);
            StyledDialog.buildIosAlert("", "是否保存草稿?", new MyDialogListener() {
                @Override
                public void onFirst() {
                    SpUtils.store(mContext, SpKey.JOB_TMP, "");
                    JobWantActivity.super.finish();
                }

                @Override
                public void onThird() {
                    super.onThird();

                }

                @Override
                public void onSecond() {
                    JobDetail postStore = getStoreJob();
                    String result = new Gson().toJson(postStore);
                    SpUtils.store(mContext, SpKey.JOB_TMP, result);
                    JobWantActivity.super.finish();


                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("不保存", "保存").show();
        } else {
            super.finish();
        }
    }

    private JobDetail resolveTmpData(String obj) {
        JobDetail result = null;
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<JobDetail>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private JobDetail getStoreJob() {
        JobDetail postStore = new JobDetail();
        postStore.jobName = jobName.getEtContent().getText().toString();
        postStore.jobPhone = jobPhone.getEtContent().getText().toString();
        postStore.jobId = jobId.getEtContent().getText().toString();
        postStore.jobAge = jobAge.getTxtContent().getText().toString();
        postStore.jobSex = jobSex.getTxtContent().getText().toString();
        postStore.jobLoc = jobLoc.getTxtContent().getText().toString();
        postStore.jobLocCode = jobLoc.getEtContent().getText().toString();

        postStore.jobNowLoc = jobNowLoc.getTxtContent().getText().toString();
        postStore.jobNowLocCode = jobNowLoc.getEtContent().getText().toString();

        postStore.adapterLocList = addLocAdapter.getResultBean();
        postStore.jobYear = jobYear.getTxtContent().getText().toString();
        postStore.jobHistory = jobHistory.getTxtContent().getText().toString();
        postStore.jobTeachHistory = jobTeachHistory.getTxtContent().getText().toString();
        postStore.jobGoodAt = jobGoodAt.getTxtContent().getText().toString();
        postStore.adapterCertPaperList = cretAdapter.getResult();
        postStore.adapterHealthPaperList = healthAdapter.getResult();
        postStore.jobOtherEt = jobOtherEt.getText().toString();
        return postStore;
    }

    private void buildTmpJob() {
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.JOB_TMP))) {
            String jsonstring = SpUtils.getValue(mContext, SpKey.JOB_TMP);
            JobDetail postStore = resolveTmpData(jsonstring);
            jobName.getEtContent().setText(postStore.jobName);
            jobPhone.getEtContent().setText(postStore.jobPhone);
            jobId.getEtContent().setText(postStore.jobId);
            jobAge.getTxtContent().setText(postStore.jobAge);
            jobSex.getTxtContent().setText(postStore.jobSex);
            jobSex.getEtContent().setText("女".equals(postStore.jobSex) ? "0" : "1");
            jobLoc.getTxtContent().setText(postStore.jobLoc);
            jobLoc.getEtContent().setText(postStore.jobLocCode);
            jobNowLoc.getTxtContent().setText(postStore.jobNowLoc);
            jobNowLoc.getEtContent().setText(postStore.jobNowLocCode);
            addLocAdapter.addDatas(postStore.adapterLocList);
            jobYear.getTxtContent().setText(postStore.jobYear);
            jobHistory.getTxtContent().setText(postStore.jobHistory);
            jobTeachHistory.getTxtContent().setText(postStore.jobTeachHistory);
            jobGoodAt.getTxtContent().setText(postStore.jobGoodAt);
            cretAdapter.addDatas(postStore.adapterCertPaperList);
            healthAdapter.addDatas(postStore.adapterHealthPaperList);
            jobOtherEt.setText(postStore.jobOtherEt);
        }
    }

    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    private void sendCode() {
        hideInput();
        if (TextUtils.isEmpty(jobPhone.getEtContent().getText().toString())) {
            showToast("请输入手机号");
            return;
        }
        if (!CheckUtils.checkPhone(jobPhone.getEtContent().getText().toString())) {
            showToast("请输入正确手机号");
            return;
        }
        Map<String, Object> codemap = new HashMap<>();
        codemap.put("mobileNumber", jobPhone.getEtContent().getText().toString());
        jobPresenter.sendJobCode(codemap);
    }

    //身份证校验通过 开始换算男女 年龄
    private void successGetManId() {
        jobId.getEtContent().clearFocus();
        Map<String, String> idresult = CheckUtils.getBirAgeSex(jobId.getEtContent().getText().toString());
        jobAge.getTxtContent().setText(idresult.get("age") + "");
        jobSex.getTxtContent().setText(idresult.get("sexName") + "");
        jobSex.getEtContent().setText(idresult.get("sexCode") + "");
    }

    private int mPos;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_IMG) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((videoPath = Matisse.obtainCaptureVideoResult(data)) != null) {//
//                        videoUrl = videoFile.getAbsolutePath();
//                        clip(videoUrl,mPos);
                    } else if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {
                        nowadapter.addData(capturePath);
                    } else if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                    } else {
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);
                        if (filePaths != null) {
                            nowadapter.addDatas(filePaths);
                        }
                    }
                }
            } else if (requestCode == RC_UPDATE_IMG) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((videoPath = Matisse.obtainCaptureVideoResult(data)) != null) {//
//                        videoUrl = videoFile.getAbsolutePath();
//                        clip(videoUrl,mPos);
                    } else if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {
                        nowadapter.addData(capturePath);
                    } else if ((cropPath = Matisse.obtainCropResult(data)) != null) {
                    } else {
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);
                        if (filePaths != null) {
                            nowadapter.updateData(filePaths.get(0), mPos);
                        }
                    }
                }

            }
            if (requestCode == PASS_INSERT_JOBHISTORY) {//从业经历
                if (data != null) {
                    jobHistory.getTxtContent().setText(data.getStringExtra("detail"));
                }
            }
            if (requestCode == PASS_INSERT_JOBTEACHHISTORY) {//培训经历
                if (data != null) {
                    jobTeachHistory.getTxtContent().setText(data.getStringExtra("detail"));

                }
            }
            if (requestCode == PASS_INSERT_JOBGOODAT) {//个人专长
                if (data != null) {
                    jobGoodAt.getTxtContent().setText(data.getStringExtra("detail"));

                }
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能选择图片");
                PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
            }
        }
        if (requestCode == RC_PERMISSION_VIDEO) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能拍摄");
                PermissionUtils.requestPermissions(this, RC_PERMISSION_VIDEO, mPermissions);
            }
        }
    }


    public void showCityPick() {

        cityPicker.showCityPicker();
    }

    private SingleWheelDialog mYearDialog;

    /**
     * 选择宝宝性别
     */
    public void chooseYear() {
        if (mYearDialog == null) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < 41; i++) {
                list.add(i + "年");
            }
            mYearDialog = SingleWheelDialog.newInstance(list);
            mYearDialog.setOnConfirmClick(new SingleWheelDialog.OnConfirmClickListener() {
                @Override
                public void onClick(int pos, String data) {
                    jobYear.getTxtContent().setText(data);
                }
            });
        }
        mYearDialog.show(getSupportFragmentManager(), "jobYear");
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        jobName = (CommonInsertSection) findViewById(R.id.jobName);
        jobPhone = (CommonInsertSection) findViewById(R.id.jobPhone);
        jobCode = (CommonInsertSection) findViewById(R.id.jobCode);
        jobId = (CommonInsertSection) findViewById(R.id.jobId);
        jobAge = (CommonInsertSection) findViewById(R.id.jobAge);
        jobSex = (CommonInsertSection) findViewById(R.id.jobSex);
        jobLoc = (CommonInsertSection) findViewById(R.id.jobLoc);
        jobNowLoc = (CommonInsertSection) findViewById(R.id.jobNowLoc);
//        jobWantLoc = (CommonInsertSection) findViewById(R.id.jobWantLoc);
        jobYear = (CommonInsertSection) findViewById(R.id.jobYear);
        jobHistory = (CommonInsertSection) findViewById(R.id.jobHistory);
        jobTeachHistory = (CommonInsertSection) findViewById(R.id.jobTeachHistory);
        jobGoodAt = (CommonInsertSection) findViewById(R.id.jobGoodAt);
        jobRecyclerCertPager = (RecyclerView) findViewById(R.id.jobRecyclerCertPager);
        jobRecyclerHealthPager = (RecyclerView) findViewById(R.id.jobRecyclerHealthPager);
        jobHide = (TextView) findViewById(R.id.jobHide);
        jobRecyclerWantLoc = (RecyclerView) findViewById(R.id.jobRecyclerWantLoc);
        jobTitleId = (SectionView) findViewById(R.id.jobTitleId);
        jobWantLoc = (CommonInsertSection) findViewById(R.id.jobWantLoc);
        jobTitleGood = (SectionView) findViewById(R.id.jobTitleGood);
        jobTitlePaper = (SectionView) findViewById(R.id.jobTitlePaper);
        jobTitleOther = (SectionView) findViewById(R.id.jobTitleOther);
        jobOtherEt = (EditText) findViewById(R.id.jobOtherEt);
        jobOtherEtCount = (TextView) findViewById(R.id.jobOtherEtCount);
    }

    @Override
    public void onSuccessGetJobType(List<JobType> results) {

    }

    private Disposable mCountDownDisposable;

    @Override
    public void onSuccessSendCode() {
        AutoDisposeConverter<Long> objectAutoDisposeConverter = AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
        jobCode.getEtContent().requestFocus();

        Observable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .to(objectAutoDisposeConverter)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCountDownDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        long reset = 60 - aLong;
                        if (reset == 0) {
                            ((TextView) jobPhone.getRightEnd()).setText("获取验证码");
                            ((TextView) jobPhone.getRightEnd()).setEnabled(true);
                        } else {
                            ((TextView) jobPhone.getRightEnd()).setText(String.format("重新发送(%s)", reset));
                            ((TextView) jobPhone.getRightEnd()).setEnabled(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onGetCodeFail() {
        ((TextView) jobPhone.getRightEnd()).setEnabled(true);
        ((TextView) jobPhone.getRightEnd()).setText("获取验证码");
    }

    @Override
    public void onSuccessAdd() {
        SpUtils.store(mContext, SpKey.JOB_TMP, "");
        super.finish();
    }


    private List<String> mBase64CretImgs = new ArrayList<>();
    private List<String> mBase64HealthImgs = new ArrayList<>();

    private void uploadCretImgs(final List<String> filePaths) {
        showLoading();
        mBase64CretImgs.clear();
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
                        mBase64CretImgs.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败");
                    }

                    @Override
                    public void onComplete() {
                        if (mBase64CretImgs.size() > 0) {
                            jobPresenter.uploadCretFile(mBase64CretImgs, 0);
                        } else {
                            onUpLoadCretSuccess(null, 0);
                        }
                    }
                });
    }

    private void uploadHealthImgs(final List<String> filePaths) {
        showLoading();
        mBase64HealthImgs.clear();
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
                        mBase64HealthImgs.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败");
                    }

                    @Override
                    public void onComplete() {
                        if (mBase64HealthImgs.size() > 0) {
                            jobPresenter.uploadHealthFile(mBase64HealthImgs, 0);
                        } else {
                            onUpLoadHealthSuccess(null, 0);
                        }
                    }
                });
    }

    private boolean checkIllegal() {

        if (TextUtils.isEmpty(jobName.getEtContent().getText().toString())) {
            showToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(jobPhone.getEtContent().getText().toString())) {
            showToast("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(jobCode.getEtContent().getText().toString())) {
            showToast("请输入验证码");
            return false;
        }
        if (TextUtils.isEmpty(jobId.getEtContent().getText().toString())) {
            showToast("请输入身份证");
            return false;
        }
        if (TextUtils.isEmpty(jobLoc.getTxtContent().getText().toString())) {
            showToast("请选择籍贯");
            return false;
        }
        if (TextUtils.isEmpty(jobNowLoc.getTxtContent().getText().toString())) {
            showToast("请选择现居住地");
            return false;
        }
        if (adapterLocList.size() == 0) {
            showToast("请选择就业意向地");
            return false;
        }
        if (TextUtils.isEmpty(jobYear.getTxtContent().getText().toString())) {
            showToast("请选择从业年限");
            return false;
        }
        if (TextUtils.isEmpty(jobHistory.getTxtContent().getText().toString())) {
            showToast("请填写从业经历");
            return false;
        }
        if (TextUtils.isEmpty(jobTeachHistory.getTxtContent().getText().toString())) {
            showToast("请填写培训经历");
            return false;
        }
        if (TextUtils.isEmpty(jobGoodAt.getTxtContent().getText().toString())) {
            showToast("请填写个人专长");
            return false;
        }
        if (adapterCertPaperList.size() == 0) {
            showToast("请上传资质证书");
            return false;
        }
        if (adapterHealthPaperList.size() == 0) {
            showToast("请上传健康证");
            return false;
        }

        return true;
    }

    private boolean checkIllegalNeedStore() {
        //有一个不为空 就要做缓存
        if (!TextUtils.isEmpty(jobName.getEtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobPhone.getEtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobCode.getEtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobId.getEtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobLoc.getTxtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobNowLoc.getTxtContent().getText().toString())) {
            return true;
        }
        if (adapterLocList.size() != 0) {
            return true;
        }
        if (!TextUtils.isEmpty(jobYear.getTxtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobHistory.getTxtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobTeachHistory.getTxtContent().getText().toString())) {
            return true;
        }
        if (!TextUtils.isEmpty(jobGoodAt.getTxtContent().getText().toString())) {
            return true;
        }
        if (adapterCertPaperList.size() != 0) {
            return true;
        }
        if (adapterHealthPaperList.size() != 0) {
            return true;
        }
        return false;
    }

    String uploadCretImgString = "";
    String uploadwantAreaCodeString = "";
    String uploadHealthImgString = "";

    @Override
    public void onSubmitBtnPressed() {
        adapterLocList = addLocAdapter.getResult();
        adapterCertPaperList = cretAdapter.getResult();
        adapterHealthPaperList = healthAdapter.getResult();
        if (!checkIllegal()) {
            return;
        }
        if (adapterCertPaperList.size() > 0) {
            uploadCretImgs(adapterCertPaperList);
        } else {
            uploadHealthImgs(adapterHealthPaperList);
        }
    }

    private List<String> internetCertPaperList = new ArrayList<>();//从adapter中获得的list
    private List<String> internetHealthPaperList = new ArrayList<>();//从adapter中获得的list

    @Override
    public void onUpLoadCretSuccess(List<String> urls, int type) {//
        internetCertPaperList = urls;
        uploadHealthImgs(adapterHealthPaperList);
    }

    @Override
    public void onUpLoadHealthSuccess(List<String> urls, int type) {//上传完成的最后一步了 可以进行最终上传了
        internetHealthPaperList = urls;
        successUploadAll();

    }

    private void successUploadAll() {
        uploadwantAreaCodeString = "";
        for (int i = 0; i < adapterLocList.size(); i++) {
            if (!TextUtils.isEmpty(adapterLocList.get(i))) {
                uploadwantAreaCodeString = uploadwantAreaCodeString + adapterLocList.get(i) + ",";
            }
        }
        uploadCretImgString = "";
        for (int i = 0; i < internetCertPaperList.size(); i++) {
            if (!TextUtils.isEmpty(internetCertPaperList.get(i))) {
                uploadCretImgString = uploadCretImgString + internetCertPaperList.get(i) + ",";
            }
        }
        uploadHealthImgString = "";
        for (int i = 0; i < internetHealthPaperList.size(); i++) {
            if (!TextUtils.isEmpty(internetHealthPaperList.get(i))) {
                uploadHealthImgString = uploadHealthImgString + internetHealthPaperList.get(i) + ",";
            }
        }
        Map<String, Object> map = new HashMap<>(10);
        map.put("realName", jobName.getEtContent().getText().toString());
        map.put("phoneNumber", jobPhone.getEtContent().getText().toString());
        map.put("verifyCode", jobCode.getEtContent().getText().toString());
        map.put("idcardNumber", jobId.getEtContent().getText().toString());
        map.put("age", jobAge.getTxtContent().getText().toString());
        map.put("sex", jobSex.getEtContent().getText().toString());
        map.put("provinceNative", jobLoc.getEtContent().getText().toString().split("-")[0]);
        map.put("cityNative", jobLoc.getEtContent().getText().toString().split("-")[1]);
        map.put("areaNative", jobLoc.getEtContent().getText().toString().split("-")[2]);
        map.put("provinceNow", jobNowLoc.getEtContent().getText().toString().split("-")[0]);
        map.put("cityNow", jobNowLoc.getEtContent().getText().toString().split("-")[1]);
        map.put("areaNow", jobNowLoc.getEtContent().getText().toString().split("-")[2]);
        map.put("areaWant", uploadwantAreaCodeString);
        map.put("workYears", jobYear.getTxtContent().getText().toString().replace("年", ""));
        map.put("workExperience", jobHistory.getTxtContent().getText().toString());
        map.put("trainingExperience", jobTeachHistory.getTxtContent().getText().toString());
        map.put("personalExpertise", jobGoodAt.getTxtContent().getText().toString());
        map.put("certificateImg", uploadCretImgString);
        map.put("healthImg", uploadHealthImgString);
        map.put("remark", jobOtherEt.getText().toString());
        map.put("technicianTypeId", technicianTypeId);
        jobPresenter.addJobDetail(map);
    }
}
