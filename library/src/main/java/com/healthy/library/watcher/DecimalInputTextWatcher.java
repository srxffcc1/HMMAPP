package com.healthy.library.watcher;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

public class DecimalInputTextWatcher implements TextWatcher {

    private static final int DEFAULT_DECIMAL_DIGITS = 2;//默认  小数的位数   2 位

    private EditText editText;
    private int decimalDigits;// 小数的位数
    private int integerDigits;// 整数的位数

    public DecimalInputTextWatcher(EditText editText) {
        this.editText = editText;
        this.decimalDigits = DEFAULT_DECIMAL_DIGITS;
    }

    public DecimalInputTextWatcher(EditText editText, int decimalDigits) {
        this.editText = editText;
        if (decimalDigits <= 0) {
            decimalDigits=Integer.MAX_VALUE;
        }
        this.decimalDigits = decimalDigits;
    }

    public DecimalInputTextWatcher(EditText editText, int integerDigits, int decimalDigits) {
        this.editText = editText;
        if (integerDigits < 0) {
            integerDigits=Integer.MAX_VALUE;
        }
        if (decimalDigits < 0) {
            decimalDigits=Integer.MAX_VALUE;
        }
        this.decimalDigits = decimalDigits;
        this.integerDigits = integerDigits;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString();
        editText.removeTextChangedListener(this);

        if (s.contains(".")) {
            //System.out.println("SRX1");
            if (integerDigits > 0) {
                //System.out.println("SRX2");
                if(decimalDigits==0){
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + decimalDigits)});
                    s = s.substring(0, s.length()-1);
                    editable.replace(0, editable.length(), s.trim());//不输入超出位数的数字
                }else {
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + decimalDigits + 1)});
                }
            }
            if (s.length() - 1 - s.indexOf(".") > decimalDigits&&decimalDigits!=0) {
                //System.out.println("SRX3");
                s = s.substring(0,
                        s.indexOf(".") + decimalDigits + 1);
                editable.replace(0, editable.length(), s.trim());//不输入超出位数的数字
            }
        } else {
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + 1)});
                if (s.length() > integerDigits) {
                    s = s.substring(0, integerDigits);
                    editable.replace(0, editable.length(), s.trim());
                }
            }

        }
        if (".".equals(s.trim())) {//小数点开头，小数点前补0
            if(decimalDigits==0){//特殊处理 点不准作为一开始的输入
                s = "";
                editable.replace(0, editable.length(), s.trim());
            }else {
                s = "0" + s;
                editable.replace(0, editable.length(), s.trim());
            }

        }
        if(s.startsWith("0")&& s.trim().length() == 1){
            if(decimalDigits==0){
                editable.replace(0, editable.length(), "");
            }
        }else {
            if (s.startsWith("0") && s.trim().length() > 1) {//多个0开头，只输入一个0
                if(decimalDigits==0){
                    editable.replace(0, editable.length(), "");
                }else {
                    if (!".".equals(s.substring(1, 2))) {
                        editable.replace(0, editable.length(), "0");
                    }
                }

            }
        }

        editText.addTextChangedListener(this);
    }
}

