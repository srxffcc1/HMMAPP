package com.healthy.library.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class TextLimitTextWatcher implements TextWatcher {
    private CharSequence temp;
    private int selectionStart;
    private int selectionEnd;

    private int num;
    private EditText editText;

    public TextLimitTextWatcher( EditText editText,int num) {
        this.num = num;
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        temp = s;
        //System.out.println("s=" + s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        selectionStart = editText.getSelectionStart();
        selectionEnd = editText.getSelectionEnd();
        if (temp.length() > num) {
            Toast.makeText(editText.getContext(),"限制输入" + num + "个字",Toast.LENGTH_SHORT).show();
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionStart;
            editText.setText(s);
            editText.setSelection(tempSelection);//设置光标在最后
        }
    }
}
