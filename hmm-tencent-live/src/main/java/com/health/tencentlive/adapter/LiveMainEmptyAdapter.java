package com.health.tencentlive.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.tencentlive.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.utils.FrameActivityManager;

import org.greenrobot.eventbus.EventBus;

public class LiveMainEmptyAdapter extends BaseAdapter<String> {



    public Object type;

    public void setType(Object type) {
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return 74;
    }

    public LiveMainEmptyAdapter() {
        this(R.layout.item_t_live_list_empty);
    }

    private LiveMainEmptyAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
         TextView passToMain;
        passToMain = (TextView) baseHolder.itemView.findViewById(R.id.passToMain);
        if(type==null){
            passToMain.setVisibility(View.GONE);
        }else {
            passToMain.setVisibility(View.VISIBLE);
            passToMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FrameActivityManager.instance().finishOthersActivity(Class.forName("com.health.client.activity.MainActivity"));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(new TabChangeModel(0));
                }
            });
        }


    }

    private void initView() {
    }
}
