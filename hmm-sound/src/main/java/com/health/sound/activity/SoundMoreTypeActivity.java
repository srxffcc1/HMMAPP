package com.health.sound.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.sound.R;
import com.health.sound.adapter.SoundListSeachAdapter;
import com.health.sound.fragment.SoundTypeFragment;
import com.health.sound.fragment.SoundTypeMoreFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import java.util.HashMap;
import java.util.Map;


@Route(path = SoundRoutes.SOUND_MORE)
public class SoundMoreTypeActivity extends BaseActivity implements IsFitsSystemWindows {
    @Autowired
    String audioType;

    @Autowired
    String audioCategoryName;
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.FrameLayout childFrame;

    @Override
    protected int getLayoutId() {
        return R.layout.sound_activity_more;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        topBar.setTitle(audioCategoryName);
        Map<String, Object> maporg=new HashMap<>();
        maporg.put("audioType",audioType);
        maporg.put("audioCategoryName",audioCategoryName);
        getSupportFragmentManager().beginTransaction().replace(R.id.childFrame , SoundTypeMoreFragment.newInstance(maporg)).commitAllowingStateLoss();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        childFrame = (FrameLayout) findViewById(R.id.childFrame);
    }
}
