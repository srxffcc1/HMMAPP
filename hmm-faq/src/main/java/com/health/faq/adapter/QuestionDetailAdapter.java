package com.health.faq.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.faq.R;
import com.health.faq.model.QuestionDetailModel;
import com.health.faq.model.QuestionModel;
import com.health.faq.model.ReplyAdoptedModel;
import com.health.faq.model.ReplyModel;
import com.health.faq.model.TitleModel;
import com.health.faq.widget.VoiceLayout;
import com.healthy.library.constant.Constants;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Li
 * @date 2019/07/02 16:20
 * @des
 */
public class QuestionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<QuestionDetailModel> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    public static final int TYPE_QUESTION_HEADER = 1;
    public static final int TYPE_REPLY = 2;
    public static final int TYPE_IMPROVE_REWARD = 3;
    public static final int TYPE_FIRST_REPLY = 4;
    public static final int TYPE_TITLE = 5;
    public static final int TYPE_DIVIDER = 6;
    public static final int TYPE_NO_REPLY = 7;
    public static final int TYPE_MY_ANSWER = 8;
    public static final int TYPE_MY_REPLY_ADOPTED = 9;
    private float mCorner;
    private OnFunctionClickListener mClickListener;

    public QuestionDetailAdapter(Context context, OnFunctionClickListener clickListener) {
        mContext = context;
        mClickListener = clickListener;
        mInflater = LayoutInflater.from(context);
        mCorner = TransformUtil.dp2px(context, 4);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final RecyclerView.ViewHolder holder;
        switch (viewType) {
            case TYPE_QUESTION_HEADER:
                holder = new HeaderHolder(mInflater
                        .inflate(R.layout.layout_question_header, viewGroup, false));
                break;
            case TYPE_FIRST_REPLY:
                holder = new FirstReplyHolder(mInflater
                        .inflate(R.layout.layout_first_reply, viewGroup, false));
                holder.itemView.findViewById(R.id.tv_reply)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mClickListener.onReplyClick(v);
                            }
                        });
                break;
            case TYPE_IMPROVE_REWARD:
                holder = new ImproveRewardHeader(mInflater
                        .inflate(R.layout.layout_improve_reward, viewGroup, false));
                holder.itemView.findViewById(R.id.tv_improve)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mClickListener.onImproveRewardClick(v);
                            }
                        });
                break;
            case TYPE_REPLY:
                holder = new ReplyHolder(mInflater
                        .inflate(R.layout.layout_reply, viewGroup, false));
                holder.itemView.findViewById(R.id.tv_adopt)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mClickListener.onAdoptClick(mList.get(holder.getAdapterPosition())
                                                .getReplyModel().getId(),
                                        mList.get(holder.getAdapterPosition())
                                                .getReplyModel().getQuestionId());
                            }
                        });
                holder.itemView.findViewById(R.id.voice)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mClickListener.onVoiceClick((VoiceLayout) v,
                                        mList.get(holder.getAdapterPosition())
                                                .getReplyModel().getSoundUrl());
                            }
                        });
                break;
            case TYPE_DIVIDER:
                holder = new DividerHolder(mInflater
                        .inflate(R.layout.layout_divider, viewGroup, false));
                break;
            case TYPE_NO_REPLY:
                holder = new NoReplyHolder(mInflater
                        .inflate(R.layout.layout_no_reply, viewGroup, false));
                break;
            case TYPE_MY_ANSWER:
                holder = new MyAnswerHolder(mInflater
                        .inflate(R.layout.layout_my_answer, viewGroup, false));
                holder.itemView.findViewById(R.id.voice)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mClickListener.onVoiceClick((VoiceLayout) v,
                                        mList.get(holder.getAdapterPosition())
                                                .getReplyModel().getSoundUrl());
                            }
                        });
                break;
            case TYPE_MY_REPLY_ADOPTED:
                holder = new MyReplyAdoptedHolder(mInflater
                        .inflate(R.layout.layout_my_reply_adopted, viewGroup, false));
                break;
            default:
                holder = new TitleHolder(mInflater
                        .inflate(R.layout.layout_title, viewGroup, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = mList.get(position).getType();
        QuestionDetailModel model = mList.get(position);
        switch (viewType) {
            case TYPE_QUESTION_HEADER:
                renderHeader((HeaderHolder) holder, model.getQuestionModel());
                break;
            case TYPE_TITLE:
                renderTitle((TitleHolder) holder, model.getTitleModel());
                break;
            case TYPE_REPLY:
                renderReply((ReplyHolder) holder, model.getReplyModel());
                break;
            case TYPE_MY_ANSWER:
                renderMyAnswer((MyAnswerHolder) holder, model.getReplyModel());
                break;
            case TYPE_MY_REPLY_ADOPTED:
                renderAdoptedHeader((MyReplyAdoptedHolder) holder, model.getAdoptedModel());
                break;
            default:
                break;
        }
    }

    private void renderAdoptedHeader(MyReplyAdoptedHolder holder, ReplyAdoptedModel model) {
        if (model.getType() == 2) {
            holder.tvIncome.setVisibility(View.VISIBLE);
            holder.tvIncome.setText(String.format("赏金¥%s 已入账", model.getIncome()));
            holder.ivHeader.setImageResource(R.drawable.faq_ic_my_reply_adopted);
            holder.tvRemind.setText("恭喜，你的回答被采纳了！");
        } else if (model.getType() == 3) {
            holder.tvIncome.setVisibility(View.GONE);
            holder.ivHeader.setImageResource(R.drawable.faq_ic_my_reply_unadopted);
            holder.tvRemind.setText("很遗憾，未被采纳，再接再厉！");
        }

    }

    private void renderMyAnswer(MyAnswerHolder holder, ReplyModel model) {
        if (model.isAudioReply()) {
            holder.tvReply.setVisibility(View.GONE);
            holder.voice.setVisibility(View.VISIBLE);
            holder.voice.setDuration(ParseUtils.parseLong(model.getDuration()));
        } else {
            holder.tvReply.setVisibility(View.VISIBLE);
            holder.voice.setVisibility(View.GONE);
            holder.tvReply.setText(model.getReplyDetail());
        }
        if (model.isShowDate()) {
            holder.tvDate.setVisibility(View.VISIBLE);
            holder.tvDate.setText(DateUtils
                    .formatTime2String("MM-dd HH:mm", model.getReplyDate()));
        } else {
            holder.tvDate.setVisibility(View.GONE);
        }
    }

    /**
     * 渲染回复
     *
     * @param holder holder
     * @param model  数据
     */
    private void renderReply(ReplyHolder holder, ReplyModel model) {
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(model.getFaceUrl())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.ic_questioner_default_avatar)

                .into(holder.ivAvatar);
        holder.tvNickname.setText(model.getNickname());
        holder.tvDate.setText(DateUtils.formatTime2String("MM-dd HH:mm", model.getReplyDate()));
        if (model.isAudioReply()) {
            holder.voice.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.GONE);
            holder.voice.setDuration(ParseUtils.parseLong(model.getDuration()));
        } else {
            holder.voice.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(model.getReplyDetail());
        }
        holder.tvAdopt.setVisibility(model.isMyQuestionReply() ? View.VISIBLE : View.GONE);
    }

    /**
     * 渲染标题
     *
     * @param holder holder
     * @param model  数据
     */
    private void renderTitle(TitleHolder holder, TitleModel model) {
        holder.tvTitle.setText(model.getTitle());
        holder.tvTitleSub.setText(model.getTitleSub());
        holder.tvDes.setText(model.getDes());
        holder.ivBadge.setVisibility(model.isShowBadge() ? View.VISIBLE : View.GONE);
    }

    /**
     * 渲染头部
     *
     * @param holder holder
     * @param item   数据
     */
    private void renderHeader(HeaderHolder holder, final QuestionModel item) {
        holder.tvSupTitle.setVisibility(item.isShowSupTitle() ? View.VISIBLE : View.GONE);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(item.getQuestionerAvatar())
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.ic_questioner_default_avatar)

                .into(holder.ivAvatar);
        holder.tvNickname.setText(item.getQuestionerNickname());
        holder.tvPeriod.setText(item.getQuestionerPeriodDes());
        holder.tvTitle.setText(item.getQuestionTitle());
        holder.tvPeriod.setBackgroundResource(item.getQuestionerPeriod() == 1 ? R.drawable.shape_period_1 :
                item.getQuestionerPeriod() == 2 ?
                        R.drawable.shape_period_2 : R.drawable.shape_period_3);
        holder.tvCost.setText(item.isHasReturn() ? "赏金已退回" :
                String.format("¥ %s", item.getQuestionCost()));
        holder.tvContent.setText(item.getQuestionContent());
        holder.tvDate.setText(DateUtils.getClassDatePost(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getCreateDate())));




