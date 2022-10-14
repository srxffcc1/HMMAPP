package com.healthy.library.business;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.VideoCommentListAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
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
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class VideoCommentDialog extends BaseDialogFragment implements HanMomCommentContract.View,
        OnRefreshLoadMoreListener, CommentDialog.OnDiscussDialogDismissListener, BaseAdapter.OnOutClickListener {

    private StatusLayout layoutStatus;
    private ConstraintLayout topLL;
    private TextView commentTitle;
    private TextView comment;
    private ImageView closeImg;
    private ConstraintLayout bottomCommentLayout;
    private LinearLayout commentLayout;
    private RecyclerView commentRecycle;
    private View view;
    private DismissListener dismissListener;
    private SmartRefreshLayout refreshLayout;
    private VideoCommentListAdapter videoCommentListAdapter;
    private String id;
    private int pageNum = 1;
    private HanMomCommentPresenter hanMomCommentPresenter;
    private CommentDialog commentDialog;
    private VideoCommentModel videoCommentModel;//被评论的评论实体
    private VideoCommentModel.ReplyListBean replyListBean;//被评论的评论实体

    public static VideoCommentDialog newInstance() {
        VideoCommentDialog fragment = new VideoCommentDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setCancelable(true);
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
        }
        view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_video_comment_layout, container, false);
            initView();
        }
        return view;
    }

    private void initView() {
        layoutStatus = (StatusLayout) view.findViewById(R.id.layout_status);
        topLL = (ConstraintLayout) view.findViewById(R.id.topLL);
        commentTitle = (TextView) view.findViewById(R.id.commentTitle);
        comment = (TextView) view.findViewById(R.id.comment);
        closeImg = (ImageView) view.findViewById(R.id.closeImg);
        bottomCommentLayout = (ConstraintLayout) view.findViewById(R.id.bottomCommentLayout);
        commentLayout = (LinearLayout) view.findViewById(R.id.commentLayout);
        commentRecycle = (RecyclerView) view.findViewById(R.id.commentRecycle);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refresh_layout);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoCommentDialog.this.dismiss();
            }
        });
        bottomCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog("我来说点什么吧…", 1);
            }
        });
        hanMomCommentPresenter = new HanMomCommentPresenter(getContext(), this);
        initList();
        getData();
    }

    public void initList() {
        refreshLayout.setOnRefreshLoadMoreListener(this);
        videoCommentListAdapter = new VideoCommentListAdapter();
        commentRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRecycle.setAdapter(videoCommentListAdapter);
        videoCommentListAdapter.setOutClickListener(this);
    }

    @Override
    public void onGetCommentListSuccess(List<VideoCommentModel> result, PageInfoEarly pageInfoEarly) {
        layoutStatus.updateStatus(StatusLayout.Status.STATUS_CONTENT);
        if (result == null) {
            return;
        }
        pageNum = pageInfoEarly.pageNum;
        comment.setText("(" + pageInfoEarly.total + ")");
        if (pageNum == 1 || pageNum == 0) {
            refreshLayout.finishRefresh();
            videoCommentListAdapter.setData((ArrayList) result);
            if (result.size() == 0) {
                layoutStatus.updateStatus(StatusLayout.Status.STATUS_EMPTY);
            }
        } else {
            refreshLayout.finishLoadMore();
            videoCommentListAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.nextPage == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onAddCommentReplySuccess(String result) {
        if (result != null && result.contains("成功")) {
            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_LONG).show();
            getData();
            EventBus.getDefault().post(new CommentEvent(3));
        } else {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAddVideoCommentSuccess(String result) {
        if (result != null && result.contains("成功")) {
            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_LONG).show();
            getData();
            EventBus.getDefault().post(new CommentEvent(3));
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
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {
        hanMomCommentPresenter.getCommentList(new SimpleHashMapBuilder<String, Object>()
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("videoId", id)
                .puts("pageSize", "10")
                .puts("pageNum", pageNum));
    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

    /**
     * 评论或者写回复
     *
     * @param hint
     */
    public void showReviewDialog(String hint, int type) {
        commentDialog = new CommentDialog(getContext(), R.style.commentDialogStyle);
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

    public interface DismissListener {

        void onDismiss();
    }

    public void setDismissListener(final DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(CommentEvent msg) {

    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        View v = getActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
