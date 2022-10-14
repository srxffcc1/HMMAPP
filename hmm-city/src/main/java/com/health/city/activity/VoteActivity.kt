package com.health.city.activity

import VideoHandle.EpEditor
import VideoHandle.EpEditor.OutputOption
import VideoHandle.EpVideo
import VideoHandle.OnEditorListener
import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.health.city.R
import com.health.city.adapter.VoteCenterAdapter
import com.health.city.adapter.VoteFooterAdapter
import com.health.city.adapter.VoteHeaderAdapter
import com.healthy.library.model.ActivityModel
import com.health.city.model.VoteModel
import com.healthy.library.base.BaseActivity
import com.healthy.library.base.BaseAdapter
import com.healthy.library.dialog.CustomTipDialog
import com.healthy.library.dialog.PhotoShowDialog
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.interfaces.OnRxFFmpegListener
import com.healthy.library.model.WidgetInputModel
import com.healthy.library.routes.CityRoutes
import com.healthy.library.utils.MyRxFFmpegSubscriber
import com.healthy.library.utils.PermissionUtils
import com.healthy.library.utils.TransformUtil
import com.hss01248.dialog.StyledDialog
import com.lxj.matisse.Matisse
import com.lxj.matisse.MimeType
import com.lxj.matisse.engine.impl.GlideEngine
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import io.microshow.rxffmpeg.RxFFmpegInvoke
import kotlinx.android.synthetic.main.activity_vote.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author long
 * @description 投票/报名 界面
 * @date 2021/6/22
 */
