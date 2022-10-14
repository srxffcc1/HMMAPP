package com.healthy.library.crash;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.routes.LibraryRoutes;

@Route(path = LibraryRoutes.LIBRARY_CRASH)
public class BugActivity extends Activity {
    private TextView bugText;
    @Autowired
    String error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_text);
        ARouter.getInstance().inject(this);
        initView();
        bugText.setText(error);
    }

    private void initView() {
        bugText = (TextView) findViewById(R.id.bugText);
    }
}
