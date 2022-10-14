package com.health.index.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.health.index.adapter.IndexToolAdapter;
import com.health.index.contract.IndexMainContract;
import com.health.index.fragment.FragmentTools;
import com.health.index.model.IndexBean;
import com.health.index.model.IndexMonTool;
import com.health.index.model.UserInfoExModel;
import com.health.index.presenter.IndexMainPresenter;
import com.health.index.widget.ToolDecoration;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.OnItemClickListener;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexCustomNews;
import com.healthy.library.model.AppIndexCustomOther;
import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.widget.StatusLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_TOOLS)
public
class ToolsActivity extends BaseActivity implements
        TextView.OnEditorActionListener,
        IndexMainContract.View{
    private android.widget.EditText serarchKeyWord;
    private android.widget.ImageView imgBack;
    private android.view.View divider2;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private RecyclerView recyclerProvince;
    private android.widget.FrameLayout recyclerCity;
    private FragmentTools fragmentTools;
    private IndexToolAdapter indexToolAdapter;
    private List<IndexMonTool> list=new ArrayList<>();
    private IndexMainPresenter indexMonPresenter;
    @Autowired
    int currentIndex = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        Map<String,Object> map=new HashMap<>();
        map.put("stage","1");
        map.put("knowOrInfoStatus","1");
        fragmentTools=FragmentTools.newInstance(map);
        getSupportFragmentManager().beginTransaction().replace(R.id.recycler_city,fragmentTools).commitAllowingStateLoss();
        recyclerProvince.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerProvince.addItemDecoration(new ToolDecoration());
        indexToolAdapter=new IndexToolAdapter(mContext);
        list.add(new IndexMonTool("备孕",1));
        list.add(new IndexMonTool("孕期",2));
        list.add(new IndexMonTool("新生儿\n（1个月以内）",3));
        list.add(new IndexMonTool("婴幼儿\n（1-12个月）",4));
        list.add(new IndexMonTool("1-3岁",5));
        list.add(new IndexMonTool("3-6岁",6));
        list.add(new IndexMonTool("6岁以上",7));
        indexToolAdapter.setData(list);
        recyclerProvince.setAdapter(indexToolAdapter);
        indexToolAdapter.setmItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                //System.out.println("点击事件进入");
                if(pos==currentIndex){
                    return;
                }
                currentIndex=pos;
                indexToolAdapter.setSelected(pos);
                fragmentTools.putMap("stage",list.get(pos).stage+"");
                fragmentTools.refresh();
            }
        });
        indexMonPresenter = new IndexMainPresenter(mContext, this);
        indexMonPresenter.getMine();

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        serarchKeyWord = (EditText) findViewById(R.id.serarchKeyWord);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        divider2 = (View) findViewById(R.id.divider2);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerProvince = (RecyclerView) findViewById(R.id.recycler_province);
        recyclerCity = (FrameLayout) findViewById(R.id.recycler_city);
        serarchKeyWord.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId== EditorInfo.IME_ACTION_SEARCH){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
                    .withString("knowOrInfoStatus", 1+"")
                    .withString("title", serarchKeyWord.getText().toString()+"")
                    .navigation();
            serarchKeyWord.setText("");

        }
        return false;
    }

    @Override
    public void getIndexSuccess(IndexBean indexBean) {

    }

    @Override
    public void onSuccessGetVideoOnlineList(List<LiveVideoMain> liveVideoMains, boolean isMore) {

    }

    @Override
    public void onSuccessGetGoodsHotList(List<ActGoodsItem> result, int firstPageSize) {

    }

    @Override
    public void getAllStatusSuccess(List<UserInfoExModel> userInfoExModels) {

    }

    @Override
    public void changeStatusSuccess() {

    }

    @Override
    public void getMineSuccess(UserInfoMonModel userInfoMonModel) {
        if(userInfoMonModel!=null){
            if(userInfoMonModel.status==3){
                if(currentIndex!=-1){
                    indexToolAdapter.setSelected(currentIndex);
                    fragmentTools.putMap("stage",list.get(currentIndex).stage+"");
                    fragmentTools.refresh();
                }else {
                    try {
                        indexToolAdapter.setSelected(userInfoMonModel.status-1);
                        fragmentTools.putMap("stage",list.get(userInfoMonModel.status-1).stage+"");
                        fragmentTools.refresh();
                    } catch (Exception e) {
                        indexToolAdapter.setSelected(0);
                        fragmentTools.putMap("stage",list.get(0).stage+"");
                        fragmentTools.refresh();
                        e.printStackTrace();
                    }
                }

            }else {
                if(currentIndex==-1){
                    try {
                        indexToolAdapter.setSelected(userInfoMonModel.status-1);
                        fragmentTools.putMap("stage",list.get(userInfoMonModel.status-1).stage+"");
                        fragmentTools.refresh();
                    } catch (Exception e) {
                        indexToolAdapter.setSelected(0);
                        fragmentTools.putMap("stage",list.get(0).stage+"");
                        fragmentTools.refresh();
                        e.printStackTrace();
                    }
                }else {
                    indexToolAdapter.setSelected(currentIndex);
                    fragmentTools.putMap("stage",list.get(currentIndex).stage+"");
                    fragmentTools.refresh();
                }
            }

        }
    }

    @Override
    public void onSuccessTongLianPhoneStatus(TongLianMemberData isPhoneChecked) {

    }

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {

    }

    @Override
    public void onSuccessGetAPPIndexCustom(AppIndexCustom appIndexCustom) {

    }

    @Override
    public void onSuccessGetAPPIndexCustomWithOther(AppIndexCustomOther appIndexCustom) {

    }

    @Override
    public void onSuccessGetAPPIndexCustomNews(AppIndexCustomNews result) {

    }

    @Override
    public void onSuccessGetQuestionList(List<FaqExportQuestion> result) {

    }


}
