package com.healthy.library.business;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.IPModel;

import java.util.ArrayList;
import java.util.List;

public class ChangeIPDialog extends BaseDialogFragment {

    private LinearLayout ipLL;
    private ImageView closeBtn;
    private View view;
    private List<IPModel> stringList = new ArrayList<>();
    private getContentListener getListener;

    public static ChangeIPDialog newInstance() {
        ChangeIPDialog fragment = new ChangeIPDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.BottomDialogAnimation);
        if (window != null) {
            View decorView = window.getDecorView();
            decorView.setPadding(0, 0, 0, 0);
            decorView.setBackgroundResource(R.drawable.shape_dialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
        }
        if (view == null) {
            view = inflater.inflate(R.layout.dialog_change_ip_layout, container, false);
            initStringList();
            initView();
        }
        return view;
    }

    private void initView() {
        ipLL = view.findViewById(R.id.ipLL);
        closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        initData();
    }

    public void initData() {
        ipLL.removeAllViews();
        if (stringList.size() == 0) {
            return;
        }
        for (int i = 0; i < stringList.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_ip_layout, null);
            final TextView selectTxt = view.findViewById(R.id.selectTxt);
            final IPModel model = stringList.get(i);
            selectTxt.setText(model.ipName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    getListener.resultContent(model.ipPath);
                }
            });
            ipLL.addView(view);
        }
    }

    public interface getContentListener {

        void resultContent(String result);
    }

    public void setResultListener(getContentListener getListener) {
        this.getListener = getListener;
    }

    private void initStringList() {
        stringList.clear();
        stringList.add(new IPModel("47预发布环境", "http://47.111.169.73:7081/cserver/public/"));
        stringList.add(new IPModel("憨妈妈线上环境", "https://capi.hanmama.com/cserver/public/"));
        stringList.add(new IPModel("100开发环境", "http://192.168.10.100:8081/cserver/public/"));
        stringList.add(new IPModel("101测试环境", "http://192.168.10.101:8081/cserver/public/"));
        stringList.add(new IPModel("102开发环境", "http://192.168.10.102:8081/cserver/public/"));
        stringList.add(new IPModel("103测试环境", "http://192.168.10.103:8081/cserver/public/"));
    }
}
