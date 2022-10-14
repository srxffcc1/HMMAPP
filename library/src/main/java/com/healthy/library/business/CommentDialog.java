package com.healthy.library.business;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.healthy.library.R;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class CommentDialog extends Dialog {

    private EditText reviewEt;
    private TextView submit;
    private TextView editHint;
    private String resultcontent;

    boolean isNormalDismiss = true;

    private Context context;

    private OnDiscussDialogDismissListener onDiscussDialogDismissListener;

    private TextView save;


    public CommentDialog(@NonNull Context context) {
        this(context, 0);
    }

    public CommentDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.comment_dialog_chat, null);
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
        editHint = (TextView) view.findViewById(R.id.editHint);
        submit = (TextView) view.findViewById(R.id.submit);
        isNormalDismiss = true;
        editHint.setText(hint);
        if (type == 1) {
            Drawable drawable = context.getDrawable(R.drawable.ic_edit_tip);
            editHint.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            editHint.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        reviewEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                if (temp != null && temp.length() > 0) {
                    editHint.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                } else {
                    editHint.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                resultcontent = s.toString();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDiscussDialogDismissListener != null) {
                    if (!TextUtils.isEmpty(reviewEt.getText().toString())) {
                        onDiscussDialogDismissListener.onDiscussDismiss(reviewEt.getText().toString(), type);
                        isNormalDismiss = false;
                        if (view instanceof TextView) {
                            try {
                                InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        CommentDialog.super.dismiss();
                    } else {
                        Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        reviewEt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                reviewEt.requestFocus();
            }
        }, 300);
    }

    private void initView() {

    }

    public String hint;
    public int type;//1评论  2评论别的评论

    public void setHint(String hint, int type) {
        this.hint = hint;
        this.type = type;
    }

    public void setOnDiscussDialogDismissListener(final OnDiscussDialogDismissListener onDiscussDialogDismissListener) {
        this.onDiscussDialogDismissListener = onDiscussDialogDismissListener;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onDiscussDialogDismissListener != null && isNormalDismiss) {
                    onDiscussDialogDismissListener.onDiscussDismiss("", -1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideInput();
                        }
                    }, 300);
                }
            }
        });
    }

    public interface OnDiscussDialogDismissListener {
        void onDiscussDismiss(String result, int type);
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
