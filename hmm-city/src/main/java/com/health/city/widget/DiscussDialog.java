package com.health.city.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.R;
import com.healthy.library.model.DiscussStore;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DiscussDialog extends Dialog {


    private String addressProvince;

    private String addressCity;

    private String addressArea;
    private EditText reviewEt;
    private ImageView scaleImg;
    private TextView submit;
    private String resultcontent;
    private String mEditText;

    public void setEditText(String mEditText) {
        this.mEditText = mEditText;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    boolean isnormaldismiss = true;

    int currentIndex = 0;
    private Context mcontext;


    public void setOnDiscussDialogClickListener(OnDiscussDialogClickListener onDiscussDialogClickListener) {
        this.onDiscussDialogClickListener = onDiscussDialogClickListener;
    }

    public void setOnScaleDialogClickListener(OnScaleDialogClickListener onScaleDialogClickListener) {
        this.onScaleDialogClickListener = onScaleDialogClickListener;
    }

    private OnDiscussDialogDismissListener onDiscussDialogDismissListener;
    private OnScaleDialogClickListener onScaleDialogClickListener;
    private OnDiscussDialogClickListener onDiscussDialogClickListener;

    private TextView save;


    public DiscussDialog(@NonNull Context context) {
        this(context, 0);
    }

    public DiscussDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);
        mcontext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.city_dialog_chat, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);//dialog底部弹出
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        reviewEt = (EditText) view.findViewById(R.id.reviewEt);
        scaleImg = (ImageView) view.findViewById(R.id.scaleImg);
        submit = (TextView) view.findViewById(R.id.submit);

        scaleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onScaleDialogClickListener != null) {
                    onScaleDialogClickListener.onScaleClick(view, null);
//                    DiscussDialog.super.dismiss();
                }
            }
        });
        isnormaldismiss = true;
        reviewEt.setHint(nowmanname);
        reviewEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                ////System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resultcontent = s.toString();
                int number = s.length();
//                tv_num.setText("" + number);

                //////System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > 0) {
                    submit.setTextColor(Color.parseColor("#ff29bda9"));
                } else {
                    submit.setTextColor(Color.parseColor("#ff9596a4"));
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDiscussDialogClickListener != null) {
                    if (!TextUtils.isEmpty(reviewEt.getText().toString())) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("content", reviewEt.getText().toString());
                        onDiscussDialogClickListener.onDiscussClick(view, map);
                        isnormaldismiss = false;
                        if (view instanceof TextView) {
                            try {
                                InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        DiscussDialog.super.dismiss();
                    } else {
                        Toast.makeText(mcontext, "请输入内容", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        if (!TextUtils.isEmpty(SpUtils.getValue(getContext(), SpKey.DISCUSS_TMP))) {
            String jsonstring = SpUtils.getValue(getContext(), SpKey.DISCUSS_TMP);
            DiscussStore postStore = resolveTmpData(jsonstring);
            reviewEt.setText(postStore.discussContent);
            reviewEt.setSelection(postStore.discussContent.length());
        }
        if (!TextUtils.isEmpty(mEditText)) {
            reviewEt.setText(mEditText);
            reviewEt.setSelection(mEditText.length());
        }

    }

    private DiscussStore resolveTmpData(String obj) {
        DiscussStore result = null;
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<DiscussStore>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        if(!TextUtils.isEmpty(reviewEt.getText().toString())){
//            StyledDialog.init(getContext());
//            StyledDialog.buildIosAlert("提示", "将此次编辑保留?", new MyDialogListener() {
//                @Override
//                public void onFirst() {
//                    SpUtils.store(getContext(), SpKey.DISCUSS_TMP, "");
//                    DiscussDialog.super.dismiss();
//                }
//
//                @Override
//                public void onThird() {
//                    super.onThird();
//
//                }
//
//                @Override
//                public void onSecond() {
//                    DiscussStore postStore = new DiscussStore();
//                    postStore.setDiscussContent(reviewEt.getText().toString());
//                    String result = new Gson().toJson(postStore);
//                    SpUtils.store(getContext(), SpKey.DISCUSS_TMP, result);
//                    DiscussDialog.super.dismiss();
//
//
//                }
//            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, R.color.dialogutil_text_gray).setBtnText("不保留", "保留","取消").show();
//        }else {
//            super.dismiss();
//        }
    }

    @Override
    public void show() {
        super.show();
        reviewEt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) mcontext).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                reviewEt.requestFocus();
            }
        }, 300);
    }

    private void initView() {

    }

    public String nowmanname;

    public void setHiht(String nowmanname) {
        this.nowmanname = nowmanname;
    }

    public void setOnDiscussDialogDismissListener(final OnDiscussDialogDismissListener onDiscussDialogDismissListener) {
        this.onDiscussDialogDismissListener = onDiscussDialogDismissListener;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDiscussDialogDismissListener != null && isnormaldismiss) {
                    onDiscussDialogDismissListener.onDiscussDiss(resultcontent);
                }
            }
        });
    }

    public interface OnDiscussDialogDismissListener {
        void onDiscussDiss(String result);
    }

    public interface OnDiscussDialogClickListener {
        void onDiscussClick(View view, Map<String, Object> map);
    }

    public interface OnScaleDialogClickListener {
        void onScaleClick(View view, Map<String, Object> map);
    }
}
