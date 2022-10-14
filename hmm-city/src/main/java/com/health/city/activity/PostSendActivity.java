package com.health.city.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.R;
import com.health.city.adapter.CityAddImgAdapter;
import com.health.city.adapter.CityAddTipAdapter;
import com.health.city.contract.PostSendContract;
import com.health.city.model.UserInfoCityModel;
import com.health.city.presenter.PostSendPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.decoration.AddImgDecoration;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.PostStore;
import com.healthy.library.model.Topic;
import com.healthy.library.model.TopicLimit;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.FileUtil;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.MediaFileUtil;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoFitCheckBox;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.lxj.matisse.CaptureMode;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 帖子发送 帖子评论 帖子回复
 */
@Route(path = CityRoutes.CITY_POSTSEND)
public class PostSendActivity extends BaseActivity implements View.OnClickListener, OnSubmitListener, AMapLocationListener, PostSendContract.View, OnRefreshLoadMoreListener, CityAddImgAdapter.OnImgChangeListener, CityAddTipAdapter.OnTipChangeListener {
    CityAddImgAdapter cityAddImgAdapter;
    CityAddTipAdapter cityAddTipAdapter;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private android.widget.EditText etEva;
    private RecyclerView otherTip;
    private RecyclerView recyclerImgs;
    private android.widget.TextView tipAddress;
    private AutoFitCheckBox nonamecheck;
    private android.widget.TextView nonamechecktext;
    private android.widget.TextView nonamecheckvalue;
    private int num = 2000;
    private int numTitle = 40;
    private int nummin = 1;
    private PostSendPresenter postSendPresenter;
    private String limitsStatus = "1"; //默认都是本地帖子
    private boolean lengthlimit = false;
    private boolean lengthlimitTitle = false;
    private boolean istoast = false;
    private android.widget.LinearLayout warmTipLL;
    private android.widget.LinearLayout etEvall;
    private ConstraintLayout nillcheck;

    @Autowired
    String topicId;
    @Autowired
    String topicName;


    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private String longitude;
    private String latitude;
    private String locCityName;
    private String areaName;
    private String newCityName;
    private int RC_LOCATION = 111020;
    private int RC_PROVINCE_CITY = 114697;
    private int reLocTime = 0;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private Dialog dialogp;
    private LinearLayout etTitleall;
    private EditText etEtTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_post;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        StyledDialog.init(this);
        topBar.setSubmitListener(this);
        postSendPresenter = new PostSendPresenter(mContext, this);
        cityAddImgAdapter = new CityAddImgAdapter(this);
        recyclerImgs.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerImgs.setAdapter(cityAddImgAdapter);
        recyclerImgs.addItemDecoration(new AddImgDecoration(mContext));


        cityAddTipAdapter = new CityAddTipAdapter(this);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        layoutManager.setFlexWrap(FlexWrap.WRAP); //设置是否换行
//        layoutManager.setFlexDirection(FlexDirection.ROW); // 设置主轴排列方式
//        layoutManager.setAlignItems(AlignItems.STRETCH);
//        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        otherTip.setLayoutManager(layoutManager);
        otherTip.setAdapter(cityAddTipAdapter);

        tipAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readyloc = tipAddress.getText().toString();
                String[] readylocarray = readyloc.split("·");
                if (readylocarray.length > 1) {
                    readyloc = readylocarray[1];
                }
                ARouter.getInstance()
                        .build(CityRoutes.CITY_LOCATION)
                        .withString("areaNameSelect", readyloc)
                        .navigation(PostSendActivity.this, RC_CHOOSE_AREA);
            }
        });
        List<String> data = new ArrayList<>();
        data.add(null);
        data.add(null);
        cityAddImgAdapter.setData(data);
        cityAddImgAdapter.setListener(this);
        findViewById(R.id.nillcheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nonamecheck.setPressed(true);
                nonamecheck.performClick();
            }
        });

        List<Topic> datatopic = new ArrayList<>();
        datatopic.add(null);
        if (!TextUtils.isEmpty(topicId)) {
            datatopic.add(new Topic(topicId, topicName));
            //System.out.println("带进来的测试:"+topicId+":"+topicName);
        }
        cityAddTipAdapter.setData(datatopic);
        cityAddTipAdapter.setListener(this);
        helper.attachToRecyclerView(recyclerImgs);
        etEva.addTextChangedListener(new TextWatcher() {
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
                int number = num - s.length();
//                tv_num.setText("" + number);
                selectionStart = etEva.getSelectionStart();
                selectionEnd = etEva.getSelectionEnd();
                ////System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > num) {
                    showToast("限制输入" + num + "个字");
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    etEva.setText(s);
                    etEva.setSelection(tempSelection);//设置光标在最后
                }
                if (s.length() >= nummin) {
                    lengthlimit = true;
                } else {

                    lengthlimit = false;
                }
            }
        });


        etEtTitle.addTextChangedListener(new TextWatcher() {
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
                int number = numTitle - s.length();
//                tv_num.setText("" + number);
                selectionStart = etEtTitle.getSelectionStart();
                selectionEnd = etEtTitle.getSelectionEnd();
                ////System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > numTitle) {
                    showToast("限制输入" + numTitle + "个字");
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    etEtTitle.setText(s);
                    etEtTitle.setSelection(tempSelection);//设置光标在最后
                }
                if (s.length() >= 0) {
                    lengthlimitTitle = true;
                } else {
                    lengthlimitTitle = false;
                }
            }
        });

        postSendPresenter.getMine();
        buildTmpPost();
        locateCheck();

    }


    private void locateCheck() {
        locate();
    }

    private void locate() {
        if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) != null) {
            successLocation();
        } else {
            mLocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setOnceLocation(true);
            //设置定位监听
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }


    @Override
    public void onBackBtnPressed() {

        super.onBackBtnPressed();
    }

    private void buildTmpPost() {
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.POST_TMP2))) {
            String jsonstring = SpUtils.getValue(mContext, SpKey.POST_TMP2);
            PostStore postStore = resolveTmpData(jsonstring);
            if (postStore.postingLimit != null) {
                topicLimit.add(postStore.postingLimit);
            }
            if (!TextUtils.isEmpty(postStore.postingAddress)) {
                tipAddress.setText(postStore.postingAddress);
            }

        }


        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.POST_TMP))) {
            String jsonstring = SpUtils.getValue(mContext, SpKey.POST_TMP);
            PostStore postStore = resolveTmpData(jsonstring);
            for (int i = 0; i < postStore.uploaduls.size(); i++) {
                if (!TextUtils.isEmpty(postStore.uploaduls.get(i))) {
                    cityAddImgAdapter.addData(postStore.uploaduls.get(i));
                }
            }
            for (int i = 0; i < postStore.topicIds.size(); i++) {
                if (postStore.topicIds.get(i) != null && !checkIsInTip(postStore.topicIds.get(i).id + "")) {
                    cityAddTipAdapter.addData(postStore.topicIds.get(i));
                }
            }
            etEva.setText(postStore.postingContent);
            etEtTitle.setText(postStore.postingTitle);
            if (postStore.getAnonymousStatus() == 0) {
                nonamecheck.setChecked(false);
            } else {
                nonamecheck.setChecked(true);
            }
            if (postStore.postingLimit != null) {
                topicLimit.add(postStore.postingLimit);
            }
            if (!TextUtils.isEmpty(postStore.postingAddress)) {
                tipAddress.setText(postStore.postingAddress);
            }

        }


    }

    private PostStore resolveTmpData(String obj) {
        PostStore result = null;
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
            Type type = new TypeToken<PostStore>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    public void finish() {
        if (getNowStatus() == StatusLayout.Status.STATUS_CONTENT) {//说明没问题的页面
            if (cityAddTipAdapter.getData().size() > 1 || etEva.getText().toString().length() > 0 || cityAddImgAdapter.getData().size() > 2) {
                StyledDialog.init(this);
                StyledDialog.buildIosAlert("", "是否保存草稿?", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        SpUtils.store(mContext, SpKey.POST_TMP, "");
                        FileUtil.deleteFileUnderParent(new File(Environment.getExternalStorageDirectory(), "mmvideo"));
                        PostSendActivity.super.finish();

                    }

                    @Override
                    public void onThird() {
                        super.onThird();

                    }

                    @Override
                    public void onSecond() {
                        PostStore postStore = getPostStore();
                        String result = new Gson().toJson(postStore);
                        SpUtils.store(mContext, SpKey.POST_TMP, result);
                        PostSendActivity.super.finish();


                    }
                }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("不保存", "保存").show();
            } else {

                FileUtil.deleteFileUnderParent(new File(Environment.getExternalStorageDirectory(), "mmvideo"));
                PostSendActivity.super.finish();
            }
        } else {
            //说明页面出问题了 出问题就自动保存了
            PostStore postStore = getPostStore();
            String result = new Gson().toJson(postStore);
            SpUtils.store(mContext, SpKey.POST_TMP, result);
            PostSendActivity.super.finish();
        }


    }

    @NonNull
    private PostStore getPostStore() {
        PostStore postStore = new PostStore();
        postStore.setPostingTitle(etEtTitle.getText().toString());
        postStore.setTopicIds(cityAddTipAdapter.getData());
        postStore.setPostingContent(etEva.getText().toString());
        postStore.setUploaduls(cityAddImgAdapter.getData());
        postStore.setAnonymousStatus(nonamecheck.isChecked() ? 1 : 0);
        postStore.setPostingLimit(topicLimit.size() > 0 ? topicLimit.get(0) : null);
        postStore.setPostingAddress(postingAddress);
        return postStore;
    }

    private void finishSuper() {

        FileUtil.deleteFileUnderParent(new File(Environment.getExternalStorageDirectory(), "mmvideo"));
        super.finish();
    }

    @Override
    public void onSuccessSendPost() {

        topBar.getmSubmitText().setEnabled(true);
        SpUtils.store(mContext, SpKey.POST_TMP, "");
        EventBus.getDefault().post(new UpdateUserLocationMsg());
        FileUtil.deleteFileUnderParent(new File(Environment.getExternalStorageDirectory(), "mmvideo"));
        super.finish();

    }

    @Override
    public void onFailSendPost() {
        topBar.getmSubmitText().setEnabled(true);
        PostStore postStore = getPostStore();
        String result = new Gson().toJson(postStore);
        SpUtils.store(mContext, SpKey.POST_TMP, result);
    }

    @Override
    public void onFailPost() {
        topBar.getmSubmitText().setEnabled(true);
        PostStore postStore = getPostStore();
        String result = new Gson().toJson(postStore);
        SpUtils.store(mContext, SpKey.POST_TMP, result);
        PostSendActivity.super.finish();

    }

    @Override
    public void onUpLoadSuccess(List<String> urlList, int type) {
        if (type == 1) {
            upvideoUrls.clear();
            upvideoUrls.add(urlList.get(0));
            if (needuploadimgs.size() > 0) {

                uploadImgs(needuploadimgs);
            } else {

                evaluate();
            }
        } else {
            upimgUrls = new String[urlList.size()];
            for (int i = 0; i < urlList.size(); i++) {
                upimgUrls[i] = urlList.get(i);
            }
            evaluate();
        }
    }

    String videoCoverImage = "";

    @Override
    public void onUpLoadSuccess2(List<String> urls, int type) {
        //说明视频缩略图上传完了 可以进行final了
        videoCoverImage = urls.get(0);
        uploadVideoFinal(needvideoUrl);

    }

    @Override
    public void onMineSuccess(UserInfoCityModel userInfoModel) {
        memberState = userInfoModel.dateContent;
        if (!SpUtils.getValue(mContext, SpKey.WARM_TMP, false)) {
            warmTipLL.setVisibility(View.VISIBLE);
            topBar.getDividerView().setVisibility(View.GONE);
            warmTipLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    warmTipLL.setVisibility(View.GONE);
                    topBar.getDividerView().setVisibility(View.VISIBLE);
                    SpUtils.store(mContext, SpKey.WARM_TMP, true);
                }
            });
        }
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        etEva = (EditText) findViewById(R.id.et_eva);
        otherTip = (RecyclerView) findViewById(R.id.other_tip);
        recyclerImgs = (RecyclerView) findViewById(R.id.recycler_imgs);
        tipAddress = (TextView) findViewById(R.id.tipAddress);
        nonamecheck = (AutoFitCheckBox) findViewById(R.id.nonamecheck);
        nonamechecktext = (TextView) findViewById(R.id.nonamechecktext);
        nonamecheckvalue = (TextView) findViewById(R.id.nonamecheckvalue);
        warmTipLL = (LinearLayout) findViewById(R.id.warmTipLL);
        etEvall = (LinearLayout) findViewById(R.id.et_evall);
        nillcheck = (ConstraintLayout) findViewById(R.id.nillcheck);
        etTitleall = (LinearLayout) findViewById(R.id.et_titleall);
        etEtTitle = (EditText) findViewById(R.id.et_et_title);
    }

    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private static final int RC_CHOOSE_IMG = 764;
    private static final int RC_CHOOSE_AREA = 769;
    private static final int RC_CHOOSE_TIP = 709;
    private static final int RC_UPDATE_IMG = 765;
    private static final int RC_PERMISSION = 45;
    private static final int RC_PERMISSION_VIDEO = 46;
    private static final int MAX_IMG_NUM = 9 + 1 + 1;
    private static final int MAX_TIP_NUM = 3 + 1;
    private static final int VIDEO_SELECT = 1317;
    private static final int VIDEO_SELECT_UPDATE = 1310;

    public int getNowVideoCount() {
        int result = 0;
        for (int i = 0; i < cityAddImgAdapter.getData().size(); i++) {
            String filepath = cityAddImgAdapter.getData().get(i);
            if (!TextUtils.isEmpty(filepath)) {
                if (MediaFileUtil.isVideoFileType(filepath)) {
                    return 1;
                }
            }
        }
        return result;
    }

    @Override
    public void onAddImg() {

        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            if (getNowVideoCount() == 1) {
                if (cityAddImgAdapter.getData().size() == MAX_IMG_NUM - 1) {
                    showToast("最多上传8张图片和1个视频");
                    return;
                }
            } else {
                if (cityAddImgAdapter.getData().size() == MAX_IMG_NUM) {
                    showToast("最多选9张图片");
                    return;
                }
            }
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .countable(true)
                    .maxSelectable(MAX_IMG_NUM - cityAddImgAdapter.getData().size() - getNowVideoCount())
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(RC_CHOOSE_IMG);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
        }
    }

    @Override
    public void onAddVideo(int pos) {
        mPos = pos;
        //System.out.println("开始的mPos"+mPos);
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
//            if (mAddImgAdapter.getData().size() == MAX_IMG_NUM) {
//                showToast("最多选9张");
//                return;
//            }
            if (getNowVideoCount() == 1) {
                showToast("最多上传1个视频");
                return;

            } else {
                if (cityAddImgAdapter.getData().size() == MAX_IMG_NUM) {
                    showToast("最多上传1个视频和8张图片");
                    return;
                }
            }
            Matisse.from(this)
                    .choose(MimeType.ofVideo())
                    .capture(true, CaptureMode.Video)
                    .countable(false)
                    .maxSelectable(1)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(VIDEO_SELECT);
//            openRealVideo(this, 15l, 32, VIDEO_SELECT);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION_VIDEO, mPermissions);
        }
    }


