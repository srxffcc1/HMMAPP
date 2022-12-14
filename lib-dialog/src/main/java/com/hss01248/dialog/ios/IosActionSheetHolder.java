package com.hss01248.dialog.ios;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hss01248.dialog.R;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.Tool;
import com.hss01248.dialog.adapter.SuperLvHolder;
import com.hss01248.dialog.config.ConfigBean;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class IosActionSheetHolder extends SuperLvHolder<ConfigBean> {
    public ListView lv;
    protected Button btnBottom;
    public TextView textView;
    private View line;

    public IosActionSheetHolder(Context context) {
        super(context);
    }

    @Override
    protected void findViews() {
        lv = (ListView) rootView.findViewById(R.id.lv);
        textView = rootView.findViewById(R.id.tv_title);
        line = rootView.findViewById(R.id.v_line);

        lv.setDivider(new ColorDrawable(lv.getResources().getColor(R.color.dialogutil_line_dd)));
        lv.setDividerHeight(1);
        btnBottom = (Button) rootView.findViewById(R.id.btn_bottom);
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.dialogutil_dialog_ios_alert_bottom;
    }

    @Override
    public void assingDatasAndEvents(final Context context, final ConfigBean bean) {

        if (TextUtils.isEmpty(bean.title)) {
            textView.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            textView.setText(bean.title);
            if (bean.titleTxtSize > 0) {
                textView.setTextSize(bean.titleTxtSize);
            }
            if (bean.titleTxtColor != 0) {
                textView.setTextColor(textView.getContext().getResources().getColor(bean.titleTxtColor));
            }
        }


        if (TextUtils.isEmpty(bean.bottomTxt)) {
            btnBottom.setVisibility(View.GONE);
        } else {
            btnBottom.setVisibility(View.VISIBLE);
            btnBottom.setText(bean.bottomTxt);
            btnBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tool.dismiss(bean);
                    bean.itemListener.onBottomBtnClick();

                }
            });
        }


        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return bean.wordsIos.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                RelativeLayout root = (RelativeLayout) View.inflate(context, R.layout.dialogutil_item_btn_bottomalert, null);
                Button view = (Button) root.findViewById(R.id.btn);
                if (getCount() >= 2) {
                    if (position == 0) {
                        if (TextUtils.isEmpty(bean.title)) {
                            view.setBackgroundResource(R.drawable.dialogutil_selector_btn_press_all_top);
                        } else {
                            view.setBackgroundResource(R.drawable.dialogutil_selector_btn_press_no_corner);
                        }
                    } else if (position == getCount() - 1) {
                        view.setBackgroundResource(R.drawable.dialogutil_selector_btn_press_all_bottom);
                    } else {
                        view.setBackgroundResource(R.drawable.dialogutil_selector_btn_press_no_corner);
                    }

                } else {
                    view.setBackgroundResource(R.drawable.dialogutil_selector_btn_press_all);
                }

                view.setText(bean.wordsIos.get(position));
                if (bean.wordsIosColors != null && bean.wordsIosColors.size() > 0) {
                    view.setTextColor(bean.wordsIosColors.get(position));
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //StyledDialog.dismiss(bean.dialog,bean.alertDialog);
                        Tool.dismiss(bean, true);
                        bean.itemListener.onItemClick(bean.wordsIos.get(position), position);

                    }
                });

                return root;
            }
        };

        lv.setAdapter(adapter);
    }


}