//        long timeDiff = (System.currentTimeMillis() - item.getCreateDate().getTime()) / 1000;
//        if (timeDiff > 60 * 60) {
//            holder.tvDate.setText(DateUtils.formatTime2String("MM-dd HH:mm", item.getCreateDate()));
//        } else {
//            holder.tvDate.setText(String.format("%s分钟前", timeDiff / 60));
//        }

        String content;
        String color;
        if (Constants.REWARD_STATUS_IN.equals(item.getStatus())) {
            content = "进行中";
            color = "#FF6266";
        } else if (Constants.REWARD_STATUS_CLOSE.equals(item.getStatus())) {
            content = "已关闭";
            color = "#9B9CA9";
        } else if (Constants.REWARD_STATUS_FINISH.equals(item.getStatus())) {
            content = "已完成";
            color = "#444444";
        } else {
            content = "异常";
            color = "#444444";
        }
        holder.tvStatus.setText(content);
        holder.tvStatus.setTextColor(Color.parseColor(color));
        holder.layoutPictures.setVisibility(item.getPhotos().length == 0 ? View.GONE : View.VISIBLE);

        holder.layoutPictures.removeAllViews();
        for (int i = 0; i < item.getPhotos().length; i++) {
            final int pos = i;
            String photo = item.getPhotos()[i];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                    (int) TransformUtil.dp2px(mContext, 75));
            params.weight = 1;
            CornerImageView imageView = new CornerImageView(mContext);
            imageView.setCornerRadius(mCorner);
            holder.layoutPictures.addView(imageView, params);
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(photo)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                            .withInt("pos", pos)
                            .withCharSequenceArray("urls", item.getPhotos())
                            .navigation();
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<QuestionDetailModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public List<QuestionDetailModel> getData() {
        return mList;
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvNickname;
        TextView tvPeriod;
        TextView tvTitle;
        TextView tvContent;
        LinearLayout layoutPictures;
        TextView tvDate;
        TextView tvCost;
        TextView tvStatus;
        TextView tvSupTitle;

        HeaderHolder(@NonNull View itemView) {
            super(itemView);
            tvSupTitle = itemView.findViewById(R.id.tv_sup_title);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvNickname = itemView.findViewById(R.id.tv_name);
            tvPeriod = itemView.findViewById(R.id.tv_period);
            tvTitle = itemView.findViewById(R.id.tv_question_title);
            tvContent = itemView.findViewById(R.id.tv_question_content);
            layoutPictures = itemView.findViewById(R.id.layout_pictures);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCost = itemView.findViewById(R.id.tv_cost);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }

    static class ImproveRewardHeader extends RecyclerView.ViewHolder {

        ImproveRewardHeader(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class FirstReplyHolder extends RecyclerView.ViewHolder {
        FirstReplyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class MyReplyAdoptedHolder extends RecyclerView.ViewHolder {

        ImageView ivHeader;
        TextView tvRemind;
        TextView tvIncome;

        MyReplyAdoptedHolder(@NonNull View itemView) {
            super(itemView);
            ivHeader = itemView.findViewById(R.id.iv_header);
            tvRemind = itemView.findViewById(R.id.tv_adopt_remind);
            tvIncome = itemView.findViewById(R.id.tv_income);
        }
    }

    static class NoReplyHolder extends RecyclerView.ViewHolder {

        NoReplyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class TitleHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvTitleSub;
        TextView tvDes;
        ImageView ivBadge;

        TitleHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTitleSub = itemView.findViewById(R.id.tv_title_sub);
            tvDes = itemView.findViewById(R.id.tv_des);
            ivBadge = itemView.findViewById(R.id.iv_badge);
        }
    }

    static class ReplyHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar;
        TextView tvNickname;
        TextView tvDate;
        TextView tvContent;
        VoiceLayout voice;
        TextView tvAdopt;

        ReplyHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvNickname = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvContent = itemView.findViewById(R.id.tv_content);
            voice = itemView.findViewById(R.id.voice);
            tvAdopt = itemView.findViewById(R.id.tv_adopt);
        }
    }

    static class DividerHolder extends RecyclerView.ViewHolder {

        DividerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class MyAnswerHolder extends RecyclerView.ViewHolder {

        TextView tvReply;
        TextView tvDate;
        VoiceLayout voice;

        MyAnswerHolder(@NonNull View itemView) {
            super(itemView);
            tvReply = itemView.findViewById(R.id.tv_reply);
            tvDate = itemView.findViewById(R.id.tv_date);
            voice = itemView.findViewById(R.id.voice);
        }
    }

    public interface OnFunctionClickListener {
        /**
         * 提高赏金
         *
         * @param view view
         */
        void onImproveRewardClick(View view);

        /**
         * 回答
         *
         * @param view view
         */
        void onReplyClick(View view);

        /**
         * 采纳最佳答案
         *
         * @param id         答案id
         * @param questionId 问题id
         */
        void onAdoptClick(String id, String questionId);

        /**
         * 播放
         *
         * @param voiceLayout 播放控件
         * @param path        路径
         */
        void onVoiceClick(VoiceLayout voiceLayout, String path);
    }
}
