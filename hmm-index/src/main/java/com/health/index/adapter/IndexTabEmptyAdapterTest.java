package com.health.index.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.example.lib_ShapeView.layout.ShapeFrameLayout;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.ImageTextView;

//测试Adapter
public class IndexTabEmptyAdapterTest extends BaseAdapter<String> {




    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible = true;

    @Override
    public int getItemViewType(int position) {
        return 13;
    }

    public IndexTabEmptyAdapterTest() {
        this(R.layout.index_hmm_tip_item_layout);
    }

    private IndexTabEmptyAdapterTest(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int i) {

    }

    private void initView() {



    }
}
