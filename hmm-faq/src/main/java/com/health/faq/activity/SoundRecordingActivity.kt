package com.health.faq.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.health.faq.R
import com.healthy.library.audio.VoiceManager
import com.healthy.library.base.BaseActivity
import com.healthy.library.routes.FaqRoutes
import com.healthy.library.utils.FileUtil
import com.healthy.library.utils.OldStatusBarUtil
import com.healthy.library.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_sound_recording.*

/**
 * @author xinkai.xu
 * 录音页面
 * */

@Route(path = FaqRoutes.FAQ_SOUND_RECORDING)
class SoundRecordingActivity : BaseActivity(), View.OnClickListener {
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val RC_PERMISSIONS = 810
    /*
    * 录音状态 0：初始状态 1：录音状态 2：暂停状态
    * */
    var type: Int = 0
    var mVoiceManage: VoiceManager? = null
    var voicePath: String = FileUtil.initPath()
    var isSound: Boolean = false
    override fun getLayoutId(): Int {
        return R.layout.activity_sound_recording
    }

    override fun findViews() {
        OldStatusBarUtil.immersive(this)
        FileUtil.delete(voicePath)
        multiWaveHeader.velocity = 1f * 9//设置波浪速度
        multiWaveHeader.waveHeight = 0
        multiWaveHeader.stop()
    }

    override fun init(savedInstanceState: Bundle?) {
        iv_sound.setOnClickListener(this)
        iv_close.setOnClickListener(this)
        tv_restart.setOnClickListener(this)
        tv_ok.setOnClickListener(this)

        //初始化录音
        mVoiceManage = VoiceManager.getInstance(mContext)
        mVoiceManage!!.setVoiceRecordListener(object : VoiceManager.VoiceRecordCallBack {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun recDoing(time: Long, strTime: String) {
                //录音中，返回时间
                tv_lock.text = strTime.substring(3, strTime.length)
                if (time == 60L * 5) {
                    //录制五分钟结束
                    multiWaveHeader.waveHeight = 0
                    iv_sound.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_sound_restart))
                    multiWaveHeader.stop()
                    iv_sound.isEnabled = false
                }
            }

            override fun recVoiceGrade(grade: Int) {
                //这里暂时不进行声浪控制
//              LogUtils.e("-----------------voice---------------" + grade)
//              //声音频率，根据这个调整波浪
//              multiWaveHeader.waveHeight = grade + 30
            }

            override fun recStart(init: Boolean) {

                val likeSelect = getResources().getDrawable(R.drawable.ic_sound_ok)
                tv_ok.setCompoundDrawablesWithIntrinsicBounds(null, likeSelect, null, null)
                tv_ok.setTextColor(Color.parseColor("#ff666666"))

                //开始录音
                tv_starttxt.visibility=View.INVISIBLE
                tv_ok.visibility = View.VISIBLE
                tv_restart.visibility = View.VISIBLE
                //设置波浪高度
                multiWaveHeader.waveHeight = 30
                // 图片变化
                iv_sound.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_sound_stop))
                multiWaveHeader.start()
            }

            override fun recPause(str: String) {

                val likeSelect = getResources().getDrawable(R.drawable.ic_sound_ok_g)
                tv_ok.setCompoundDrawablesWithIntrinsicBounds(null, likeSelect, null, null)
                tv_ok.setTextColor(Color.parseColor("#ff2ecdb6"))
                //暂停
                multiWaveHeader.waveHeight = 0

                iv_sound.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_sound_restart))
                multiWaveHeader.stop()
            }


            override fun recFinish(length: Long, strLength: String, path: String) {
                //录音结束，文件地址，这里做上传操作
                var intent = Intent()
                var bundle = Bundle()
                bundle.putCharSequence("path", path)
                intent.putExtras(bundle)
                setResult(Activity.RESULT_OK, intent)
                finish()


            }
        })

    }

    override fun onClick(v: View?) {
        when (v) {
            //录音按钮
            iv_sound -> {
                when (type) {
                    0 -> {
                        //开始录音.设置录音位置
//                        mVoiceManage?.startVoiceRecord();//暂停或继续
                        //先判断是否有权限
                        if (PermissionUtils.hasPermissions(this, *permissions)) {
                            mVoiceManage?.startVoiceRecord(voicePath)
                            type = 1
                            isSound = true
                        } else {
                            AlertDialog.Builder(this)
                                    .setTitle("权限提示")
                                    .setMessage("为了您正常使用APP，请同意以下权限：录音权限")
                                    .setPositiveButton("确定", null)
                                    .setOnDismissListener {
                                        PermissionUtils.requestPermissions(this, RC_PERMISSIONS,
                                                *permissions)
                                    }.create().show()
                        }
                    }
                    1 -> {
                        //暂停录制
                        mVoiceManage?.pauseOrStartVoiceRecord()//暂停或继续
                        type = 2
                    }
                    2 -> {
                        //重新开始录制
                        mVoiceManage?.pauseOrStartVoiceRecord()//暂停或继续
                        type = 1
                    }
                }
            }
            //关闭页面
            iv_close -> {
                //关闭录音
                if (isSound) {
                    isSound = false
                    mVoiceManage?.stopVoiceRecord()
                }
                //关闭页面
                finish()
            }
            //重新录制
            tv_restart -> {
                if (PermissionUtils.hasPermissions(this, *permissions)) {
                    iv_sound.isEnabled = true
                    mVoiceManage?.startVoiceRecord(voicePath)
                    type = 1
                    isSound = true
                } else {
                    AlertDialog.Builder(this)
                            .setTitle("权限提示")
                            .setMessage("为了您正常使用APP，请同意以下权限：录音权限")
                            .setPositiveButton("确定", null)
                            .setOnDismissListener {
                                PermissionUtils.requestPermissions(this, RC_PERMISSIONS,
                                        *permissions)
                            }.create().show()
                }
            }
            //完成按钮
            tv_ok -> {
                //停止录音
                if (tv_lock.text == "00:00") {
                    Toast.makeText(mContext, "时间过短", Toast.LENGTH_SHORT).show();
                }
                multiWaveHeader.waveHeight = 0
                iv_sound.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_sound_restart))
                multiWaveHeader.stop()
                isSound = false
                mVoiceManage?.stopVoiceRecord()

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSIONS) {
            mVoiceManage?.startVoiceRecord(voicePath)
            type = 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //停止录音，销毁录音
        multiWaveHeader.stop()
        if (isSound) {
            isSound=false
            mVoiceManage?.stopVoiceRecord()
        }
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        //暂停录制
        if (isSound) {
            mVoiceManage?.pauseOrStartVoiceRecord()//暂停或继续
            type = 2
        }

    }

}