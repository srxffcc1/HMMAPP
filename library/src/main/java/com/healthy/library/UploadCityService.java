package com.healthy.library;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
@Deprecated
public class UploadCityService extends Service {
    private Binder mBinder = new IUploadAidlInterface.Stub(){

        @Override
        public void progressCreate(String json) throws RemoteException {

        }

        @Override
        public void progressStart() throws RemoteException {

        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public UploadCityService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "---->远程服务启动等待开始任务");

    }
}