@Route(path = CityRoutes.CITY_VOTE)
class VoteActivity : BaseActivity(), IsFitsSystemWindows,
    BaseAdapter.OnOutClickListener {

    @Autowired
    lateinit var viewType: String
    private val mPermissions = arrayOf(Manifest.permission.CAMERA)
    private val RC_IMG = 0x01
    private val RC_VIDEO = 0x02
    private val RC_PERMISSION = 0x03
    private var mClickFunction: String = ""
    private var mImageUrl: String = ""
    private var mVideoUrl: String = ""
    private var mFileCreateTime: String = ""

    var mActionCount = 0
    private var dialog: Dialog? = null
    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var mVoteHeaderAdapter: VoteHeaderAdapter
    private lateinit var mVoteCenterAdapter: VoteCenterAdapter
    private lateinit var mVoteFooterAdapter: VoteFooterAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_vote
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        buildRecycler()
        //TODO layout_vote_content 后续根据接口更换背景色
        if (viewType.toInt() == 1) {
            refresh_layout.setEnableRefresh(false)
            refresh_layout.setEnableLoadMore(false)
            refresh_layout.setEnableOverScrollDrag(true)
        }
    }

    override fun findViews() {
        super.findViews()


        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                refreshLayout.finishRefresh(1000)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                refreshLayout.finishLoadMore(1000)
                refreshLayout.finishLoadMoreWithNoMoreData()
            }
        })
    }

    /**
     * 配置recyclerView
     */
    private fun buildRecycler() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)

        rv.apply {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
        }

        //HeaderAdapter
        mVoteHeaderAdapter = VoteHeaderAdapter()
        val activityModel = ActivityModel()
        activityModel.isActivityStatus = "1"
        activityModel.votingType = 2
        mVoteHeaderAdapter.setModel(activityModel)
        mVoteHeaderAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mVoteHeaderAdapter)

        if (viewType.toInt() == 1) {
            //BodyAdapter 只有报名的时候需要使用
            mVoteCenterAdapter = VoteCenterAdapter()
            val mCenterList = mutableListOf<WidgetInputModel>()
            for (index in 0..6) {
                val widgetInputModel = WidgetInputModel().apply {
                    this.type = viewType.toInt()
                    this.title = if (0 == index) "姓名" else if (1 == index) "性别"
                    else if (2 == index) "联系方式" else if (3 == index) "身份证号"
                    else if (4 == index) "联系地址" else if (5 == index) "参赛口号" else ""
                    this.rightEditHint = if (1 == index) "请选择" else "请输入"
                    this.isEdit = 1 != index
                }
                mCenterList.add(widgetInputModel)
            }
            mVoteCenterAdapter.setData(mCenterList as ArrayList<WidgetInputModel>)
            mVoteCenterAdapter.moutClickListener = this
            delegateAdapter.addAdapter(mVoteCenterAdapter)
        }

        //FooterAdapter
        mVoteFooterAdapter = VoteFooterAdapter()
        if (viewType.toInt() == 1) {
            mVoteFooterAdapter.setModel(VoteModel().apply { this.type = viewType.toInt() });
        } else {
            val mFooterData = mutableListOf<VoteModel>()
            for (index in 0..10) {
                val voteModel = VoteModel().apply {
                    this.type = viewType.toInt()
                }
                mFooterData.add(voteModel)
            }
            mVoteFooterAdapter.setData(mFooterData as ArrayList<VoteModel>)
        }
        mVoteFooterAdapter.moutClickListener = this
        delegateAdapter.addAdapter(mVoteFooterAdapter)

    }

    override fun outClick(function: String, obj: Any) {
        /**
         * Kotlin is 相当于 Java instanceof
         * Kotlin as 类型转换 as?安全转换 当转型不成功的时候，它会返回 null。
         */
        when (function) {
            "uploadImage" -> {
                mClickFunction = function
                onAddResource()
            }
            "uploadVideo" -> {
                mClickFunction = function
                onAddResource()
            }
            "lookVideo" -> ""
            "deleteImage" -> mImageUrl = ""
            "deleteVideo" -> mVideoUrl = ""
            "editFocusChange" -> {
                val childAt = rv.getChildAt(obj.toString().toInt())
                rv.smoothScrollBy(
                    0,
                    TransformUtil.px2dp(mContext, childAt.top.toFloat()).toInt()
                )
            }
            "submit" -> {
                val mData = mVoteCenterAdapter.mDataMap
                for (item in mData) {
                    Log.e(
                        "VoteCenter",
                        "数据: key =  ${item.key} value = ${item.value}"
                    )
                }
            }
            "voteAction" -> {
                mActionCount++
                if (mActionCount % 2 == 0) {
                    CustomTipDialog.newInstance()
                        .setMessage("投票成功")
                        .setTipType(CustomTipDialog.TOAST_TYPE_SUCCESS)
                        .show(supportFragmentManager, "tipDialog")
                } else {
                    CustomTipDialog.newInstance()
                        .setMessage("您当日投票次数已用完")
                        .setTipType(CustomTipDialog.TOAST_TYPE_ERROR)
                        .show(supportFragmentManager, "tipDialog")
                }
            }
            "video" -> PhotoShowDialog.newInstance(0).show(supportFragmentManager, "videoDialog")
            "photo" -> PhotoShowDialog.newInstance(1).show(supportFragmentManager, "photoDialog")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, *mPermissions)) {
                showToast("需要同意存储权限才能选择${if (mClickFunction == "uploadImage") "图片" else "视频"}")
                PermissionUtils.requestPermissions(
                    this,
                    RC_PERMISSION,
                    *mPermissions
                )
            } else {
                onAddResource()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            data?.let { it ->
                if (requestCode == RC_IMG) {
                    if (!TextUtils.isEmpty(Matisse.obtainCaptureImageResult(it))) {//拍摄
                        mImageUrl = Matisse.obtainCaptureImageResult(it)
                    } else {
                        val filePaths = Matisse.obtainSelectPathResult(it)
                        mImageUrl = filePaths[0]
                    }
                    mVoteHeaderAdapter.setResource(mImageUrl, mVideoUrl)
                }
                if (requestCode == RC_VIDEO) {
                    if (!TextUtils.isEmpty(Matisse.obtainCaptureVideoResult(it))) {
                        mVideoUrl = Matisse.obtainCaptureVideoResult(it)
                    } else {
                        mVideoUrl = Matisse.obtainSelectPathResult(it)[0]
                    }
                    clip(mVideoUrl)
                }
            }
        }
    }

    /**
     * 添加资源
     */
    fun onAddResource() {
        if (PermissionUtils.hasPermissions(mContext, *mPermissions)) {
            Matisse.from(this)
                .choose(if (mClickFunction == "uploadImage") MimeType.ofImage() else MimeType.ofVideo())
                .capture(true)
                .countable(true)
                .maxSelectable(1)
                .imageEngine(GlideEngine())
                .theme(R.style.ImgPicker)
                .showSingleMediaType(true)
                .forResult(if (mClickFunction == "uploadImage") RC_IMG else RC_VIDEO)
        } else {
            PermissionUtils.requestPermissions(this, RC_PERMISSION, *mPermissions)
        }
    }

    private fun clip(tmpvideoUrl: String) {
        val epVideo = EpVideo(tmpvideoUrl)
        epVideo.clip(1f, 15f)
        val dir = File(Environment.getExternalStorageDirectory(), "mmvideo")

        mFileCreateTime = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
            .format(Date())

        val fileName = mFileCreateTime + "_clip.mp4"
        val outputOption = OutputOption(dir.absolutePath + "/" + fileName)
        mVideoUrl = dir.absolutePath + "/" + fileName

        EpEditor.exec(epVideo, outputOption, object : OnEditorListener {
            override fun onSuccess() {
                compressVideo(mVideoUrl)
            }

            override fun onFailure() {}
            override fun onProgress(progress: Float) {}
        })
    }

    private fun compressVideo(filePath: String) {
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    /**
                     * 视频压缩
                     * 第一个参数:视频源文件路径
                     * 第二个参数:压缩后视频保存的路径
                     */
                    val dir = File(Environment.getExternalStorageDirectory(), "mmvideo")

                    val comPressPath =
                        File(dir.absolutePath, "comp_" + File(filePath).name).absolutePath
                    val secondText =
                        "ffmpeg -y -i $filePath -crf 35 -vcodec libx264 -preset superfast $comPressPath"
                    val commands = secondText.split(" ".toRegex()).toTypedArray()

                    val myRxFFmpegSubscriber =
                        MyRxFFmpegSubscriber(this@VoteActivity, object : OnRxFFmpegListener {
                            override fun onFinish() {
                                mVoteHeaderAdapter.setResource(mImageUrl, mVideoUrl)
                                StyledDialog.dismiss(dialog)
                                dialog = null
                            }

                            override fun onProgress(progress: Int, progressTime: Long) {
                                if (dialog == null) {
                                    dialog = StyledDialog.buildLoading("视频压缩中...").show()
                                } else {
                                    StyledDialog.updateLoadingMsg("压缩进度:$progress%", dialog)
                                }
                            }

                            override fun onError(message: String?) {
                                showToast(message)
                            }
                        })
                    RxFFmpegInvoke.getInstance()
                        .runCommandRxJava(commands)
                        .subscribe(myRxFFmpegSubscriber)
                    mVideoUrl = comPressPath
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }
}