package com.healthy.library.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.healthy.library.R
import com.healthy.library.base.BaseFragment
import com.healthy.library.constant.SpKey
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayConfig
import com.tencent.rtmp.TXLivePlayer
import kotlinx.android.synthetic.main.fragment_tencent_video_layout.*


/**
 * @author long
 * @description
 * @date 2021/6/23
 */
class TencentVideoFragment : BaseFragment() {

    private val TAG = this@TencentVideoFragment.javaClass.simpleName

    /** 初始化播放器 */
    private lateinit var mLivePlayer: TXLivePlayer

    /** 多播放地址情况处理 */
    private var videoUrls: MutableList<String>? = null

    /** 原始传入的url地址 */
    private var videoUrl: String = ""

    /** 当前播放position 避免多个url情况 顺序播放 **/
    private var playerPosition: Int = 0

    /** 视频总时长  秒数 */
    private var totalSecond = 0

    /** 当前播放时长  秒数 */
    private var currentSecond = 0

    /** 是否拖动进度条 */
    private var isSetSeekBar = false

    /** 播放类型 */
    private var mPlayType: Int = TXLivePlayer.PLAY_TYPE_VOD_FLV

    /** 是否自动播放 */
    private var isAutoPlay = true

    companion object {
        /**
         * isDialogStyle （是否已弹框样式显示）可以不传默认false整个页面Fragment展示
         * 必传字段
         * url (视频地址）
         */
        fun newInstance(maporg: Map<String, Any>): TencentVideoFragment {
            val fragment = TencentVideoFragment()
            val args = Bundle()
            bundleMap(maporg, args)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tencent_video_layout
    }

    override fun onResume() {
        if (::mLivePlayer.isInitialized) {
            if (!isFirstLoad && totalSecond > 0) {
                setViewStatus()
            }
            //恢复播放
            //mLivePlayer.resume()
        }
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::mLivePlayer.isInitialized) {
            //处于播放状态在进行切换状态
            if (mLivePlayer.isPlaying) {
                setViewStatus()
            }
            //暂停播放 ( 如果有需求运行后台播放 则删除 onPause/onResume 配置即可)
            //mLivePlayer.pause()
        }
    }

    override fun findViews() {
        val isDialogStyle = basemap["isDialogStyle"].toString().toBoolean()
        isAutoPlay = basemap["isAutoPlay"].toString().toBoolean()
        if (isDialogStyle) {
            val layoutParams = bottomPlayerLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.apply {
                leftMargin = TransformUtil.dp2px(mContext, 20f).toInt()
                rightMargin = TransformUtil.dp2px(mContext, 20f).toInt()
                bottomMargin = TransformUtil.dp2px(mContext, 10f).toInt()
                bottomPlayerLayout.layoutParams = this
            }
        }

        initPlayer()
        initListener()
    }

