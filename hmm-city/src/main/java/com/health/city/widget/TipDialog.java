package com.health.city.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.health.city.R;
import com.healthy.library.model.TopicLimit;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TipDialog extends Dialog {


    private String addressProvince;

    private String addressCity;

    private String addressArea;

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }


    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    private SlidingTabLayout st;
    private ViewPager vp;
    private EditText serarchKeyWord;
    private Context mcontext;

    public void setOnTipDialogClickListener(OnTipDialogClickListener onTipDialogClickListener) {
        this.onTipDialogClickListener = onTipDialogClickListener;
    }

    private OnTipDialogClickListener onTipDialogClickListener;

    private TextView save;


    public TipDialog(@NonNull Context context) {
        this(context, 0);
    }

    public TipDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);
        mcontext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(mcontext).inflate(R.layout.city_dialog_addtip,null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        RadioButton radio1=view.findViewById(R.id.radio1);
        RadioButton radio2=view.findViewById(R.id.radio2);
        serarchKeyWord = (EditText) view.findViewById(R.id.serarchKeyWord);
        save = (TextView) view.findViewById(R.id.save);

        radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    currentIndex=0;
                }
            }
        });
        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    currentIndex=1;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTipDialogClickListener != null) {
                    if(!TextUtils.isEmpty(serarchKeyWord.getText().toString())){
                        Map<String, Object> map = new HashMap<>();

                        map.put("limitsStatus","0");
                        if(currentIndex==0){
                            map.put("limitsStatus","1");
                            map.put("topicLimitsList",new TopicLimit(addressProvince,addressCity,addressArea));
                        }
                        map.put("topicName",serarchKeyWord.getText().toString());
                        onTipDialogClickListener.onClick(view, map);
                        dismiss();
                    }else {
                        Toast.makeText(mcontext,"话题不能为空",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        serarchKeyWord.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) mcontext).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                serarchKeyWord.requestFocus();
            }
        },300);
    }
    private void initView() {

    }

    public interface OnTipDialogClickListener {
        void onClick(View view, Map<String, Object> map);
    }
}
