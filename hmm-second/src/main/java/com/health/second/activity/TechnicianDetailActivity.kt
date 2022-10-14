package com.health.second.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.lib_ShapeView.view.ShapeTextView
import com.health.second.R
import com.health.second.contract.ShopDetailContract
import com.health.second.model.PeopleListModel
import com.health.second.presenter.ShopDetailPresenter
import com.healthy.library.banner.ScaleInTransformer
import com.healthy.library.base.BaseActivity
import com.healthy.library.businessutil.GlideCopy
import com.healthy.library.constant.UrlKeys
import com.healthy.library.interfaces.IsFitsSystemWindows
import com.healthy.library.interfaces.IsNeedShare
import com.healthy.library.model.*
import com.healthy.library.routes.SecondRoutes
import com.healthy.library.utils.DrawableUtils
import com.healthy.library.utils.SpUtils
import com.healthy.library.utils.TransformUtil
import com.healthy.library.widget.CornerImageView
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_technician_detail.*
import java.util.HashMap

@Route(path = SecondRoutes.SECOND_TECHNICIAN_DETAIL)
class TechnicianDetailActivity : BaseActivity(), IsFitsSystemWindows, IsNeedShare,
    ShopDetailContract.View {

    @Autowired
    @JvmField
    var shopId: String = ""

    @Autowired
    @JvmField
    var pos: Int = 0

    private var shopDetailPresenter: ShopDetailPresenter? = null
    private var cardAdapter: CardAdapter? = null
    private val mMap = mutableMapOf<String, Any>()
    private var userId: String? = null
    private var surl: String? = null
    private var sdes: String? = null
    private var stitle: String? = null
    private var sBitmap: Bitmap? = null
    private var list: List<PeopleListModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        shopDetailPresenter = ShopDetailPresenter(this, this)
        cardAdapter = CardAdapter()
        getData()
    }

    override fun getData() {
        super.getData()
        mMap.clear()
        mMap["shopId"] = shopId
        mMap["currentPage"] = "1"
        mMap["pageSize"] = "0"
        shopDetailPresenter?.getPeopleList(mMap)
    }

    override fun onGetStoreDetailSuccess(storeDetailModel: ShopDetailModel?) {
        TODO("Not yet implemented")
    }

    override fun onGetPeopleListSuccess(result: MutableList<PeopleListModel>?) {
        if (result != null && result.size > 0) {
            showContent()
            for (index in result.indices) {
                shopDetailPresenter!!.getManDetail(result[index].id, result[index])
            }
            list = result
            banner.setAdapter(cardAdapter, pos)
            cardAdapter!!.setData(result)
            cardAdapter!!.notifyDataSetChanged()
        } else {
            showEmpty()
        }
    }

    override fun onGetGoodsListSuccess(
        list: MutableList<SortGoodsListModel>?,
        pageInfo: OrderListPageInfo?
    ) {
        TODO("Not yet implemented")
    }


    override fun onGetMarketingSuccess(list: MutableList<ShopDetailMarketing>?) {
        TODO("Not yet implemented")
    }

    override fun onSuccessManDetail(result: TechnicianResult?) {
        cardAdapter!!.notifyDataSetChanged()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_technician_detail
    }

    override fun findViews() {
        super.findViews()
        backImg.setOnClickListener { finish() }
        banner.setPageMargin(
            TransformUtil.dp2px(mContext, 10f).toInt(),
            TransformUtil.dp2px(mContext, 10f).toInt()
        )
            .setAutoPlay(false)
            .addPageTransformer(ScaleInTransformer())
            .setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (list != null) {
                        userId = list!![position].id
                        GlideCopy.with(mContext).load(list!![position].faceUrl)
                            .into(object : SimpleTarget<Drawable?>() {
                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable?>?
                                ) {
                                    sBitmap = DrawableUtils.drawableToBitmap(resource)
                                }
                            })
                    }
                }
            })
        shareImg.setOnClickListener {
            buildDes()
            showShare()
        }
    }

    internal class CardAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
        private var items: List<PeopleListModel>? = null
        fun setData(items: List<PeopleListModel>?) {
            this.items = items
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_technician_detail_layout, parent, false)
            return CardViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val cardViewHolder = holder as CardViewHolder
            val model: PeopleListModel = items!!.get(position)
            GlideCopy.with()
                .load(model.faceUrl)
                .error(R.drawable.img_1_1_default)

                .into(cardViewHolder.userImg)
            holder.userName.text = model.realName
            try {
                holder.peopleTag.text = model?.technicianResult?.tagTechnician!!.split(",")[0]
            } catch (e: Exception) {
            }
            try {
                if (model.technicianResult.profession!=null) {
                    holder.userTag.visibility = View.VISIBLE
                        holder.userTag.text = model.technicianResult.profession
                } else {
                    holder.userTag.visibility = View.GONE
                }
            } catch (e: Exception) {
            }
            holder.goodWork.text = ""
            holder.goodWorkContent.text = ""
            holder.userInfo.text = ""
            holder.userInfoContent.text = ""
            holder.healthTitle.text = ""
            if (model.technicianResult != null) {
                if (model.technicianResult.beGood != null) {
                    holder.goodWork.text = "擅长"
                    holder.goodWorkContent.text =
                        Html.fromHtml(model.technicianResult.beGood).toString().replace("\n", "")
                } else {
                    holder.goodWork.visibility = View.GONE
                    holder.goodWorkContent.visibility = View.GONE
                }
                if (model.technicianResult.remark != null) {
                    holder.userInfo.text = "个人介绍"
                    holder.userInfoContent.text =
                        Html.fromHtml(model.technicianResult.remark).toString().replace("\n", "")
                } else {
                    holder.userInfo.visibility = View.GONE
                    holder.userInfoContent.visibility = View.GONE
                }
                if (model.technicianResult.healthCertificateUrl != null) {
                    holder.healthTitle.text = "健康证展示"
                    GlideCopy.with()
                        .load(model.technicianResult.healthCertificateUrl)
                        .error(R.drawable.img_1_1_default)

                        .into(holder.healthImg)
                } else {
                    holder.healthTitle.visibility = View.GONE
                    holder.healthImg.visibility = View.GONE
                }
            }
        }

        override fun getItemCount(): Int {
            return if (items != null) items!!.size else 0
        }
    }

    internal class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImg: CornerImageView = itemView.findViewById(R.id.userImg)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val technicianIcon: ImageView = itemView.findViewById(R.id.technician_icon)
        val peopleTag: TextView = itemView.findViewById(R.id.peopleTag)
        val userTag: ShapeTextView = itemView.findViewById(R.id.userTag)
        val goodWork: TextView = itemView.findViewById(R.id.goodWork)
        val goodWorkContent: TextView = itemView.findViewById(R.id.goodWorkContent)
        val userInfo: TextView = itemView.findViewById(R.id.userInfo)
        val userInfoContent: TextView = itemView.findViewById(R.id.userInfoContent)
        val healthTitle: TextView = itemView.findViewById(R.id.healthTitle)
        val healthImg: ImageView = itemView.findViewById(R.id.healthImg)

    }

    override fun getSurl(): String {
        return surl!!
    }

    override fun getSdes(): String {
        return sdes!!
    }

    override fun getStitle(): String {
        return stitle!!
    }

    override fun getsBitmap(): Bitmap? {
        return null
    }

    private fun buildDes() {
        val nokmap: MutableMap<String, String> = HashMap<String, String>()
        nokmap["soure"] = "服务人员详情页分享量"
        MobclickAgent.onEvent(mContext, "event2APPPeopleDetialShareClick", nokmap)

        val urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_serviceStaff)
        val url = String.format("%s?id=%s&shopid=%s", urlPrefix, userId, shopId)
        surl = url
        stitle = "这个老师不错哟，推荐给你看看"
        sdes = " "
    }
}