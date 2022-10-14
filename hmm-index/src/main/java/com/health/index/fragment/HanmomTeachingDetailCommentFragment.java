package com.health.index.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.index.R;
import com.health.index.adapter.IndexTabEmptyAdapter;
import com.healthy.library.adapter.VideoCommentListAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.CommentDialog;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.HanMomCommentContract;
import com.healthy.library.message.CommentEvent;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCommentModel;
import com.healthy.library.presenter.HanMomCommentPresenter;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class HanmomTeachingDetailCommentFragment extends BaseFragment implements HanMomCommentContract.View,
        OnLoadMoreListener, BaseAdapter.OnOutClickListener, CommentDialog.OnDiscussDialogDismissListener {


    private String id;
    private String mParam2;

    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recycler;
    private VideoCommentListAdapter videoCommentListAdapter;
    private HanMomCommentPresenter hanMomCommentPresenter;
    private int pageNum = 1;
    private CommentDialog commentDialog;
    private VideoCommentModel videoCommentModel;//被评论的评论实体
    private VideoCommentModel.ReplyListBean replyListBean;//被评论的评论实体
    private IndexTabEmptyAdapter indexTabEmptyAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    public HanmomTeachingDetailCommentFragment() {

    }


    public static HanmomTeachingDetailCommentFragment newInstance(String id, String param2) {
        HanmomTeachingDetailCommentFragment fragment = new HanmomTeachingDetailCommentFragment();
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
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hanmom_teaching_detail_comment;
    }

    @Override
    protected void findViews() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        hanMomCommentPresenter = new HanMomCommentPresenter(getContext(), this);
        initView();
        getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        layoutRefresh.setEnableRefresh(false);
        layoutRefresh.setOnLoadMoreListener(this);

        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);

        videoCommentListAdapter = new VideoCommentListAdapter();
        videoCommentListAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(videoCommentListAdapter);

        indexTabEmptyAdapter = new IndexTabEmptyAdapter();
        delegateAdapter.addAdapter(indexTabEmptyAdapter);
    }

    @Override
    public void getData() {
        super.getData();
        hanMomCommentPresenter.getCommentList(new SimpleHashMapBuilder<String, Object>()
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("videoId", id)
                .puts("pageSize", "10")
                .puts("pageNum", pageNum));
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if (function.equals("unPraise")) {
            hanMomCommentPresenter.addCommentPraise(new SimpleHashMapBuilder<String, Object>()
                    .puts("discussId", (String) obj)
                    .puts(Functions.FUNCTION, "8100")
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 2);
        }
        if (function.equals("praise")) {
            hanMomCommentPresenter.addCommentPraise(new SimpleHashMapBuilder<String, Object>()
                    .puts("discussId", (String) obj)
                    .puts(Functions.FUNCTION, "8099")
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 1);
        }
        if (function.equals("comment")) {
            videoCommentModel = (VideoCommentModel) obj;
            if (videoCommentModel != null) {
                showReviewDialog("回复 @" + videoCommentModel.fromMemberName, 2);
            }
        }
        if (function.equals("reply")) {
            replyListBean = (VideoCommentModel.ReplyListBean) obj;
            if (replyListBean != null) {
                showReviewDialog("回复 @" + replyListBean.fromMemberName, 3);
            }
        }
    }

    /**
     * 评论或者写回复
     *
     * @param hint
     */
    public void showReviewDialog(String hint, int type) {
        commentDialog = new CommentDialog(getContext(), com.healthy.library.R.style.commentDialogStyle);
        commentDialog.setHint(hint, type);
        commentDialog.setOnDiscussDialogDismissListener(this);
        try {
            commentDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDiscussDismiss(String result, int type) {
        if (type == -1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideInput();
                }
            }, 300);
            return;
        }
        if (result != null && !TextUtils.isEmpty(result)) {
            if (type == 1) {//评论
                hanMomCommentPresenter.addVideoComment(new SimpleHashMapBuilder<String, Object>()
                        .puts("videoId", id)
                        .puts("content", result)
                        .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        .puts("memberType", "1")
                        .puts("memberState", SpUtils.getValue(getContext(), SpKey.STATUS_STR)));
            } else if (type == 2) {//评论评论
                if (videoCommentModel != null) {
                    hanMomCommentPresenter.addCommentReply(new SimpleHashMapBuilder<String, Object>()
                            .puts("videoDiscussId", videoCommentModel.id)
                            .puts("content", result)
                            .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            .puts("fromMemberType", "1")
                            //.puts("memberState", SpUtils.getValue(getContext(), SpKey.STATUS_STR))
                            .puts("toMemberId", videoCommentModel.fromMemberId)
                            .puts("toMemberType", videoCommentModel.memberType)
                            .puts("parentId", videoCommentModel.id));
                }
            } else {//评论评论的评论
                if (replyListBean != null) {
                    hanMomCommentPresenter.addCommentReply(new SimpleHashMapBuilder<String, Object>()
                            .puts("videoDiscussId", replyListBean.videoDiscussId)
                            .puts("content", result)
                            .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            .puts("fromMemberType", "1")
                            //.puts("memberState", SpUtils.getValue(getContext(), SpKey.STATUS_STR))
                            .puts("toMemberId", replyListBean.fromMemberId)
                            .puts("toMemberType", replyListBean.toMemberType)
                            .puts("parentId", replyListBean.id));
                }
            }

        }
        //Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetCommentListSuccess(List<VideoCommentModel> result, PageInfoEarly pageInfoEarly) {
        showContent();
        if (result == null) {
            return;
        }
        pageNum = pageInfoEarly.pageNum;
        if (pageNum == 1 || pageNum == 0) {
            if (result.size() == 0) {
                indexTabEmptyAdapter.setModel("");
                showEmpty();
            } else {
                indexTabEmptyAdapter.setModel(null);
                videoCommentListAdapter.setData((ArrayList) result);
            }
        } else {
            layoutRefresh.finishLoadMore();
            indexTabEmptyAdapter.setModel(null);
            videoCommentListAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.nextPage == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onAddCommentReplySuccess(String result) {
        if (result != null && result.contains("成功")) {
            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_LONG).show();
            getData();
            EventBus.getDefault().post(new CommentEvent(2));
        } else {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAddVideoCommentSuccess(String result) {
        if (result != null && result.contains("成功")) {
            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_LONG).show();
            getData();
            EventBus.getDefault().post(new CommentEvent(2));
        } else {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAddCommentPraiseSuccess(String result, int type) {
        if (type == 1) {
            Toast.makeText(getContext(), "点赞成功", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "取消赞成功", Toast.LENGTH_LONG).show();
        }
        getData();
    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void comment(CommentEvent msg) {
        if (msg.type == 1) {
            showReviewDialog("我来说点什么吧…", 1);
        }
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        View v = mActivity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}