//    private void openRealVideo(Activity context, long limit_time, int size, int code) {
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        if (size != 0) {
//            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 32 * 1024 * 1024L);//限制录制大小(10M=10 * 1024 * 1024L)
//        }
//        if (limit_time != 0) {
//
//            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, limit_time);//限制录制时间(10秒=10)
//        }
//        File dir = new File(Environment.getExternalStorageDirectory(), "mmvideo");
//        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
//                .format(new Date()) + ".mp4";
//        if ((!dir.exists() && dir.mkdir()) || dir.exists()) {
//            videoFile = new File(dir, fileName);
//        } else {
//            videoFile = null;
//        }
//
//        if (videoFile != null) {
//            Uri uri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                String authority = getPackageName() + ".fileprovider"; //【清单文件中provider的authorities属性的值】
//                uri = FileProvider.getUriForFile(context, authority, videoFile);
//            } else {
//                uri = Uri.fromFile(videoFile);
//            }
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//            context.startActivityForResult(intent, code);
//
//        }
//
//
//
//    }

    private int mPos;

    @Override
    public void onUpdate(int pos) {

        String path=cityAddImgAdapter.getData().get(pos);
        System.out.println("上传文件地址:"+path);
        final String[] array = new String[]{path};
        ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                .withCharSequenceArray("urls", array)
                .withInt("pos", 0)
                .navigation();

//        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
//            mPos = pos;
//            Matisse.from(this)
//                    .choose(MimeType.ofImage())
//                    .capture(true, CaptureMode.Image)
//                    .countable(false)
//                    .maxSelectable(1)
//                    .imageEngine(new GlideEngine())
//                    .theme(R.style.ImgPicker)
//                    .showSingleMediaType(true)
//                    .forResult(RC_UPDATE_IMG);
//        } else {
//            PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
//        }
    }

    @Override
    public void onVideoUpdate(int pos) {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            mPos = pos;
            Matisse.from(this)
                    .choose(MimeType.ofVideo())
                    .capture(true, CaptureMode.Video)
                    .countable(false)
                    .imageEngine(new GlideEngine())
                    .theme(R.style.ImgPicker)
                    .showSingleMediaType(true)
                    .forResult(RC_UPDATE_IMG);
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION_VIDEO, mPermissions);
        }
    }

    @Override
    public void onAddTip() {
        if (cityAddTipAdapter.getData().size() <= 3) {
            ARouter.getInstance()
                    .build(CityRoutes.CITY_TIPLIST)
                    .navigation(this, RC_CHOOSE_TIP);
        } else {
            Toast.makeText(mContext, "最多添加3个话题", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_LOCATION) {
            if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                locate();
            } else {
                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, mPermissions)) {
                    PermissionUtils.showRationale(mActivity);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(mPermissions, RC_LOCATION);
                    }
                }
            }
        }


        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能选择图片");
