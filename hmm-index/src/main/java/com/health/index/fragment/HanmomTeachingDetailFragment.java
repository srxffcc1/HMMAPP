package com.health.index.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.index.R;
import com.health.index.adapter.VideoIntroduceAdapter;
import com.health.index.contract.HanMomVideoContract;
import com.healthy.library.model.VideoListModel;
import com.health.index.presenter.HanMomVideoPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.utils.JsBridge;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.WebViewSettingOld;
import com.healthy.library.utils.WebViewSettingPost;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;


public class HanmomTeachingDetailFragment extends BaseFragment implements HanMomVideoContract.View {


    private String id;
    private String mParam2;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;


    private HanMomVideoPresenter hanMomVideoPresenter;
    private VideoIntroduceAdapter videoIntroduceAdapter;
    private int needChangeTab = 0;

    public HanmomTeachingDetailFragment() {

    }

    public static HanmomTeachingDetailFragment newInstance(String id, String param2) {
        HanmomTeachingDetailFragment fragment = new HanmomTeachingDetailFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hanmom_teaching_detail;
    }

    @Override
    protected void findViews() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);

        layoutRefresh.setEnableRefresh(false);
        layoutRefresh.setEnableLoadMore(false);
        hanMomVideoPresenter = new HanMomVideoPresenter(mContext, this);
        videoIntroduceAdapter = new VideoIntroduceAdapter();
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerNews.setAdapter(videoIntroduceAdapter);
        getData();
//        recyclerNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollVertically(1)) {
//                    needChangeTab++;
//                    if (needChangeTab > 1 && newState == recyclerView.SCROLL_STATE_IDLE) {
//                        needChangeTab = 0;
//                        EventBus.getDefault().post(new CommentEvent(2));
//                    }
//                }
//            }
//        });
    }

    @Override
    public void getData() {
        super.getData();
        hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", id)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 1);
    }

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {

    }

    private void addViewHead(String videoIntroduce) {
        videoIntroduceAdapter.removeAllHeaderView();
        View view = getLayoutInflater().inflate(R.layout.video_introduce_webview, null);
        final WebView tipContent = view.findViewById(R.id.videoContent);
        String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />" +
                "<style>img{max-width:100% !important;height:auto !important;}</style>"
                + "<style>body{max-width:100% !important;}</style>" + "</head><body>";
        tipContent.loadDataWithBaseURL(null,
                sHead + videoIntroduce + "</body></html>", "text/html", "utf-8", null);
        tipContent.setWebViewClient(new ResizeImgWebviewClient());
        tipContent.addJavascriptInterface(new JsBridge(), "JsBridge");
        WebViewSettingOld.setWebViewParam(tipContent, mContext);
        videoIntroduceAdapter.addHeaderView(view);
        videoIntroduceAdapter.notifyDataSetChanged();
    }

    private void addNullViewHead() {
        videoIntroduceAdapter.removeAllHeaderView();
        View view = getLayoutInflater().inflate(R.layout.index_mon_empty, null);
        videoIntroduceAdapter.addHeaderView(view);
        videoIntroduceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetVideoDetailSuccess(VideoListModel result, int type) {
        if (result != null) {
            if (result.videoIntroduce != null && !TextUtils.isEmpty(result.videoIntroduce)) {
                showContent();
                addViewHead(result.videoIntroduce);
            } else {
                addNullViewHead();
            }
        } else {
            addNullViewHead();
        }
    }

    public class ResizeImgWebviewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            resizeImg(view);
            addImgClickEvent(view);

        }

        /**
         * 添加图片点击事件
         *
         * @param view
         */
        private void addImgClickEvent(WebView view) {
            view.loadUrl("javascript:(function(){ "
                    + "var objs = document.getElementsByTagName('img');"
                    + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ "
                    + "array[j]=objs[j].src;" + " }  "
                    + "for(var i=0;i<objs.length;i++){"
                    + "objs[i].i=i;"
                    + "objs[i].onclick=function(){  window.JsBridge.openImage(this.src,array,this.i);" + "}  " + "}    })()");

        }

        /**
         * 重新调整图片宽高
         *
         * @param view
         */
        private void resizeImg(WebView view) {

        }


    }

    @Override
    public void onAddPraiseSuccess(String result, int type) {

    }
}