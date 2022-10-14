package com.health.tencentlive.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.healthy.library.model.ChatMessage;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.ImageSpanCentre;

public class PushLiveChatRoomAdapter extends BaseAdapter<ChatMessage> {

    public PushLiveChatRoomAdapter() {
        this(R.layout.item_live_chat_room_layout);
    }

    public PushLiveChatRoomAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView userNickName = holder.getView(R.id.userNickName);
        TextView chartContent = holder.getView(R.id.chartContent);
        LinearLayout layout = holder.getView(R.id.layout);
        final ChatMessage chatMessage = getDatas().get(position);
        if (chatMessage.type == 1) {
            layout.setBackground(context.getResources().getDrawable(R.drawable.shape_live_view_bg));
        } else if (chatMessage.type == 2) {
            layout.setBackground(context.getResources().getDrawable(R.drawable.shape_live_view_bg));
//            layout.setBackground(context.getResources().getDrawable(R.drawable.shape_live_item_bg));
        } else if (chatMessage.type == 3) {
            layout.setBackground(context.getResources().getDrawable(R.drawable.shape_live_add_room_message_item));
        } else if (chatMessage.type == 4) {
            layout.setBackground(context.getResources().getDrawable(R.drawable.shape_live_buy_goods_message_item));
        }
        String nickName = null;
        String message = null;
        if (chatMessage.userName != null && !TextUtils.isEmpty(chatMessage.userName)) {
            if (chatMessage.type == 3) {
                nickName = chatMessage.userName;
            } else {
                nickName = chatMessage.userName + "：";
            }
        } else {
            nickName = chatMessage.senderID;
        }
        if (chatMessage.message != null && !TextUtils.isEmpty(chatMessage.message)) {
            message = chatMessage.message;
        } else {
            message = "";
        }
        if (chatMessage.type == 1) {
            SpannableString goodsTitleSpan = new SpannableString(nickName + message);
            goodsTitleSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffe9a4")), 0, nickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            goodsTitleSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, goodsTitleSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            chartContent.setText(goodsTitleSpan);
        } else if (chatMessage.type == 2) {
            SpannableString spanString = new SpannableString("  " + nickName + message);
            Drawable drawable = context.getResources().getDrawable(R.drawable.live_anchor_icon);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpanCentre imageSpan = new ImageSpanCentre(drawable, ImageSpanCentre.CENTRE);
            spanString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            chartContent.setText(spanString);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("stopSpeed", chatMessage);
                }
            }
        });
    }
}
