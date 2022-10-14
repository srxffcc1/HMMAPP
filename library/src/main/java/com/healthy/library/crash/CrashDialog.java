package com.healthy.library.crash;

/**
 *  @author x024
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.healthy.library.LibApplication;
import com.healthy.library.R;

public class CrashDialog extends Activity {

	private String mFilePath;
	private Button btnExit, btnRestart;
	private Boolean StorageState = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crash);
		CrashDialog.this.setFinishOnTouchOutside(false);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mFilePath = Environment.getExternalStorageDirectory() + "/" + LibApplication.ERROR_FILENAME;
			StorageState = true;
		} else {
			Log.i(LibApplication.TAG, "哦豁，说好的SD呢...");
		}

		new Thread(upLog).start();
		initView();
	}

	private void initView() {
		btnExit = (Button) findViewById(R.id.cash_exit);
		btnRestart = (Button) findViewById(R.id.cash_restart);

		btnExit.setOnClickListener(mOnClick);
		btnRestart.setOnClickListener(mOnClick);

	}

	OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.cash_exit) {
				exit();
			} else if (id == R.id.cash_restart) {
				restart();
			}
		}
	};

	// 上传错误信息
	Runnable upLog = new Runnable() {
		@Override
		public void run() {
			try {

				String Mobile = Build.MODEL;
				String maxMemory = "" + getmem_TOLAL() / 1024 + "m";
				String nowMemory = "" + getmem_UNUSED(CrashDialog.this) / 1024 + "m";
				String eMessage = "未获取到错误信息";
				if (StorageState) {
					eMessage = FileTxt.ReadTxt(mFilePath).replace("'", "");
				}
				Log.i(LibApplication.TAG, "Mobile:" + Mobile + " | maxMemory:" + maxMemory + " |nowMemory:" + nowMemory
						+ " |eMessage:" + eMessage);

				/**
				 * 可以在这调你自己的接口上传信息
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void exit() {
//		FileTxt.deleteFile(mFilePath);
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private void restart() {
		Intent intent = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		exit();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		exit();
	}

	// 获取可用内存
	public static long getmem_UNUSED(Context mContext) {
		long MEM_UNUSED;
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);

		MEM_UNUSED = mi.availMem / 1024;
		return MEM_UNUSED;
	}

	// 获取剩余内存
	public static long getmem_TOLAL() {
		long mTotal;
		String path = "/proc/meminfo";
		String content = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path), 8);
			String line;
			if ((line = br.readLine()) != null) {
				content = line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		int begin = content.indexOf(':');
		int end = content.indexOf('k');

		content = content.substring(begin + 1, end).trim();
		mTotal = Integer.parseInt(content);
		return mTotal;
	}

}
