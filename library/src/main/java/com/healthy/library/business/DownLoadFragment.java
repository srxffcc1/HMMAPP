package com.healthy.library.business;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.UpdateDownLoadInfoMsg;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.OsUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownLoadFragment extends BaseDialogFragment {
    private DownloadManager mDownloadManager;
    private long mDownloadId;
    private String mApkName = "憨妈妈.apk";
    private AlertDialog mAlertDialog;
    private ConstraintLayout dialogBg;
    private ImageView updateTopImg;
    private TextView uodateTitle;
    private TextView uodateInfo;
    private TextView uodateContext;
    private TextView uodateButton;
    private ImageView closeMessageDialog;
    private TextView closeMessageText;
    private IntentFilter filter;
    private String versionBeanString;
    private UpdateVersion updateVersion;
    private final QueryRunnable mQueryProgressRunnable = new QueryRunnable();
    int downloadfailtime=0;
    OnDownLoadCloseClickListener onDownLoadCloseClickListener;

    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public DownLoadFragment setOnDownLoadCloseClickListener(OnDownLoadCloseClickListener onDownLoadCloseClickListener) {
        this.onDownLoadCloseClickListener = onDownLoadCloseClickListener;
        return this;
    }

    public static DownLoadFragment newInstance(String versionBeanString) {
        Bundle args = new Bundle();
        DownLoadFragment fragment = new DownLoadFragment();
        args.putString("updateBean", versionBeanString);
        fragment.setArguments(args);
        return fragment;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                int min = msg.arg1;
                int max = msg.arg2;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                String k = df.format(min * 100.00 / max) + "%";
                uodateButton.setText("进度" + k);
            }
        }
    };

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                versionBeanString = getArguments().getString("updateBean");
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_buglynew, null);
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .create();
            updateVersion = resolveUpdate(versionBeanString);
            mDownloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            initView(view);
            if (updateVersion.forceUpate == 0) {
                mAlertDialog.setCancelable(true);
                mAlertDialog.setCanceledOnTouchOutside(true);
                setCancelable(true);
            } else {
                mAlertDialog.setCancelable(false);
                mAlertDialog.setCanceledOnTouchOutside(false);
                setCancelable(false);
            }
            if (BuildConfig.DEBUG) {
                mAlertDialog.setCancelable(true);
                mAlertDialog.setCanceledOnTouchOutside(true);
                setCancelable(true);
            }


        }
        return mAlertDialog;
    }

