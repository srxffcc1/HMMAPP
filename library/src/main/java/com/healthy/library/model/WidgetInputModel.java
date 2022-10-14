package com.healthy.library.model;

import android.widget.EditText;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.healthy.library.widget.NewInputView;

import java.io.Serializable;

/**
 * @author long
 * @description
 * @date 2021/6/26
 */
public class WidgetInputModel implements MultiItemEntity, Serializable {

    private int type;
    private String title;
    private int titleColor;
    private String rightEditHint;
    private String rightEditBody;
    private String rightBody;
    private int rightColor;
    private boolean isEdit;
    private NewInputView mInputView;
    private EditText mEditText;

    public EditText getEditText() {
        return mEditText;
    }

    public void setEditText(EditText mEditText) {
        this.mEditText = mEditText;
    }

    public NewInputView getInputView() {
        return mInputView;
    }

    public void setInputView(NewInputView mInputView) {
        this.mInputView = mInputView;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public String getRightEditHint() {
        return rightEditHint;
    }

    public void setRightEditHint(String rightEditHint) {
        this.rightEditHint = rightEditHint;
    }

    public String getRightEditBody() {
        return rightEditBody;
    }

    public void setRightEditBody(String rightEditBody) {
        this.rightEditBody = rightEditBody;
    }

    public String getRightBody() {
        return rightBody;
    }

    public void setRightBody(String rightBody) {
        this.rightBody = rightBody;
    }

    public int getRightColor() {
        return rightColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    @Override
    public String toString() {
        return "WidgetInputModel{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", titleColor=" + titleColor +
                ", rightEditHint='" + rightEditHint + '\'' +
                ", rightEditBody='" + rightEditBody + '\'' +
                ", rightBody='" + rightBody + '\'' +
                ", rightColor=" + rightColor +
                ", isEdit=" + isEdit +
                '}';
    }

    @Override
    public int getItemType() {
        return type;
    }
}
