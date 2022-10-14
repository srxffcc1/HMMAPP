package com.health.index.fragment

import android.os.Bundle
import android.util.Base64
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.health.index.R
import com.healthy.library.base.BaseFragment
import com.healthy.library.constant.SpKey
import com.healthy.library.constant.UrlKeys
import com.healthy.library.routes.IndexRoutes
import com.healthy.library.routes.SoundRoutes
import com.healthy.library.utils.SpUtils
import kotlinx.android.synthetic.main.fragment_index_baby.*


class IndexBabyFragment : BaseFragment() {

    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString("type")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_index_baby
    }

    override fun findViews() {
        when (type) {
            "0" -> {//备孕
                view1.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_beiyun_bg1))
                view2.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_beiyun_bg2))
                view3.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_beiyun_bg3))
                view4.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_beiyun_bg4))
                title1.text = "孕育百科"
                title2.text = "专家咨询"
                title3.text = "心理咨询"
                title4.text = "备孕说法"
                content1.text = "科学备孕必备知识"
                content2.text = "3分钟在线极速问诊"
                content3.text = "家庭指导在线沟通"
                content4.text = "备孕攻略小秘诀"
                view1.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS)
                        .withInt("currentIndex", 1)
                        .navigation()
                }
                view2.setOnClickListener {
                    var url =
                        "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick"
                    url = String.format(
                        url!!,
                        String(
                            Base64.decode(
                                SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                                Base64.DEFAULT
                            )
                        )
                    )
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef", true)
                        .withString("title", "极速问诊")
                        .withBoolean("isinhome", false)
                        .withBoolean("doctorshop", true)
                        .withString("url", url)
                        .navigation()
                }
                view3.setOnClickListener {
                    var url =
                        "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/MentalityList01?dietitianGoodLabelType=7"
                    url = String.format(
                        url!!,
                        String(
                            Base64.decode(
                                SpUtils.getValue(mContext, SpKey.USER_ID).toByteArray(),
                                Base64.DEFAULT
                            )
                        )
                    )
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withBoolean("isNeedRef", true)
                        .withString("title", "心理咨询")
                        .withBoolean("doctorshop", true)
                        .withBoolean("isinhome", false)
                        .withString("url", url)
                        .navigation()
                }
                view4.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                        .withString("type", "0")
                        .navigation()
                }
            }
            "1" -> {//孕期
                view1.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_xue_bg1))
                view2.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_xue_bg3))
                view3.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_xue_bg4))
                view4.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_xue_bg2))
                title1.text = "能不能吃"
                title4.text = "胎教音乐"
                title2.text = "孕期食谱"
                title3.text = "产检助手"
                content1.text = "避开饮食黑名单"
                content4.text = "享受与宝宝的交流时光"
                content2.text = "满足你的孕期味蕾"
                content3.text = "产检指南小助手"
                view4.visibility=View.INVISIBLE
                view1.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_CANEAT)
                        .navigation()
                }
                view2.setOnClickListener {
                    ARouter.getInstance()
                        .build(SoundRoutes.SOUND_MAIN)
                        .withString("audioType", "1")
                        .withString("choseTypeName", "胎教")
                        .navigation()
                }
                view3.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_FOOD)
                        .withString("activityType", "孕期食谱")
                        .navigation()
                }
                view4.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_BIRTH_CHECK_LIST)
                        .withInt("id", -1)
                        .navigation()
                }
            }
            "2" -> {//育儿
                view4.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_yuer_bg1))
                view1.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_yuer_bg2))
                view2.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_yuer_bg3))
                view3.setBackgroundDrawable(resources.getDrawable(R.drawable.index_baby_yuer_bg4))
                title4.text = "儿歌故事"
                title1.text = "疫苗助手"
                title2.text = "月子餐"
                title3.text = "宝宝辅食"
                content4.text = "新手爸妈的哄睡宝典"
                content1.text = "制定宝宝疫苗计划"
                content2.text = "助你身体快速恢复"
                content3.text = "分月龄辅食指导"
                view4.visibility=View.INVISIBLE
                view1.setOnClickListener {
                    ARouter.getInstance()
                        .build(SoundRoutes.SOUND_MAIN)
                        .withInt("audioType", 1)
                        .withInt("currentIndex", 2)
                        .navigation()
                }
                view2.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_VACC_CHECK)
                        .withInt("status", SpUtils.getValue(mContext, SpKey.STATUS).toInt())
                        .navigation()
                }
                view3.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_FOOD)
                        .withString("activityType", "月子食谱")
                        .navigation()
                }
                view4.setOnClickListener {
                    ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_FOOD)
                        .withString("activityType", "宝宝辅食")
                        .navigation()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            IndexBabyFragment().apply {
                arguments = Bundle().apply {
                    putString("type", param1)
                }
            }
    }
}