//    @Override
//    public void onDestroy() {//错误写法 此声明周期不对
//        super.onDestroy();
//        stopQuery();
//    }

    private class QueryRunnable implements Runnable {
        @Override
        public void run() {
            queryState();
//            mHandler.postDelayed(mQueryProgressRunnable, 100);
        }
    }
    boolean isneedstop=false;
    //查询下载进度
    private void queryState() {
//        System.out.println("取消下载了");
        // 通过ID向下载管理查询下载情况，返回一个cursor
//        System.out.println("查看当前的id:" + mDownloadId);
        Cursor c = mDownloadManager.query(new DownloadManager.Query().setFilterById(mDownloadId));
        if (c == null) {
//            Toast.makeText(this, "下载失败",Toast.LENGTH_SHORT).show();
//            finish();
            System.out.println("取消下载了1");
            try {
                SpUtils.store(getActivity(), SpKey.USE_DOWN, 0L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismissTmp();
        } else { // 以下是从游标中进行信息提取
            if (!c.moveToFirst()) {
                System.out.println("查不到东西了");
//                Toast.makeText(this,"下载失败",Toast.LENGTH_SHORT).show();
//                finish();
//                System.out.println("取消下载了2");
//                try {
//                    SpUtils.store(getActivity(),SpKey.USE_DOWN,0l);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                SpUtils.store(getActivity(), SpKey.USE_DOWN, 0L);
                dismissTmp();
                if (!c.isClosed()) {
                    c.close();
                }
                return;
            }
            int mDownload_so_far = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int mDownload_all = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Message msg = Message.obtain();

            if (mDownload_all > 0) {
                msg.what = 1001;
                msg.arg1 = mDownload_so_far;
                msg.arg2 = mDownload_all;
                int min = msg.arg1;
                int max = msg.arg2;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                String k = df.format(min * 100.00 / max) + "%";
                EventBus.getDefault().post(new UpdateDownLoadInfoMsg((min * 100.00 / max)*10));
                System.out.println("下载进度" + k);

                if (mDownload_so_far == mDownload_all) {
//                    System.out.println("下载完毕:关闭掉了");
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        isneedstop=true;
                        mHandler.removeCallbacks(mQueryProgressRunnable);
                        installApk(mDownloadId);
                    } else {
//                        System.out.println("取消下载了34");
//                        mDownloadManager.remove(mDownloadId);
//                        SpUtils.store(getActivity(),SpKey.USE_DOWN,0l);
                    }
                    dismissTmp();
                } else {
                    mHandler.sendMessage(msg);
                }
            }else {
                downloadfailtime++;
                if(downloadfailtime>10){
                    System.out.println("下载异常");
                    isneedstop=true;
                    try {//携带下载链接跳转到浏览器
                        String url=updateVersion.downloadUrl;
                        if (!TextUtils.isEmpty(url) && url.contains("http")) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(url);
                            intent.setData(content_url);
                            startActivity(intent);
                            mHandler.removeCallbacks(mQueryProgressRunnable);
                            dismissTmp();
                        }

                    } catch (Exception e) {
                        //异常处理
                    }
                }
            }

            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        if(isneedstop){
            return;
        }
        mHandler.postDelayed(mQueryProgressRunnable, 1000);
    }

    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }


    /**
     * @param
     * @param
     */
    private void installApk(final long downloadId) {
        System.out.println("发起安装Fragment");
        if(!SpUtils.getValue(LibApplication.getAppContext(),"ISAPKINSTALL",false)){
            SpUtils.store(LibApplication.getAppContext(),"ISAPKINSTALL",true);
            long completeDownLoadId = downloadId;

            SpUtils.store(LibApplication.getAppContext(), SpKey.USE_DOWN, 0L);
            Uri uri;
            final Intent intentInstall = new Intent();
            intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentInstall.setAction(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                uri = mDownloadManager.getUriForDownloadedFile(completeDownLoadId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = queryDownloadedApk(LibApplication.getAppContext(), completeDownLoadId);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 以上
                uri = FileProvider.getUriForFile(LibApplication.getAppContext(),
                        "com.health.client.fileprovider",
                        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName));
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            System.out.println("发起安装:"+uri);
            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            mDownloadId=0;

            if(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).exists()){//再校验下文件是不是合法
                PackageManager pm = LibApplication.getAppContext().getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                if(info != null){
                    ApplicationInfo appInfo = info.applicationInfo;
                    String version=info.versionName;
                    if(updateVersion.version.equals(version)){
                        if(!OsUtils.isAppInBackground(LibApplication.getAppContext())){
                            LibApplication.getAppContext().startActivity(intentInstall);
                            FrameActivityManager.instance().appExit(LibApplication.getAppContext());
                            System.exit(0);
                        }
                    }
                }else {//不合法就跳浏览器下吧 受不了了
                    String url=updateVersion.downloadUrl;
                    if (!TextUtils.isEmpty(url) && url.contains("http")) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                        mHandler.removeCallbacks(mQueryProgressRunnable);
                    }
                }
            }



        }
    }
    private void installApk() {
        System.out.println("发起安装Fragment");
        if(!SpUtils.getValue(LibApplication.getAppContext(),"ISAPKINSTALL",false)){
            SpUtils.store(LibApplication.getAppContext(),"ISAPKINSTALL",true);
            SpUtils.store(LibApplication.getAppContext(), SpKey.USE_DOWN, 0L);
            Uri uri;
            final Intent intentInstall = new Intent();
            intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentInstall.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0以下
                File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName);
                uri = Uri.fromFile(apkFile);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 以上
                uri = FileProvider.getUriForFile(LibApplication.getAppContext(),
                        "com.health.client.fileprovider",
                        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName));
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            System.out.println("发起安装:"+uri);
            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            mDownloadId=0;

            if(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).exists()){//再校验下文件是不是合法
                PackageManager pm = LibApplication.getAppContext().getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                if(info != null){
                    ApplicationInfo appInfo = info.applicationInfo;
                    String version=info.versionName;
                    if(updateVersion.version.equals(version)){
                        if(!OsUtils.isAppInBackground(LibApplication.getAppContext())){
                            LibApplication.getAppContext().startActivity(intentInstall);
                            FrameActivityManager.instance().appExit(LibApplication.getAppContext());
                            System.exit(0);
                        }
                    }
                }else {//不合法就跳浏览器下吧 受不了了
                    String url=updateVersion.downloadUrl;
                    if (!TextUtils.isEmpty(url) && url.contains("http")) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                        mHandler.removeCallbacks(mQueryProgressRunnable);
                        dismissTmp();
                    }
                }
            }


        }
    }

    private void stopQuery() {
        mHandler.removeCallbacks(mQueryProgressRunnable);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        getDialog().setCancelable(true);
//        getDialog().setCanceledOnTouchOutside(true);

    }

    private void startDownLoad(final DownloadApkInfo apkInfo) {

        if (PermissionUtils.hasPermissions(getActivity(), mPermissions)) {
            //System.out.println("申请权限");


            if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).exists()) {
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).delete();
            }
            Uri uri = Uri.parse(apkInfo.downloadUrl);
            DownloadManager.Request requestApk = new DownloadManager.Request(uri);
            File dir = new File(Environment.getExternalStorageDirectory(), "mmapp");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            requestApk.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_MOBILE
                            | DownloadManager.Request.NETWORK_WIFI);
            requestApk.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mApkName);
            requestApk.setTitle("憨妈妈");      //设置下载中通知栏的提示消息
            requestApk.setDescription("憨妈妈更新下载");//设置设置下载中通知栏提示的介绍
            mDownloadId = mDownloadManager.enqueue(requestApk);
            SpUtils.store(getActivity(), SpKey.USE_DOWN, mDownloadId);
            startQuery();
            System.out.println("开始下载了" + mDownloadId);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //System.out.println("申请权限");
                requestPermissions(mPermissions, 1008);
            }
        }
    }

    private void startQuery() {
        mHandler.removeCallbacks(mQueryProgressRunnable);
        if (mDownloadId != 0) {
            mHandler.post(mQueryProgressRunnable);
        }
    }
    private void initView(View view) {
        dialogBg = (ConstraintLayout) view.findViewById(R.id.dialog_bg);
        updateTopImg = (ImageView) view.findViewById(R.id.updateTopImg);
        uodateTitle = (TextView) view.findViewById(R.id.uodateTitle);
        uodateInfo = (TextView) view.findViewById(R.id.uodateInfo);
        uodateContext = (TextView) view.findViewById(R.id.uodateContext);
        uodateButton = (TextView) view.findViewById(R.id.uodateButton);
        closeMessageDialog = (ImageView) view.findViewById(R.id.closeMessageDialog);
        closeMessageText = (TextView) view.findViewById(R.id.closeMessageText);
        uodateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionUtils.hasPermissions(LibApplication.getAppContext(), mPermissions)){
                    if(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).exists()){//文件存在 则判断下版本试试目标版本
                        PackageManager pm = LibApplication.getAppContext().getPackageManager();
                        PackageInfo info = pm.getPackageArchiveInfo(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mApkName).getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                        if(info != null){
                            ApplicationInfo appInfo = info.applicationInfo;
                            String version=info.versionName;
                            if(updateVersion.version.equals(version)){
                                installApk();
                                return;
                            }
                        }
                    }
                    mDownloadId = SpUtils.getValue(getActivity(), SpKey.USE_DOWN, 0L);
                    if (mDownloadId == 0) {
                        uodateButton.setText("下载开始中...");
                        System.out.println("开始下载了1");
                        startDownLoad(new DownloadApkInfo(updateVersion.downloadUrl));
                        if (updateVersion.forceUpate == 0) {
                            mAlertDialog.setCancelable(true);
                            mAlertDialog.setCanceledOnTouchOutside(true);
                            setCancelable(true);
                        }
                    } else {
                        startQuery();
                    }
                    Toast.makeText(LibApplication.getAppContext(),"已为您下载",Toast.LENGTH_SHORT).show();
                    dismissTmp();
                }else {
                    requestPermissions(mPermissions, 19008);
//                    Toast.makeText(LibApplication.getAppContext(),"下载,需要SD卡文件存储权限",Toast.LENGTH_LONG).show();
//                    IntentUtils.toSelfSetting(LibApplication.getAppContext());
                }


            }
        });
        uodateTitle.setText("V" + updateVersion.version + "版本，全新体验");
        uodateInfo.setText("版本" + updateVersion.version);
        uodateContext.setText(updateVersion.remark);
        if (updateVersion.forceUpate == 1) {
            closeMessageDialog.setVisibility(View.GONE);
        }
        closeMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uodateButton.getText().toString().contains("下载")) {
                    SpUtils.store(getActivity(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "推荐", true);
                }
                dismissSuper();
                if(onDownLoadCloseClickListener!=null){
                    onDownLoadCloseClickListener.onCloseClick();
                }

            }
        });
        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!uodateButton.getText().toString().contains("下载")) {
                    SpUtils.store(getActivity(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "推荐", true);
                }
                if(onDownLoadCloseClickListener!=null){
                    onDownLoadCloseClickListener.onCloseClick();
                }
            }
        });
        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!uodateButton.getText().toString().contains("下载")) {
                    SpUtils.store(getActivity(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "推荐", true);
                }
                if(onDownLoadCloseClickListener!=null){
                    onDownLoadCloseClickListener.onCloseClick();
                }
            }
        });
    }
    public interface OnDownLoadCloseClickListener{
        void onCloseClick();
    }

    public void dismissTmp() {
//        super.dismissTmp();
    }
    public void dismissSuper() {
        super.dismiss();
    }

    class DownloadApkInfo {
        public String downloadUrl;
        public String description;
        public float downloadSize;
        public String versionName;
        public int versionCode;

        public DownloadApkInfo(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }
    }
}
