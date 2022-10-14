package com.health.mine.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.documentfile.provider.DocumentFile;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.BuildConfig;
import com.health.mine.R;
import com.health.mine.contract.SettingsContract;
import com.health.mine.presenter.SettingsPresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.business.ChangeIPDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.dialog.TongLianPhoneUnBindDialog;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.net.RetrofitHelper;
import com.healthy.library.presenter.DeleteJiGuangPresenter;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.tinker.lib.tinker.Tinker;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Li
 * @date 2019/04/17 14:34
 * @des 设置界面
 */
@Route(path = MineRoutes.MINE_SETTINGS)
public class SettingsActivity extends BaseActivity implements SettingsContract.View {

    private SectionView mSectionVersion;
    private TextView mTvCopyright;
    private TopBar mTopBar;
    private StatusLayout mStatusLayout;
    private SettingsPresenter mPresenter;
    private SectionView sectionClear;
    private UpdateVersion updateVersion;
    private SectionView sectionIp;
    private SectionView section_security;
    private SectionView section_privacidad;
    private ChangeIPDialog changeIPDialog;
    private SectionView sectionSdk;
    private SectionView sectionNotice;
    private SectionView section_phone;
    private SectionView section_pwd;
    private StatusLayout layoutStatus;
    private TopBar topBar;
    private android.widget.LinearLayout topView;
    private SectionView sectionAbout1;
    private SectionView sectionSource;
    private SectionView sectionUse;
    private SectionView sectionKillOut;
    private SectionView sectionPatch;
    private SectionView sectionSecurity;
    private SectionView sectionPrivacidad;
    private android.widget.ImageView ivLogo;
    private TextView tvCopyright;
    private TextView title;
    private SectionView sectionPhone;
    private SectionView sectionPwd;
    private TextView logout;
    private SectionView sectionUnbind;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_settings;
    }

    @Override
    protected void findViews() {
        mSectionVersion = findViewById(R.id.section_about1);
        mTvCopyright = findViewById(R.id.tv_copyright);
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
        section_security = findViewById(R.id.section_security);
        section_privacidad = findViewById(R.id.section_privacidad);
        section_phone = findViewById(R.id.section_phone);
        section_pwd = findViewById(R.id.section_pwd);
        initView();
        if (!ChannelUtil.isRealRelease()) {
            sectionUnbind.setVisibility(View.VISIBLE);
            sectionPatch.setVisibility(View.VISIBLE);
            sectionClear.setVisibility(View.VISIBLE);
        } else {
            sectionUnbind.setVisibility(View.GONE);
            sectionPatch.setVisibility(View.GONE);
            sectionClear.setVisibility(View.GONE);
        }
        if (ChannelUtil.isRealRelease()) {
            sectionIp.setVisibility(View.GONE);
        } else {
            sectionIp.setVisibility(View.VISIBLE);
            sectionIp.setTitle("设置ip 当前:" + RetrofitHelper.getIp(mContext));
        }
        String phone = SpUtils.getValue(mContext, SpKey.PHONE);
        section_phone.setDes((phone.substring(0, 3) + "****" + phone.substring(7, phone.length())) + "\t");
    }

    private UpdateVersion resolveUpdate(String obj) {
        UpdateVersion result = null;
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
            Type type = new TypeToken<UpdateVersion>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mPresenter = new SettingsPresenter(mContext, this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mSectionVersion.setDes(String.format("V %s", BuildConfig.VERSION_NAME));
        mTvCopyright.setText(String.format("Copyright © 2019-%s 梅氏健康.All Rights Reserved",
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
        updateVersion = resolveUpdate(SpUtils.getValue(mContext, SpKey.USE_UPDATE));

        if (updateVersion != null) {
            //System.out.println("最新版本号:" + updateVersion.getVersionCode());
        }
        section_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sectionNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(AppRoutes.APP_MESSAGESETTING)
                        .navigation();
            }
        });
        sectionSdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                        .withString("url", UrlKeys.URL_HMMPTSDK)
                        .withString("title", "")
                        .navigation();
            }
        });
        section_privacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                        .withString("url", UrlKeys.URL_HMMPTYSXY)
                        .withString("title", "")
                        .navigation();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    File file = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        file = uriToFileApiQ(mContext,uri);
                    }else {
                        try {
                            file=new File(getPath(mContext,uri));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (file.exists()) {
                        if(SpUtilsOld.getValue(LibApplication.getAppContext(),SpKey.YSLOOK,false)){
                            String upLoadFilePath = file.toString();
                            if(upLoadFilePath.contains("patch_signed_7zip")){
                                try {
                                    Tinker.with(mContext).cleanPatch();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println("补丁测试:开始手动应用");
                                TinkerManager.getInstance().applyPatch(getApplicationContext(), upLoadFilePath);
                            }
                        }

                    }
                }
            }
        }
    }
    public static String getFileRealNameFromUri(Context context, Uri fileUri) {
        if (context == null || fileUri == null) return null;
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, fileUri);
        if (documentFile == null) return null;
        return documentFile.getName();
    }
    //将uri对应的文件复制一份到私有目录，之后就可以操作复制后的File了
    @RequiresApi(Build.VERSION_CODES.Q)
    public File uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        if (uri == null) return file;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = getFileRealNameFromUri(context,uri);
            InputStream is = null;
            try {
                is = contentResolver.openInputStream(uri);
                File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                byte[] b = new byte[1024];
                while ((is.read(b)) != -1) {
                    fos.write(b);// 写入数据
                }
                file = cache;
                fos.close();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("返回的文件地址:"+file.getAbsolutePath());
        return file;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public String uriToFilePathApiQ(Context context, Uri uri) {
        File file = null;
        if (uri == null) return null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = getFileRealNameFromUri(context,uri);
            InputStream is = null;
            try {
                is = contentResolver.openInputStream(uri);
                File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                byte[] b = new byte[1024];
                while ((is.read(b)) != -1) {
                    fos.write(b);// 写入数据
                }
                file = cache;
                fos.close();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(file==null){
            return null;
        }
        return file.getAbsolutePath();
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public void logout(View view) {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("是否退出登录")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteJiGuangPresenter(mContext).deleteJiGuangOnly();
                        mPresenter.logout();
                    }
                }).create().show();
    }
    public void chosePatch(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    TongLianPhoneUnBindDialog tongLianPhoneBindDialog;
    public void unbindPhone(View view) {

        TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
        if(tongLianMemberData!=null&&tongLianMemberData.memberInfo.isPhoneChecked) {//绑定了
            if (tongLianPhoneBindDialog == null) {
                tongLianPhoneBindDialog = TongLianPhoneUnBindDialog.newInstance();
            }
            tongLianPhoneBindDialog.show(getSupportFragmentManager(), "手机解除绑定");
            tongLianPhoneBindDialog.setOnDialogAgreeClickListener(new TongLianPhoneUnBindDialog.OnDialogAgreeClickListener() {
                @Override
                public void onDialogAgree() {

                }
            });
        }else {

        }

    }
    public void clearSp(View view) {
//        String userId = SpUtils.getValue(mContext, SpKey.USER_ID);
//        String userStatus = SpUtils.getValue(mContext, SpKey.STATUS);
//        String userToken = SpUtils.getValue(mContext, SpKey.TOKEN);
//        String phone = SpUtils.getValue(mContext, SpKey.PHONE);
//        String locChose = SpUtils.getValue(mContext, SpKey.LOC_CHOSE);
//        boolean ysStatus = SpUtils.getValue(mContext, SpKey.YSLOOK, false);

//        SpUtils.store(mContext,SpKey.LOC_CHOSE,locChose);
//        SpUtils.store(mContext, SpKey.USER_ID, userId);
//        SpUtils.store(mContext, SpKey.STATUS, userStatus);
//        SpUtils.store(mContext, SpKey.SHOW_GUIDE, false);
//        SpUtils.store(mContext, SpKey.YSLOOK, true);
//        SpUtils.store(mContext, SpKey.TOKEN, userToken);
//        SpUtils.store(mContext, SpKey.PHONE, phone);
//        Toast.makeText(mContext, "清空完成", Toast.LENGTH_SHORT).show();

//        DataCleanManager.clearAllCache(this);
        Toast.makeText(getApplication(), "缓存已清理", Toast.LENGTH_LONG).show();
//        Beta.cleanTinkerPatch(true);
//        Beta.unInit();
        SpUtils.clear(mContext);
        SpUtils.store(mContext, SpKey.YSLOOK, true);
        SpUtilsOld.clear(mContext);
        SpUtilsOld.store(mContext, SpKey.YSLOOK, true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                Tinker.with(getApplication()).cleanPatch();
//                FrameActivityManager.instance().appExit(LibApplication.getAppContext());
//            }
//        }, 2000);

    }

    @Override
    public void onLogoutSuccess() {
        String phone = SpUtils.getValue(this, SpKey.PHONE);
//        JPushInterface.deleteAlias(getApplicationContext(), Integer.parseInt(phone.substring(phone.length() > 4 ? phone.length() - 4 : 0, phone.length())));
        SpUtils.store(mContext, SpKey.USER_ID, null);
        SpUtils.store(mContext, SpKey.STATUS, null);
        String testIP = SpUtils.getValue(mContext, SpKey.TEST_IP);
        boolean isShowUserGift = SpUtils.getValue(mContext, "isShowUserGift", false);
        boolean isShowUserActGift = SpUtils.getValue(mContext, "isShowUserActGift", false);
        String value = SpUtils.getValue(mContext, "redDotKey");
        SpUtils.clear(mContext);
        SpUtils.store(mContext,"redDotKey",value);
        //新客礼包首次进入引导 -> 2021/11/02 跟手机进行绑定 不根据当前用户绑定
        SpUtils.store(mContext, "isShowUserGift", isShowUserGift);
        //节日礼包首次进入引导
        SpUtils.store(mContext, "isShowUserActGift", isShowUserActGift);
        SpUtils.store(mContext, SpKey.TEST_IP, testIP);
        SpUtils.store(mContext, SpKey.SHOW_GUIDE, false);
        SpUtils.store(mContext, SpKey.YSLOOK, true);
        SpUtils.store(mContext, "isShowZZ", true);
        SpUtils.store(mContext, "专家答疑Guide", 1);
        SpUtils.store(mContext, "完善资料Guide", 1);
        SpUtils.store(mContext, "同城憨妈Guide", 1);
        SpUtils.isFirst(mContext, "floatMall2");
        SpUtils.isFirst(mContext, "floatPost");
        SpUtils.isFirst(mContext, "floatFaq");
        RetrofitHelper.clear();
        ARouter.getInstance()
                .build(AppRoutes.APP_LOGINTRANSFER)
                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.getUserInfo();
        }
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {
        showContent();
        if (userInfoModel != null) {
            section_pwd.setVisibility(View.VISIBLE);
            if (userInfoModel.haveLoginPwd) {
                section_pwd.setDes("修改\t");
                section_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_UPDATEPASSWORD)
                                .withString("type", "2")
                                .navigation();
                    }
                });
            } else {
                section_pwd.setDes("新设\t");
                section_pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(AppRoutes.APP_GETCODE)
                                .withString("type", "1")
                                .navigation();
                    }
                });
            }
        } else {
            section_pwd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        showContent();
    }

    public void checkVersion(View view) {
//        if (Beta.getUpgradeInfo() != null) {
//            if (Beta.getUpgradeInfo().versionCode > BuildConfig.VERSION_CODE) {
//                Beta.checkUpgrade();
//                return;
//            }
//        }


    }

    public void setIp(View view) {
        changeIPDialog = ChangeIPDialog.newInstance();
        changeIPDialog.show(getSupportFragmentManager(), "IP");
        changeIPDialog.setResultListener(new ChangeIPDialog.getContentListener() {
            @Override
            public void resultContent(String result) {
                SpUtils.store(mContext, SpKey.TEST_IP, result + "");
                Toast.makeText(mContext, "修改IP成功等30秒再登录\n以免短信收不到", Toast.LENGTH_LONG).show();
                onLogoutSuccess();
            }
        });
//        StyledDialog.init(this);
//        StyledDialog.buildMdInput("修改IP", "输入IP", "",
//                "", "", new InputFilter[]{}, null, new MyDialogListener() {
//                    @Override
//                    public void onFirst() {
//
//                    }
//
//                    @Override
//                    public void onSecond() {
//
//                    }
//
//                    @Override
//                    public boolean onInputValid(CharSequence input1, CharSequence input2, EditText editText1, EditText editText2) {
//                        SpUtils.store(mContext, SpKey.TEST_IP, input1.toString());
//                        Toast.makeText(mContext, "修改IP成功重启APP", Toast.LENGTH_LONG).show();
//                        onLogoutSuccess();
//                        return true;
//                    }
//
//                    @Override
//                    public void onGetInput(CharSequence input1, CharSequence input2) {
//                        super.onGetInput(input1, input2);
//                    }
//                })
//                .setInput2HideAsPassword(true)
//                .setCancelable(true, true)
//                .show();
    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void showDataErr() {

    }

    public void passAbout(View view) {
//        if (Beta.getUpgradeInfo() != null) {
//            if (Beta.getUpgradeInfo().versionCode > BuildConfig.VERSION_CODE) {
//                Beta.checkUpgrade();
//                return;
//            }
//        }
        ARouter.getInstance()
                .build(MineRoutes.MINE_SETTINGSABOUT)
                .navigation();

    }

    public void passSource(View view) {
        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
        }
    }

    public void passHelp(View view) {
        ARouter.getInstance()
                .build(MineRoutes.MINE_SETTINGSABOUTHELP)
                .navigation();
    }

    public void killOut(View view) {
        ARouter.getInstance()
                .build(MineRoutes.MINE_SETTINGS_KILL)
                .navigation();
    }

    public void changePhone(View view) {
//        ARouter.getInstance()
//                .build(AppRoutes.APP_UPDATEPHONE)
//                .navigation();
    }

    private void initView() {
        sectionClear = (SectionView) findViewById(R.id.section_clear);
        sectionIp = (SectionView) findViewById(R.id.section_ip);
        sectionSdk = (SectionView) findViewById(R.id.section_sdk);
        sectionNotice = (SectionView) findViewById(R.id.section_notice);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        topBar = (TopBar) findViewById(R.id.top_bar);
        topView = (LinearLayout) findViewById(R.id.topView);
        sectionAbout1 = (SectionView) findViewById(R.id.section_about1);
        sectionSource = (SectionView) findViewById(R.id.section_source);
        sectionUse = (SectionView) findViewById(R.id.section_use);
        sectionKillOut = (SectionView) findViewById(R.id.section_KillOut);
        sectionPatch = (SectionView) findViewById(R.id.section_patch);
        sectionSecurity = (SectionView) findViewById(R.id.section_security);
        sectionPrivacidad = (SectionView) findViewById(R.id.section_privacidad);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        tvCopyright = (TextView) findViewById(R.id.tv_copyright);
        title = (TextView) findViewById(R.id.title);
        sectionPhone = (SectionView) findViewById(R.id.section_phone);
        sectionPwd = (SectionView) findViewById(R.id.section_pwd);
        logout = (TextView) findViewById(R.id.logout);
        sectionUnbind = (SectionView) findViewById(R.id.section_unbind);
    }
}