    private fun initListener() {

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //进度发生改变时会触发
                seekBar!!.progress = seekBar.progress
                startTime.text = FormatRunTime(seekBar.progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //放开SeekBar时触发
                mLivePlayer.seek(seekBar!!.progress)
                mLivePlayer.resumeLive() //恢复播放
            }

        })

        /** 视频播放器 */
        video_view.setOnClickListener { onClick() }

        /** 屏幕中间 播放/暂停按钮 */
        livePlayerImg.setOnClickListener { onClick() }

        /** 进度条 播放/暂停按钮 */
        livePlayer.setOnClickListener { onClick() }
    }

    /**
     * 统一点击事件方法处理
     */
    fun onClick() {
        if (/*!isAutoPlay && */totalSecond == 0) {
            setProgressStyle(true)
            mLivePlayer.startPlay(videoUrl, mPlayType)
        }
        setViewStatus()
    }

    /**
     * 初始化播放器
     */
    private fun initPlayer() {
        videoUrl = basemap["url"].toString()
        //创建 player 对象
        mLivePlayer = TXLivePlayer(mContext)
        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(video_view)
        val mPlayConfig = TXLivePlayConfig()
        //自动模式
        mPlayConfig.setAutoAdjustCacheTime(true)
        mPlayConfig.setMinAutoAdjustCacheTime(1f)
        mPlayConfig.setMaxAutoAdjustCacheTime(5f)
        mLivePlayer.setConfig(mPlayConfig)

        if (videoUrl.contains(",")) {
            videoUrls = videoUrl.split(",") as MutableList
            videoUrl = videoUrls!![playerPosition]
        }

        /** 判断视频后缀格式 */
        setPlayType(isAutoPlay)

        // 设置填充模式
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION)
        // 设置画面渲染方向
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT)
        //Android 示例代码
        mLivePlayer.setPlayListener(object : ITXLivePlayListener {
            override fun onPlayEvent(event: Int, param: Bundle) {
                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                }
                if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    Log.e(TAG, "视频播放开始")
                    progressBar?.let {
                        it.visibility = View.GONE
                    }
                    progressTip?.let {
                        it.visibility = View.GONE
                    }
                    isFirstLoad = false
                }
                if (event == TXLiveConstants.PLAY_EVT_GET_PLAYINFO_SUCC) {
                    Log.e(TAG, "获取点播文件信息成功")
                }
                if (event == TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND) {
                    Log.e(TAG, "播放文件不存在")
                }
                if (event == TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL) {
                    Log.e(TAG, "H265 解码失败")
                }
                if (event == TXLiveConstants.PLAY_ERR_HLS_KEY) {
                    Log.e(TAG, "HLS 解码 key 获取失败")
                }
                if (event == TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL) {
                    Log.e(TAG, "获取点播文件信息失败")
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    //视频播放进入缓冲状态，缓冲结束之后会有 PLAY_BEGIN 事件
                    setProgressStyle(true, "努力加载中···")
                }
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {

                }

                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    val str2 = param.getInt("EVT_MSG")
                    val str3 = param.getInt("EVT_PLAY_PROGRESS_MS")
                    val str4 = param.getInt("EVT_PLAYABLE_DURATION_MS")
                    val str5 = param.getInt("EVT_PLAY_DURATION")
                    val str7 = param.getInt("EVT_PLAYABLE_DURATION")
                    totalSecond = param.getInt("EVT_PLAY_DURATION_MS") //视频总时长  秒数
                    currentSecond = param.getInt("EVT_PLAY_PROGRESS") //当前播放进度  秒数
                    Log.e(TAG, "视频总时长：$totalSecond")
                    Log.e(TAG, "当前播放到：$currentSecond")
                    setProgress()
                }
                if (event == TXLiveConstants.PLAY_WARNING_SEVER_CONN_FAIL) {
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEADDRESS, null)
                    SpUtils.store(mContext, SpKey.LIVETMPCOURSEID, null)
                    showToast("服务器连接失败")
                    Log.e(TAG, "服务器连接失败（仅播放 RTMP:// 地址时会抛送）")
                }
                if (event == TXLiveConstants.PLAY_WARNING_RECONNECT) {

                }
            }

            override fun onNetStatus(status: Bundle) {}
        })
    }

    /**
     * 判断后缀调整对应的播放格式
     * @param isNext 是否播放下一个视频
     */
    private fun setPlayType(isNext: Boolean) {
        val split = videoUrl.split(".")
        split?.let {
            mPlayType = when (it[it.size - 1]) {
                "hls" -> TXLivePlayer.PLAY_TYPE_VOD_HLS
                "mp4" -> TXLivePlayer.PLAY_TYPE_VOD_MP4
                "flv" -> TXLivePlayer.PLAY_TYPE_VOD_FLV
                "rtmp" -> TXLivePlayer.PLAY_TYPE_LIVE_RTMP
                else -> TXLivePlayer.PLAY_TYPE_VOD_FLV
            }

            //非自动播放 暂停且切换状态
            if (isAutoPlay) {
                mLivePlayer.startPlay(videoUrl, mPlayType)
            } else {
                if (isNext) {
                    mLivePlayer.startPlay(videoUrl, mPlayType)
                    return
                }
                setProgressStyle(false)
                livePlayer.setImageDrawable(resources.getDrawable(R.drawable.live_pause))
                livePlayerImg.setImageDrawable(resources.getDrawable(R.drawable.live_player_stop))
                livePlayerImg.visibility = View.VISIBLE
            }
        }
    }

    /**
     *  切换 view播放/暂停状态及样式
     */
    private fun setViewStatus() {
        livePlayerImg.visibility = if (mLivePlayer.isPlaying) View.VISIBLE else View.GONE

        if (mLivePlayer.isPlaying) { //正在播放
            livePlayer.setImageDrawable(resources.getDrawable(R.drawable.live_pause))
            livePlayerImg.setImageDrawable(resources.getDrawable(R.drawable.live_player_stop))
            mLivePlayer.pause()
        } else {
            livePlayer.setImageDrawable(resources.getDrawable(R.drawable.live_plear))
            livePlayerImg.setImageDrawable(resources.getDrawable(R.drawable.live_player_play))
            mLivePlayer.resume()
        }
        setProgressStyle(progressBar.visibility == View.VISIBLE)
    }

    private fun setProgressStyle(isShowProgress: Boolean, tipMessage: String = "努力加载中···") {
        progressBar.visibility = if (isShowProgress) View.VISIBLE else View.GONE
        progressTip.visibility = if (isShowProgress) View.VISIBLE else View.GONE
        progressTip.text = tipMessage
    }

    /**
     * 计算进度
     */
    private fun setProgress() {
        if (!isSetSeekBar) {
            seekBar.max = totalSecond / 1000
            endTime.text = FormatRunTime((totalSecond / 1000).toLong())
            isSetSeekBar = true
        }
        seekBar.progress = currentSecond
        startTime.text = FormatRunTime(currentSecond.toLong())
        if (currentSecond == totalSecond / 1000) { //说明进度条走完了
            mLivePlayer.pause()
            playerPosition++;
            if ((videoUrls != null && videoUrls?.isNotEmpty()!!) && playerPosition < videoUrls!!.size) {
                //视频总时长重置
                totalSecond = 0;
                setProgressStyle(true, "正在准备下一段视频···")
                mLivePlayer.stopPlay(true)
                isSetSeekBar = false
                videoUrl = videoUrls!![playerPosition]
                // 支持直接播放下一段视频 (因为没有选集的功能)
                setPlayType(true)
                mLivePlayer.prepareLiveSeek(videoUrls!![playerPosition], 0)
            } else {
                //没有多地址情况
                mLivePlayer.pause()
                //视频总时长重置
                totalSecond = 0;
                livePlayer.setImageDrawable(resources.getDrawable(R.drawable.live_pause))
                livePlayerImg.setImageDrawable(resources.getDrawable(R.drawable.live_player_stop))
                livePlayerImg.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 时间计算
     */
    fun FormatRunTime(runTime: Long): String {
        if (runTime <= 0) {
            return "00:00"
        }
        var hour: Long = 0
        var minute: Long = 0
        if (runTime >= 3600) {
            hour = runTime / 3600
        }
        if (runTime >= 60) {
            minute = runTime % 3600 / 60
        }
        val second = runTime % 60
        return if (hour <= 0) {
            unitTimeFormat(minute) + ":" +
                    unitTimeFormat(second)
        } else {
            unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
                    unitTimeFormat(second)
        }
    }

    private fun unitTimeFormat(number: Long): String {
        return String.format("%02d", number)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLivePlayer.pause()//暂停
        mLivePlayer.stopPlay(true) // true 代表清除最后一帧画面
        video_view.onDestroy()
    }

}