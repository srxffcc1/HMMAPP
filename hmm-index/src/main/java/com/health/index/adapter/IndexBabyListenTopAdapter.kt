//package com.health.index.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import com.alibaba.android.arouter.launcher.ARouter
//import com.alibaba.android.vlayout.LayoutHelper
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper
//import com.health.index.R
//import com.healthy.library.base.BaseAdapter
//import com.healthy.library.base.BaseHolder
//import com.healthy.library.businessutil.ListUtil
//import com.healthy.library.routes.SecondRoutes
//import com.healthy.library.routes.SoundRoutes
//import com.healthy.library.utils.ParseUtils
//import com.healthy.library.widget.CornerImageView
////import com.ximalaya.ting.android.opensdk.model.track.Track
////import com.ximalaya.ting.android.opensdk.model.track.TrackList
//
///**
// * 创建日期：2022/1/5 9:39
// * @author LiuWei
// * @version 1.0
// * 包名： com.health.index.adapter
// * 类说明：
// */
//
//class IndexBabyListenTopAdapter :
//    BaseAdapter<String>(R.layout.index_baby_listen_top_adapter_layout) {
//
//    private var records: MutableList<Track>? = null
//    private var id: String = ""
//
//    public fun setAdapterData(data: MutableList<Track>?) {
//        this.records = data
//    }
//
//    public fun setAdapterId(id: String) {
//        this.id = id
//    }
//
//    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
//        var audioLiner = holder.getView<LinearLayout>(R.id.audioLiner)
//        var baby = holder.getView<ConstraintLayout>(R.id.baby)
//        var mom = holder.getView<ConstraintLayout>(R.id.mom)
//        var listenAll = holder.getView<TextView>(R.id.listenAll)
//        var seeAll = holder.getView<TextView>(R.id.seeAll)
//        baby.setOnClickListener {
//            ARouter.getInstance()
//                .build(SoundRoutes.SOUND_MAIN)
//                .withInt("audioType", 1)
//                .navigation()
//        }
//        mom.setOnClickListener {
//            ARouter.getInstance()
//                .build(SoundRoutes.SOUND_MAIN_MON)
//                .withInt("audioType", 2)
//                .navigation()
//        }
//        seeAll.setOnClickListener {
//            ARouter.getInstance()
//                .build(SoundRoutes.SOUND_DETAIL)
//                .withInt("audioType",2)
//                .withString("id", id)
//                .navigation()
//        }
//        listenAll.setOnClickListener {
//            ARouter.getInstance()
//                .build(SoundRoutes.SOUND_DETAIL)
//                .withInt("audioType", 2)
//                .withString("id",id)
//                .navigation()
//        }
//        audioLiner.removeAllViews()
//        if (!ListUtil.isEmpty(records)) {
//            for ((index, e) in records!!.withIndex()) {
//                var view = LayoutInflater.from(context)
//                    .inflate(R.layout.item_index_baby_listen_audio_list, null)
//                var audioImg = view.findViewById<CornerImageView>(R.id.audioImg)
//                var audioTitle = view.findViewById<TextView>(R.id.audioTitle)
//                var play = view.findViewById<ImageView>(R.id.play)
//                var line = view.findViewById<View>(R.id.line)
//                com.healthy.library.businessutil.GlideCopy.with(context)
//                    .load(e.coverUrlSmall)
//
//                    .into(audioImg)
//                audioTitle.text = e.trackTitle
//
//                if (index == records!!.size - 1) {
//                    line.visibility = View.INVISIBLE
//                } else {
//                    line.visibility = View.VISIBLE
//                }
//                view.setOnClickListener {
//                    ARouter.getInstance()
//                        .build(SoundRoutes.SOUND_DETAIL)
//                        .withInt("audioType",2)
//                        .withString("id", id)
//                        .navigation()
//                }
//                audioLiner.addView(view)
//            }
//        }
//    }
//
//    override fun onCreateLayoutHelper(): LayoutHelper {
//        return LinearLayoutHelper()
//    }
//}