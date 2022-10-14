package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.constant.Constants;
import com.healthy.library.model.BackListModel;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.FoldTextView;

public class OrderBackDetialHeaderAdapter extends BaseMultiItemAdapter<BackListModel> {
    public boolean isShowContent = false;

    public OrderBackDetialHeaderAdapter() {
        this(R.layout.item_order_back_detial_create);
        addItemType(0, R.layout.item_order_back_detial_create);//开始退款
        addItemType(1, R.layout.item_order_back_detial_start);//退款中
        addItemType(2, R.layout.item_order_back_detial_finish);//退款完成
        addItemType(5, R.layout.item_order_back_detial_fail);//拒绝退款//撤销退款//退款失败
    }

    //    @Override
//    public int getItemViewType(int position) {
//        return getDefItemViewType(position);
//    }
    @Override
    public int getItemViewType(int position) {
        if (getModel().disposeAgree == -1 || getModel().canceled == 1) {
            return 5;//商家拒绝/撤销
        } else {
            if (getModel().status == -1 || getModel().status == -2 || getModel().status == 3) {
                return 5;
            } else {
                return getModel().status;
            }
        }
    }

    public OrderBackDetialHeaderAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final TextView tv_store_name = holder.getView(R.id.tv_store_name);
        TextView tv_creat_time = holder.getView(R.id.tv_creat_time);
        String payType = "";
        if (getModel().payAmountType == Integer.parseInt(Constants.PAY_IN_A_LI) || getModel().payAmountType == 1) {
            payType = "支付宝";
        } else if (getModel().payAmountType == Integer.parseInt(Constants.PAY_IN_WX) || getModel().payAmountType == 2) {
            payType = "微信";
        } else if (getModel().payAmountType == 3) {
            payType = "微信小程序";
        }
        if (getItemViewType(position) == 0) {
            final FoldTextView tv_content = holder.getView(R.id.tv_content);
            // final TextView recoverpagecontent = holder.getView(R.id.recoverpagecontent);
            tv_store_name.setText("已提交退款申请至" + getModel().deliveryShopName);
            tv_creat_time.setText(getModel().createTime);
            // tv_content.clearText();
            // tv_content.setMaxLineCount(4);
            tv_content.setText("申请说明：" + getModel().refundComment);
            tv_content.setExpand(isShowContent)
                    .setOnTipClickListener(new FoldTextView.onTipClickListener() {
                        @Override
                        public void onTipClick(boolean flag) {
                            isShowContent = flag;
                        }
                    });

            LinearLayout linearLayout = holder.getView(R.id.imgLL);
            if (getModel().refundAttachList != null || getModel().refundAttachList.size() > 0) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.removeAllViews();
                final String[] array = new String[getModel().refundAttachList.size()];
                for (int i = 0; i < getModel().refundAttachList.size(); i++) {
                    array[i] = getModel().refundAttachList.get(i).attachUrl;
                    final int pos = i;
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                            .LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 10, 0, 0);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.item_order_back_detial_img_layout, null);
                    CornerImageView iv_product = view.findViewById(R.id.iv_product);
                    com.healthy.library.businessutil.GlideCopy.with(context).load(getModel().refundAttachList.get(i).attachUrl)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(iv_product);
                    view.setLayoutParams(layoutParams);
                    iv_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                    .withCharSequenceArray("urls", array)
                                    .withInt("pos", pos)
                                    .navigation();
                        }
                    });
                    linearLayout.addView(view);
                }
            } else {
                linearLayout.setVisibility(View.GONE);
            }

        } else if (getItemViewType(position) == 1) {
            TextView tv_start_time = holder.getView(R.id.tv_start_time);
            TextView start_content = holder.getView(R.id.start_start_content);
            tv_creat_time.setText(getModel().createTime);
            tv_store_name.setText("已提交退款申请至" + getModel().deliveryShopName);
            tv_start_time.setText(getModel().disposeTime);
            if (getModel().refundPoints == null || Double.parseDouble(getModel().refundPoints) == 0) {
                if (getModel().refundAmount == null || Double.parseDouble(getModel().refundAmount) == 0) {
                    start_content.setText("商家同意退款，系统将在1-2日提交至个人账户处理");//说明是0元退款
                } else {
                    start_content.setText("商家同意退款，系统将在1-2日提交至" + payType + "处理");
                }
            } else {
                start_content.setText("商家同意退款，系统将在1-2日提交至个人账户处理");
            }

        } else if (getItemViewType(position) == 2) {
            TextView tv_finish_time = holder.getView(R.id.tv_finish_time);
            TextView finish_create_content = holder.getView(R.id.finish_create_content);
            TextView finish_create_time = holder.getView(R.id.finish_create_time);
            TextView tv_start_time = holder.getView(R.id.tv_start_time);
            TextView finish_content = holder.getView(R.id.finish_content);
            TextView finish_time = holder.getView(R.id.finish_time);
            TextView tv_one = holder.getView(R.id.tv_one);
            TextView tv_two = holder.getView(R.id.tv_two);
            TextView tv_three = holder.getView(R.id.tv_three);
            final ConstraintLayout create_constraintLayout = holder.getView(R.id.create_constraintLayout);
            final ConstraintLayout start_constraintLayout = holder.getView(R.id.start_constraintLayout);
            final ConstraintLayout finish_constraintLayout = holder.getView(R.id.finish_constraintLayout);
            tv_store_name.setText("已完成");
            tv_creat_time.setText(getModel().createTime);
            tv_start_time.setText(getModel().disposeTime);
            if (!TextUtils.isEmpty(getModel().payAmountTime)) {
                tv_finish_time.setText(getModel().payAmountTime);
                finish_time.setText(getModel().payAmountTime);
            }
            if (TextUtils.isEmpty(getModel().refundPoints) || Double.parseDouble(getModel().refundPoints) == 0) {
                if (getModel().refundAmount == null || Double.parseDouble(getModel().refundAmount) == 0) {//说明是0元退款
                    finish_content.setText("已成功退款至个人账户，请注意查收");
                    finish_create_content.setText("已提交退款申请至个人账户，退款将按支付路径原路返还，请注意查收");
                } else {
                    finish_content.setText("已成功退款至" + payType + "账户，请注意查收");
                    finish_create_content.setText("已提交退款申请至" + payType + "，退款将按支付路径原路返还，请注意查收");
                }
            } else {
                finish_content.setText("已成功退款至个人账户，请注意查收");
                finish_create_content.setText("已提交退款申请至个人账户，退款将按支付路径原路返还，请注意查收");
            }
            finish_create_time.setText(getModel().disposeTime);
            //提交申请
            final FoldTextView tv_content = holder.getView(R.id.tv_content);
            // final TextView recoverpagecontent = holder.getView(R.id.recoverpagecontent);

            //tv_creat_time.setText(getModel().getCreateTime());
            // tv_content.clearText();
            // tv_content.setMaxLineCount(4);

            tv_content.setExpand(isShowContent)
                    .setOnTipClickListener(new FoldTextView.onTipClickListener() {
                        @Override
                        public void onTipClick(boolean flag) {
                            isShowContent = flag;
                        }
                    });

            LinearLayout linearLayout = holder.getView(R.id.imgLL);
            if (getModel().refundAttachList != null || getModel().refundAttachList.size() > 0) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.removeAllViews();
                final String[] array = new String[getModel().refundAttachList.size()];
                for (int i = 0; i < getModel().refundAttachList.size(); i++) {
                    array[i] = getModel().refundAttachList.get(i).attachUrl;
                    final int pos = i;
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                            .LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 10, 0, 0);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.item_order_back_detial_img_layout, null);
                    CornerImageView iv_product = view.findViewById(R.id.iv_product);
                    com.healthy.library.businessutil.GlideCopy.with(context).load(getModel().refundAttachList.get(i).attachUrl)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(iv_product);
                    view.setLayoutParams(layoutParams);
                    iv_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                    .withCharSequenceArray("urls", array)
                                    .withInt("pos", pos)
                                    .navigation();
                        }
                    });
                    linearLayout.addView(view);
                }
            } else {
                linearLayout.setVisibility(View.GONE);
            }
            //处理中
            TextView start_content = holder.getView(R.id.start_start_content);
            //tv_store_name.setText("已提交退款申请至" + getModel().getShopName());
            if (TextUtils.isEmpty(getModel().refundPoints) || Double.parseDouble(getModel().refundPoints) == 0) {
                if (getModel().refundAmount == null || Double.parseDouble(getModel().refundAmount) == 0) {
                    start_content.setText("商家同意退款，系统将在1-2日提交至个人账户处理");//说明是0元退款
                } else {
                    start_content.setText("商家同意退款，系统将在1-2日提交至" + payType + "处理");
                }
            } else {
                start_content.setText("商家同意退款，系统将在1-2日提交至个人账户处理");
            }
            tv_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_constraintLayout.setVisibility(View.VISIBLE);
                    start_constraintLayout.setVisibility(View.GONE);
                    finish_constraintLayout.setVisibility(View.GONE);
                    //tv_store_name.setText("已提交退款申请至" + getModel().getShopName());
                    tv_content.setText("申请说明：" + getModel().refundComment + "");
                    tv_store_name.setText("已完成");
                }
            });
            tv_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_constraintLayout.setVisibility(View.GONE);
                    start_constraintLayout.setVisibility(View.VISIBLE);
                    finish_constraintLayout.setVisibility(View.GONE);
                    //tv_store_name.setText("已提交退款申请至" + getModel().getShopName());
                    tv_store_name.setText("已完成");
                }
            });
            tv_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_constraintLayout.setVisibility(View.GONE);
                    start_constraintLayout.setVisibility(View.GONE);
                    finish_constraintLayout.setVisibility(View.VISIBLE);
                    tv_store_name.setText("已完成");
                }
            });
        } else if (getItemViewType(position) == 5) {
            TextView tv_start_time = holder.getView(R.id.tv_start_time);
            TextView fail_content = holder.getView(R.id.fail_content);
            tv_creat_time.setText(getModel().createTime);
            TextView tv_one = holder.getView(R.id.tv_one);
            TextView tv_two = holder.getView(R.id.tv_two);
            TextView fail_title = holder.getView(R.id.fail_title);
            TextView tv_creat_txt2 = holder.getView(R.id.tv_creat_txt2);
            final ConstraintLayout create_constraintLayout = holder.getView(R.id.create_constraintLayout);
            final ConstraintLayout fail_constraintLayout = holder.getView(R.id.fail_constraintLayout);
            if (getModel().status == 3) {
                tv_store_name.setText("商家拒绝申请");
                tv_creat_txt2.setText("商家已处理");
                fail_title.setVisibility(View.VISIBLE);
                tv_start_time.setText(getModel().disposeTime);
                if (TextUtils.isEmpty(getModel().disposeComment)) {
                    fail_content.setText("");
                } else {
                    fail_content.setText("商家说明：" + getModel().disposeComment);
                }
            } else if (getModel().status == -1) {
                tv_store_name.setText("申请已撤销");
                tv_creat_txt2.setText("用户撤销");
                fail_title.setVisibility(View.GONE);
                tv_start_time.setText(getModel().latestUpdateTime);
                fail_content.setText("用户已撤销申请");
            } else if (getModel().status == -2) {
                tv_store_name.setText("退款失败");
                tv_creat_txt2.setText("退款失败");
                fail_title.setVisibility(View.GONE);
                tv_start_time.setText(getModel().latestUpdateTime);
                fail_content.setText("商户退款失败");
            }
            //提交申请
            final FoldTextView tv_content = holder.getView(R.id.tv_content);
            // final TextView recoverpagecontent = holder.getView(R.id.recoverpagecontent);

            //tv_creat_time.setText(getModel().getCreateTime());
            // tv_content.clearText();
            // tv_content.setMaxLineCount(4);

            tv_content.setExpand(isShowContent)
                    .setOnTipClickListener(new FoldTextView.onTipClickListener() {
                        @Override
                        public void onTipClick(boolean flag) {
                            isShowContent = flag;
                        }
                    });

            LinearLayout linearLayout = holder.getView(R.id.imgLL);
            if (getModel().refundAttachList != null || getModel().refundAttachList.size() > 0) {
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.removeAllViews();
                final String[] array = new String[getModel().refundAttachList.size()];
                for (int i = 0; i < getModel().refundAttachList.size(); i++) {
                    array[i] = getModel().refundAttachList.get(i).attachUrl;
                    final int pos = i;
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout
                            .LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 10, 0, 0);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.item_order_back_detial_img_layout, null);
                    CornerImageView iv_product = view.findViewById(R.id.iv_product);
                    com.healthy.library.businessutil.GlideCopy.with(context).load(getModel().refundAttachList.get(i).attachUrl)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(iv_product);
                    view.setLayoutParams(layoutParams);
                    iv_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                    .withCharSequenceArray("urls", array)
                                    .withInt("pos", pos)
                                    .navigation();
                        }
                    });
                    linearLayout.addView(view);
                }
            } else {
                linearLayout.setVisibility(View.GONE);
            }
            tv_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_constraintLayout.setVisibility(View.VISIBLE);
                    fail_constraintLayout.setVisibility(View.GONE);
                    tv_content.setText("申请说明：" + getModel().refundComment + "");
                }
            });
            tv_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    create_constraintLayout.setVisibility(View.GONE);
                    fail_constraintLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
