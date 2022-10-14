package com.healthy.library.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
@Deprecated
/**
 * Author: Li
 * Date: 2018/10/10 0010
 * Description: 输入手机号自动添加空格
 */
public class PhoneNumFormatWatcher implements TextWatcher {

    private EditText mEditText;

    public PhoneNumFormatWatcher(EditText editText) {
        mEditText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        int length = s.toString().length();
        //删除数字
        if (count == 0) {
            if (length == 4) {
                mEditText.setText(s.subSequence(0, 3));
            }
            if (length == 9) {
                mEditText.setText(s.subSequence(0, 8));
            }
        }
        //添加数字
        if (count == 1) {
            if (length == 4) {
                String part1 = s.subSequence(0, 3).toString();
                String part2 = s.subSequence(3, length).toString();
                mEditText.setText(String.format("%s %s", part1, part2));
            }
            if (length == 9) {
                String part1 = s.subSequence(0, 8).toString();
                String part2 = s.subSequence(8, length).toString();
                mEditText.setText(String.format("%s %s", part1, part2));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        mEditText.setSelection(mEditText.getText().toString().length());
    }
}