//                PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
            }
        }
        if (requestCode == RC_PERMISSION_VIDEO) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能拍摄");
//                PermissionUtils.requestPermissions(this, RC_PERMISSION_VIDEO, mPermissions);
            }
        }
    }

    private String videoUrl = null;
    private int duration;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_CHOOSE_IMG) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {//拍摄
                        cityAddImgAdapter.addData(capturePath);
                    } else {//选择
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);
                        if (filePaths != null) {
                            cityAddImgAdapter.addDatas(filePaths);
                        }
                    }
                }
            } else if (requestCode == RC_UPDATE_IMG) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((capturePath = Matisse.obtainCaptureImageResult(data)) != null) {//拍摄
                        cityAddImgAdapter.addData(capturePath);
                    } else {//选择
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);
                        if (filePaths != null) {
                            cityAddImgAdapter.updateData(filePaths.get(0), mPos);
                        }
                    }
                }

            } else if (requestCode == VIDEO_SELECT) {
                if (data != null) {
                    String capturePath = null;
                    String videoPath = null;
                    String cropPath = null;
                    if ((videoPath = Matisse.obtainCaptureVideoResult(data)) != null) {//拍摄
                        videoUrl = videoPath;
                        clip(videoUrl, mPos);
                    } else {
                        List<String> filePaths = Matisse.obtainSelectPathResult(data);//选择
                        if (filePaths != null) {
                            videoUrl = filePaths.get(0);
                            clip(videoUrl, mPos);
                        }
                    }
                }

            } else if (requestCode == VIDEO_SELECT_UPDATE) {


            } else if (requestCode == RC_CHOOSE_AREA) {
                if (data != null) {
                    limitsStatus = data.getStringExtra("limitsStatus");
                    postingAddress = data.getStringExtra("postingAddress");
//                    topicLimit.clear();
                    if (!TextUtils.isEmpty(postingAddress)) {//当用户点击了选择位置 则使用定位的来
                        tipAddress.setText(postingAddress);
                        successLocationOrg();
//                        topicLimit.add(new TopicLimit(data.getStringExtra("provinceNo")
//                                , data.getStringExtra("cityNo") , data.getStringExtra("areaNo")));
                        //不对定位进行修改了
                    } else {//用户没有选择选择位置则用的是选择的门店的信息

                        tipAddress.setText("所在位置");
                        successLocation();
                    }


//                    PostStore postStore = new PostStore();
//                    postStore.setPostingLimit(topicLimit.size() > 0 ? topicLimit.get(0) : null);
//                    postStore.setPostingAddress(postingAddress);
//                    String result = new Gson().toJson(postStore);
//                    SpUtils.store(mContext, SpKey.POST_TMP2, result);
                }
            } else if (requestCode == RC_CHOOSE_TIP) {
                if (data != null) {
                    boolean showTip = data.getBooleanExtra("showTip", true);
                    if (showTip) {
                        if (!checkIsInTip(data.getStringExtra("id"))) {
                            cityAddTipAdapter.addData(new Topic(data.getStringExtra("id"), data.getStringExtra("topicName")));
                        }

                    }
                }
            }
        }

    }

    private void clip(String tmpvideoUrl, int tmpmPos) {
        EpVideo epVideo = new EpVideo(tmpvideoUrl);
        epVideo.clip(1, 15);
        File dir = new File(Environment.getExternalStorageDirectory(), "mmvideo");
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date()) + "_clip.mp4";
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(dir.getAbsolutePath() + "/" + fileName);
        videoUrl = dir.getAbsolutePath() + "/" + fileName;
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {
//                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////
////                        cityAddImgAdapter.updateData(videoUrl, mPos);
////                    }
////                });
                compressVideo(videoUrl);


            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onProgress(float progress) {
                //这里获取处理进度
                //System.out.println("裁剪进度:"+progress);
            }
        });
    }

    private void clip(String tmpvideoUrl) {
        EpVideo epVideo = new EpVideo(tmpvideoUrl);
        epVideo.clip(1, 15);
        File dir = new File(Environment.getExternalStorageDirectory(), "mmvideo");
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date()) + "_clip.mp4";
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(dir.getAbsolutePath() + "/" + fileName);
        videoUrl = dir.getAbsolutePath() + "/" + fileName;
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {

                compressVideo(videoUrl);
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onProgress(float progress) {
                //这里获取处理进度
                //System.out.println("裁剪进度:"+progress);
            }
        });
    }

    private boolean checkIsInTip(String id) {
        boolean result = false;

        if (!TextUtils.isEmpty(id)) {
            List<Topic> topics = cityAddTipAdapter.getData();
            for (int i = 0; i < topics.size(); i++) {

                if (topics.get(i) != null) {
                    if (topics.get(i).id.equals(id)) {
                        return true;
                    }
                }

            }
        } else {
            return false;
        }
        return result;
    }

    private String[] upimgUrls;
    private String uptips = "";
    private List<String> upvideoUrls = new ArrayList<>();
    private List<String> mBase64Imgs = new ArrayList<>();
    private List<String> mBase64Imgs2 = new ArrayList<>();
    private List<String> mBase64Videos = new ArrayList<>();

    private List<String> needuploadimgs = new ArrayList<>();
    private String needvideoUrl;
    private TextView txtTitle;

    private void checkupload(final List<String> filePaths) {
        for (int i = 0; i < filePaths.size(); i++) {
            String filepath = filePaths.get(i);
            //System.out.println("需要上传的资源名称:"+filepath);
            if (!TextUtils.isEmpty(filepath)) {
                if (MediaFileUtil.isVideoFileType(filepath)) {
                    needvideoUrl = filepath;
                } else {
                    needuploadimgs.add(filepath);
                }
            }
        }
        if (!TextUtils.isEmpty(needvideoUrl)) {
            uploadVideo(needvideoUrl);
        } else {
            if (needuploadimgs.size() > 0) {
                uploadImgs(needuploadimgs);
            } else {
                evaluate();
            }
        }

    }

    String postingAddress;
    String memberState;
    private List<TopicLimit> topicLimit = new ArrayList<>();

    private void evaluate() {
        Map<String, Object> map = new HashMap<>(10);
        map.put("topicIds", uptips);
        map.put("postingTitle",etEtTitle.getText().toString());
        map.put("postingContent", etEva.getText().toString().replace("\n", "<br>"));
        map.put("postingStatus", "0");
        map.put("memberState", memberState);
        map.put("imgUrls", upimgUrls);
        map.put("imageState", upimgUrls == null ? "0" : "1");
        map.put("videoUrls", upvideoUrls);
        map.put("videoFramePicture", videoCoverImage);
        map.put("videoState", upvideoUrls.isEmpty() ? "0" : "1");
        map.put("anonymousStatus", nonamecheck.isChecked() ? "1" : "0");
        map.put("limitsStatus", limitsStatus);
        map.put("postingLimitsList", topicLimit);
        map.put("postingAddress", postingAddress);
        postSendPresenter.sendPost(map);
    }

    /**
     * 上传图片
     *
     * @param filePaths 图片路径集合
     */
    private void uploadImgs(final List<String> filePaths) {
        showLoading();
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
                        postSendPresenter.uploadFile(mBase64Imgs, 0);
                    }
                });
    }


    /**
     * 上传图片缩略图
     */
    private void uploadImgsFrame(final Bitmap bitmap) {
        showLoading();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                String base64 = BitmapUtils.bitmapToBase64(bitmap);
                emitter.onNext(base64);
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
                        mBase64Imgs2.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败");
                    }

                    @Override
                    public void onComplete() {
                        postSendPresenter.uploadFile2(mBase64Imgs2, 0);
                    }
                });
    }


    private void uploadVideoFinal(final String filePath) {
        showLoading();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {

                String base64 = FileUtil.file2Base64(filePath);
                emitter.onNext(base64);
                emitter.onComplete();
            }
        }).compose(RxThreadUtils.<String>Obs_io_main())
                .to(RxLifecycleUtils.<String>bindLifecycle(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        mBase64Videos.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败：" + e);
                    }

                    @Override
                    public void onComplete() {
                        postSendPresenter.uploadFile(mBase64Videos, 1);
                    }
                });
    }

    MyRxFFmpegSubscriber myRxFFmpegSubscriber;

    private void compressVideo(final String filePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                showLoading();
                Toast.makeText(mContext, "视频压缩中", Toast.LENGTH_SHORT).show();
            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    /**
                     * 视频压缩
                     * 第一个参数:视频源文件路径
                     * 第二个参数:压缩后视频保存的路径
                     */
                    File dir = new File(Environment.getExternalStorageDirectory(), "mmvideo");
                    final String comPressPath = new File(dir.getAbsolutePath(), "comp_" + new File(filePath).getName()).getAbsolutePath();
                    String secondText = "ffmpeg -y -i " + filePath + " -crf 35 -vcodec libx264 -preset superfast " + comPressPath + "";

                    String[] commands = secondText.split(" ");

                    myRxFFmpegSubscriber = new MyRxFFmpegSubscriber(PostSendActivity.this);
                    RxFFmpegInvoke.getInstance()
                            .runCommandRxJava(commands)
                            .subscribe(myRxFFmpegSubscriber);


                    videoUrl = comPressPath;

//                    final String comPressPath = SiliCompressor.with(mContext).compressVideo(filePath, dir.getAbsolutePath());
//                    //System.out.println("压缩后的地址："+comPressPath);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            videoUrl=comPressPath;
//                        }
//                    });


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//
//                        }
//                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void uploadVideo(final String filePath) {
        showLoading();
        if (!TextUtils.isEmpty(filePath)) {
            Bitmap bitmap = null;
//            //System.out.println("出第一帧得文件位置："+filePath);
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(Uri.fromFile(new File(filePath)).toString(), new HashMap());
//            bitmap = retriever.getFrameAtTime();
            bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);

            uploadImgsFrame(bitmap);
//            uploadVideoFinal(filePath);
        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    /**
//                     * 视频压缩
//                     * 第一个参数:视频源文件路径
//                     * 第二个参数:压缩后视频保存的路径
//                     */
//                    File dir = new File(Environment.getExternalStorageDirectory(), "mmvideo");
//                    final String comPressPath = SiliCompressor.with(mContext).compressVideo(filePath, dir.getAbsolutePath());
//                    //System.out.println("压缩后的地址："+comPressPath);
//
//
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    private boolean checkIllegal() {
        if (!lengthlimit) {
            showToast("最少输入" + nummin + "个字");
            return false;
        }
        if (TextUtils.isEmpty(etEva.getText().toString())) {
            showToast("请输入帖子内容");
            return false;
        }
        if (cityAddTipAdapter.getData().size() > 4) {
            showToast("最多选择3个话题");
            return false;
        }
        if (TextUtils.isEmpty(etEtTitle.getText().toString())) {
            showToast("请输入帖子标题");
            return false;
        }
        uptips = "";
        List<Topic> topics = cityAddTipAdapter.getData();

        for (int i = 0; i < topics.size(); i++) {
            if (topics.get(i) != null) {
                if (!TextUtils.isEmpty(topics.get(i).id + "")) {
                    uptips = uptips + topics.get(i).id + ",";
                }
            }

        }
        if (uptips.length() > 0) {
            uptips = uptips.substring(0, uptips.length() - 1);
        }
//        if (TextUtils.isEmpty(uptips)) {
//            showToast("请选择话题");
//            return false;
//        }
        return true;
    }

    @Override
    public void onSubmitBtnPressed() {
        if (!checkIllegal()) {
            return;
        }
        topBar.getmSubmitText().setEnabled(false);
        if (cityAddImgAdapter.getData().size() >= 2) {
            checkupload(cityAddImgAdapter.getData());
        } else {
            evaluate();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getData() {
        super.getData();
        showContent();
        onSubmitBtnPressed();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {

            LocUtil.storeLocation(mContext, aMapLocation);
            successLocation();
        } else {
            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
            layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
                @Override
                public void onRetryClick() {
                    mLocationClient.startLocation();
                }
            });
            if (NavigateUtils.openGPSSettings(mContext) && reLocTime <= 1) {
                mLocationClient.startLocation();
                reLocTime++;
            } else {
                MessageDialog.newInstance()
                        .setMessageTopRes(R.drawable.dialog_message_icon_loc, "开启定位", "为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
                            @Override
                            public void onMessageOkClick(View view) {
                                IntentUtils.toLocationSetting(mContext);
                            }
                        })
                        .show(getSupportFragmentManager(), "打开定位");

            }
        }
    }

    private void successLocation() {
        latitude = LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE);
        longitude = LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE);
        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        cityNo = LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE);
        locCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG);
        newCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE);
        topicLimit.clear();
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        topicLimit.add(new TopicLimit((Integer.parseInt(areaNo) / 10000 * 10000) + ""
                , (Integer.parseInt(areaNo) / 100 * 100) + "", areaNo));
    }

    private void successLocationOrg() {
        latitude = LocUtil.getLatitude(mContext, SpKey.LOC_ORG);
        longitude = LocUtil.getLongitude(mContext, SpKey.LOC_ORG);
        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_ORG);
        cityNo = LocUtil.getCityNo(mContext, SpKey.LOC_ORG);
        locCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG);
        newCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE);
        topicLimit.clear();
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        topicLimit.add(new TopicLimit((Integer.parseInt(areaNo) / 10000 * 10000) + ""
                , (Integer.parseInt(areaNo) / 100 * 100) + "", areaNo));
    }

    public class MyRxFFmpegSubscriber extends RxFFmpegSubscriber {

        private WeakReference<PostSendActivity> mWeakReference;

        public MyRxFFmpegSubscriber(PostSendActivity homeFragment) {
            mWeakReference = new WeakReference<>(homeFragment);
        }

        @Override
        public void onFinish() {

            cityAddImgAdapter.updateData(videoUrl, mPos);
            //System.out.println("FF压缩完成");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StyledDialog.dismiss(dialogp);
                    dialogp = null;
                }
            }, 500);

        }

        @Override
        public void onProgress(int progress, long progressTime) {
            if (dialogp == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dialogp = StyledDialog.buildLoading("视频压缩中...").show();
                    }
                });
            } else {
                //System.out.println("压缩进度:"+progress);
                StyledDialog.updateLoadingMsg("压缩进度:" + progress + "%", dialogp);
            }

//            showLoading();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(String message) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFrlg = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                dragFrlg = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFrlg, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //滑动事件  下面注释的代码，滑动后数据和条目错乱，被舍弃
//            Collections.swap(datas,viewHolder.getAdapterPosition(),target.getAdapterPosition());
//            ap.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());

            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition == cityAddImgAdapter.getData().size() - 1 || fromPosition == cityAddImgAdapter.getData().size() - 2) {
                return false;
            }
            if (toPosition == cityAddImgAdapter.getData().size() - 1 || toPosition == cityAddImgAdapter.getData().size() - 2) {
                return false;
            }
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cityAddImgAdapter.getData(), i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cityAddImgAdapter.getData(), i, i - 1);
                }
            }
            cityAddImgAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //侧滑删除可以使用；
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        /**
         * 长按选中Item的时候开始调用
         * 长按高亮
         * @param viewHolder
         * @param actionState
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.RED);
                //获取系统震动服务//震动70毫秒
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(70);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原高亮
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
            cityAddImgAdapter.notifyDataSetChanged();  //完成拖动后刷新适配器，这样拖动后删除就不会错乱
        }
    });
}
