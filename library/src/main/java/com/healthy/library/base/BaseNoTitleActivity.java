package com.healthy.library.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;


/**
 * @author Li
 * @date 2019/03/01 11:29
 * @des activity
 */

public  class BaseNoTitleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}
