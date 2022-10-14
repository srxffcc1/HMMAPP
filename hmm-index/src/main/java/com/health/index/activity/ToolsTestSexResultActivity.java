package com.health.index.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.Random;

@Route(path = IndexRoutes.INDEX_TOOLS_TEST_SEX_RESULT)
public class ToolsTestSexResultActivity extends BaseActivity implements IsNeedShare {

    @Autowired
    String sex;
    @Autowired
    String vcType;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private android.widget.ImageView sexImg;
    private android.widget.TextView sexTxt;
    private androidx.constraintlayout.widget.ConstraintLayout toolsButtonLeft;
    private androidx.constraintlayout.widget.ConstraintLayout toolsButtonRight;
    private android.widget.TextView textTitle;
    public String[] maletxts={"快来接男宝一枚，宝宝活泼可爱，将来肯定是一个" +
            "风度翩翩的少年!","恭喜宝妈将会有一个可爱的男宝宝，宝宝活泼开朗，将" +
            "来有当科学家的可能哦!"};
    public String[] femaletxts={"快来接女宝一枚，宝宝活泼开朗，将来肯定是一个" +
            "才貌双全的小仙女!","恭喜宝妈将会有一个女宝宝，宝宝活泼可爱，将来肯定" +
            "是妈妈的贴心小棉袄!"};
    String surl;
    String sdes;
    String stitle;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_test_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        if("女".equals(sex)){
            sexImg.setImageResource(R.drawable.tools_result_female);
            sexTxt.setText(femaletxts[new Random().nextInt(2)]);
        }else {
            sexImg.setImageResource(R.drawable.tools_result_male);
            sexTxt.setText(maletxts[new Random().nextInt(2)]);

        }
        if("1".equals(vcType)){
            textTitle.setText("清宫表测男女");
        }
        if("2".equals(vcType)){
            textTitle.setText("算胎动测男女");
        }
        if("3".equals(vcType)){
            textTitle.setText("看肚子测男女");
        }
        if("4".equals(vcType)){
            textTitle.setText("看变化测男女");
        }
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                buildDes();
                showShare();
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        sexImg = (ImageView) findViewById(R.id.sexImg);
        sexTxt = (TextView) findViewById(R.id.sexTxt);
        toolsButtonLeft = (ConstraintLayout) findViewById(R.id.toolsButtonLeft);
        toolsButtonRight = (ConstraintLayout) findViewById(R.id.toolsButtonRight);
        textTitle = (TextView) findViewById(R.id.textTitle);
    }

    public void tryAgain(View view){
        finish();
        if("1".equals(vcType)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_EXL)
                    .navigation();
        }
        if("2".equals(vcType)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX)
                    .withString("vcType","2")
                    .withString("title","算胎动测男女")
                    .navigation();
        }
        if("3".equals(vcType)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX)
                    .withString("vcType","3")
                    .withString("title","看肚子测男女")
                    .navigation();
        }
        if("4".equals(vcType)){
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TEST_SEX)
                    .withString("vcType","4")
                    .withString("title","看变化测男女")
                    .navigation();
        }
    }
    public void shareOut(View view){
        buildDes();
        showShare();
    }




    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babySexUrl);
        String sexInt="1";
        if("女".equals(sex)){
            sexInt="2";
        }else {
            sexInt="1";
        }
        String url = String.format("%s?sex=%s&type=%s&info=%s", urlPrefix,sexInt, vcType,sexInt);
        surl=url;
        stitle="";
        if("1".equals(vcType)){
            stitle="我在用清宫表预测生男生女你也来试试吧~";
            sdes=" ";
        }
        if("2".equals(vcType)){
            stitle="我在用算胎动预测生男生女你也来试试吧~";
            sdes=" ";
        }
        if("3".equals(vcType)){
            stitle="我在用看肚子预测生男生女你也来试试吧~";
            sdes=" ";
        }
        if("4".equals(vcType)){
            stitle="我在用看变化预测生男生女你快来试试吧~";
            sdes=" ";
        }

    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
