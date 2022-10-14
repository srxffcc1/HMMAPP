package com.healthy.library.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.fragment.PhotoFragment;
import com.healthy.library.fragment.VideoFragment;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.MediaFileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/13 10:53
 * @des 查看图片详情
 */
@Route(path = LibraryRoutes.LIBRARY_PHOTO_DETAIL)
public class PhotoDetailActivity extends BaseActivity implements IsFitsSystemWindows {


//    HackyViewPager mPager;

    @Autowired
    String[] urls;
    @Autowired
    int pos;
    private ViewPager pagerPhoto;
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;

    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    protected void findViews() {
//        mPager = findViewById(R.id.pager_photo);
    }

    public void close(View view){
        finish();
    }

    @Override
    protected void init(Bundle bundle) {
        initView();
        ARouter.getInstance().inject(this);
        if (urls == null) {
            return;
        }
        fragments.clear();
        List<String> titles = new ArrayList<>();
        List<String> urlList = Arrays.asList(urls);
        for (int i = 0; i <urlList.size() ; i++) {
            titles.add("1"+i);
            if(MediaFileUtil.isVideoFileType(urlList.get(i))){
                fragments.add(VideoFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("url", urlList.get(i)).puts("clickPlay",true)));
            }else {
                fragments.add(PhotoFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("fit",true).puts("url", urlList.get(i))));
            }
        }
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(((BaseActivity) mContext).getSupportFragmentManager(), fragments, titles);
            // adapter
            pagerPhoto.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.setOnPageClickListener(new FragmentStatePagerItemAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(int index) {
                finish();
            }
        });
        pagerPhoto.setCurrentItem(pos);
    }

    private void initView() {
        pagerPhoto = (ViewPager) findViewById(R.id.pager_photo);
    }
    private void videoPlayEnd() {//播放关闭
//        if (FloatingPlayer.getInstance().getKSYTextureView() != null) {
//            try {
//                //System.out.println("Act关闭直接把视频关闭");
//                FloatingPlayer.getInstance().destroy();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }
    @Override
    public void finish() {
            videoPlayEnd();
        super.finish();
    }
}
